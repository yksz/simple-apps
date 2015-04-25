package clock;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.IntStream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DigitalClock extends Application {

    private static final double DEFAULT_SCENE_WIDTH = 360.0;
    private static final double DEFAULT_SCENE_HEIGHT = 120.0;
    private static final int DEFAULT_FONTSIZE = 60;
    private static final double WIDTH_FONTSIZE_RATE = DEFAULT_SCENE_WIDTH / DEFAULT_FONTSIZE;
    private static final double HEIGHT_FONTSIZE_RATE = DEFAULT_SCENE_HEIGHT / DEFAULT_FONTSIZE;

    private static Map<String, Color> declaredColors = getDeclaredColors();

    private static Map<String, Color> getDeclaredColors() {
        Map<String, Color> map = new TreeMap<>();
        try {
            Class<?> clazz = Class.forName("javafx.scene.paint.Color");
            if (clazz != null) {
                for (Field field : clazz.getFields()) {
                    Object obj = field.get(null);
                    if (obj instanceof Color) {
                        map.put(field.getName(), (Color) obj);
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return map;
    }

    private final Label label = new Label();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Timeline timeline = new Timeline();

    private ZoneId timeZone = ZoneId.systemDefault();
    private String fontFamily = "System";
    private int fontSize = DEFAULT_FONTSIZE;
    private String fontColor = "BLACK";
    private String backgroundColor = "WHITESMOKE";

    private Stage stage;
    private final ContextMenu contextMenu = new ContextMenu();
    private double deltaWidth, deltaHeight;

    public void start() throws Exception {
        start(new Stage());
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setUpContextMenu();
        setUpTimeline();
        Scene scene = createScene();
        stage.setScene(scene);
        stage.setOnShown(event -> {
            deltaWidth = stage.getWidth() - scene.getWidth();
            deltaHeight = stage.getHeight() - scene.getHeight();
        });
        stage.setOnCloseRequest(event -> {
            timeline.stop();
            storePreferences();
        });
        loadPreferences();
        stage.show();
    }

    private Scene createScene() {
        VBox root = new VBox(label);
        root.setAlignment(Pos.CENTER);
        root.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY)
                contextMenu.hide();
            if (event.getButton() == MouseButton.SECONDARY)
                contextMenu.show(root, event.getScreenX(), event.getScreenY());
        });
        return new Scene(root, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
    }

    private void setUpTimeline() {
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now(timeZone);
        label.setText(formatter.format(now));
    }

    public void setTimeZone(String zone) {
        timeZone = ZoneId.of(zone);
        updateTime();
        stage.setTitle(zone);
    }

    public void setFont(String family) {
        fontFamily = family;
        double size = label.getFont().getSize();
        label.setFont(Font.font(family, size));
    }

    public void setFontSize(int size) {
        fontSize = size;
        String family = label.getFont().getFamily();
        label.setFont(Font.font(family, size));
        stage.setWidth(size * WIDTH_FONTSIZE_RATE + deltaWidth);
        stage.setHeight(size * HEIGHT_FONTSIZE_RATE + deltaHeight);
    }

    public void setFontColor(String color) {
        fontColor = color;
        label.setTextFill(declaredColors.get(color));
    }

    public void setBackgroundColor(String color) {
        backgroundColor = color;
        stage.getScene().getRoot().setStyle("-fx-background-color: " + color);
    }

    private void setUpContextMenu() {
        contextMenu.getItems().addAll(
                createTimeZoneMenu(),
                createFontMenu(),
                createFontSizeMenu(),
                createFontColorMenu(),
                createBackgroundColorMenu());
    }

    private Menu createTimeZoneMenu() {
        Menu timeZoneMenu = new Menu("Time Zone");
        ZoneId.getAvailableZoneIds().stream().sorted().forEach(zone -> {
            MenuItem menuItem = new MenuItem(zone.toString());
            menuItem.setOnAction(event -> setTimeZone(zone.toString()));
            timeZoneMenu.getItems().add(menuItem);
        });
        return timeZoneMenu;
    }

    private Menu createFontMenu() {
        Menu fontMenu = new Menu("Font");
        Font.getFamilies().stream().forEach(fontFamily -> {
            MenuItem menuItem = new MenuItem(fontFamily);
            menuItem.setOnAction(event -> setFont(fontFamily));
            fontMenu.getItems().add(menuItem);
        });
        return fontMenu;
    }

    private Menu createFontSizeMenu() {
        Menu fontSizeMenu = new Menu("Font Size");
        IntStream.rangeClosed(8, 200)
                .filter(n -> n % 4 == 0)
                .forEach(fontSize -> {
                    MenuItem menuItem = new MenuItem(String.valueOf(fontSize));
                    menuItem.setOnAction(event -> setFontSize(fontSize));
                    fontSizeMenu.getItems().add(menuItem);
                });
        return fontSizeMenu;
    }

    private Menu createFontColorMenu() {
        Menu fontColorMenu = new Menu("Font Color");
        declaredColors.forEach((name, color) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> setFontColor(name));
            menuItem.setStyle("-fx-background-color: " + name);
            fontColorMenu.getItems().add(menuItem);
        });
        return fontColorMenu;
    }

    private Menu createBackgroundColorMenu() {
        Menu backgroundColorMenu = new Menu("Background Color");
        declaredColors.forEach((name, color) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> setBackgroundColor(name));
            menuItem.setStyle("-fx-background-color: " + name);
            backgroundColorMenu.getItems().add(menuItem);
        });
        return backgroundColorMenu;
    }

    private void loadPreferences() {
        try {
            Preferences.load();
        } catch (IOException e) {
        }
        stage.setX(Double.parseDouble(Preferences.get(Preferences.POINT_X, stage.getX())));
        stage.setY(Double.parseDouble(Preferences.get(Preferences.POINT_Y, stage.getY())));
        setTimeZone(Preferences.get(Preferences.TIME_ZONE, timeZone));
        setFont(Preferences.get(Preferences.FONT_FAMILY, fontFamily));
        setFontSize(Integer.parseInt(Preferences.get(Preferences.FONT_SIZE, fontSize)));
        setFontColor(Preferences.get(Preferences.FONT_COLOR, fontColor));
        setBackgroundColor(Preferences.get(Preferences.BACKGROUND_COLOR, backgroundColor));
    }

    private void storePreferences() {
        Preferences.set(Preferences.POINT_X, stage.getX());
        Preferences.set(Preferences.POINT_Y, stage.getY());
        Preferences.set(Preferences.TIME_ZONE, timeZone);
        Preferences.set(Preferences.FONT_FAMILY, fontFamily);
        Preferences.set(Preferences.FONT_SIZE, fontSize);
        Preferences.set(Preferences.FONT_COLOR, fontColor);
        Preferences.set(Preferences.BACKGROUND_COLOR, backgroundColor);
        try {
            Preferences.store();
        } catch (IOException e) {
        }
    }

    private static enum Preferences {
        POINT_X,
        POINT_Y,
        TIME_ZONE,
        FONT_FAMILY,
        FONT_SIZE,
        FONT_COLOR,
        BACKGROUND_COLOR,
        ;

        private static final String PREFERENCES_FILE = "digitalclock.properties";
        private static final Properties props = new Properties();

        static <T> String get(Preferences key, T defaultValue) {
            return props.getProperty(key.toString(), defaultValue.toString());
        }

        static <T> void set(Preferences key, T value) {
            props.setProperty(key.toString(), value.toString());
        }

        static void load() throws IOException {
            props.load(new FileInputStream(PREFERENCES_FILE));
        }

        static void store() throws IOException {
            try (OutputStream out = new FileOutputStream(PREFERENCES_FILE)) {
                props.store(out, null);
            }
        }

        private final String name;

        private Preferences() {
            name = name().toLowerCase().replaceAll("_", ".");
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
