package calculator.core.lexer;

import java.util.NoSuchElementException;

import calculator.core.lexer.Token.Type;

public class Tokenizer {

    private int currentPosition;
    private final int maxPosition;
    private final String str;

    public Tokenizer(String str) {
        if (str == null)
            throw new NullPointerException("str must not be null");

        this.str = str;
        this.maxPosition = str.length();
    }

    public boolean hasMoreTokens() {
        return currentPosition < maxPosition;
    }

    public Token nextToken() {
        if (currentPosition >= maxPosition)
            throw new NoSuchElementException();

        return scanToken();
    }

    private Token scanToken() {
        int startPosition = currentPosition;

        while (true) {
            if (currentPosition >= maxPosition)
                break;

            char ch = str.charAt(currentPosition);
            currentPosition++;
            if (isParenthesis(ch)) {
                String text = str.substring(startPosition, currentPosition);
                return new Token(Type.PARENTHESIS, text);
            } else if (isSymbol(ch)) {
                currentPosition = scanSymbolToken(currentPosition);
                String text = str.substring(startPosition, currentPosition);
                return new Token(Type.SYMBOL, text);
            } else if (isNumber(ch)) {
                currentPosition = scanNumberToken(currentPosition);
                String text = str.substring(startPosition, currentPosition);
                return new Token(Type.NUMBER, text);
            } else if (isName(ch)) {
                currentPosition = scanNameToken(currentPosition);
                String text = str.substring(startPosition, currentPosition);
                return new Token(Type.NAME, text);
            } else {
            }
        }

        String text = str.substring(startPosition, currentPosition);
        return new Token(Type.UNKNOWN, text);
    }

    private boolean isParenthesis(char c) {
        return c == '(' || c == ')';
    }

    private boolean isSymbol(char c) {
        return match(c, "\\W");
    }

    private boolean isNumber(char c) {
        return match(c, "\\d");
    }

    private boolean isName(char c) {
        return match(c, "[a-zA-Z_]");
    }

    private boolean match(char c, String regex) {
        String s = String.valueOf(c);
        return s.matches(regex);
    }

    private int scanSymbolToken(int i) {
        return scanMatchedToken(i, "[\\W&&[^()]]");
    }

    private int scanNumberToken(int i) {
        return scanMatchedToken(i, "[\\d\\.]");
    }

    private int scanNameToken(int i) {
        return scanMatchedToken(i, "\\w");
    }

    private int scanMatchedToken(int i, String regex) {
        int position = i;
        while (true) {
            if (position >= maxPosition)
                break;
            char ch = str.charAt(position);
            if (match(ch, regex)) {
                position++;
            } else {
                break;
            }
        }
        return position;
    }

}
