package gui.view;

import gui.controller.Controller;
import gui.model.Answer;
import gui.model.Expression;
import gui.model.Model;
import gui.view.button.ClearButton;
import gui.view.button.EqualButton;
import gui.view.button.NumberButton;
import gui.view.button.OperatorButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class View extends JFrame {

    private static final String LOOK_AND_FEEL = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    private static final String TITLE = "Calculator";

    private final Expression expression;
    private final Answer answer;

    private final Controller controller;

    private JTextField textField = new JTextField("");
    private JPanel operationPanel = new JPanel();

    public View() {
        Model model = new Model();
        this.expression = model.getExpression();
        this.answer = model.getAnswer();
        this.controller = new Controller(model, this);

        setLookAndFeel(LOOK_AND_FEEL);

        setupTextField();
        setupOperationPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(textField, BorderLayout.NORTH);
        panel.add(operationPanel, BorderLayout.CENTER);

        this.getContentPane().add(panel);
        this.setTitle(TITLE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update() {
        if (answer.getStr().isEmpty()) {
            textField.setText(expression.getStr());
        } else {
            textField.setText(answer.getStr());
            expression.setStr(answer.getStr());
            answer.clear();
        }
        repaint();
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(LOOK_AND_FEEL);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupTextField() {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        textField.setMargin(new Insets(4, 2, 2, 2));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
    }

    private void setupOperationPanel() {
        layoutOperationPanel();
        operationPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        operationPanel.setPreferredSize(new Dimension(240, 240));
    }

    private void layoutOperationPanel() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 100.0;
        constraints.weighty = 100.0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;

        // "AC"
        {
            JButton button = new ClearButton("AC", controller);
            setGridBag(operationPanel, button, layout, constraints, 4, 0, 1, 1);
        }
        // "CE"
        {
            JButton button = new ClearButton("CE", controller);
            setGridBag(operationPanel, button, layout, constraints, 3, 0, 1, 1);
        }
        // "0"
        {
            JButton button = new NumberButton("0", controller);
            setGridBag(operationPanel, button, layout, constraints, 0, 4, 1, 1);
        }
        // "1"
        {
            JButton button = new NumberButton("1", controller);
            setGridBag(operationPanel, button, layout, constraints, 0, 3, 1, 1);
        }
        // "2"
        {
            JButton button = new NumberButton("2", controller);
            setGridBag(operationPanel, button, layout, constraints, 1, 3, 1, 1);
        }
        // "3"
        {
            JButton button = new NumberButton("3", controller);
            setGridBag(operationPanel, button, layout, constraints, 2, 3, 1, 1);
        }
        // "4"
        {
            JButton button = new NumberButton("4", controller);
            setGridBag(operationPanel, button, layout, constraints, 0, 2, 1, 1);
        }
        // "5"
        {
            JButton button = new NumberButton("5", controller);
            setGridBag(operationPanel, button, layout, constraints, 1, 2, 1, 1);
        }
        // "6"
        {
            JButton button = new NumberButton("6", controller);
            setGridBag(operationPanel, button, layout, constraints, 2, 2, 1, 1);
        }
        // "7"
        {
            JButton button = new NumberButton("7", controller);
            setGridBag(operationPanel, button, layout, constraints, 0, 1, 1, 1);
        }
        // "8"
        {
            JButton button = new NumberButton("8", controller);
            setGridBag(operationPanel, button, layout, constraints, 1, 1, 1, 1);
        }
        // "9"
        {
            JButton button = new NumberButton("9", controller);
            setGridBag(operationPanel, button, layout, constraints, 2, 1, 1, 1);
        }
        // "."
        {
            JButton button = new NumberButton(".", controller);
            setGridBag(operationPanel, button, layout, constraints, 1, 4, 1, 1);
        }
        // "+"
        {
            JButton button = new OperatorButton("+", controller);
            setGridBag(operationPanel, button, layout, constraints, 3, 1, 1, 1);
        }
        // "-"
        {
            JButton button = new OperatorButton("-", controller);
            setGridBag(operationPanel, button, layout, constraints, 4, 1, 1, 1);
        }
        // "*"
        {
            JButton button = new OperatorButton("*", controller);
            setGridBag(operationPanel, button, layout, constraints, 3, 2, 1, 1);
        }
        // "/"
        {
            JButton button = new OperatorButton("/", controller);
            setGridBag(operationPanel, button, layout, constraints, 4, 2, 1, 1);
        }
        // "%"
        {
            JButton button = new OperatorButton("%", controller);
            setGridBag(operationPanel, button, layout, constraints, 3, 3, 1, 1);
        }
        // "^"
        {
            JButton button = new OperatorButton("^", controller);
            setGridBag(operationPanel, button, layout, constraints, 4, 3, 1, 1);
        }
        // "("
        {
            JButton button = new OperatorButton("(", controller);
            setGridBag(operationPanel, button, layout, constraints, 3, 4, 1, 1);
        }
        // ")"
        {
            JButton button = new OperatorButton(")", controller);
            setGridBag(operationPanel, button, layout, constraints, 4, 4, 1, 1);
        }
        // "="
        {
            JButton button = new EqualButton("=", controller);
            setGridBag(operationPanel, button, layout, constraints, 2, 4, 1, 1);
        }

        operationPanel.setLayout(layout);
    }

    private void setGridBag(Container container, Component component,
            GridBagLayout layout, GridBagConstraints constraints,
            int gx, int gy, int gw, int gh) {
        constraints.gridx = gx;
        constraints.gridy = gy;
        constraints.gridwidth = gw;
        constraints.gridheight = gh;
        layout.setConstraints(component, constraints);
        container.add(component);
    }

}
