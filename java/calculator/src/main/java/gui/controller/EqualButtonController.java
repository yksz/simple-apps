package gui.controller;

import gui.model.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
