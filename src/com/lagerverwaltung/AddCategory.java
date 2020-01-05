package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AddCategoryDialog is the window used to create new categories
 */
public class AddCategory {

    /**
     * acceptButtonClicked is true when the acceptButton is pressed
     */
    private Boolean acceptButtonClicked = false;

    /**
     * Label that holds tite for categoryNameTextField
     */
    private JLabel categoryNameLabel = new JLabel("Kategoriename");

    /**
     * text field that holds the name of the new category
     */
    private JTextField categoryNameTextField = new JTextField(15);

    /**
     * button to close dialog and cancel creating category
     */
    private JButton dismissButton = new JButton("Abbrechen");

    /**
     * button to confirm input and create new category
     */
    private JButton acceptButton = new JButton("Erstellen");

    private JDialog dialog;

    /**
     * Constructor for a new AddCategoryDialog
     */
    AddCategory() {
        dialog = new JDialog();
        dialog.setTitle("Kategorie hinzufügen");
        dialog.setLayout(new GridLayout(2,1));
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setSize(340,120);
        dialog.setAlwaysOnTop(true);

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EmptyBorder(10,10,10,10));
        inputPanel.setLayout(new GridLayout(0,2,6,3));

        categoryNameTextField.setToolTipText(
                "<html>" +
                        "• Muss eindeutig sein" + "<br>" +
                        "• Maximal 256 Zeichen" + "<br>" +
                        "• Erlaubte Zeichen: Buchstaben, Zahlen" +
                "</html>");

        inputPanel.add(categoryNameLabel);
        inputPanel.add(categoryNameTextField);

        JPanel buttonPanel = new JPanel();

        ActionListener buttonListener = new ButtonListener();
        acceptButton.addActionListener(buttonListener);
        dismissButton.addActionListener(buttonListener);

        buttonPanel.add(dismissButton);
        buttonPanel.add(acceptButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * ButtonListener listens for button presses. When a button is pressed ButtonListener checks which button has been
     * pressed and what should be done.
     */
    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == dismissButton) {
                acceptButtonClicked = false;
                dialog.dispose();
            }

            if (e.getSource() == acceptButton) {
                if (isValidInput()) {
                    if (App.getInventory().addCategory(new Category(getCategoryName()))) {
                        acceptButtonClicked = true;
                        dialog.dispose();
                    } else {
                        showErrorOptionPane("Die Kategorie konnte nicht erstellt werden. Der Kategoriename muss eindeutig sein.");
                    }
                } else {
                    showErrorOptionPane("Fehlerhafter Kategoriename.\n" +
                            "• Muss eindeutig sein\n" +
                            "• Maximal 256 Zeichen\n" +
                            "• Erlaubte Zeichen: Buchstaben, Zahlen\n" +
                            "• Beispiel: Schreibwaren123");
                }
            }
        }
    }

    public Boolean isAcceptButtonClicked() {
        return this.acceptButtonClicked;
    }

    private String getCategoryName() {
        return categoryNameTextField.getText();
    }

    /**
     * @return is the entered name a valid category name?
     */
    private Boolean isValidInput() {
        if (getCategoryName().matches("[a-zA-ZöäüÖÄÜß0-9]{1,256}")) {
            return true;
        }
        return false;
    }

    private void showErrorOptionPane(String message) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog,message,"Fehler beim Erstellen einer Kategorie",JOptionPane.ERROR_MESSAGE);
    }
}