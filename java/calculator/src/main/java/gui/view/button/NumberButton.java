package gui.view.button;

import gui.controller.Controller;

import java.awt.Color;

@SuppressWarnings("serial")
public class NumberButton extends CalcButton{

    public NumberButton(String name, Controller controller) {
        super(name, controller);
        setForeground(Color.WHITE);
        setBackground(Color.DARK_GRAY);
    }

}
