package gui.controller;

import java.math.BigDecimal;

import calculator.Calculator;
import calculator.parser.SyntaxException;
import gui.model.Answer;
import gui.model.Expression;
import gui.model.Model;
import gui.view.View;

public class Controller {

    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void append(String str) {
        Expression expr = model.getExpression();
        expr.append(str);
        view.update();
    }

    public void calculate() {
        Expression expr = model.getExpression();
        Answer ans;
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
        model.setAnswer(ans);
        view.update();
    }

    public void clear() {
        Expression expr = model.getExpression();
        expr.clear();
        view.update();
    }

    public void deleteLastChar() {
        Expression expr = model.getExpression();
        expr.deleteLastChar();
        view.update();
    }

}
