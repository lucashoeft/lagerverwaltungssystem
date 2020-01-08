package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * The InventoryItemInputPanel class extends the JPanel class from the Java swing package.
 */
public class InventoryItemInputPanel extends JPanel {

    /**
     * The label which shows the location of the description text field.
     */
    private JLabel descriptionLabel = new JLabel("Produktbezeichnung");

    /**
     * The text field which holds the description of the inventory item.
     */
    private JTextField descriptionTextField = new JTextField(15);

    /**
     * The label which shows the location of the category combo box.
     */
    private JLabel categoryLabel = new JLabel("Kategorie");

    /**
     * The combo box which lets the user select a category.
     */
    private JComboBox categoryComboBox = new JComboBox();

    /**
     * The label which shows the location of the stock text field.
     */
    private JLabel stockLabel = new JLabel("Lagerbestand");

    /**
     * The text field which holds the stock of the inventory item.
     */
    private JTextField stockTextField = new JTextField(15);

    /**
     * The label which shows the location of the location text field.
     */
    private JLabel locationLabel = new JLabel("Lagerort");

    /**
     * The text field which holds the location of the inventory item.
     */
    private JTextField locationTextField = new JTextField(15);

    /**
     * The label which shows the location of the weight text field.
     */
    private JLabel weightLabel = new JLabel("Gewicht in g");

    /**
     * The text field which holds the weight of the inventory item.
     */
    private JTextField weightTextField = new JTextField(15);

    /**
     * The label which shows the location of the price text field.
     */
    private JLabel priceLabel = new JLabel("Preis in €");

    /**
     * The text field which holds the price of the inventory item.
     */
    private JTextField priceTextField = new JTextField(15);

    /**
     * The constructor of the InventoryItemInputPanel class.
     *
     * @param categoryMap hash map with all available categories
     */
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
        categoryComboBox.setPrototypeDisplayValue("Category Name");
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

    /**
     * Returns the description from the description text field.
     *
     * @return the description of the inventory item
     */
    public String getDescription() {
        return descriptionTextField.getText();
    }

    /**
     * Returns the category that is currently chosen in the category combo box.
     *
     * @return the category that is currently chosen
     * @throws NullPointerException if no category was available to select
     */
    public String getCategory() throws NullPointerException {
        return categoryComboBox.getSelectedItem().toString();
    }

    /**
     * Returns the stock value that was entered into the text field.
     *
     * <p>This method remove dots which were used for better readability.
     *
     * @return the stock value
     * @throws IllegalArgumentException if input is not a number
     */
    public Integer getStock() throws IllegalArgumentException {
        String stringValue = stockTextField.getText().replace(".","");

        String exceptionText = "Fehlerhafter Lagerbestand.\n" +
                "• Minimal: 0 Stück\n" +
                "• Maximal: 100.000.000 Stück\n" +
                "• Beispiel: 45";

        try {
            return Integer.parseInt(stringValue);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(exceptionText);
        }
    }

    public String getItemLocation() {
        return locationTextField.getText();
    }

    /**
     * Converts the input to decigram and returns the value.
     *
     * <p>This method removes all dots that were used for better
     * readability and then check based how many decimal places are after the comma and then converts the gram value
     * to its decigram value. Zero to one decimal places are allowed. If the input is not a number or does not conform
     * to the comma representation that is used in Germany, it throws a IllegalArgumentException.
     *
     * @return the weight value in decigram
     * @throws IllegalArgumentException if input is not a number or does not conform to the comma representation
     */
    public Integer getWeight() throws IllegalArgumentException {
        String stringValue = weightTextField.getText().replace(".","");

        String exceptionText = "Fehlerhaftes Gewicht.\n" +
                "• Angabe des Stückgewichts\n" +
                "• Minimal: 0,1 g\n" +
                "• Maximal: 10.000.000,0 g\n" +
                "• Beispiel: 68,4";

        if (stringValue.contains(",")) {
            if (stringValue.matches(".*,")) {
                String value = stringValue.replace(",","");
                try {
                    return Integer.parseInt(value) * 10;
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException(exceptionText);
                }
            } else if (stringValue.matches(".*,[0-9]")) {
                String value = stringValue.replace(",","");
                try {
                    return Integer.parseInt(value);
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException(exceptionText);
                }
            } else {
                throw new IllegalArgumentException(exceptionText);
            }
        } else {
            try {
                return Integer.parseInt(stringValue) * 10;
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException(exceptionText);
            }
        }
    }

