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

    public InventoryItem() {
    }

    public String toStringCSV() {
        return description+","+category+","+stock.toString()+","+location+","+weight.toString()+","+price.toString();
    }


}
