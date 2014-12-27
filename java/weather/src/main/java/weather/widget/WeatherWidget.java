package weather.widget;

import impl.org.controlsfx.i18n.Localization;

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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.controlsfx.dialog.Dialogs;

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

    static {
        Localization.setLocale(Locale.ENGLISH);
    }

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
                if (iconPath == null)
                    continue;
                URL url = Loader.getResource(iconPath.trim());
                if (url != null)
                    icon[i].setImage(new Image(url.toString()));
            }
        } catch (Exception e) {
            Dialogs.create()
                .owner(this)
                .title("Exception Dialog")
                .masthead("Exception occured!")
                .message("Could not get weather forecast")
                .showException(e);
        }
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
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
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
        date = new Label("?date?");
        date.setFont(Font.font("System", 12));
        date.setTextFill(Color.WHITE);
        vbox.getChildren().add(date);

        for (int i = 0; i < FORECAST_DAYS; i++) {
            // Day
            day[i] = new Label("?day?");
            day[i].setFont(Font.font("System", 12));
            day[i].setTextFill(Color.WHITE);
            // Temperature
            temperature[i] = new Label("?temperature?");
            temperature[i].setFont(Font.font("System", 11));
            temperature[i].setTextFill(Color.WHITE);
            // Weather icon
            icon[i] = new ImageView();
            icon[i].setFitWidth(50);
            icon[i].setFitHeight(50);
            icon[i].setPreserveRatio(true);
            vbox.getChildren().addAll(day[i], icon[i], temperature[i]);
            // Separator
            if (i != FORECAST_DAYS - 1) {
                Separator separator = new Separator();
                separator.setValignment(VPos.CENTER);
                vbox.getChildren().add(separator);
            }
        }

        scene.setRoot(vbox);
    }

    private void setupContextMenu() {
        MenuItem prefMenuItem = new MenuItem("Preferences...");
        prefMenuItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                prefsDialog.setLocationRelativeToScreen();
                prefsDialog.show();
            }
        });

        MenuItem updateMenuItem = new MenuItem("Update the forecast");
        updateMenuItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                updateForecast();
            }
        });

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>(){
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
        });

        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(prefMenuItem, updateMenuItem, new SeparatorMenuItem(), exitMenuItem);
    }

}
