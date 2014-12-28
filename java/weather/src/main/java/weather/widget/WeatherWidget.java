package weather.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.controlsfx.dialog.Dialogs;

import weather.api.Forecast;
import weather.api.WeatherApi;
import weather.widget.attribute.Preferences;
import weather.widget.attribute.Provider;
import weather.widget.config.Config;
import weather.widget.config.Config.Key;
import weather.widget.dialog.Dialog;
import weather.widget.dialog.PreferencesDialog;
import weather.widget.util.Loader;

public class WeatherWidget extends Application {

    private static final int FORECAST_DAYS = 3;

    private final Preferences prefs = new Preferences();
    private final Properties iconProp = new Properties();

    private Stage stage;
    private ContextMenu contextMenu = new ContextMenu();
    private Dialog prefsDialog;

    private Label date = new Label();
    private Label[] day = new Label[FORECAST_DAYS];
    private Label[] temperature = new Label[FORECAST_DAYS];
    private ImageView[] icon = new ImageView[FORECAST_DAYS];

    private MouseEvent pressed;

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.initStyle(StageStyle.TRANSPARENT);

        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsDialog = new PreferencesDialog(stage, this, prefs);

        doLayout();
        updateForecast();
        stage.show();
        stage.toBack();
    }

    private void loadConfig() throws IOException {
        // config.xml
        String providerName = Config.get(Key.PROVIDER);
        prefs.setProvider(Provider.toProvider(providerName));
        String location = Config.get(Key.LOCATION);
        prefs.setLocation(location);
        String x = Config.get(Key.POSITION_X);
        stage.setX(Double.parseDouble(x));
        String y = Config.get(Key.POSITION_Y);
        stage.setY(Double.parseDouble(y));

        // icon.xml
        String iconPropName = Config.get(Key.ICON_PROPERTIES);
        File file = Loader.getResourceAsFile(iconPropName);
        InputStream in = new FileInputStream(file);
        iconProp.loadFromXML(in);
    }

    public boolean updateForecast() {
        if (prefs.getLocation() == null)
            return false;

        WeatherApiFactory factory = WeatherApiFactory.getInstance();
        WeatherApi api = factory.getWeatherApi(prefs.getProvider());
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
            return true;
        } catch (Exception e) {
            Dialogs.create()
                .owner(stage)
                .title("Exception Dialog")
                .masthead("Exception occured!")
                .message("Could not get weather forecast")
                .showException(e);
            return false;
        }
    }

    private void doLayout() {
        Region root = createForcastLayout();
        root.setBackground(null);
        Scene scene = new Scene(root, 80, 320);
        scene.setFill(Color.rgb(0, 0, 0, 0.7));
        stage.setScene(scene);

        setupContextMenu();
    }

    private Region createForcastLayout() {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY)
                contextMenu.hide();
            if (event.getButton() == MouseButton.SECONDARY)
                contextMenu.show(vbox, event.getScreenX(), event.getScreenY());
        });
        vbox.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            pressed = event;
        });
        vbox.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            stage.setX(event.getScreenX() - pressed.getSceneX());
            stage.setY(event.getScreenY() - pressed.getSceneY());
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

        return vbox;
    }

    private void setupContextMenu() {
        MenuItem prefMenuItem = new MenuItem("Preferences...");
        prefMenuItem.setOnAction(event -> {
            prefsDialog.setLocationRelativeToScreen();
            prefsDialog.show();
        });

        MenuItem updateMenuItem = new MenuItem("Update the forecast");
        updateMenuItem.setOnAction(event -> {
            updateForecast();
        });

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> {
            Config.set(Key.PROVIDER, prefs.getProvider().toString());
            Config.set(Key.LOCATION, prefs.getLocation());
            Config.set(Key.POSITION_X, Double.toString(stage.getX()));
            Config.set(Key.POSITION_Y, Double.toString(stage.getY() ));
            try {
                Config.write();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });

        contextMenu.getItems().addAll(prefMenuItem, updateMenuItem, new SeparatorMenuItem(), exitMenuItem);
    }

    public static void main(String[] args) {
        launch(WeatherWidget.class, args);
    }

}
