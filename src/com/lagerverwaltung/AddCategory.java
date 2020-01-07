package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AddCategory class is responsible for letting users create new categories. It checks if the category name is a
 * valid name and then handles the saving process. If creating the new category is not possible, it shows an appropriate
 * error message to the user.
 */
public class AddCategory {

    /**
     * The flag which tells if the accept button was the trigger to close the dialog window.
     */
    private Boolean acceptButtonClicked = false;

    /**
     * The label explains to the users what value he has to put into the text field.
     */
    private JLabel categoryNameLabel = new JLabel("Kategoriename");

    /**
     * The text field holds the name of the new category.
     */
    private JTextField categoryNameTextField = new JTextField(15);

    /**
     * The button to cancel and close the dialog.
     */
    private JButton dismissButton = new JButton("Abbrechen");

    /**
     * The button to confirm input which triggers then the saving process.
     */
    private JButton acceptButton = new JButton("Erstellen");

    private JDialog dialog;

    /**
     * The constructor for a the AddCategory class.
     *
     * <p>It creates the JDialog and adds the components of the graphical user interface. It also assigns the inner
     * ButtonListener class to the buttons.
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
     * The ButtonListener class implements the ActionListener interface and adds the functionality to create a new
     * category.
     */
    class ButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * <p>This method detects which button was pressed and based on the button it reacts accordingly. It triggers
         * the saving process if the save button was clicked, but also shows an error message if creating a new category
         * was not possible
         *
         * @param e the event that occurred
         */
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

    /**
     * Returns the boolean value if the create button was clicked.
     *
     * @return true if create button was clicked
     */
    public Boolean isAcceptButtonClicked() {
        return this.acceptButtonClicked;
    }

    /**
     * Returns the category name which was entered into the text field.
     *
     * @return the input of the text field
     */
    private String getCategoryName() {
        return categoryNameTextField.getText();
    }

    /**
     * Returns the boolean value if the input of the text field is allowed to be name of a category.
     *
     * <p>This method checks if the input consists only out of alphabetic characters and numbers and if it has a length between 1
     * and 256 characters.
     *
     * @return true if input is valid
     */
    private Boolean isValidInput() {
        return getCategoryName().matches("[a-zA-ZöäüÖÄÜß0-9]{1,256}");
    }

    /**
     * Shows the custom error message to the user above every other JDialog or JFrame.
     *
     * @param message the error message that shall be shown
     */
    private void showErrorOptionPane(String message) {
        final JDialog messageDialog = new JDialog();
        messageDialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(messageDialog,message,"Fehler beim Erstellen einer Kategorie",JOptionPane.ERROR_MESSAGE);
    }
}
