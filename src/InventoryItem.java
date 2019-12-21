public class InventoryItem {
    String description;
    String category;
    Integer stock;
    String location;
    Double weight;
    Double price;

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

    public InventoryItem(String description, String category, Integer stock, String location, Double weight, Double price) {
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.location = location;
        this.weight = weight;
        this.price = price;
    }

    // converting attributes into csv-compatible string
    public String toStringCSV() {
        String csv = "";
        // no further checks like for embedded comma, correct location encoding, ... (has to be done by caller)
        try {
            csv = description+","+category+","+stock.toString()+","+location+","+weight.toString()+","+price.toString();
        }
        catch (Exception ignored) { } // don't export invalid items
        return csv;
    }

    // return true if inventory is valid
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

    public String getLocation() {
        return location;
    }

    public int getShelf(){
        return (Integer.parseInt(location.substring(0,3)));
    }

    public Double getWeight(){
        return weight;
    }

    public int getStock(){
        return stock;
    }

    public void setStock(Integer stock){
        this.stock = stock;
    }

    public boolean isUnique(Object o){
        if ((o == null) || (o.getClass() != this.getClass())){
            return false;
        }
        else {
            InventoryItem obj = (InventoryItem) o;
            return (!obj.getDescription().equals(getDescription()) && !obj.getLocation().equals(getLocation()));
        }
    }

    public boolean equals(Object o){
        if ((o == null) || (o.getClass() != this.getClass())){
            return false;
        }
        else {
            InventoryItem obj = (InventoryItem) o;
            return (obj.getDescription().equals(getDescription()) && obj.getLocation().equals(getLocation()));
        }
    }


}
