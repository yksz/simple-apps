package interpreter;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class CommonInterpreter {

    public static void handleEmptyCommand(String text) {
    }

    public static void handleExitCommand(String text) {
        System.exit(0);
    }

    public static void handleMethodCommand(String text) {
        String[] strs = text.substring(":method ".length()).trim().split(" ");
        try {
            showMethod(Class.forName(strs[0]), strs.length > 1 ? strs[1] : null);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
    }

    private static void showMethod(Class<?> clazz, String prefix) {
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
    private static String getMethodString(Method method) {
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

    private static String getClassName(String fqcn) {
        int lastIndex = fqcn.lastIndexOf(".");
        return lastIndex != -1 ? fqcn.substring(lastIndex + 1) : fqcn;
    }

}
