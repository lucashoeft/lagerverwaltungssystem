package com.lagerverwaltung;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AddInventoryItem class is responsible for letting the user create new inventory items.
 */
public class AddInventoryItem {

    /**
     * The boolean value which shows if the create button was clicked.
     */
    private Boolean acceptButtonClicked = false;

    /**
     * The panel holds the labels and text fields which are used for putting the attributes in for the new inventory
     * item.
     */
    private InventoryItemInputPanel inputPanel = new InventoryItemInputPanel(App.getInventory().getCategoryMap());

    /**
     * The button to close dialog without creating a new inventory item.
     */
    private JButton dismissButton = new JButton("Abbrechen");

    /**
     * The button to trigger the save event and to close the dialog.
     */
    private JButton acceptButton = new JButton("Erstellen");

    /**
     * The dialog which holds all the components of the graphical user interface.
     */
    private JDialog dialog;

    /**
     * The constructor for the AddInventoryItem class.
     *
     * <p>It initialises the dialog window and all components of the graphical user interface. It also connects the
     * buttons to the action listeners.
     */
    public AddInventoryItem() {
        dialog = new JDialog();
        dialog.setResizable(false);
        dialog.setTitle("Artikel erstellen");
        dialog.setSize(340,260);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

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
     * Returns the boolean value if create button was clicked.
     *
     * @return true if create button was clicked
     */
    public Boolean isAcceptButtonClicked() {
        return this.acceptButtonClicked;
    }

    /**
     * The ButtonListener class implements the ActionListener interface and adds the functionality to dismiss and create
     * a new inventory item.
     */
    class ButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * <p>This method detects which button was pressed and reacts accordingly. It tries to save the inventory item
         * if the accept button was clicked and shows an error message if saving was not possible.
         *
         * @param e the event that occurred
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == dismissButton) {
                acceptButtonClicked = false;
                dialog.dispose();
            }

            if (e.getSource() == acceptButton) {
                try {
                    InventoryItem item = new InventoryItem(
                            inputPanel.getDescription(),
                            inputPanel.getCategory(),
                            inputPanel.getStock(),
                            inputPanel.getItemLocation(),
                            inputPanel.getWeight(),
                            inputPanel.getPrice()
                    );
                    if (App.getInventory().addNewItem(item)) {
                        acceptButtonClicked = true;
                        dialog.dispose();
                    } else {
                        showErrorOptionPane("Der Artikel konnte dem Lagerbestand nicht hinzugefügt werden.\n" +
                                "Die Produktbezeichnung und der Lagerort müssen eindeutig sein\n" +
                                "und das Regalgewicht darf 10.000.000 g nicht überschreiten");
                    }
                } catch (IllegalArgumentException iae){
                    showErrorOptionPane(iae.getMessage());
                } catch (NullPointerException npe) {
                    showErrorOptionPane("Es muss erst eine Kategorie erstellt werden, bevor ein Artikel erstellt werden kann.");
                }
            }
        }
    }

    /**
     * Shows the custom error message to the user above every other JDialog or JFrame.
     *
     * @param message the error message that shall be shown
     */
    private void showErrorOptionPane(String message) {
        final JDialog messageDialog = new JDialog();
        messageDialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(messageDialog,message,"Fehler beim Erstellen eines Artikels",JOptionPane.ERROR_MESSAGE);
    }
}
