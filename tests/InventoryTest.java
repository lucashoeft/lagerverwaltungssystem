import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
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
    void initStorage() {
        Inventory myInventory = new Inventory();

        Assertions.assertEquals(0, myInventory.getShelfMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().size());

        myInventory.addCategory(new Category("cat0"));
        myInventory.addCategory(new Category("cat1"));
        Assertions.assertEquals(2, myInventory.getCategoryMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat0").getCount());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat1").getCount());

        myInventory.addItem(new InventoryItem("item11", "cat1", 1100, "001001", 10, 199));
        myInventory.addItem(new InventoryItem("item12", "cat1", 1200, "001002", 10, 199));
        //myInventory.addItem(new InventoryItem("item13", "cat1", 1300, "001001", 10.0, 1.99)); // location already in use -> ignore in init()
        myInventory.addItem(new InventoryItem("item21", "cat1", 2, "002001", (1000 * 1000) / 2, 199));
        myInventory.addItem(new InventoryItem("item31", "cat1", 1, "003001", (1000 * 1000 * 10 * 10) + 1, 199)); // to heavy item -> ignore in init()

        //myInventory.initStorage();
        //TODO Assertions.assertEquals(2, myInventory.getShelfMap().size());
        Assertions.assertEquals(2, myInventory.getCategoryMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat0").getCount());
        //TODO Assertions.assertEquals(3, myInventory.getCategoryMap().get("cat1").getCount());
    }

    @Test
    void updateStorageContent() {

        HashMap<Integer, Shelf> myMap = new HashMap<>();
        myMap.put(1, new Shelf(1, 0));
        myMap.put(2, new Shelf(2, 0));
        myMap.put(3, new Shelf(3, 0));
        Inventory myInventory = new Inventory();
        myInventory.setShelfMap(myMap);
        Assertions.assertEquals(1, myInventory.getShelfMap().get(1).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myInventory.addItemToStorage(new InventoryItem("item11", "cat1", 0, "001001", 10, 199), 100));
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(3).getWeight());

        //Assertions.assertEquals(false, myStorage.addItemToStorage(new InventoryItem("item12", "cat1", 0, "001001", 10.0, 1.99), 0)); // same place in shelf -> reject
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(0.0, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(0.0, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myInventory.addItemToStorage(new InventoryItem("item12", "cat1", 0, "001002", 20, 199), 100));
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myInventory.addItemToStorage(new InventoryItem("item21", "cat1", 0, "002001", 20, 199), 1000));
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myInventory.addItemToStorage(new InventoryItem("item31", "cat1", 0, "003001", 1 * 1000 * 1000 * 10, 199), 10));
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(10_000_000_0, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertFalse(myInventory.addItemToStorage(new InventoryItem("item32", "cat1", 0, "003002", 1, 199), 1)); // adding 1g exceeds weight limit
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20.0 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(10*1000*1000*10, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myInventory.removeItemFromStorage(new InventoryItem("item31", "cat1", 10, "003001", 10 * 1000 * 1000, 199), 1)); // remove one item
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(10*1000*1000*9, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myInventory.addItemToStorage(new InventoryItem("item32", "cat1", 0, "003002", 1, 199), 1)); // adding 1g now allowed
        Assertions.assertEquals(3, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(1*1000*1000*90+1, myInventory.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myInventory.addItemToStorage(new InventoryItem("item41", "cat1", 0, "004001", 1, 199), 10)); // new shelf required
        Assertions.assertEquals(4, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(1*1000*1000*90+1, myInventory.getShelfMap().get(3).getWeight());
        Assertions.assertEquals(4, myInventory.getShelfMap().get(4).getId());
        Assertions.assertEquals(1*10, myInventory.getShelfMap().get(4).getWeight());

        Assertions.assertTrue(myInventory.removeItemFromStorage(new InventoryItem("item41", "cat1", 10, "004001", 1, 199), 1));
        Assertions.assertEquals(4, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(1*1000*1000*90+1, myInventory.getShelfMap().get(3).getWeight());
        Assertions.assertEquals(4, myInventory.getShelfMap().get(4).getId());
        Assertions.assertEquals(1*9, myInventory.getShelfMap().get(4).getWeight());

        Assertions.assertFalse(myInventory.removeItemFromStorage(new InventoryItem("item41", "cat1", 9, "004001", 1, 199), 10)); // can not remove more than in stock
        Assertions.assertEquals(4, myInventory.getShelfMap().size());
        Assertions.assertEquals(10 * 100 + 20 * 100, myInventory.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(20 * 1000, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myInventory.getShelfMap().get(3).getId());
        Assertions.assertEquals(1*1000*1000*90+1, myInventory.getShelfMap().get(3).getWeight());
        Assertions.assertEquals(4, myInventory.getShelfMap().get(4).getId());
        Assertions.assertEquals(1*9, myInventory.getShelfMap().get(4).getWeight());

        Assertions.assertFalse(myInventory.removeItemFromStorage(new InventoryItem("item51", "cat1", 10, "005001", 1, 199), 1)); // remove not existing item

    }

}