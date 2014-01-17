package gui.model;

public class Expression {

    private StringBuilder builder;

    public Expression() {
        builder = new StringBuilder();
    }

    public Expression append(String str) {
        builder.append(str);
        return this;
    }

    public Expression clear() {
        builder.delete(0, builder.length());
        return this;
    }

    public Expression deleteLastChar() {
        builder.deleteCharAt(builder.length() - 1);
        return this;
    }

    public String getStr() {
        return builder.toString();
    }

    public Expression setStr(String str) {
        clear();
        append(str);
        return this;
    }

}
