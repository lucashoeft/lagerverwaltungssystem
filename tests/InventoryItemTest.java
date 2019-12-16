import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InventoryItemTest {

    @Test
    void toStringCSV() {
        InventoryItem myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", 100, "000000", 321.99, 2.99);
        Assertions.assertEquals("Buntstifte Marke \\\"Premium\\\"\\, Variante 2018,Buntstifte,100,000000,321.99,2.99", myItem.toStringCSV());

        // check invalid items
        myItem = new InventoryItem(null, "Buntstifte", 100, "000000", 321.99, 2.99);
        Assertions.assertEquals("", myItem.toStringCSV());

        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", null, 100, "000000", 321.99, 2.99);
        Assertions.assertEquals("", myItem.toStringCSV());

        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", null, "000000", 321.99, 2.99);
        Assertions.assertEquals("", myItem.toStringCSV());

        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", 100, null, 321.99, 2.99);
        Assertions.assertEquals("", myItem.toStringCSV());

        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", 100, "000000", null, 2.99);
        Assertions.assertEquals("", myItem.toStringCSV());

        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", 100, "000000", 321.99, null);
        Assertions.assertEquals("", myItem.toStringCSV());
    }
}