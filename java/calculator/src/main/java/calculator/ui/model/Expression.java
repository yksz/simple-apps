package calculator.ui.model;

import java.math.BigDecimal;
import java.util.Observable;

import calculator.core.Calculator;
import calculator.core.parser.SyntaxException;

public class Expression extends Observable {

    private final StringBuilder builder;

    public Expression() {
        builder = new StringBuilder();
    }

    public void append(String str) {
        builder.append(str);
        setChanged();
        notifyObservers();
    }

    public void clear() {
        builder.delete(0, builder.length());
        setChanged();
        notifyObservers();
    }

    public void deleteLastChar() {
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);
        setChanged();
        notifyObservers();
    }

    public void calculate() {
        Answer ans;
        try {
            BigDecimal num = Calculator.calculate(builder.toString());
            ans = new Answer(num);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            ans = new Answer("Math ERROR");
        } catch (SyntaxException e) {
            e.printStackTrace();
            ans = new Answer("Syntax ERROR");
        }
        set(ans.toString());
        setChanged();
        notifyObservers();
    }

    private void set(String str) {
        clear();
        append(str);
    }

    public String toString() {
        return builder.toString();
    }

}
