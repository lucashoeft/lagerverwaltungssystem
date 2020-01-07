package com.lagerverwaltung;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ViewInventoryItem class is responsible for letting the user managing the displayed inventory item. It displays
 * the attributes of the inventory item, but also let the user edit and delete the inventory item.
 */
public class ViewInventoryItem {

    /**
     * A file handler for saving the changes of the inventory item.
     */
    private FileHandler fileHandler = new FileHandler();

    /**
     * The inventory item which is shown to the user.
     */
    private InventoryItem inventoryItem;

    /**
     * The boolean value which shows if the inventory item has been updated.
     */
    private Boolean inventoryUpdated = false;

    /**
     * The panel holds the labels and text fields which are used for displaying the attributes of the selected inventory
     * item to the user.
     */
    private InventoryItemInputPanel inputPanel = new InventoryItemInputPanel(App.getInventory().getCategoryMap());

    /**
     * The buttons of the graphical user interface which can be used to edit and delete the inventory item.
     */
    private JButton leftButton = new JButton("Bearbeiten");
    private JButton rightButton = new JButton("Löschen");

    /**
     * The dialog which holds all the components of the graphical user interface.
     */
    private JDialog dialog;

    /**
     * The constructor of the ViewInventoryItem class.
     *
     * <p>As a parameter, the constructor receives the inventory item that shall be shown the user. It assigns
     * the parameter to the attribute of the class, creates the JDialog and passes the inventory item to the text
     * fields.
     *
     * @param inventoryItem the inventory item that shall be shown
     */
    public ViewInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;

        dialog = new JDialog();
        dialog.setResizable(false);
        dialog.setTitle("Artikel anzeigen");
        dialog.setSize(340,260);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel buttonPanel = new JPanel();

        ActionListener buttonListener = new ButtonListener();
        leftButton.addActionListener(buttonListener);
        rightButton.addActionListener(buttonListener);

        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);

        inputPanel.setInventoryItem(this.inventoryItem);
        inputPanel.setIsEnabled(false);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * The ButtonListener class implements the ActionListener interface and adds the functionality to edit and delete
     * the inventory item.
     */
    class ButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * <p>This method detects which button was pressed and based on the current title and reacts accordingly.
         *
         * @param e the event that occurred
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == leftButton) {
                if (leftButton.getText().equals("Bearbeiten")) {
                    dialog.setTitle("Artikel bearbeiten");

                    inputPanel.setIsEnabled(true);
                    leftButton.setText("Abbrechen");
                    rightButton.setText("Speichern");
                } else if (leftButton.getText() == "Abbrechen") {
                    dialog.setTitle("Artikel anzeigen");

                    inputPanel.setInventoryItem(inventoryItem);
                    inputPanel.setIsEnabled(false);

                    leftButton.setText("Bearbeiten");
                    rightButton.setText("Löschen");
                }
            }
            if (e.getSource() == rightButton) {
                if (rightButton.getText().equals("Löschen")) {
                    final JDialog inputDialog = new JDialog();
                    inputDialog.setAlwaysOnTop(true);
                    if (JOptionPane.showConfirmDialog(inputDialog, "Wollen Sie den Artikel wirklich löschen?", "Artikel löschen", JOptionPane.YES_NO_OPTION) == 0) {
                        if (App.getInventory().deleteItem(inventoryItem.getDescription())) {
                            fileHandler.storeInventoryInCSV(App.getInventory());
                            inventoryUpdated = true;
                            dialog.dispose();
                        } else {
                            final JDialog errorDialog = new JDialog();
                            errorDialog.setAlwaysOnTop(true);
                            JOptionPane.showMessageDialog(errorDialog, "Der Artikel konnte nicht gelöscht werden!", "Fehler beim Löschen eines Artikels", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (rightButton.getText().equals("Speichern")) {
                    try {
                        InventoryItem item = new InventoryItem(
                                inputPanel.getDescription(),
                                inputPanel.getCategory(),
                                inputPanel.getStock(),
                                inputPanel.getItemLocation(),
                                inputPanel.getWeight(),
                                inputPanel.getPrice()
                        );
                        App.getInventory().deleteItem(inventoryItem.getDescription());
                        if (App.getInventory().addNewItem(item)) {
                            dialog.setTitle("Artikel anzeigen");
                            inventoryItem = item;
                            inventoryUpdated = true;
                            inputPanel.setIsEnabled(false);
                            leftButton.setText("Bearbeiten");
                            rightButton.setText("Löschen");
                            fileHandler.storeInventoryInCSV(App.getInventory());
                        } else {
                            App.getInventory().addNewItem(inventoryItem);
                            showErrorOptionPane("Die Veränderung konnte nicht gespeichert werden.\n" +
                                    "Die Produktbezeichnung und der Lagerort müssen eindeutig sein\n" +
                                    "und das Regalgewicht darf 10.000.000 g nicht überschreiten");
                        }
                    } catch (IllegalArgumentException iae) {
                        showErrorOptionPane(iae.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Returns the boolean value if inventory updated.
     *
     * @return true if inventory updated while the inventory item was viewed
     */
    public Boolean isInventoryUpdated() {
        return this.inventoryUpdated;
    }

    /**
     * Shows the custom error message to the user above every other JDialog or JFrame.
     *
     * @param message the error message that shall be shown
     */
    private void showErrorOptionPane(String message) {
        final JDialog messageDialog = new JDialog();
        messageDialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(messageDialog,message,"Fehler beim Bearbeiten eines Artikels",JOptionPane.ERROR_MESSAGE);
    }
}