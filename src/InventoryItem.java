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
        catch (Exception e) { } // don't export invalid items
        return csv;
    }

    // return true if inventory is valid
    public boolean isValid() {
        if ((description == null) || (description.indexOf(',') >= 0)) return false;
        if ((category == null) || (category.indexOf(',') >= 0)) return false;
        if ((location == null) || (location.matches("^[0-9]{6}$") == false)) return false;
        if ((stock == null) || (stock < 0)) return false;
        if ((weight == null) || (weight <= 0)) return false;
        if ((price == null) || (price < 0)) return false;
        return true;
    }
}
