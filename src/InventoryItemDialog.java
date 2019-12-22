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
public class InventoryItemDialog extends JDialog {

    public Boolean acceptButtonClicked = false;

    private JLabel descriptionLabel = new JLabel("Produktbezeichnung");
    private JTextField descriptionTextField = new JTextField(15);

    private JLabel categoryLabel = new JLabel("Kategorie");
    private JComboBox categoryComboBox = new JComboBox();

    private JLabel stockLabel = new JLabel("Lagerbestand");
    private JTextField stockTextField = new JTextField(15);

    private JLabel locationLabel = new JLabel("Lagerort");
    private JTextField locationTextField = new JTextField(15);

    private JLabel weightLabel = new JLabel("Gewicht in g");
    private JTextField weightTextField = new JTextField(15);

    private JLabel priceLabel = new JLabel("Preis in €");
    private JTextField priceTextField = new JTextField(15);

    private JButton dismissButton = new JButton("Abbrechen");
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
        categoryComboBox.addItem("Anspitzer");
        categoryComboBox.addItem("Federmäppchen");
        categoryComboBox.addItem("Füllfederhalter & Kugelschreiber");
        categoryComboBox.addItem("Marker & Filzstifte");
        categoryComboBox.addItem("Minen Patronen & Tintenlöscher");
        categoryComboBox.addItem("Radiergummies & Korrekturtools");
        categoryComboBox.addItem("Stifte");
        categoryComboBox.addItem("Technisches Zeichnen");

        // TODO get list of all existing categories and add to categoryComboBox

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
                    // TODO: Check if description is unique
                    // TODO: Check if location is unique
                    // TODO: Check if weight*stock is allowed
                    // TODO: If every case above is true, add element to inventory

                    acceptButtonClicked = true;
                    dispose();
                } else {
                    System.out.println("Input not valid!");
                    /* final JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    JOptionPane.showMessageDialog(dialog,"Eingegebene Werte sind fehlerhaft oder nicht vorhanden. Bitte überprüfen Sie Ihre Eingabe!","Fehler beim Erstellen eines Artikels",JOptionPane.ERROR_MESSAGE);
                    */
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
        // Produktbezeichnung (Produktname)
        String stringValue = descriptionTextField.getText();

        if (stringValue.matches("[a-zA-ZöäüÖÄÜß0-9()!?.\\-]{1,256}") ) {
            return true;
        }
        return false;
    }

    /**
     * @return true if stock is entered without error else false
     */
    private Boolean isValidStock() {
        // Bestand
        String stringValue = stockTextField.getText();

        try {
            Integer stockValue = Integer.parseInt(stringValue);
            if (stockValue >= 0 && stockValue <= 100_000_000) {
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
        // Lagerort (Lagerplatznummer)
        String stringValue = locationTextField.getText();

        if (stringValue.matches("[0-9]{6}")) {
            return true;
        }
        return false;
    }

    /**
     * @return true if weight is entered without error else false
     */
    private Boolean isValidWeight() {
        // Gewicht
        String stringValue = weightTextField.getText();

        try {
            if (stringValue.contains(",")) {
                if (stringValue.matches(".*,")) {
                    String value = stringValue.replace(",","");
                    Long decigramValue = Long.parseLong(value) * 10;
                    if (decigramValue >= 1 && decigramValue <= 100_000_000) {
                        return true;
                    }
                } else if (stringValue.matches(".*,[0-9]")) {
                    String value = stringValue.replace(",","");
                    Long decigramValue = Long.parseLong(value);
                    if (decigramValue >= 1 && decigramValue <= 100_000_000) {
                        return true;
                    }
                }
            } else {
                Long decigramValue = Long.parseLong(stringValue) * 10;
                if (decigramValue >= 1 && decigramValue <= 100_000_000) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return true if price is entered without error else false
     */
    private Boolean isValidPrice() {
        // Preis
        String stringValue = priceTextField.getText();

        try {
            if (stringValue.contains(",")) {
                if (stringValue.matches(".*,")) {
                    String value = stringValue.replace(",","");
                    Integer centValue = Integer.parseInt(value) * 100;
                    if (centValue >= 1 && centValue <= 99_99_900) {
                        return true;
                    }
                } else if (stringValue.matches(".*,[0-9]")) {
                    String value = stringValue.replace(",","");
                    Integer centValue = Integer.parseInt(value) * 10;
                    if (centValue >= 1 && centValue <= 99_99_900) {
                        return true;
                    }
                } else if (stringValue.matches(".*,[0-9][0-9]")) {
                    String value = stringValue.replace(",","");
                    Integer centValue = Integer.parseInt(value);
                    if (centValue >= 1 && centValue <= 99_99_900) {
                        return true;
                    }
                }
            } else {
                String value = stringValue.replace(",","");
                Integer centValue = Integer.parseInt(value) * 100;
                if (centValue >= 1 && centValue <= 99_99_900) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
