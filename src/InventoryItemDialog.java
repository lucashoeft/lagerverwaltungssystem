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
public class InventoryItemDialog extends JDialog {

    /**
     * is true when acceptButton has been clicked
     */
    public Boolean acceptButtonClicked = false;

    /**
     * Label which holds title for descriptionTextField
     */
    private JLabel descriptionLabel = new JLabel("Produktbezeichnung");

    /**
     * text field which holds the new description of the item
     */
    private JTextField descriptionTextField = new JTextField(15);

    /**
     * Label which holds title for categoryComboBox
     */
    private JLabel categoryLabel = new JLabel("Kategorie");

    /**
     * Dropdown menu so select category9
     */
    private JComboBox categoryComboBox = new JComboBox();

    /**
     * Label which holds label for stockTextField
     */
    private JLabel stockLabel = new JLabel("Lagerbestand");

    /**
     * text field which holds the new stock of the item
     */
    private JTextField stockTextField = new JTextField(15);

    /**
     * Label which golds title for locationTextField
     */
    private JLabel locationLabel = new JLabel("Lagerort");

    /**
     * text field which holds the new location of the item
     */
    private JTextField locationTextField = new JTextField(15);

    /**
     * Label which holds title for weightTextField
     */
    private JLabel weightLabel = new JLabel("Gewicht in g");

    /**
     * text field which holds the new weight of the item in gram
     */
    private JTextField weightTextField = new JTextField(15);

    /**
     * Label which holds description of priceTextField
     */
    private JLabel priceLabel = new JLabel("Preis in €");

    /**
     * text field which holds the new price of the item
     */
    private JTextField priceTextField = new JTextField(15);

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
    public InventoryItemDialog() {
        this.setResizable(false);
        this.setTitle("Artikel erstellen");

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EmptyBorder(10,10,10,10));
        inputPanel.setLayout(new GridLayout(0,2,6,3));

        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionTextField);

        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);

        for(Category cat : App.getInventory().getCategoryMap().values()){
            categoryComboBox.addItem(cat.getName());
        }

        inputPanel.add(stockLabel);
        inputPanel.add(stockTextField);

        inputPanel.add(locationLabel);
        inputPanel.add(locationTextField);

        inputPanel.add(weightLabel);
        inputPanel.add(weightTextField);

        inputPanel.add(priceLabel);
        inputPanel.add(priceTextField);

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
                                getDescription(),
                                getCategory(),
                                getStock(),
                                getItemLocation(),
                                getWeight(),
                                getPrice()
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

    private String getDescription() {
        return descriptionTextField.getText();
    }

    private String getCategory() {
        return categoryComboBox.getSelectedItem().toString();
    }

    private Integer getStock() throws Exception {
        // Bestand
        String stringValue = stockTextField.getText();

        try {
            Integer stockValue = Integer.parseInt(stringValue);
            return stockValue;
        } catch (Exception e) {
            throw new Exception();
        }
    }

    private String getItemLocation() {
        return locationTextField.getText();
    }

    private Integer getWeight() {
        String stringValue = weightTextField.getText();

        if (stringValue.contains(",")) {
            if (stringValue.matches(".*,")) {
                String value = stringValue.replace(",","");
                Integer decigramValue = Integer.parseInt(value) * 10;
                return decigramValue;
            } else if (stringValue.matches(".*,[0-9]")) {
                String value = stringValue.replace(",","");
                Integer decigramValue = Integer.parseInt(value);
                return decigramValue;
            }
        }
        Integer decigramValue = Integer.parseInt(stringValue) * 10;
        return decigramValue;
    }

    private Integer getPrice() {
        String stringValue = priceTextField.getText();
        if (stringValue.contains(",")) {
            if (stringValue.matches(".*,")) {
                String value = stringValue.replace(",", "");
                Integer centValue = Integer.parseInt(value) * 100;
                return centValue;
            } else if (stringValue.matches(".*,[0-9]")) {
                String value = stringValue.replace(",", "");
                Integer centValue = Integer.parseInt(value) * 10;
                return centValue;
            } else if (stringValue.matches(".*,[0-9][0-9]")) {
                String value = stringValue.replace(",", "");
                Integer centValue = Integer.parseInt(value);
                return centValue;
            }
        }
        String value = stringValue.replace(",","");
        Integer centValue = Integer.parseInt(value) * 100;
        return centValue;
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
        if (getDescription().matches("[a-zA-ZöäüÖÄÜß0-9()!?.\\-]{1,256}") ) {
            return true;
        }
        return false;
    }

    /**
     * @return true if stock is entered without error else false
     */
    private Boolean isValidStock() {
        try {
            if (getStock() >= 0 && getStock() <= 100_000_000) {
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
        if (getItemLocation().matches("[0-9]{6}")) {
            return true;
        }
        return false;
    }

    /**
     * @return true if weight is entered without error else false
     */
    private Boolean isValidWeight() {
        if (getWeight() >= 1 && getWeight() <= 100000000) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if price is entered without error else false
     */
    private Boolean isValidPrice() {
        if (getPrice() >= 1 && getPrice() <= 99_99_900) {
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
