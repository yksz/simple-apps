package weather.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.SeparatorBuilder;
import javafx.scene.control.SeparatorMenuItemBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import weather.api.Forecast;
import weather.api.WeatherAPI;
import weather.widget.attribute.Provider;
import weather.widget.config.Config;
import weather.widget.config.Config.Key;
import weather.widget.dialog.Dialog;
import weather.widget.dialog.preferences.Preferences;
import weather.widget.dialog.preferences.PreferencesDialog;
import weather.widget.util.Loader;

public class WeatherWidget extends Stage {

    private static final int FORECAST_DAYS = 3;

    private final Preferences prefs;
    private final Properties iconProp;

    private ContextMenu contextMenu;
    private Dialog prefsDialog;

    private Label date;
    private Label[] day;
    private Label[] temperature;
    private ImageView[] icon;

    private MouseEvent pressed;

    public WeatherWidget(Stage dummy) {
        super.initStyle(StageStyle.TRANSPARENT);
        super.initModality(Modality.WINDOW_MODAL);
        super.initOwner(dummy);

        prefs = new Preferences();
        iconProp = new Properties();

        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        prefsDialog = new PreferencesDialog(this, prefs);
        day = new Label[FORECAST_DAYS];
        temperature = new Label[FORECAST_DAYS];
        icon = new ImageView[FORECAST_DAYS];

        doLayout();
        updateForecast();
    }

    public void updateForecast() {
        if (prefs.getLocation() == null)
            return;

        WeatherAPIFactory factory = WeatherAPIFactory.getInstance();
        WeatherAPI api = factory.getWeatherAPI(prefs.getProvider());
        try {
            Forecast[] forecast = api.getForecast(prefs.getLocation());
            date.setText(String.format(Locale.ENGLISH, "%1$tb %1$te, %1$tY", forecast[0].getDate()));

            for (int i = 0; i < FORECAST_DAYS; i++) {
                day[i].setText(forecast[i].getDay());
                temperature[i].setText(forecast[i].getLowTemperature().getCelsius()
                        + "-" + forecast[i].getHighTemperature().getCelsius() + "℃");

                String iconPath = iconProp.getProperty(forecast[i].getCondition());
                if (iconPath == null)
                    iconPath = iconProp.getProperty("Unknown");

                URL url = Loader.getResource(iconPath.trim());
                if (url != null)
                    icon[i].setImage(new Image(url.toString()));
            }
        } catch (Exception e) {
            Dialogs.showErrorDialog(this, null, "Could not get weather forecast", "Exception Encountered", e);
        }
    }

    private void loadConfig() throws IOException {
        // config.xml
        String providerName = Config.get(Key.PROVIDER);
        prefs.setProvider(Provider.toProvider(providerName));
        String location = Config.get(Key.LOCATION);
        prefs.setLocation(location);
        String x = Config.get(Key.POSITION_X);
        super.setX(Double.parseDouble(x));
        String y = Config.get(Key.POSITION_Y);
        super.setY(Double.parseDouble(y));

        // icon.xml
        String iconPropName = Config.get(Key.ICON_PROPERTIES);
        File file = Loader.getResourceAsFile(iconPropName);
        InputStream in = new FileInputStream(file);
        iconProp.loadFromXML(in);
    }

    private void doLayout() {
        Group root = new Group();
        Scene scene = new Scene(root, 80, 320);
        scene.setFill(Color.rgb(0, 0, 0, 0.7));
        this.setScene(scene);
        layoutForcast(scene);
        setupContextMenu();
    }

    private void layoutForcast(Scene scene) {
        VBox vbox = VBoxBuilder.create()
                .alignment(Pos.CENTER)
                .spacing(5)
                .build();
        vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.PRIMARY)
                    contextMenu.hide();
                if (e.getButton() == MouseButton.SECONDARY)
                    contextMenu.show(vbox, e.getScreenX(), e.getScreenY());
            }
        });
        vbox.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                pressed = e;
            }
        });
        vbox.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setX(e.getScreenX() - pressed.getSceneX());
                setY(e.getScreenY() - pressed.getSceneY());
            }
        });

        // Date
        date = LabelBuilder.create()
                .font(Font.font("System", 12))
                .textFill(Color.rgb(255, 255, 255))
                .text("?date?")
                .build();
        vbox.getChildren().add(date);

        for (int i = 0; i < FORECAST_DAYS; i++) {
            // Day
            day[i] = LabelBuilder.create()
                    .font(Font.font("System", 12))
                    .textFill(Color.rgb(255, 255, 255))
                    .text("?day?")
                    .build();
            // Temperature
            temperature[i] = LabelBuilder.create()
                    .font(Font.font("System", 11))
                    .textFill(Color.rgb(255, 255, 255))
                    .text("?temperature?")
                    .build();
            // Weather icon
            icon[i] = ImageViewBuilder.create()
                    .fitWidth(50)
                    .fitHeight(50)
                    .preserveRatio(true).build();
            vbox.getChildren().addAll(day[i], icon[i], temperature[i]);

            // Separator
            if (i != FORECAST_DAYS - 1)
                vbox.getChildren().add(
                        SeparatorBuilder.create()
                        .valignment(VPos.CENTER)
                        .build()
                );
        }

        scene.setRoot(vbox);
    }

    private void setupContextMenu() {
        contextMenu = ContextMenuBuilder.create()
                .build();
        contextMenu.getItems().addAll(
                MenuItemBuilder.create()
                .text("Preferences...")
                .onAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        prefsDialog.setLocationRelativeToScreen();
                        prefsDialog.show();
                    }
                })
                .build()
                ,
                MenuItemBuilder.create()
                .text("Update the forecast")
                .onAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        updateForecast();
                    }
                })
                .build()
                ,
                SeparatorMenuItemBuilder.create()
                .build()
                ,
                MenuItemBuilder.create()
                .text("Exit")
                .onAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        Config.set(Key.PROVIDER, prefs.getProvider().toString());
                        Config.set(Key.LOCATION, prefs.getLocation());
                        Config.set(Key.POSITION_X, Double.toString(getX()));
                        Config.set(Key.POSITION_Y, Double.toString(getY() ));
                        try {
                            Config.write();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                })
                .build()
        );
    }

}
