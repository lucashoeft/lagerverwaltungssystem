
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
    Integer weight;     // in 0.1g
    Integer price;       // in cents

    /*
    Lagerort:
    0 - 9 sind verfügbare Zeichen
    Nach Muster: ABCDDD
    A - Abteilung
    B - Sub-Abteilung
    C - Regalnummer
    DDD - Platznummer
    >> Ergibt max. 1.000.000 Einträge

    */

    public InventoryItem(String description, String category, Integer stock, String location, Integer weight, Integer price) {
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
        catch (Exception ignored) { } // don't export invalid items
        return csv;
    }

    // return true if inventory item is valid
    /**
     * @return true if inventory is valid
     */
    public boolean isValid() {
        if ((description == null) || (description.indexOf(',') >= 0)) return false;
        if ((category == null) || (category.indexOf(',') >= 0)) return false;
        if ((location == null) || (!location.matches("^[0-9]{6}$"))) return false;
        if ((stock == null) || (stock < 0)) return false;
        if ((weight == null) || (weight <= 0)) return false;
        return (price != null) && (price >= 0);
    }

    public String getDescription() {
        return description;
    }

    public String getCategory(){
        return category;
    }

    public Category getCategoryObj(){
        return new Category(category, 1);
    }

    public String getLocation() {
        return location;
    }

    /**
     * @return shelf identification number
     */
    public int getShelf(){
        return (Integer.parseInt(location.substring(0,3)));
    }

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

    public int getPrice() {
        return price;
    }

    public boolean checkUsage(InventoryItem item){
        return (item.getDescription().equals(getDescription()) || item.getLocation().equals(getLocation()));
    }


}
