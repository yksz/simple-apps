package calculator.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import calculator.ui.controller.CalcButtonController;
import calculator.ui.controller.ClearButtonController;
import calculator.ui.controller.EqualButtonController;
import calculator.ui.model.Expression;
import calculator.ui.model.Model;
import calculator.ui.view.button.ClearButton;
import calculator.ui.view.button.EqualButton;
import calculator.ui.view.button.NumberButton;
import calculator.ui.view.button.OperatorButton;

@SuppressWarnings("serial")
public class View extends JFrame implements Observer {

    private static final String LOOK_AND_FEEL = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    private static final String TITLE = "Calculator";

    private final Model model;

    private JTextField textField = new JTextField("");
    private JPanel operationPanel = new JPanel();

    public View() {
        this.model = new Model();
        model.addObserver(this);

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

    @Override
    public void update(Observable o, Object arg) {
        Expression expr = model.getExpression();
        textField.setText(expr.toString());
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
            JButton button = new ClearButton("AC");
            setGridBag(operationPanel, button, layout, constraints, 4, 0, 1, 1);
            button.addActionListener(new ClearButtonController(model));
        }
        // "CE"
        {
            JButton button = new ClearButton("CE");
            setGridBag(operationPanel, button, layout, constraints, 3, 0, 1, 1);
            button.addActionListener(new ClearButtonController(model));
        }
        // "0"
        {
            JButton button = new NumberButton("0");
            setGridBag(operationPanel, button, layout, constraints, 0, 4, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "1"
        {
            JButton button = new NumberButton("1");
            setGridBag(operationPanel, button, layout, constraints, 0, 3, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "2"
        {
            JButton button = new NumberButton("2");
            setGridBag(operationPanel, button, layout, constraints, 1, 3, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "3"
        {
            JButton button = new NumberButton("3");
            setGridBag(operationPanel, button, layout, constraints, 2, 3, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "4"
        {
            JButton button = new NumberButton("4");
            setGridBag(operationPanel, button, layout, constraints, 0, 2, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "5"
        {
            JButton button = new NumberButton("5");
            setGridBag(operationPanel, button, layout, constraints, 1, 2, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "6"
        {
            JButton button = new NumberButton("6");
            setGridBag(operationPanel, button, layout, constraints, 2, 2, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "7"
        {
            JButton button = new NumberButton("7");
            setGridBag(operationPanel, button, layout, constraints, 0, 1, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "8"
        {
            JButton button = new NumberButton("8");
            setGridBag(operationPanel, button, layout, constraints, 1, 1, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "9"
        {
            JButton button = new NumberButton("9");
            setGridBag(operationPanel, button, layout, constraints, 2, 1, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "."
        {
            JButton button = new NumberButton(".");
            setGridBag(operationPanel, button, layout, constraints, 1, 4, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "+"
        {
            JButton button = new OperatorButton("+");
            setGridBag(operationPanel, button, layout, constraints, 3, 1, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "-"
        {
            JButton button = new OperatorButton("-");
            setGridBag(operationPanel, button, layout, constraints, 4, 1, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "*"
        {
            JButton button = new OperatorButton("*");
            setGridBag(operationPanel, button, layout, constraints, 3, 2, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "/"
        {
            JButton button = new OperatorButton("/");
            setGridBag(operationPanel, button, layout, constraints, 4, 2, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "%"
        {
            JButton button = new OperatorButton("%");
            setGridBag(operationPanel, button, layout, constraints, 3, 3, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "^"
        {
            JButton button = new OperatorButton("^");
            setGridBag(operationPanel, button, layout, constraints, 4, 3, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "("
        {
            JButton button = new OperatorButton("(");
            setGridBag(operationPanel, button, layout, constraints, 3, 4, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // ")"
        {
            JButton button = new OperatorButton(")");
            setGridBag(operationPanel, button, layout, constraints, 4, 4, 1, 1);
            button.addActionListener(new CalcButtonController(model, button));
        }
        // "="
        {
            JButton button = new EqualButton("=");
            setGridBag(operationPanel, button, layout, constraints, 2, 4, 1, 1);
            button.addActionListener(new EqualButtonController(model));
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
