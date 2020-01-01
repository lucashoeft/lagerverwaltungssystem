import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewInventoryItemDialog extends JDialog {

    private FileHandler fileHandler = new FileHandler();

    private InventoryItem inventoryItem;

    Boolean inventoryUpdated = false;
    private InventoryItemInputPanel inputPanel = new InventoryItemInputPanel(App.getInventory().getCategoryMap());
    private JButton leftButton = new JButton("Bearbeiten");
    private JButton rightButton = new JButton("Löschen");

    public ViewInventoryItemDialog(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;

        this.setResizable(false);
        this.setTitle("Artikel anzeigen");

        JPanel buttonPanel = new JPanel();

        ActionListener buttonListener = new ButtonListener();
        leftButton.addActionListener(buttonListener);
        rightButton.addActionListener(buttonListener);

        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);

        inputPanel.setDescription(this.inventoryItem.description);
        inputPanel.setCategory(this.inventoryItem.category);
        inputPanel.setStock(this.inventoryItem.stock);
        inputPanel.setItemLocation(this.inventoryItem.location);
        inputPanel.setWeight(this.inventoryItem.weight);
        inputPanel.setPrice(this.inventoryItem.price);

        inputPanel.setIsEnabled(false);
        this.add(inputPanel, BorderLayout.CENTER);

        this.add(buttonPanel, BorderLayout.SOUTH);

        this.pack();

    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == leftButton) {
                if (leftButton.getText() == "Bearbeiten") {
                    setTitle("Artikel bearbeiten");

                    inputPanel.setIsEnabled(true);
                    leftButton.setText("Abbrechen");
                    rightButton.setText("Speichern");
                } else if (leftButton.getText() == "Abbrechen") {
                    setTitle("Artikel anzeigen");

                    inputPanel.setDescription(inventoryItem.description);
                    inputPanel.setCategory(inventoryItem.category);
                    inputPanel.setStock(inventoryItem.stock);
                    inputPanel.setItemLocation(inventoryItem.location);
                    inputPanel.setWeight(inventoryItem.weight);
                    inputPanel.setPrice(inventoryItem.price);

                    inputPanel.setIsEnabled(false);
                    leftButton.setText("Bearbeiten");
                    rightButton.setText("Löschen");
                }
            }
            if (e.getSource() == rightButton) {
                if (rightButton.getText() == "Löschen") {
                    final JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    if (JOptionPane.showConfirmDialog(dialog,"Wollen Sie den Artikel wirklich löschen?", "Artikel löschen", JOptionPane.YES_NO_OPTION) == 0) {
                        if (App.getInventory().deleteItem(inventoryItem.description)) {
                            fileHandler.storeInventoryInCSV(App.getInventory());
                            inventoryUpdated = true;
                            dispose();
                        } else {
                            final JDialog errorDialog = new JDialog();
                            errorDialog.setAlwaysOnTop(true);
                            JOptionPane.showMessageDialog(errorDialog,"Der Artikel konnte nicht gelöscht werden!","Fehler beim Löschen eines Artikels",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (rightButton.getText() == "Speichern") {
                    try {
                        if (isValidInput()) {
                            try {
                                InventoryItem item = new InventoryItem(
                                        inputPanel.getDescription(),
                                        inputPanel.getCategory(),
                                        inputPanel.getStock(),
                                        inputPanel.getItemLocation(),
                                        inputPanel.getWeight(),
                                        inputPanel.getPrice()
                                );
                                App.getInventory().deleteItem(inventoryItem.description);
                                if (App.getInventory().addNewItem(item)) {
                                    setTitle("Artikel anzeigen");
                                    inventoryItem = item;
                                    inventoryUpdated = true;
                                    inputPanel.setIsEnabled(false);
                                    leftButton.setText("Bearbeiten");
                                    rightButton.setText("Löschen");
                                    fileHandler.storeInventoryInCSV(App.getInventory());
                                } else {
                                    App.getInventory().addNewItem(inventoryItem);
                                    showErrorOptionPane();
                                }
                            } catch (NumberFormatException err) {
                                showErrorOptionPane();
                            }
                        } else {
                            showErrorOptionPane();
                        }

                    } catch (NumberFormatException err) {
                        showErrorOptionPane();
                    }
                }
            }
        }
    }

    /**
     * @return true if all Fields are entered without error else false
     */
    private Boolean isValidInput() {
        if (isValidDescription() && isValidStock() && isValidLocation() && isValidWeight() && isValidPrice()) {
            return true;
        }
        return false;
    }

    /**
     * @return true if description is entered without error else false
     */
    private Boolean isValidDescription() {
        if (inputPanel.getDescription().matches("[a-zA-ZöäüÖÄÜß0-9()!?.\\-]{1,256}") ) {
            return true;
        }
        return false;
    }

    /**
     * @return true if stock is entered without error else false
     */
    private Boolean isValidStock() {
        try {
            if (inputPanel.getStock() >= 0 && inputPanel.getStock() <= 100_000_000) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return true if location is entered without error else false
     */
    private Boolean isValidLocation() {
        if (inputPanel.getItemLocation().matches("[0-9]{6}")) {
            return true;
        }
        return false;
    }

    /**
     * @return true if weight is entered without error else false
     */
    private Boolean isValidWeight() {
        if (inputPanel.getWeight() >= 1 && inputPanel.getWeight() <= 100000000) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if price is entered without error else false
     */
    private Boolean isValidPrice() {
        if (inputPanel.getPrice() >= 1 && inputPanel.getPrice() <= 99_99_900) {
            return true;
        } else {
            return false;
        }
    }

    private void showErrorOptionPane() {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog,"Eingegebene Werte sind fehlerhaft. Bitte überprüfen Sie Ihre Eingabe!","Fehler beim Erstellen eines Artikels",JOptionPane.ERROR_MESSAGE);
    }
}