package gui.controller;

import java.math.BigDecimal;

import logic.calculator.Calculator;
import logic.parser.SyntaxException;
import gui.model.Answer;
import gui.model.Expression;
import gui.model.Model;
import gui.view.View;

public class Controller {

    private final Expression expression;
    private final Answer answer;

    private final View view;

    public Controller(Model model, View view) {
        this.expression = model.getExpression();
        this.answer = model.getAnswer();
        this.view = view;
    }

    public void append(String str) {
        expression.append(str);
        view.update();
    }

    public void calculate() {
        try {
            BigDecimal ans = Calculator.calculate(expression.getStr());
            answer.setStr(ans.toString());
            answer.deleteDotZero();
        } catch (ArithmeticException e) {
            e.printStackTrace();
            answer.setStr("Math ERROR");
        } catch (SyntaxException e) {
            e.printStackTrace();
            answer.setStr("Syntax ERROR");
        }
        view.update();
    }

    public void clear() {
        expression.clear();
        view.update();
    }

    public void deleteLastChar() {
        expression.deleteLastChar();
        view.update();
    }

}
