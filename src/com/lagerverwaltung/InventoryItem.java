package com.lagerverwaltung;

/**
 *
 * The InventoryItem Object is one type of item which is stored in the warehouse.
 *
 *
 * @author ...
 */
public class InventoryItem {
    /**
     * Short description of the item
     */
    private String description;

    /**
     * Category of the item
     */
    private String category;

    /**
     * Amount of items of this type on stock
     */
    private Integer stock;

    /**
     * location in the Warehouse which holds this item
     * location= ABCDDD
     * A = department
     * B = sub-department
     * C = shelf number
     * DDD = location number on shelf
     * This results in a maximum of 1.000.000 different items in the System
     */
    private String location;

    /**
     * Weight of an item in 0.1 gram
     */
    private Integer weight;

    /**
     * Price of an item in cents
     */
    private Integer price;

    /**
     * Constructor for a new InventoryItem object
     *
     * @param description Short description of the item
     * @param category Category of the item
     * @param stock Amount of items of this type on stock
     * @param location location of the item
     * @param weight weight of an item in 0.1 gram
     * @param price price of an item in cents
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
     * Converts attributes of InventoryItem into csv-compatible string
     *
     * @return csv-compatible string representation of an InventoryItem
     */
    public String toStringCSV() {
        return description + "," + category + "," + stock.toString() + "," + location + "," + weight.toString() + "," + price.toString();
    }

    /**
     * @return item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return item category
     */
    public String getCategory(){
        return category;
    }

    /**
     * @return item category as an category object
     */
    public Category getCategoryObj(){
        return new Category(category, 1);
    }

    /**
     * @return item location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return shelf identification number
     */
    public int getShelf(){
        return (Integer.parseInt(location.substring(0,3)));
    }

    /**
     * @return item weight
     */
    public Integer getWeight(){
        return weight;
    }

    /**
     * @return amount of items on stock
     */
    public int getStock(){
        return stock;
    }

    /**
     * @param stock new amount of items on stock
     */
    public void setStock(Integer stock){
        this.stock = stock;
    }

    /**
     * @return item price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Checks if an item is already in use, description and location have to be unique
     *
     * @param item item which needs to be checked if its already in use
     * @return true if item is already used, else return false
     */
    public boolean checkUsage(InventoryItem item){
        return (item.getDescription().equals(getDescription()) || item.getLocation().equals(getLocation()));
    }

    /**
     * Checks if description is valid
     *
     * @param description description which needs to be checked
     * @return true if description is entered without error, else false
     */
    private Boolean isValidDescription(String description) {
        return (description.matches("[a-zA-ZöäüÖÄÜß0-9()!?.\\-]{1,256}"));
    }

    /**
     * Checks if stock is valid
     *
     * @param stock stock which needs to be checked
     * @return true if stock is entered without error, else false
     */
    private Boolean isValidStock(Integer stock) {
        return (stock >= 0 && stock <= 100_000_000);
    }

    /**
     * Checks if location is valid
     *
     * @param location location which needs to be checked
     * @return true if location is entered without error, else false
     */
    private Boolean isValidLocation(String location) {
        return (location.matches("[0-9]{6}"));
    }

    /**
     * Checks if weight is valid
     *
     * @param weight weight which needs to be checked
     * @return true if weight is entered without error else false
     */
    private Boolean isValidWeight(Integer weight) {
        return (weight >= 1 && weight <= 100000000);
    }

    /**
     * Checks if price is valid
     *
     * @param price price which needs to be checked
     * @return true if price is entered without error else false
     */
    private Boolean isValidPrice(Integer price) {
        return (price >= 1 && price <= 99_99_900);
    }
}