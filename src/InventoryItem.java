public class InventoryItem {
    String description;
    String category;
    Integer stock;
    String location;
    Double weight;
    Double price;

    public InventoryItem(String description, String category, Integer stock, String location, Double weight, Double price) {
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.location = location;
        this.weight = weight;
        this.price = price;
    }

    @Override
    public String toString() {
        return "InventoryItem(description=" + description + ", category=" + category + "]";
    }
}
