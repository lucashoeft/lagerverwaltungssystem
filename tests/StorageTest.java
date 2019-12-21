import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class StorageTest {

    @Test
    void init() {
        Inventory myInventory = new Inventory();
        myInventory.addItem(new InventoryItem("item11", "cat1", 1100, "001001", 10.0, 1.99));
        myInventory.addItem(new InventoryItem("item12", "cat1", 1200, "001002", 10.0, 1.99));
        //myInventory.addItem(new InventoryItem("item13", "cat1", 1300, "001001", 10.0, 1.99)); // location already in use -> ignore in init()
        myInventory.addItem(new InventoryItem("item21", "cat1", 2, "002001", (1000.0 * 1000) / 2, 1.99));
        myInventory.addItem(new InventoryItem("item31", "cat1", 1, "003001", (1000.0 * 1000 * 10) + 0.1, 1.99)); // to heavy item -> ignore in init()

        Storage myStorage = new Storage();
        Assertions.assertEquals(0, myStorage.getShelfMap().size());

        myStorage.init(myInventory);
        Assertions.assertEquals(2, myStorage.getShelfMap().size());

    }

    @Test
    void updateStorageContent() {

        HashMap<Integer, Shelf> myMap = new HashMap<>();
        myMap.put(1, new Shelf(1, 0.0));
        myMap.put(2, new Shelf(2, 0.0));
        myMap.put(3, new Shelf(3, 0.0));
        Storage myStorage = new Storage();
        myStorage.setShelfMap(myMap);
        Assertions.assertEquals(1, myStorage.getShelfMap().get(1).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myStorage.addItemToStorage(new InventoryItem("item11", "cat1", 0, "001001", 10.0, 1.99), 100));
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(3).getWeight());

        //Assertions.assertEquals(false, myStorage.addItemToStorage(new InventoryItem("item12", "cat1", 0, "001001", 10.0, 1.99), 0)); // same place in shelf -> reject
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myStorage.addItemToStorage(new InventoryItem("item12", "cat1", 0, "001002", 20.0, 1.99), 100));
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myStorage.addItemToStorage(new InventoryItem("item21", "cat1", 0, "002001", 20.0, 1.99), 1000));
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(0.0, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myStorage.addItemToStorage(new InventoryItem("item31", "cat1", 0, "003001", 1.0 * 1000 * 1000, 1.99), 10));
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(10000000.0, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertFalse(myStorage.addItemToStorage(new InventoryItem("item32", "cat1", 0, "003002", 1.0, 1.99), 1)); // adding 1g exceeds weight limit
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(1.0*1000*1000*10, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myStorage.removeItemFromStorage(new InventoryItem("item31", "cat1", 10, "003001", 1.0 * 1000 * 1000, 1.99), 1)); // remove one item
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(1.0*1000*1000*9, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myStorage.addItemToStorage(new InventoryItem("item32", "cat1", 0, "003002", 1.0, 1.99), 1)); // adding 1g now allowed
        Assertions.assertEquals(3, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(1.0*1000*1000*9+1, myStorage.getShelfMap().get(3).getWeight());

        Assertions.assertTrue(myStorage.addItemToStorage(new InventoryItem("item41", "cat1", 0, "004001", 1.0, 1.99), 10)); // new shelf required
        Assertions.assertEquals(4, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(1.0*1000*1000*9+1, myStorage.getShelfMap().get(3).getWeight());
        Assertions.assertEquals(4, myStorage.getShelfMap().get(4).getId());
        Assertions.assertEquals(1.0*10, myStorage.getShelfMap().get(4).getWeight());

        Assertions.assertTrue(myStorage.removeItemFromStorage(new InventoryItem("item41", "cat1", 10, "004001", 1.0, 1.99), 1));
        Assertions.assertEquals(4, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(1.0*1000*1000*9+1, myStorage.getShelfMap().get(3).getWeight());
        Assertions.assertEquals(4, myStorage.getShelfMap().get(4).getId());
        Assertions.assertEquals(1.0*9, myStorage.getShelfMap().get(4).getWeight());

        Assertions.assertFalse(myStorage.removeItemFromStorage(new InventoryItem("item41", "cat1", 9, "004001", 1.0, 1.99), 10)); // can not remove more than in stock
        Assertions.assertEquals(4, myStorage.getShelfMap().size());
        Assertions.assertEquals(10.0 * 100 + 20.0 * 100, myStorage.getShelfMap().get(1).getWeight());
        Assertions.assertEquals(2, myStorage.getShelfMap().get(2).getId());
        Assertions.assertEquals(20.0 * 1000, myStorage.getShelfMap().get(2).getWeight());
        Assertions.assertEquals(3, myStorage.getShelfMap().get(3).getId());
        Assertions.assertEquals(1.0*1000*1000*9+1, myStorage.getShelfMap().get(3).getWeight());
        Assertions.assertEquals(4, myStorage.getShelfMap().get(4).getId());
        Assertions.assertEquals(1.0*9, myStorage.getShelfMap().get(4).getWeight());

        Assertions.assertFalse(myStorage.removeItemFromStorage(new InventoryItem("item51", "cat1", 10, "005001", 1.0, 1.99), 1)); // remove not existing item

    }

}