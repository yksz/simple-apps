package calculator.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import calculator.ui.model.Expression;

public class ClearButtonController implements ActionListener {

    private final Expression expr;

    public ClearButtonController(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "AC")
            expr.clear();
        else if (e.getActionCommand() == "CE")
            expr.deleteLastChar();
        else
            ;
    }

}
