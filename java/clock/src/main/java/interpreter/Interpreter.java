package interpreter;

import groovy.lang.GroovyShell;

import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Interpreter {

    private static final String INIT_GROOVY_PATH = "interpreter/init.groovy";

    private final GroovyShell groovy = new GroovyShell();
    private String imports = "";

    public Interpreter() {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource(INIT_GROOVY_PATH);
        if (url == null)
            return;
        try {
            List<String> lines = Files.readAllLines(Paths.get(url.toURI()));
            for (String line : lines)
                evaluate(line);
        } catch (Exception e) {
        }
    }

    public void evaluate(String str) {
        Objects.requireNonNull(str, "str must not be null");

        String text = str.trim();
        System.out.println("> " + text);
        if (text.equals("")) {
            // ignore
        } else if (text.equals(":exit")) {
            System.exit(0);
        } else if (text.equals(":help")) {
            showHelpMessage();
        } else if (text.equals(":imports")) {
            System.out.println(imports);
        } else if (text.startsWith(":method ")) {
            handleMethodCommand(text);
        } else if (text.startsWith("import ")) {
            evaluateWithGroovy(text, () -> imports += text + ";\n");
        } else {
            evaluateWithGroovy(text);
        }
    }

    private void evaluateWithGroovy(String text) {
        evaluateWithGroovy(text, null);
    }

    private void evaluateWithGroovy(String text, Runnable func) {
        try {
            Object result = groovy.evaluate(imports + text);
            System.out.println("==> " + result);
            if (func != null) func.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void showHelpMessage() {
        System.out.println(":exit       Exit the interpreter");
        System.out.println(":help       Display this help message");
        System.out.println(":imports    Show imports");
        System.out.println("import      Import a class into the namespace");
        System.out.println(":method     Show methods of a specified class");
        System.out.println();
    }

    private void handleMethodCommand(String text) {
        String[] strs = text.substring(":method ".length()).trim().split(" ");
        try {
            showMethod(Class.forName(strs[0]), strs.length > 1 ? strs[1] : null);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
    }

    private void showMethod(Class<?> clazz, String prefix) {
        List<String> methodStrings = new LinkedList<>();
        for (Method method : clazz.getMethods())
            methodStrings.add(getMethodString(method));
        Stream<String> stream = methodStrings.stream();
        if (prefix != null)
            stream = stream.filter(str -> str.startsWith(prefix));
        stream.forEach(System.out::println);
        System.out.println();
    }

    /**
     * FORMAT
     * <pre>
     *  methodName(paramType, ...) : returnType - declaringClass
     * </pre>
     */
    private String getMethodString(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getName());
        builder.append("(");
        Class<?>[] params = method.getParameterTypes();
        if (params.length != 0) {
            builder.append(getClassName(params[0].getTypeName()));
            for (int i = 1; i < params.length; i++) {
                builder.append(", ");
                builder.append(getClassName(params[i].getTypeName()));
            }
        }
        builder.append(")");
        builder.append(" : ");
        builder.append(getClassName(method.getReturnType().getTypeName()));
        builder.append(" - ");
        builder.append(getClassName(method.getDeclaringClass().getTypeName()));
        return builder.toString();
    }

    private String getClassName(String fqcn) {
        int lastIndex = fqcn.lastIndexOf(".");
        return lastIndex != -1 ? fqcn.substring(lastIndex + 1) : fqcn;
    }

}
