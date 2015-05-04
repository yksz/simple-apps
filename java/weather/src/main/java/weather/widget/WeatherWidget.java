package weather.widget;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import javafx.application.Application;
import javafx.geometry.Pos;
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
import weather.widget.Config.Key;

public class WeatherWidget extends Application {

    private static final int FORECAST_DAYS = 3;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 320;
    private static final Color BACKGROUND_COLOR = Color.rgb(0, 0, 0, 0.7);

    private final Label[] date = new Label[1];
    private final Label[] day = new Label[FORECAST_DAYS];
    private final Label[] temperature = new Label[FORECAST_DAYS];
    private final ImageView[] icon = new ImageView[FORECAST_DAYS];

    private Stage stage;
    private final ContextMenu contextMenu = new ContextMenu();

    private final Preferences prefs = new Preferences();
    private final Properties iconProps = new Properties();

    private MouseEvent pressed;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        loadConfig();
        setUpContextMenu();
        stage.setScene(createScene());
        stage.initStyle(StageStyle.TRANSPARENT);
        updateForecast();
        stage.show();
        stage.toBack();
    }

    private void loadConfig() {
        // config.xml
        stage.setX(Double.parseDouble(Config.get(Key.WIDGET_X)));
        stage.setY(Double.parseDouble(Config.get(Key.WIDGET_Y)));
        prefs.setProvider(Provider.getByName(Config.get(Key.WEBAPI_PROVIDER)));
        prefs.setLocation(Config.get(Key.LOCATION));

        // icon.xml
        String iconPropsFile = Config.get(Key.ICON_PROPERTIES_FILE);
        try {
            InputStream in = Loader.getResourceAsStream(iconPropsFile);
            iconProps.loadFromXML(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateForecast() {
        try {
            getForecast();
        } catch (Exception e) {
            Dialogs.create()
                    .owner(stage)
                    .title("Exception Dialog")
                    .masthead("Exception occured!")
                    .message("Could not get weather forecast")
                    .showException(e);
        }
    }

    private void getForecast() throws Exception {
        if (prefs.getLocation() == null)
            return;

        WeatherApi api = WeatherApiFactory.getWeatherApi(prefs.getProvider());
        Forecast[] forecast = api.getForecast(prefs.getLocation());
        date[0].setText(String.format(Locale.ENGLISH, "%1$tb %1$te, %1$tY", forecast[0].getDate()));
        for (int i = 0; i < FORECAST_DAYS; i++) {
            day[i].setText(forecast[i].getDay());
            temperature[i].setText(forecast[i].getLowTemperature().getCelsius()
                    + "-" + forecast[i].getHighTemperature().getCelsius() + "℃");
            String iconPath = iconProps.getProperty(forecast[i].getCondition());
            if (iconPath == null)
                iconPath = iconProps.getProperty("Unknown");
            if (iconPath == null)
                continue;
            URL url = Loader.getResource(iconPath.trim());
            if (url != null)
                icon[i].setImage(new Image(url.toString()));
        }
    }

    private Scene createScene() {
        Region root = createForcastLayout();
        root.setBackground(null);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(BACKGROUND_COLOR);
        return scene;
    }

    private Region createForcastLayout() {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY)
                contextMenu.hide();
            if (event.getButton() == MouseButton.SECONDARY)
                contextMenu.show(vbox, event.getScreenX(), event.getScreenY());
        });
        vbox.setOnMousePressed(event -> pressed = event);
        vbox.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - pressed.getSceneX());
            stage.setY(event.getScreenY() - pressed.getSceneY());
        });

        // Date
        date[0] = new Label("?date?");
        date[0].setFont(Font.font("System", 12));
        date[0].setTextFill(Color.WHITE);
        vbox.getChildren().add(date[0]);
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
            if (i != FORECAST_DAYS - 1)
                vbox.getChildren().add(new Separator());
        }

        return vbox;
    }

    private void setUpContextMenu() {
        PreferencesDialog prefsDialog = new PreferencesDialog(stage, this, prefs);

        MenuItem prefsMenuItem = new MenuItem("Preferences...");
        prefsMenuItem.setOnAction(event -> {
            prefsDialog.setLocationRelativeToScreen();
            prefsDialog.show();
        });

        MenuItem updateMenuItem = new MenuItem("Update the forecast");
        updateMenuItem.setOnAction(event -> updateForecast());

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> {
            Config.set(Key.WIDGET_X, Double.toString(stage.getX()));
            Config.set(Key.WIDGET_Y, Double.toString(stage.getY() ));
            Config.set(Key.WEBAPI_PROVIDER, prefs.getProvider().toString());
            Config.set(Key.LOCATION, prefs.getLocation());
            try {
                Config.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });

        contextMenu.getItems().addAll(prefsMenuItem, updateMenuItem, new SeparatorMenuItem(), exitMenuItem);
    }

    public static void main(String[] args) {
        launch(WeatherWidget.class, args);
    }

}
