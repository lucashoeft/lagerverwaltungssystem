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
                "\"Dreikant-Buntstift - STABILO Trio dick kurz - 12er Pack - mit 12 verschiedenen Farben\",\"Stifte\",21,\"000012\",69.2,4.75\n" +
                "\"Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065\",\"Füllfederhalter & Kugelschreiber\",8,\"000001\",116,15.71\n"; // from example
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
            Assertions.assertEquals(116.0, items.get(1).weight);
            Assertions.assertEquals(15.71, items.get(1).price);

            // store the database database
            myInventory.store();
            myInventory.loadData(); // ... reload t
            items = myInventory.getItems(); // and check its content
            Assertions.assertEquals(2, items.size());
            Assertions.assertEquals("Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065", items.get(1).description);
            Assertions.assertEquals("Füllfederhalter & Kugelschreiber", items.get(1).category);
            Assertions.assertEquals(8, items.get(1).stock);
            Assertions.assertEquals("000001", items.get(1).location);
            Assertions.assertEquals(116.0, items.get(1).weight);
            Assertions.assertEquals(15.71, items.get(1).price);

            // cleanup used file
            TestHelpers.deleteTmpFile(fileName);
        });
    }
}