
/**
 *
 * The InventoryItem Object is one type of item which is stored in the warehouse.
 *
 *
 * @author ...
 * @version 1.0
 */
public class InventoryItem {
    /**
     * Short description of the item
     */
    String description;

    /**
     * Category of the item
     */
    String category;

    /**
     * Amount of items of this type on stock
     */
    Integer stock;

    /**
     * location in the Warehouse which holds this item
     * location= ABCDDD
     * A = department
     * B = sub-department
     * C = shelf number
     * DDD = location number on shelf
     * This results in a maximum of 1.000.000 different items in the System
     */
    String location;

    /**
     * The weight of a single of item of this type
     */
    Double weight;

    /**
     * the price of a single item of this type
     */
    Double price;

    /**
     * Constructor for a InventoryItem object
     * @param description Short description of the item
     * @param category Category of the item
     * @param stock Amount of items of this type on stock
     * @param location location in the Warehouse which holds this item
     * @param weight The weight of a single of item of this type
     * @param price the price of a single item of this type
     */
    public InventoryItem(String description, String category, Integer stock, String location, Double weight, Double price) {
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.location = location;
        this.weight = weight;
        this.price = price;
    }

    /**
     * converting attributes of InventoryItem into csv-compatible string
     * @return csv-compatible string representation of an InventoryItem
     */
    public String toStringCSV() {
        String csv = "";
        // no further checks like for embedded comma, correct location encoding, ... (has to be done by caller)
        try {
            csv = description+","+category+","+stock.toString()+","+location+","+weight.toString()+","+price.toString();
        }
        catch (Exception e) { } // don't export invalid items
        return csv;
    }

    /**
     * @return true if inventory is valid
     */
    public boolean isValid() {
        if ((description == null) || (description.indexOf(',') >= 0)) return false;
        if ((category == null) || (category.indexOf(',') >= 0)) return false;
        if ((location == null) || (location.matches("^[0-9]{6}$") == false)) return false;
        if ((stock == null) || (stock < 0)) return false;
        if ((weight == null) || (weight <= 0)) return false;
        if ((price == null) || (price < 0)) return false;
        return true;
    }

    /**
     * @return shelf identification number
     */
    public int getShelf(){
        return (Integer.parseInt(location.substring(0,3)));
    }

    /**
     * @return weight of a single InventoryItem
     */
    public Double getWeight(){
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
}
