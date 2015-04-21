package clock;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
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

    private static final String TITLE = "Digital Clock";
    private static final double SCENE_WIDTH = 300.0;
    private static final double SCENE_HEIGHT = 120.0;
    private static final int DEFAULT_FONTSIZE = 60;
    private static final double WIDTH_FONTSIZE_RATE = SCENE_WIDTH / DEFAULT_FONTSIZE;
    private static final double HEIGHT_FONTSIZE_RATE = SCENE_HEIGHT / DEFAULT_FONTSIZE;

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
    private final ZoneId zone;

    private Stage stage;
    private final ContextMenu contextMenu = new ContextMenu();
    private double deltaWidth, deltaHeight;

    public DigitalClock() {
        this(ZoneId.systemDefault());
    }

    public DigitalClock(ZoneId zone) {
        this.zone = zone;
    }

    public DigitalClock(String zone) {
        this(ZoneId.of(zone));
    }

    public void start() throws Exception {
        start(new Stage());
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setUpContextMenu();
        setUpTimeline(stage);
        Scene scene = createScene();
        stage.setScene(scene);
        stage.setTitle(TITLE + " [" + zone + "]");
        stage.setResizable(false);
        stage.show();
        deltaWidth = stage.getWidth() - scene.getWidth();
        deltaHeight = stage.getHeight() - scene.getHeight();
    }

    private Scene createScene() {
        label.setFont(new Font(DEFAULT_FONTSIZE));
        VBox root = new VBox(label);
        root.setAlignment(Pos.CENTER);
        root.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY)
                contextMenu.hide();
            if (event.getButton() == MouseButton.SECONDARY)
                contextMenu.show(root, event.getScreenX(), event.getScreenY());
        });
        return new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private void setUpTimeline(Stage stage) {
        updateTime();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        stage.setOnCloseRequest(event -> timeline.stop());
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now(zone);
        label.setText(formatter.format(now));
    }

    public void setFont(String family) {
        double size = label.getFont().getSize();
        label.setFont(Font.font(family, size));
    }

    public void setFontSize(int size) {
        String family = label.getFont().getFamily();
        label.setFont(Font.font(family, size));
        stage.setWidth(size * WIDTH_FONTSIZE_RATE + deltaWidth);
        stage.setHeight(size * HEIGHT_FONTSIZE_RATE + deltaHeight);
    }

    public void setFontColor(String color) {
        label.setTextFill(declaredColors.get(color));
    }

    public void setBackgroundColor(String color) {
        stage.getScene().getRoot().setStyle("-fx-background-color: " + color);
    }

    private void setUpContextMenu() {
        Menu fontMenu = createFontMenu();
        Menu fontSizeMenu = createFontSizeMenu();
        Menu fontColorMenu = createFontColorMenu();
        Menu backgroundColorMenu = createBackgroundColorMenu();
        contextMenu.getItems().addAll(fontMenu, fontSizeMenu, fontColorMenu, backgroundColorMenu);
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
        IntStream.rangeClosed(8, 100)
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
            fontColorMenu.getItems().add(menuItem);
        });
        return fontColorMenu;
    }

    private Menu createBackgroundColorMenu() {
        Menu backgroundColorMenu = new Menu("Background Color");
        declaredColors.forEach((name, color) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(event -> setBackgroundColor(name));
            backgroundColorMenu.getItems().add(menuItem);
        });
        return backgroundColorMenu;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
