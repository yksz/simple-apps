package calculator.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import calculator.ui.model.Expression;

public class CalcButtonController implements ActionListener {

    private final Expression expr;
    private final JButton button;

    public CalcButtonController(Expression expr, JButton button) {
        this.expr = expr;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        expr.append(button.getText());
    }

}
