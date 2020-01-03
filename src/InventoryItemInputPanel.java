import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class InventoryItemInputPanel extends JPanel {

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

    InventoryItemInputPanel(HashMap<String, Category> categoryMap) {
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new GridLayout(0,2,6,3));

        descriptionTextField.setToolTipText(
                "<html>" +
                        "• Muss eindeutig sein" + "<br>" +
                        "• Maximal 256 Zeichen" + "<br>" +
                        "• Erlaubte Zeichen: Buchstaben, Bindestrich, " + "<br>" +
                        "Ausrufe- und Fragezeichen, Punkt," + "<br>" +
                        "Klammer auf, Klammer zu, Zahlen" +
                "</html>");
        categoryComboBox.setToolTipText(
                "Sollte keine Kategorie zur Auswahl stehen, muss erst eine Kategorie erstellt werden");
        stockTextField.setToolTipText(
                "<html>" +
                        "• Minimal: 0 Stück" + "<br>" +
                        "• Maximal: 100.000.000 Stück" + "<br>" +
                "</html>");
        locationTextField.setToolTipText(
                "<html>" +
                        "• Muss eindeutig sein" + "<br>" +
                        "• Minimal: 000000" + "<br>" +
                        "• Maximal: 999999" + "<br>" +
                        "• Immer 6 Zeichen angeben" + "<br>" +
                "</html>");
        weightTextField.setToolTipText(
                "<html>" +
                        "• Angabe des Stückgewichts" + "<br>" +
                        "• Minimal: 0,1 g" + "<br>" +
                        "• Maximal: 10.000.000,0 g" + "<br>" +
                "</html>");
        priceTextField.setToolTipText(
                "<html>" +
                        "• Minimal: 0,01 €" + "<br>" +
                        "• Maximal: 99.999,00 €" + "<br>" +
                        "</html>");

        this.add(descriptionLabel);
        this.add(descriptionTextField);

        this.add(categoryLabel);
        this.add(categoryComboBox);

        for(Category cat : categoryMap.values()){
            categoryComboBox.addItem(cat.getName());
        }

        this.add(stockLabel);
        this.add(stockTextField);

        this.add(locationLabel);
        this.add(locationTextField);

        this.add(weightLabel);
        this.add(weightTextField);

        this.add(priceLabel);
        this.add(priceTextField);

    }

    public String getDescription() {
        return descriptionTextField.getText();
    }

    public String getCategory() throws NullPointerException {
        return categoryComboBox.getSelectedItem().toString();
    }

    public Integer getStock() throws IllegalArgumentException {
        String stringValue = stockTextField.getText();
        try {
            return Integer.parseInt(stringValue);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException("Fehlerhafter Lagerbestand.\n" +
                    "• Minimal: 0 Stück\n" +
                    "• Maximal: 100.000.000 Stück\n" +
                    "• Beispiel: 45");
        }
    }

    public String getItemLocation() {
        return locationTextField.getText();
    }

    public Integer getWeight() throws IllegalArgumentException {
        String stringValue = weightTextField.getText().replace(".","");

        if (stringValue.contains(",")) {
            if (stringValue.matches(".*,")) {
                String value = stringValue.replace(",","");
                try {
                    return Integer.parseInt(value) * 10;
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("Fehlerhaftes Gewicht.\n" +
                            "• Angabe des Stückgewichts\n" +
                            "• Minimal: 0,1 g\n" +
                            "• Maximal: 10.000.000,0 g\n" +
                            "• Beispiel: 68,4");
                }
            } else if (stringValue.matches(".*,[0-9]")) {
                String value = stringValue.replace(",","");
                try {
                    return Integer.parseInt(value);
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("Fehlerhaftes Gewicht.\n" +
                            "• Angabe des Stückgewichts\n" +
                            "• Minimal: 0,1 g\n" +
                            "• Maximal: 10.000.000,0 g\n" +
                            "• Beispiel: 68,4");
                }
            } else {
                throw new IllegalArgumentException("Fehlerhaftes Gewicht.\n" +
                        "• Angabe des Stückgewichts\n" +
                        "• Minimal: 0,1 g\n" +
                        "• Maximal: 10.000.000,0 g\n" +
                        "• Beispiel: 68,4");
            }
        } else {
            try {
                return Integer.parseInt(stringValue) * 10;
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("Fehlerhaftes Gewicht.\n" +
                        "• Angabe des Stückgewichts\n" +
                        "• Minimal: 0,1 g\n" +
                        "• Maximal: 10.000.000,0 g\n" +
                        "• Beispiel: 68,4");
            }
        }
    }

    public Integer getPrice() throws IllegalArgumentException {
        String stringValue = priceTextField.getText().replace(".","");

        if (stringValue.contains(",")) {
            if (stringValue.matches(".*,")) {
                String value = stringValue.replace(",", "");
                try {
                    return Integer.parseInt(value) * 100;
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("Fehlerhafter Preis\n" +
                            "• Minimal: 0,01 €\n" +
                            "• Maximal: 99.999,00 €\n" +
                            "• Beispiel: 6,99");
                }
            } else if (stringValue.matches(".*,[0-9]")) {
                String value = stringValue.replace(",", "");
                try {
                    return Integer.parseInt(value) * 10;
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("Fehlerhafter Preis\n" +
                            "• Minimal: 0,01 €\n" +
                            "• Maximal: 99.999,00 €\n" +
                            "• Beispiel: 6,99");
                }
            } else if (stringValue.matches(".*,[0-9][0-9]")) {
                String value = stringValue.replace(",", "");
                try {
                    return Integer.parseInt(value);
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("Fehlerhafter Preis\n" +
                            "• Minimal: 0,01 €\n" +
                            "• Maximal: 99.999,00 €\n" +
                            "• Beispiel: 6,99");
                }
            } else {
                throw new IllegalArgumentException("Fehlerhafter Preis\n" +
                        "• Minimal: 0,01 €\n" +
                        "• Maximal: 99.999,00 €\n" +
                        "• Beispiel: 6,99");
            }
        } else {
            try {
                return Integer.parseInt(stringValue) * 100;
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("Fehlerhafter Preis\n" +
                        "• Minimal: 0,01 €\n" +
                        "• Maximal: 99.999,00 €\n" +
                        "• Beispiel: 6,99");
            }
        }
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        setDescription(inventoryItem.description);
        setCategory(inventoryItem.category);
        setStock(inventoryItem.stock);
        setItemLocation(inventoryItem.location);
        setWeight(inventoryItem.weight);
        setPrice(inventoryItem.price);
    }

    private void setDescription(String text) {
        this.descriptionTextField.setText(text);
    }

    private void setCategory(String text) {
        this.categoryComboBox.setSelectedItem(text);
    }

    private void setStock(Integer text) {
        this.stockTextField.setText(text.toString());
    }

    private void setItemLocation(String text) {
        this.locationTextField.setText(text);
    }

    private void setWeight(Integer text) {
        Double str = text.doubleValue() / 10.0;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMinimumFractionDigits(1);
        this.weightTextField.setText(nf.format(str));
    }

    private void setPrice(Integer text) {
        Double str = text.doubleValue() / 100.0;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMinimumFractionDigits(2);
        this.priceTextField.setText(nf.format(str));
    }

    public void setIsEnabled(Boolean isEnabled) {
        if (isEnabled) {
            this.descriptionTextField.setEditable(true);
            this.categoryComboBox.setEnabled(true);
            this.stockTextField.setEditable(true);
            this.locationTextField.setEditable(true);
            this.weightTextField.setEditable(true);
            this.priceTextField.setEditable(true);
        } else {
            this.descriptionTextField.setEditable(false);
            this.categoryComboBox.setEnabled(false);
            this.stockTextField.setEditable(false);
            this.locationTextField.setEditable(false);
            this.weightTextField.setEditable(false);
            this.priceTextField.setEditable(false);
        }
    }
}