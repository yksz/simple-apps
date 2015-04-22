package console;

import interpreter.Interpreter;

import java.io.PrintStream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Console extends Application {

    private static final String TITLE = "JavaFX Console";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final double OPACITY = 0.9;
    private static final String CONSOLE_CSS_PATH = "console/console.css";

    private final TextArea textArea = new TextArea();
    private final TextField textField = new TextField();
    private final History<String> history = new History<>(100);
    private final Interpreter interpreter = new Interpreter();

    @Override
    public void start(Stage stage) throws Exception {
        setUpStage(stage);
        try {
            interpreter.initialize();
        } catch (Exception e) {
        }
        stage.show();
        textField.requestFocus();
    }

    private void setUpStage(Stage stage) {
        stage.setTitle(TITLE);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setOpacity(OPACITY);
        stage.setScene(createScene());
    }

    private Scene createScene() {
        setUpTextArea();
        setUpTextField();
        Scene scene = new Scene(new VBox(textArea, textField), null);
        textArea.minHeightProperty().bind(scene.heightProperty().subtract(textField.heightProperty()));
        scene.getStylesheets().add(CONSOLE_CSS_PATH);
        return scene;
    }

    private void setUpTextArea() {
        TextAreaOutputStream out = new TextAreaOutputStream(textArea);
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
        textArea.setEditable(false);
        textArea.setWrapText(true);
    }

    private void setUpTextField() {
        textField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case ENTER:
                handleEnterKeyEvent();
                break;
            case UP:
                handleUpKeyEvent();
                break;
            case DOWN:
                handleDownKeyEvent();
                break;
            default:
                break;
            }
        });
    }

    private void handleEnterKeyEvent() {
        String input = textField.getText();
        if (!input.trim().isEmpty() && !input.equals(history.getLast()))
            history.add(input);
        textField.setText("");
        interpreter.evaluate(input);
    }

    private void handleUpKeyEvent() {
        String input = history.undo();
        if (input != null)
            textField.setText(input);
    }

    private void handleDownKeyEvent() {
        String input = history.redo();
        if (input != null)
            textField.setText(input);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
