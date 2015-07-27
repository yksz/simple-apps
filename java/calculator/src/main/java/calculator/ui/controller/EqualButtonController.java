package calculator.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import calculator.ui.model.Model;

public class EqualButtonController implements ActionListener {

    private final Model model;

    public EqualButtonController(Model model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.calculate();
    }

}
