package interpreter;

import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.script.ScriptEngine;

public abstract class Interpreter {

    protected final ScriptEngine engine;

    public Interpreter(ScriptEngine engine) {
        this.engine = Objects.requireNonNull(engine, "engine must not be null");
    }

    public void setWriter(Writer writer) {
        engine.getContext().setWriter(writer);
    }

    public void setErrorWriter(Writer writer) {
        engine.getContext().setErrorWriter(writer);
    }

    public abstract void initialize();

    protected void initialize(String resourceName) {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource(resourceName);
        if (url == null) {
            System.err.println("Resource not found: " + resourceName);
            return;
        }
        try {
            List<String> lines = Files.readAllLines(Paths.get(url.toURI()));
            for (String line : lines)
                evaluateWithScriptEngine(line, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void evaluate(String str);

    protected void evaluateWithScriptEngine(String script) {
        evaluateWithScriptEngine(script, result -> System.out.println("==> " + result));
    }

    protected void evaluateWithScriptEngine(String script, Consumer<Object> func) {
        try {
            Object result = engine.eval(script);
            if (func != null)
                func.accept(result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
