import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InventoryTest {

    @Test
    void getPath() {
        Inventory myInventory = new Inventory();
        Assertions.assertEquals("", myInventory.getPath());
    }

    @Test
    void setPath() {
        assertDoesNotThrow(() -> {
            File tmpFile = File.createTempFile("my_test_database_", ".csv");
            String fileName = tmpFile.getCanonicalPath();
            Inventory myInventory = new Inventory();
            myInventory.setPath(fileName);
            Assertions.assertEquals(fileName, myInventory.getPath());
            TestHelpers.deleteTmpFile(fileName);
        });
    }

    @Test
    void loadAndStore() {
        assertDoesNotThrow(() -> {
            // generate a database file
            String content =
                "\"Dreikant-Buntstift - STABILO Trio dick kurz - 12er Pack - mit 12 verschiedenen Farben\",\"Stifte\",21,\"000012\",692,475\n" +
                "\"Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065\",\"Füllfederhalter & Kugelschreiber\",8,\"000001\",116,1571\n"; // from example
            String fileName = TestHelpers.createTmpFileWithContent("my_test_database_", ".csv", content);

            // load the database file
            Inventory myInventory = new Inventory();
            myInventory.setPath(fileName);
            myInventory.loadData();
            List<InventoryItem> items = myInventory.getItems();
            Assertions.assertEquals(2, items.size());
            Assertions.assertEquals("Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065", items.get(1).description);
            Assertions.assertEquals("Füllfederhalter & Kugelschreiber", items.get(1).category);
            Assertions.assertEquals(8, items.get(1).stock);
            Assertions.assertEquals("000001", items.get(1).location);
            Assertions.assertEquals(116, items.get(1).weight);
            Assertions.assertEquals(1571, items.get(1).price);

            // store the database database
            FileHandler myFileHandler = new FileHandler();
            myFileHandler.storeInventoryInCSV(myInventory);
            myInventory.loadData(); // ... reload t
            items = myInventory.getItems(); // and check its content
            Assertions.assertEquals(2, items.size());
            Assertions.assertEquals("Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065", items.get(1).description);
            Assertions.assertEquals("Füllfederhalter & Kugelschreiber", items.get(1).category);
            Assertions.assertEquals(8, items.get(1).stock);
            Assertions.assertEquals("000001", items.get(1).location);
            Assertions.assertEquals(116, items.get(1).weight);
            Assertions.assertEquals(1571, items.get(1).price);

            // cleanup used file
            TestHelpers.deleteTmpFile(fileName);
        });
    }

    @Test
    void checkUnique() {
        Inventory myInventory = TestHelpers.createInventory();

        // location already in use
        Assertions.assertFalse(myInventory.checkUnique(new InventoryItem("item4", "cat1", 1100, "003001", 10.0, 1.99)));
        // article already existing
        Assertions.assertFalse(myInventory.checkUnique(new InventoryItem("item31", "cat1", 1100, "004001", 10.0, 1.99)));
        // article already existing and location already used
        Assertions.assertFalse(myInventory.checkUnique(new InventoryItem("item31", "cat1", 1100, "003001", 10.0, 1.99)));
        // new article in new location
        Assertions.assertTrue(myInventory.checkUnique(new InventoryItem("item4", "cat1", 1100, "004001", 10.0, 1.99)));
    }
}