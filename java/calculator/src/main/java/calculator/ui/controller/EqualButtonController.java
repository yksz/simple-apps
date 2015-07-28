package calculator.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import calculator.ui.model.Expression;

public class EqualButtonController implements ActionListener {

    private final Expression expr;

    public EqualButtonController(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        expr.calculate();
    }

}
