import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    @Test
    void toStringCSV() {
        InventoryItem myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", 100, "000000", 321.99, 2.99);
        Assertions.assertEquals("Buntstifte Marke \\\"Premium\\\"\\, Variante 2018,Buntstifte,100,000000,321.99,2.99", myItem.toStringCSV());
    }
}