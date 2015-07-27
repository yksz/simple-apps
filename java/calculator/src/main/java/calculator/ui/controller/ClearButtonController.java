package calculator.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import calculator.ui.model.Model;

public class ClearButtonController implements ActionListener {

    private final Model model;

    public ClearButtonController(Model model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "AC")
            model.clearExpression();
        else if (e.getActionCommand() == "CE")
            model.deleteLastCharOfExpression();
        else
            ;
    }

}
