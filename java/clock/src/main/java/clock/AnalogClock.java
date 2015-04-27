package clock;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AnalogClock extends Application {

    private static final double SIZE = 80.0;
    private static final double CENTER_X = SIZE;
    private static final double CENTER_Y = SIZE;
    private static final double RADIUS= SIZE;
    private static final double DEGREES_PER_HOUR = 360 / 12;
    private static final double DEGREES_PER_MINUTE = 360 / 60;
    private static final double DEGREES_PER_SECOND = 360 / 60;

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

    private final Circle clockDial = new Circle();
    private final Label timeZoneLabel = new Label();
    private final Rotate hourHandRotate = new Rotate(0, CENTER_X, CENTER_Y);
    private final Rotate minuteHandRotate = new Rotate(0, CENTER_X, CENTER_Y);
    private final Rotate secondHandRotate = new Rotate(0, CENTER_X, CENTER_Y);

    private final Timeline timeline = new Timeline();
    private ZoneId timeZone = ZoneId.systemDefault();

    private Stage stage;
    private final ContextMenu contextMenu = new ContextMenu();
    private MouseEvent pressed;

    public void start() throws Exception {
        start(new Stage());
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setUpContextMenu();
        setUpTimeline();
        stage.setScene(createScene());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.sizeToScene();
        stage.show();
    }

    private Scene createScene() {
        Group root = new Group(
                setUpClockDial(),
                createClockCenter(),
                createTickMarks(),
                setUpTimeZoneLabel(),
                createHourHand(),
                createMinuteHand(),
                createSecondHand());
        root.setOnMouseClicked(event -> showAndHideContextMenu(root, event));
        root.setOnMousePressed(event -> pressed = event);
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - pressed.getSceneX());
            stage.setY(event.getScreenY() - pressed.getSceneY());
        });
        return new Scene(root, null);
    }

    private Node setUpClockDial() {
        clockDial.setCenterX(CENTER_X);
        clockDial.setCenterY(CENTER_Y);
        clockDial.setRadius(RADIUS - 1);
        clockDial.setFill(Color.WHITESMOKE);
        clockDial.setStroke(Color.BLACK);
        clockDial.setStrokeWidth(2);
        return clockDial;
    }

    private Node createClockCenter() {
        Circle clockCenter = new Circle();
        clockCenter.setCenterX(CENTER_X);
        clockCenter.setCenterY(CENTER_Y);
        clockCenter.setRadius(RADIUS * 0.05);
        clockCenter.setFill(Color.BLACK);
        return clockCenter;
    }

    private Node createTickMarks() {
        List<Node> tickMarks = IntStream.range(0, 60).mapToObj(n -> {
            Line line = (n % 5 == 0) ?
                    new Line(CENTER_X, RADIUS * 0.02, CENTER_X, RADIUS * 0.15) :
                    new Line(CENTER_X, RADIUS * 0.05, CENTER_X, RADIUS * 0.07);
            line.setStrokeWidth(2);
            line.getTransforms().add(new Rotate(DEGREES_PER_MINUTE * n, CENTER_X, CENTER_Y));
            return line;
        }).collect(Collectors.toList());
        return new Group(tickMarks);
    }

    private Node setUpTimeZoneLabel() {
        timeZoneLabel.setLayoutX(CENTER_X * 0.35);
        timeZoneLabel.setLayoutY(CENTER_Y * 0.55);
        setTimeZone(timeZone.toString());
        return timeZoneLabel;
    }

    private Node createHourHand() {
        Line hourHand = new Line();
        hourHand.setStartX(CENTER_X);
        hourHand.setStartY(CENTER_Y);
        hourHand.setEndX(CENTER_X);
        hourHand.setEndY(RADIUS * 0.3);
        hourHand.setStrokeWidth(3);
        hourHand.getTransforms().add(hourHandRotate);
        return hourHand;
    }

    private Node createMinuteHand() {
        Line minuteHand = new Line();
        minuteHand.setStartX(CENTER_X);
        minuteHand.setStartY(CENTER_Y);
        minuteHand.setEndX(CENTER_X);
        minuteHand.setEndY(RADIUS * 0.1);
        minuteHand.setStrokeWidth(3);
        minuteHand.getTransforms().add(minuteHandRotate);
        return minuteHand;
    }

    private Node createSecondHand() {
        Line secondHand = new Line();
        secondHand.setStartX(CENTER_X);
        secondHand.setStartY(CENTER_Y);
        secondHand.setEndX(CENTER_X);
        secondHand.setEndY(RADIUS * 0.2);
        secondHand.setStroke(Color.RED);
        secondHand.getTransforms().add(secondHandRotate);
        return secondHand;
    }

    private void setUpTimeline() {
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void tick() {
        LocalTime time = LocalTime.now(timeZone);
        hourHandRotate.setAngle(time.getHour() * DEGREES_PER_HOUR);
        minuteHandRotate.setAngle(time.getMinute() * DEGREES_PER_MINUTE);
        secondHandRotate.setAngle(time.getSecond() * DEGREES_PER_SECOND);
    }

    public void setTimeZone(String zone) {
        timeZone = ZoneId.of(zone);
        int index = zone.lastIndexOf("/");
        if (index != -1)
            zone = zone.substring(index + 1);
        timeZoneLabel.setText(zone);
        tick();
    }

    public void setBackgroundColor(String color) {
        clockDial.setFill(declaredColors.get(color));
    }

    private void setUpContextMenu() {
        contextMenu.getItems().addAll(
                createTimeZoneMenu(),
                createBackgroundColorMenu(),
                new SeparatorMenuItem(),
                createExitMenu());
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

    private Menu createBackgroundColorMenu() {
        Menu backgroundColorMenu = new Menu("Background Color");
        declaredColors.forEach((name, color) -> {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setStyle("-fx-background-color: " + name);
            menuItem.setOnAction(event -> setBackgroundColor(name));
            backgroundColorMenu.getItems().add(menuItem);
        });
        return backgroundColorMenu;
    }

    private MenuItem createExitMenu() {
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> System.exit(0));
        return exitMenuItem;
    }

    private void showAndHideContextMenu(Node anchor, MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY)
            contextMenu.hide();
        if (event.getButton() == MouseButton.SECONDARY)
            contextMenu.show(anchor, event.getScreenX(), event.getScreenY());
    }

    public static void main(String[] args) {
        launch(args);
    }

}