import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

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
            FileHandler fileHandler = new FileHandler();

            // generate a database file
            String content =
                "STABILO-Dreikant-Buntstift,Stifte,21,010012,692,475\n" +
                "Lamy-Safari-Füllhalter,Füllfederhalter,8,010001,116,1571\n"; // from example
            String fileName = TestHelpers.createTmpFileWithContent("my_test_database_", ".csv", content);

             // load the database file
            Inventory myInventory = new Inventory();
            myInventory.setPath(fileName);
            HashMap<String, InventoryItem> itemMap = fileHandler.readInventoryFromCSV(Paths.get(myInventory.getPath())).getItemMap();
            myInventory.setItemMap(itemMap);
            HashMap<String, InventoryItem> items = myInventory.getItemMap();
            Assertions.assertEquals(2, items.size());
            Assertions.assertEquals("Lamy-Safari-Füllhalter", items.get("Lamy-Safari-Füllhalter").description);
            Assertions.assertEquals("Füllfederhalter", items.get("Lamy-Safari-Füllhalter").category);
            Assertions.assertEquals(8, items.get("Lamy-Safari-Füllhalter").stock);
            Assertions.assertEquals("010001", items.get("Lamy-Safari-Füllhalter").location);
            Assertions.assertEquals(116, items.get("Lamy-Safari-Füllhalter").weight);
            Assertions.assertEquals(1571, items.get("Lamy-Safari-Füllhalter").price);

            Assertions.assertEquals(1, myInventory.getShelfMap().size());
            Assertions.assertEquals(10, myInventory.getShelfMap().get(10).getId());
            Assertions.assertEquals(21*692+8*116, myInventory.getShelfMap().get(10).getWeight());

            Assertions.assertEquals(2, myInventory.getCategoryMap().size());
            Assertions.assertEquals(1, myInventory.getCategoryMap().get("Stifte").getCount());
            Assertions.assertEquals(1, myInventory.getCategoryMap().get("Füllfederhalter").getCount());

            // store the database database

            fileHandler.storeInventoryInCSV(myInventory);
            myInventory.setItemMap(fileHandler.readInventoryFromCSV(Paths.get(myInventory.getPath())).getItemMap()); // ... reload t
            items = myInventory.getItemMap(); // and check its content
            Assertions.assertEquals(2, items.size());
            Assertions.assertEquals("Lamy-Safari-Füllhalter", items.get("Lamy-Safari-Füllhalter").description);
            Assertions.assertEquals("Füllfederhalter", items.get("Lamy-Safari-Füllhalter").category);
            Assertions.assertEquals(8, items.get("Lamy-Safari-Füllhalter").stock);
            Assertions.assertEquals("010001", items.get("Lamy-Safari-Füllhalter").location);
            Assertions.assertEquals(116, items.get("Lamy-Safari-Füllhalter").weight);
            Assertions.assertEquals(1571, items.get("Lamy-Safari-Füllhalter").price);

            // cleanup used file
            TestHelpers.deleteTmpFile(fileName);
        });
    }

    @Test
    void addAndDeleteItem() {
        Inventory myInventory = new Inventory();

        Assertions.assertEquals(0, myInventory.getShelfMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().size());

        Assertions.assertTrue(myInventory.addCategory(new Category("cat0")));
        Assertions.assertFalse(myInventory.addCategory(new Category("cat0")));
        Assertions.assertTrue(myInventory.addCategory(new Category("cat1")));
        Assertions.assertEquals(2, myInventory.getCategoryMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat0").getCount());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat1").getCount());

        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item11", "cat1", 1100, "001001", 10, 199)));
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item12", "cat1", 1200, "001002", 10, 199)));
        Assertions.assertFalse(myInventory.addNewItem(new InventoryItem("item13", "cat1", 1300, "001001", 10, 199))); // location already in use -> ignore
        Assertions.assertTrue(myInventory.addNewItem(new InventoryItem("item21", "cat1", 2, "002001", (1000 * 1000) / 2, 199)));
        Assertions.assertFalse(myInventory.addNewItem(new InventoryItem("item31", "cat1", 1, "003001", (1000 * 1000 * 10 * 10) + 1, 199))); // to heavy item -> ignore in init()

        Assertions.assertEquals(2, myInventory.getShelfMap().size());
        Assertions.assertEquals(2, myInventory.getCategoryMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat0").getCount());
        Assertions.assertEquals(3, myInventory.getCategoryMap().get("cat1").getCount());

        Assertions.assertFalse(myInventory.deleteItem("item31")); // item not in inventory
        Assertions.assertTrue(myInventory.deleteItem("item21"));
        Assertions.assertEquals(2, myInventory.getShelfMap().size());
        Assertions.assertEquals(2, myInventory.getCategoryMap().size());
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat0").getCount());
        Assertions.assertEquals(2, myInventory.getCategoryMap().get("cat1").getCount());
        Assertions.assertEquals(2, myInventory.getShelfMap().get(2).getId());
        Assertions.assertEquals(0, myInventory.getShelfMap().get(2).getWeight());
        Assertions.assertFalse(myInventory.deleteItem("item21")); // Item already deleted

        Assertions.assertTrue(myInventory.removeCategory(new Category("cat0")));
        Assertions.assertEquals(1, myInventory.getCategoryMap().size());
        Assertions.assertFalse(myInventory.removeCategory(new Category("cat0"))); // category does not exist
        Assertions.assertFalse(myInventory.removeCategory(new Category("cat1"))); // category still used

        Assertions.assertTrue(myInventory.deleteItem("item11"));
        Assertions.assertTrue(myInventory.deleteItem("item12"));
        Assertions.assertEquals(0, myInventory.getCategoryMap().get("cat1").getCount());
        Assertions.assertTrue(myInventory.removeCategory(new Category("cat1")));
        Assertions.assertEquals(0, myInventory.getCategoryMap().size());

    }

    @Test
    void increaseAndDecreaseStock() {
        Inventory myInventory = new Inventory();

        //myInventory.addCategory(new Category("cat1"));
        myInventory.addNewItem(new InventoryItem("item11", "cat1", 1100, "001001", 10, 199));
        myInventory.addNewItem(new InventoryItem("item12", "cat1", 1200, "001002", 10, 199));

        // item does not exist
        Assertions.assertFalse(myInventory.increaseStockBy("item21", 10));
        Assertions.assertFalse(myInventory.decreaseStockBy("item21", 10));
        Assertions.assertEquals(1100, myInventory.getItem("item11").getStock());
        Assertions.assertEquals(1200, myInventory.getItem("item12").getStock());

        // item exists
        Assertions.assertTrue(myInventory.increaseStockBy("item11", 10));
        Assertions.assertTrue(myInventory.increaseStockBy("item12", -10));
        Assertions.assertEquals(1110, myInventory.getItem("item11").getStock());
        Assertions.assertEquals(1190, myInventory.getItem("item12").getStock());
        Assertions.assertTrue(myInventory.decreaseStockBy("item11", 10));
        Assertions.assertTrue(myInventory.decreaseStockBy("item12", -10));
        Assertions.assertEquals(1100, myInventory.getItem("item11").getStock());
        Assertions.assertEquals(1200, myInventory.getItem("item12").getStock());
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
        Assertions.assertEquals(0, myInventory.getShelfMap().get(3).getWeight());

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