package calculator.core.lexer;


public class Token {

    public enum Type {
        UNKNOWN,
        PARENTHESIS,
        SYMBOL,
        NUMBER,
        NAME,
    }

    private final Type type;
    private final String text;

    public Token(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Token [type=");
        builder.append(type);
        builder.append(", text=");
        builder.append(text);
        builder.append("]");
        return builder.toString();
    }

}
