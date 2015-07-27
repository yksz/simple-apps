package calculator.ui.view.button;

import java.awt.Color;

@SuppressWarnings("serial")
public class NumberButton extends CalcButton {

    public NumberButton(String name) {
        super(name);
        setForeground(Color.WHITE);
        setBackground(Color.DARK_GRAY);
    }

}
