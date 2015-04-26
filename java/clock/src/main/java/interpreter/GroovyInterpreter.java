package interpreter;

import java.util.Objects;

import javax.script.ScriptEngineManager;

public class GroovyInterpreter extends Interpreter {

    private String imports = "";

    public GroovyInterpreter() {
        super(new ScriptEngineManager().getEngineByName("groovy"));
    }

    @Override
    public void initialize() {
        initialize("interpreter/init.groovy");
    }

    @Override
    public void evaluate(String str) {
        Objects.requireNonNull(str, "str must not be null");

        String text = str.trim();
        System.out.println("groovy> " + text);
        if (text.equals("")) {
            CommonInterpreter.handleEmptyCommand(text);
        } else if (text.equals(":exit")) {
            CommonInterpreter.handleExitCommand(text);
        } else if (text.equals(":help")) {
            showHelpMessage();
        } else if (text.startsWith(":method ")) {
            CommonInterpreter.handleMethodCommand(text);
        } else if (text.equals(":imports")) {
            System.out.println(imports);
        } else if (text.startsWith("import ")) {
            evaluateWithScriptEngine(text, result -> {
                System.out.println("==> " + result);
                imports += text + ";\n";
            });
        } else {
            evaluateWithScriptEngine(text);
        }
    }

    private void showHelpMessage() {
        System.out.println(":exit       Exit the interpreter");
        System.out.println(":help       Display this help message");
        System.out.println(":method     Show methods of a specified class");
        System.out.println("import      Import a class into the namespace");
        System.out.println(":imports    Show imports");
    }

}
