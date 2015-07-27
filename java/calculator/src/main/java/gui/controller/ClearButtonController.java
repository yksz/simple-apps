package gui.controller;

import gui.model.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
