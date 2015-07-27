package gui.view.button;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")
public abstract class CalcButton extends JButton {

    public CalcButton(String name) {
        super(name);
        setPreferredSize(new Dimension(48, 36));
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
    }

}
