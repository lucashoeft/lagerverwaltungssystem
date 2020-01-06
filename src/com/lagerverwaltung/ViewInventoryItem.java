package com.lagerverwaltung;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewInventoryItem {

    private FileHandler fileHandler = new FileHandler();

    private InventoryItem inventoryItem;

    private Boolean inventoryUpdated = false;
    private InventoryItemInputPanel inputPanel = new InventoryItemInputPanel(App.getInventory().getCategoryMap());
    private JButton leftButton = new JButton("Bearbeiten");
    private JButton rightButton = new JButton("Löschen");

    private JDialog dialog;

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

    class ButtonListener implements ActionListener {
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

    public Boolean isInventoryUpdated() {
        return this.inventoryUpdated;
    }

    private void showErrorOptionPane(String message) {
        final JDialog messageDialog = new JDialog();
        messageDialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(messageDialog,message,"Fehler beim Bearbeiten eines Artikels",JOptionPane.ERROR_MESSAGE);
    }
}