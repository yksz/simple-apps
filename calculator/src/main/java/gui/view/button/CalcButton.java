package gui.view.button;

import gui.controller.Controller;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public abstract class CalcButton extends JButton implements ActionListener {

    protected Controller controller;

    public CalcButton(String name, Controller controller) {
        super(name);
        this.controller = controller;
        setPreferredSize(new Dimension(48, 36));
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.append(getText());
    }

}
