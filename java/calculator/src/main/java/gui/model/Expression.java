package gui.model;

public class Expression {

    private final StringBuilder builder;

    public Expression() {
        builder = new StringBuilder();
    }

    Expression append(String str) {
        builder.append(str);
        return this;
    }

    Expression clear() {
        builder.delete(0, builder.length());
        return this;
    }

    Expression deleteLastChar() {
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);
        return this;
    }

    Expression setString(String str) {
        clear();
        append(str);
        return this;
    }

    public String toString() {
        return builder.toString();
    }

}
