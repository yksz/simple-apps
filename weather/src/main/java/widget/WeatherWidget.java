package widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import util.Loader;
import weather.Weather;
import weather.apis.WeatherApi;
import widget.attribute.Provider;
import widget.config.Config;
import widget.config.Config.Key;
import widget.dialog.Dialog;
import widget.dialog.preferences.Preferences;
import widget.dialog.preferences.PreferencesDialog;
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

public class WeatherWidget extends Stage {

    private static final int FORECAST_DAYS = 3;

    private final Preferences _prefs;
    private final Properties  _iconProp;

    private ContextMenu _contextMenu;
    private Dialog      _prefsDialog;

    private Label       _date;
    private Label[]     _day;
    private Label[]     _temperature;
    private ImageView[] _icon;

    private MouseEvent _pressed;

    public WeatherWidget(Stage dummy) {
        super.initStyle(StageStyle.TRANSPARENT);
        super.initModality(Modality.WINDOW_MODAL);
        super.initOwner(dummy);

        _prefs    = new Preferences();
        _iconProp = new Properties();

        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        _prefsDialog = new PreferencesDialog(this, _prefs);
        _day         = new Label[FORECAST_DAYS];
        _temperature = new Label[FORECAST_DAYS];
        _icon        = new ImageView[FORECAST_DAYS];

        doLayout();
        updateForecast();
    }

    public void updateForecast() {
        if (_prefs.getLocation() == null)
            return;

        WeatherApiFactory factory = WeatherApiFactory.getInstance();
        WeatherApi api = factory.getWeatherAPI(_prefs.getProvider());
        try {
            Weather[] weather = api.getWeather(_prefs.getLocation());
            _date.setText(String.format(Locale.ENGLISH, "%1$tb %1$te, %1$tY", weather[0].getDate()));

            for (int i = 0; i < FORECAST_DAYS; i++) {
                _day[i].setText(weather[i].getDay());
                _temperature[i].setText(weather[i].getLowTempC() + "-" + weather[i].getHighTempC() + "℃");

                String iconPath = _iconProp.getProperty(weather[i].getCondition());
                if (iconPath == null)
                    iconPath = _iconProp.getProperty("Unknown");

                URL url = Loader.getResource(iconPath.trim());
                if (url != null)
                    _icon[i].setImage(new Image(url.toString()));
            }
        } catch (Exception e) {
            Dialogs.showErrorDialog(this, null, "Could not get weather forecast", "Exception Encountered", e);
        }
    }

    private void loadConfig() throws IOException {
        // config.xml
        final String providerName = Config.get(Key.PROVIDER);
        _prefs.setProvider(Provider.toProvider(providerName));
        final String location = Config.get(Key.LOCATION);
        _prefs.setLocation(location);
        final String x = Config.get(Key.POSITION_X);
        super.setX(Double.parseDouble(x));
        final String y = Config.get(Key.POSITION_Y);
        super.setY(Double.parseDouble(y));

        // icon.xml
        final String iconPropName = Config.get(Key.ICON_PROPERTIES);
        File file = Loader.getResourceAsFile(iconPropName);
        InputStream in = new FileInputStream(file);
        _iconProp.loadFromXML(in);
    }

    private void doLayout() {
        Group root = new Group();
        Scene scene = new Scene(root, 80, 320);
        scene.setFill(Color.rgb(0, 0, 0, 0.7));
        this.setScene(scene);
        this.layoutForcast(scene);
        this.setupContextMenu();
    }

    private void layoutForcast(Scene scene) {
        final VBox vbox = VBoxBuilder.create()
                .alignment(Pos.CENTER)
                .spacing(5)
                .build();

        vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.PRIMARY)
                    _contextMenu.hide();

                if (e.getButton() == MouseButton.SECONDARY)
                    _contextMenu.show(vbox, e.getScreenX(), e.getScreenY());
            }
         });
        vbox.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                _pressed = e;
            }
         });
        vbox.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setX(e.getScreenX() - _pressed.getSceneX());
                setY(e.getScreenY() - _pressed.getSceneY());
            }
         });

        // Date
        _date = LabelBuilder.create()
                .font(Font.font("System", 12))
                .textFill(Color.rgb(255, 255, 255))
                .text("?date?")
                .build();
        vbox.getChildren().add(
                _date
        );

        for (int i = 0; i < FORECAST_DAYS; i++) {
            // Day
            _day[i] = LabelBuilder.create()
                    .font(Font.font("System", 12))
                    .textFill(Color.rgb(255, 255, 255))
                    .text("?day?")
                    .build();

            // Temperature
            _temperature[i] = LabelBuilder.create()
                    .font(Font.font("System", 11))
                    .textFill(Color.rgb(255, 255, 255))
                    .text("?temperature?")
                    .build();

            // Weather icon
            _icon[i] = ImageViewBuilder.create()
                    .fitWidth(50)
                    .fitHeight(50)
                    .preserveRatio(true).build();

            vbox.getChildren().addAll(
                    _day[i],
                    _icon[i],
                    _temperature[i]
            );

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
        _contextMenu = ContextMenuBuilder.create()
                .build();

        _contextMenu.getItems().addAll(
                MenuItemBuilder.create()
                .text("Preferences...")
                .onAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        _prefsDialog.setLocationRelativeToScreen();
                        _prefsDialog.show();
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
                        Config.set(Key.PROVIDER, _prefs.getProvider().toString());
                        Config.set(Key.LOCATION, _prefs.getLocation());
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
