package gui.controller;

import gui.model.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CalcButtonController implements ActionListener {

    private final Model model;
    private final JButton button;

    public CalcButtonController(Model model, JButton button) {
        this.model = model;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.appendToExpression(button.getText());
    }

}
