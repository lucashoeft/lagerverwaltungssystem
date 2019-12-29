import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * This Dialog class is opened when a user wants to create a new InventoryItem.
 *
 * @author ...
 * @version 1.0
 */
import java.util.Iterator;

/**
 *
 * InventoryItemDialog is the window which is used to create new items
 *
 * @author ...
 * @version 1.0
 */
public class AddInventoryItemDialog extends JDialog {

    /**
     * is true when acceptButton has been clicked
     */
    public Boolean acceptButtonClicked = false;

    private InventoryItemInputPanel inputPanel = new InventoryItemInputPanel();

    /**
     * Button to close dialog without any changes to the item
     */
    private JButton dismissButton = new JButton("Abbrechen");

    /**
     * Button to close dialog but save all changes if possible
     */
    private JButton acceptButton = new JButton("Erstellen");

    /**
     * Constructor for the InventoryItemDialog
     */
    public AddInventoryItemDialog() {
        this.setResizable(false);
        this.setTitle("Artikel erstellen");

        JPanel buttonPanel = new JPanel();

        ActionListener buttonListener = new ButtonListener();
        acceptButton.addActionListener(buttonListener);
        dismissButton.addActionListener(buttonListener);

        buttonPanel.add(dismissButton);
        buttonPanel.add(acceptButton);

        this.add(inputPanel, BorderLayout.CENTER);

        this.add(buttonPanel, BorderLayout.SOUTH);

        this.pack();
    }

    /**
     * ButtonListener contains all actions that are taken when certain buttons are clicked
     */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == dismissButton) {
                acceptButtonClicked = false;
                dispose();
            }

            if (e.getSource() == acceptButton) {
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
                        if (App.getInventory().addNewItem(item)) {
                            acceptButtonClicked = true;
                            dispose();
                        } else {
                            showErrorOptionPane();
                        }
                    } catch (Exception err){
                        showErrorOptionPane();
                    }
                } else {
                    showErrorOptionPane();
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
