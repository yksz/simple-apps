package interpreter;

import java.util.Objects;

import javax.script.ScriptEngineManager;

public class JavaScriptInterpreter extends Interpreter {

    public JavaScriptInterpreter() {
        super(new ScriptEngineManager().getEngineByName("js"));
    }

    @Override
    public void initialize() {
        initialize("interpreter/init.js");
    }

    @Override
    public void evaluate(String str) {
        Objects.requireNonNull(str, "str must not be null");

        String text = str.trim();
        System.out.println("js> " + text);
        if (text.equals("")) {
            CommonInterpreter.handleEmptyCommand(text);
        } else if (text.equals(":exit")) {
            CommonInterpreter.handleExitCommand(text);
        } else if (text.equals(":help")) {
            showHelpMessage();
        } else if (text.startsWith(":method ")) {
            CommonInterpreter.handleMethodCommand(text);
        } else {
            evaluateWithScriptEngine(text, result -> System.out.println("==> " + result));
        }
    }

    private void showHelpMessage() {
        System.out.println(":exit      Exit the interpreter");
        System.out.println(":help      Display this help message");
        System.out.println(":method    Show methods of a specified class");
    }

}
