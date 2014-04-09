package Graphics;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class Settings extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider sldRowsAmount;
    private JSlider sldCollsAmount;
    private JLabel lblRowsAmountValue;
    private JLabel lblColumnsAmountValue;
    private JLabel lblRowsAmountText;
    private JLabel lblColumnsAmountText;
    private JCheckBox cbProportionalSlide;

    private ViewController controller;
    private int rowsAmount;
    private int collsAmount;

    public Settings(ViewController controller) {
        this.controller = controller;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setContentPane(contentPane);
        setSize(800, 500);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        sldRowsAmount.setValue(controller.getRowsAmount());
        sldCollsAmount.setValue(controller.getColumnsAmount());
        lblRowsAmountValue.setText(String.valueOf(controller.getRowsAmount()));
        lblColumnsAmountValue.setText(String.valueOf(controller.getColumnsAmount()));
        addActionListeners();
    }

    private void addActionListeners(){
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        sldRowsAmount.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rowsAmount = sldRowsAmount.getValue();
                if (cbProportionalSlide.isSelected())
                    sldCollsAmount.setValue(rowsAmount);
                lblRowsAmountValue.setText(String.valueOf(rowsAmount));
            }
        });

        sldCollsAmount.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                collsAmount = sldCollsAmount.getValue();
                if (cbProportionalSlide.isSelected())
                    sldRowsAmount.setValue(collsAmount);
                lblColumnsAmountValue.setText(String.valueOf(collsAmount));
            }
        });

        // call onCancel() when cross is clicked
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        controller.resizeField(rowsAmount, collsAmount);
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
