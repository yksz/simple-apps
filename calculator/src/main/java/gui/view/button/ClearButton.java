package gui.view.button;

import gui.controller.Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ClearButton extends CalcButton {

    public ClearButton(String name, Controller controller) {
        super(name, controller);
        setForeground(Color.WHITE);
        setBackground(Color.RED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "AC")
            controller.clear();
        else if (e.getActionCommand() == "CE")
            controller.deleteLastChar();
        else
            ;
    }

}
