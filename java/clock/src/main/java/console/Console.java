package console;

import interpreter.GroovyInterpreter;
import interpreter.Interpreter;
import interpreter.JavaScriptInterpreter;

import java.io.PrintStream;
import java.io.PrintWriter;

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
    private static final String CSS_FILE = "console/console.css";

    private final TextArea textArea = new TextArea();
    private final TextField textField = new TextField();
    private final TextAreaOutputStream textAreaOut = new TextAreaOutputStream(textArea);
    private final History<String> history = new History<>(100);
    private Interpreter interpreter = new GroovyInterpreter();

    public Console() {
        System.setOut(new PrintStream(textAreaOut, true));
        System.setErr(new PrintStream(textAreaOut, true));
    }

    @Override
    public void start(Stage stage) throws Exception {
        setUpInterpreter();
        setUpStage(stage);
        stage.show();
        textField.requestFocus();
    }

    private void setUpInterpreter() {
        interpreter.setWriter(new PrintWriter(textAreaOut, true));
        interpreter.setErrorWriter(new PrintWriter(textAreaOut, true));
        interpreter.initialize();
        interpreter.evaluate("");
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
        scene.getStylesheets().add(CSS_FILE);
        return scene;
    }

    private void setUpTextArea() {
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
        evaluate(input);
    }

    private void evaluate(String input) {
        if (input.equals(":groovy")) {
            System.out.println("\n Switch to Groovy interpreter");
            interpreter = new GroovyInterpreter();
            setUpInterpreter();
        } else if (input.equals(":js")) {
            System.out.println("\n Switch to JavaScript interpreter");
            interpreter = new JavaScriptInterpreter();
            setUpInterpreter();
        } else {
            interpreter.evaluate(input);
        }
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
