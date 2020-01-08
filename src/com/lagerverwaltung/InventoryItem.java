package com.lagerverwaltung;

/**
 *
 * The InventoryItem class models an inventory item which is unique by its description and its location.
 */
public class InventoryItem {

    /**
     * The unique description of the inventory item.
     */
    private String description;

    /**
     * The category of the inventory item.
     */
    private String category;

    /**
     * The amount of inventory items that are currently on stock.
     */
    private Integer stock;

    /**
     * The location shows where the inventory item can be found.
     *
     * <p>The location number is a number of six digits between 0 and 9. The first three digits represent the unqiue
     * shelf number were you can read from the department, the sub department and the shelf number itself. The last
     * three digits show the storage location in the shelf. This means that the inventory exists out of 1000 shelves
     * (000 to 999) which can hold 1000 inventory items (000 to 999) each. This means that the inventory can hold
     * 1000000 inventory items.
     */
    private String location;

    /**
     * The weight of one inventory item in decigram.
     *
     * <p>This number shows the weight of one singular inventory item and not the weight of one singular inventory item
     * multiplied by the stock number.
     */
    private Integer weight;

    /**
     * The price of one inventory item in cent.
     *
     * <p>This number shows the price of one singular inventory item and not the price of one singular inventory item
     * multiplied by the stock number.
     */
    private Integer price;

    /**
     * The constructor of the InventoryItem class
     *
     * <p>It tries to create a new inventory item, but throws an IllegalArgumentException if one or more of the
     * attributes is not following the allowed standards.
     *
     * @param description the description of the inventory item
     * @param category the category of the inventory item
     * @param stock the amount of inventory items that are on stock
     * @param location the location of the inventory item
     * @param weight the weight of the inventory item in decigram
     * @param price the price of the inventory item in cent
     * @throws IllegalArgumentException the exception if creating was not possible
     */
    public InventoryItem(String description, String category, Integer stock, String location, Integer weight, Integer price) throws IllegalArgumentException {
        if (isValidDescription(description)) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Fehlerhafte Produktbezeichnung.\n" +
                    "• Muss eindeutig sein\n" +
                    "• Maximal 256 Zeichen\n" +
                    "• Erlaubte Zeichen: Buchstaben, Bindestrich,\n" +
                    "Ausrufe- und Fragezeichen, Punkt,\n" +
                    "Klammer auf, Klammer zu, Zahlen\n" +
                    "• Beispiel: Blauer-Stift-123");
        }

        this.category = category;

        if (isValidStock(stock)) {
            this.stock = stock;
        } else {
            throw new IllegalArgumentException("Fehlerhafter Lagerbestand.\n" +
                    "• Minimal: 0 Stück\n" +
                    "• Maximal: 100.000.000 Stück\n" +
                    "• Beispiel: 45");
        }

        if (isValidLocation(location)) {
            this.location = location;
        } else {
            throw new IllegalArgumentException("Fehlerhafter Lagerort.\n" +
                    "• Muss eindeutig sein\n" +
                    "• Minimal: 000000\n" +
                    "• Maximal: 999999\n" +
                    "• Immer 6 Zeichen angeben\n" +
                    "• Beispiel: 000043");
        }

        if (isValidWeight(weight)) {
            this.weight = weight;
        } else {
            throw new IllegalArgumentException("Fehlerhaftes Gewicht.\n" +
                    "• Angabe des Stückgewichts\n" +
                    "• Minimal: 0,1 g\n" +
                    "• Maximal: 10.000.000,0 g\n" +
                    "• Beispiel: 68,4");
        }

        if (isValidPrice(price)) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Fehlerhafter Preis\n" +
                    "• Minimal: 0,01 €\n" +
                    "• Maximal: 99.999,00 €\n" +
                    "• Beispiel: 6,99");
        }
    }

    /**
     * Converts the attributes of an inventory item into a csv-compatible string.
     *
     * @return the csv-compatible string representation of an inventory item
     */
    public String toStringCSV() {
        return description + "," + category + "," + stock.toString() + "," + location + "," + weight.toString() + "," + price.toString();
    }

    /**
     * @return the description of the inventory item
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the inventory item category
     */
    public String getCategory(){
        return category;
    }

    /**
     * Returns a new category with the name of the category the inventory item is in.
     *
     * @return the category named by the category of the inventory item
     */
    public Category getCategoryObj(){
        return new Category(category, 1);
    }

    /**
     * Returns the full location where the inventory item is located.
     *
     * @return the inventory item location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the number of the shelf the inventory item is stored in.
     *
     * @return the number of the shelf
     */
    public int getShelf(){
        return (Integer.parseInt(location.substring(0,3)));
    }

    /**
     * Returns the weight of one singular inventory item.
     *
     * @return the inventory item weight
     */
    public Integer getWeight(){
        return weight;
    }

    /**
     * @return the amount of inventory items that are on stock
     */
    public int getStock(){
        return stock;
    }

    /**
     * Updates the current stock of the inventory item to a new number.
     *
     * @param stock the new amount of items that are on stock
     */
    public void setStock(Integer stock){
        this.stock = stock;
    }

    /**
     * Returns the price of one singular inventory item.
     *
     * @return the inventory item price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Compares an inventory item to this inventory item and checks if both the description and the location are unique.
     *
     * @param item an inventory item to check against this inventory item
     * @return true if inventory items are equal by either description or location
     */
    public boolean checkUsage(InventoryItem item){
        return (item.getDescription().equals(getDescription()) || item.getLocation().equals(getLocation()));
    }

    /**
     * Checks if the description is valid.
     *
     * <p>This method checks if the description consists only out of alphabetical characters, numbers, open bracket,
     * closed bracket, exclamation mark, question mark, dot and minus. The lengths needs to between 1 and 256
     * characters.
     *
     * @param description the description which needs to be checked
     * @return true if description is valid
     */
    private Boolean isValidDescription(String description) {
        return (description.matches("[a-zA-ZöäüÖÄÜß0-9()!?.\\-]{1,256}"));
    }

    /**
     * Checks if the stock is valid.
     *
     * <p>This method checks if the stock is between 0 and 100000000.
     *
     * @param stock the stock which needs to be checked
     * @return true if stock is valid
     */
    private Boolean isValidStock(Integer stock) {
        return (stock >= 0 && stock <= 100_000_000);
    }

    /**
     * Checks if the location is valid.
     *
     * <p>This method checks if the location is a number of six digits between 0 and 9. A number like 000000 is allowed.
     *
     * @param location tje location which needs to be checked
     * @return true if location is valid
     */
    private Boolean isValidLocation(String location) {
        return (location.matches("[0-9]{6}"));
    }

    /**
     * Checks if the weight is valid.
     *
     * <p>This method checks if the weight is between 1 and 100000000 decigram, which equals 10 tones.
     *
     * @param weight the weight which needs to be checked
     * @return true if weight is valid
     */
    private Boolean isValidWeight(Integer weight) {
        return (weight >= 1 && weight <= 100000000);
    }

    /**
     * Checks if the price is valid.
     *
     * <p>This method checks if the price is between 1 and 9999900 cents, which equals 99999 euros.
     *
     * @param price the price which needs to be checked
     * @return true if price is valid
     */
    private Boolean isValidPrice(Integer price) {
        return (price >= 1 && price <= 99_99_900);
    }
}
