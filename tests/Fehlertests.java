import com.lagerverwaltung.Category;
import com.lagerverwaltung.FileHandler;
import com.lagerverwaltung.Inventory;
import com.lagerverwaltung.InventoryItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Fehlertests {

    @Test
    void addItemTest() {
        Inventory myInventory = new Inventory();

        Assertions.assertEquals(0, myInventory.getShelfMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().size());

        Assertions.assertTrue(myInventory.addCategory(new Category("cat0")));
        Assertions.assertEquals(1, myInventory.getCategoryMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat0").getCount());

        // Äquivalenzklasse 1 (description = "" invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("", "cat0", 1, "001001", 10, 199)));
        // ÄK 2 (description valid)
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item2", "cat0", 1, "001002", 10, 199)));
        // ÄK 3 (description invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("<>item3#", "cat0", 1200, "001003", 10, 199)));

        // ÄK 4 (category = "" invalid)
        Assertions.assertFalse(myInventory.addNewItem(new InventoryItem("item4", "", 1, "001004", 10, 199)));
        // ÄK 5 (category valid)
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item5", "cat0", 1, "001005", 10, 199)));
        // ÄK 6 (category invalid)
        Assertions.assertFalse(myInventory.addNewItem(new InventoryItem("item6", "<>cat0#", 1, "001006", 10, 199)));

        // ÄK 7 (stock <0 invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item7", "cat0", -5, "001007", 10, 199)));
        // ÄK 8 (stock valid)
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item8", "cat0", 1, "001008", 10, 199)));
        // ÄK 9 (stock >100000000 invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item9", "cat0", 100000001, "001009", 10, 199)));

        // ÄK 10 (location = "" invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item10", "cat0", 1, "", 10, 199)));
        // ÄK 11 (location valid)
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item11", "cat0", 1, "001011", 10, 199)));
        // ÄK 12 (location invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item12", "cat0", 100000001, "0010121", 10, 199)));

        // ÄK 13 (weight <0 invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item13", "cat0", 1, "001013", -10, 199)));
        // ÄK 14 (weight valid)
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item14", "cat0", 1, "001014", 10, 199)));
        // ÄK 15 (weight > 100000000 invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item15", "cat0", 1, "001015", 100000001, 199)));

        // ÄK 16 (price <0 invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item16", "cat0", 1, "001016", 10, -199)));
        // ÄK 17 (price valid)
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item17", "cat0", 1, "001017", 10, 199)));
        // ÄK 18 (price > 9999900 invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item18", "cat0", 1, "001018", 10, 9999901)));

        // ÄK 19 (stock || weight negative invalid)
        Assertions.assertThrows(IllegalArgumentException.class, () -> myInventory.addNewItem(new InventoryItem("item20", "cat0", -100, "003020", 1000000, 199)));
        // ÄK 19 (stock + weight at location <= 10t valid)
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item19", "cat0", 100, "002019", 10, 199)));
        // ÄK 20 (stock + weight at location > 10t invalid)
        Assertions.assertFalse(myInventory.addNewItem(new InventoryItem("item20", "cat0", 10000, "003020", 1000000, 199)));
    }
}
