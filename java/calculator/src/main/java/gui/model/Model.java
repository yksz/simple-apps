package gui.model;

import java.math.BigDecimal;
import java.util.Observable;

import calculator.Calculator;
import calculator.parser.SyntaxException;

public class Model extends Observable {

    private final Expression expr = new Expression();
    private Answer ans;

    public void calculate() {
        try {
            BigDecimal num = Calculator.calculate(expr.toString());
            ans = new Answer(num);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            ans = new Answer("Math ERROR");
        } catch (SyntaxException e) {
            e.printStackTrace();
            ans = new Answer("Syntax ERROR");
        }
        moveAnswerToExpression();
        setChanged();
        notifyObservers();
    }

    private void moveAnswerToExpression() {
        expr.setString(ans.toString());
        ans = null;
    }

    public void appendToExpression(String str) {
        expr.append(str);
        setChanged();
        notifyObservers();
    }

    public void clearExpression() {
        expr.clear();
        setChanged();
        notifyObservers();
    }

    public void deleteLastCharOfExpression() {
        expr.deleteLastChar();
        setChanged();
        notifyObservers();
    }

    public boolean hasAnswer() {
        return ans != null;
    }

    public Expression getExpression() {
        return expr;
    }

    public Answer getAnswer() {
        return ans;
    }

}