    /**
     * Converts the input to cent and returns the value.
     *
     * <p>This method removes all dots that were used for better readability and then checks based how many decimal
     * places are after the comma and then converts the euro value to its cent value. Zero to two decimal places are
     * allowed. If the input is not a number or does not conform to the comma representation that is used in Germany,
     * it throws a IllegalArgumentException.
     *
     * @return the price value converted to cent
     * @throws IllegalArgumentException if input is not a number or does not conform to the comma representation
     */
    public Integer getPrice() throws IllegalArgumentException {
        String stringValue = priceTextField.getText().replace(".","");

        String exceptionText = "Fehlerhafter Preis\n" +
                "• Minimal: 0,01 €\n" +
                "• Maximal: 99.999,00 €\n" +
                "• Beispiel: 6,99";

        if (stringValue.contains(",")) {
            if (stringValue.matches(".*,")) {
                String value = stringValue.replace(",", "");
                try {
                    return Integer.parseInt(value) * 100;
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException(exceptionText);
                }
            } else if (stringValue.matches(".*,[0-9]")) {
                String value = stringValue.replace(",", "");
                try {
                    return Integer.parseInt(value) * 10;
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException(exceptionText);
                }
            } else if (stringValue.matches(".*,[0-9][0-9]")) {
                String value = stringValue.replace(",", "");
                try {
                    return Integer.parseInt(value);
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException(exceptionText);
                }
            } else {
                throw new IllegalArgumentException(exceptionText);
            }
        } else {
            try {
                return Integer.parseInt(stringValue) * 100;
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException(exceptionText);
            }
        }
    }

    /**
     * Sets attributes of the inventory item to the text fields.
     *
     * @param inventoryItem the inventory item to be shown.
     */
    public void setInventoryItem(InventoryItem inventoryItem) {
        setDescription(inventoryItem.getDescription());
        setCategory(inventoryItem.getCategory());
        setStock(inventoryItem.getStock());
        setItemLocation(inventoryItem.getLocation());
        setWeight(inventoryItem.getWeight());
        setPrice(inventoryItem.getPrice());
    }

    /**
     * Activates or deactivates the text fields and combo box depending on the parameter.
     *
     * @param isEnabled the boolean value if text fields are enabled.
     */
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

    /**
     * Sets the input of the description text field.
     *
     * @param text the new input for the description text field
     */
    private void setDescription(String text) {
        this.descriptionTextField.setText(text);
    }

    /**
     * Selects the category in the category combo box.
     *
     * @param text the category to be selected
     */
    private void setCategory(String text) {
        this.categoryComboBox.setSelectedItem(text);
    }

    /**
     * Sets the input of the stock text field.
     *
     * @param text the new input for the stock text field
     */
    private void setStock(Integer text) {
        this.stockTextField.setText(text.toString());
    }

    /**
     * Sets the input of the location text field.
     *
     * @param text the new input for the location text field
     */
    private void setItemLocation(String text) {
        this.locationTextField.setText(text);
    }

    /**
     * Converts the decigram value to the gram value and puts it into the weight text field.
     *
     * @param text the weight in decigram
     */
    private void setWeight(Integer text) {
        Double str = text.doubleValue() / 10.0;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMinimumFractionDigits(1);
        this.weightTextField.setText(nf.format(str));
    }

    /**
     * Converts the cent value to the euro value and puts it into the price text field.
     *
     * @param text the price in cent
     */
    private void setPrice(Integer text) {
        Double str = text.doubleValue() / 100.0;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMinimumFractionDigits(2);
        this.priceTextField.setText(nf.format(str));
    }
}