package gui.view.button;

import gui.controller.Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class EqualButton extends CalcButton {

    public EqualButton(String name, Controller controller) {
        super(name, controller);
        setBackground(Color.ORANGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.calculate();
    }

}
