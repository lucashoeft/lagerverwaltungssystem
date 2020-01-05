import com.lagerverwaltung.InventoryItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InventoryItemTest {

    @Test
    void checkUsage() {
        InventoryItem i = new InventoryItem("test1", "cat", 100, "001001", 100, 100);
        Assertions.assertTrue(i.checkUsage(new InventoryItem("test1", "cat", 100, "001002", 100, 100))); // same name
        Assertions.assertTrue(i.checkUsage(new InventoryItem("test2", "cat", 100, "001001", 100, 100))); // same location
        Assertions.assertTrue(i.checkUsage(new InventoryItem("test1", "cat", 100, "001001", 100, 100))); // same name and location
        Assertions.assertFalse(i.checkUsage(new InventoryItem("test2", "cat", 100, "001002", 100, 200))); // different name and location
    }
}