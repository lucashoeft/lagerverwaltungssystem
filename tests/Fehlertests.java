import com.lagerverwaltung.Category;
import com.lagerverwaltung.Inventory;
import com.lagerverwaltung.InventoryItem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Fehlertests {

    private Inventory myInventory = new Inventory();

    @Before
    public void setupTest() {
        assertEquals(0, myInventory.getShelfMap().size());
        assertEquals(0, myInventory.getCategoryMap().size());

        assertTrue(myInventory.addCategory(new Category("cat0")));
        assertEquals(1, myInventory.getCategoryMap().size());
        assertEquals(0, myInventory.getCategoryMap().get("cat0").getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass1Test() {
        // ÄK 1 (description = "" invalid)
        myInventory.addNewItem(new InventoryItem("", "cat0", 1, "001001", 10, 199));
    }

    @Test
    public void EquivalenceClass2Test() {
        // ÄK 2 (description valid)
        assertTrue(myInventory.addNewItem(new InventoryItem("item2", "cat0", 1, "001002", 10, 199)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass3Test() {
        // ÄK 3 (description invalid)
        myInventory.addNewItem(new InventoryItem("<>item3#", "cat0", 1200, "001003", 10, 199));
    }

    @Test
    public void EquivalenceClass4Test() {
        // ÄK 4 (category = "" invalid)
        assertFalse(myInventory.addNewItem(new InventoryItem("item4", "", 1, "001004", 10, 199)));
    }

    @Test
    public void EquivalenceClass5Test() {
        // ÄK 5 (category valid)
        assertTrue(myInventory.addNewItem(new InventoryItem("item5", "cat0", 1, "001005", 10, 199)));
    }

    @Test
    public void EquivalenceClass6Test() {
        // ÄK 6 (category invalid)
        assertFalse(myInventory.addNewItem(new InventoryItem("item6", "<>cat0#", 1, "001006", 10, 199)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass7Test() {
        // ÄK 7 (stock <0 invalid)
        myInventory.addNewItem(new InventoryItem("item7", "cat0", -5, "001007", 10, 199));
    }

    @Test
    public void EquivalenceClass8Test() {
        // ÄK 8 (stock valid)
        assertTrue(myInventory.addNewItem(new InventoryItem("item8", "cat0", 1, "001008", 10, 199)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass9Test() {
        // ÄK 9 (stock >100000000 invalid)
        myInventory.addNewItem(new InventoryItem("item9", "cat0", 100000001, "001009", 10, 199));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass10Test() {
        // ÄK 10 (location = "" invalid)
        myInventory.addNewItem(new InventoryItem("item10", "cat0", 1, "", 10, 199));
    }

    @Test
    public void EquivalenceClass11Test() {
        // ÄK 11 (location valid)
        assertTrue(myInventory.addNewItem(new InventoryItem("item11", "cat0", 1, "001011", 10, 199)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass12Test() {
        // ÄK 12 (location invalid)
        myInventory.addNewItem(new InventoryItem("item12", "cat0", 100000001, "0010121", 10, 199));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass13Test() {
        // ÄK 13 (weight <0 invalid)
        myInventory.addNewItem(new InventoryItem("item13", "cat0", 1, "001013", -10, 199));
    }

    @Test
    public void EquivalenceClass14Test() {
        // ÄK 14 (weight valid)
        assertTrue(myInventory.addNewItem(new InventoryItem("item14", "cat0", 1, "001014", 10, 199)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass15Test() {
        // ÄK 15 (weight > 100000000 invalid)
        myInventory.addNewItem(new InventoryItem("item15", "cat0", 1, "001015", 100000001, 199));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass16Test() {
        // ÄK 16 (price <0 invalid)
        myInventory.addNewItem(new InventoryItem("item16", "cat0", 1, "001016", 10, -199));

    }

    @Test
    public void EquivalenceClass17Test() {
        // ÄK 17 (price valid)
        assertTrue(myInventory.addNewItem(new InventoryItem("item17", "cat0", 1, "001017", 10, 199)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass18Test() {
        // ÄK 18 (price > 9999900 invalid)
        myInventory.addNewItem(new InventoryItem("item18", "cat0", 1, "001018", 10, 9999901));
    }

    @Test(expected = IllegalArgumentException.class)
    public void EquivalenceClass19Test() {
        // ÄK 19 (stock || weight negative invalid)
        myInventory.addNewItem(new InventoryItem("item20", "cat0", -100, "003020", 1000000, 199));
    }

    @Test
    public void EquivalenceClass20Test() {
        // ÄK 20 (stock + weight at location > 10t invalid)
        assertFalse(myInventory.addNewItem(new InventoryItem("item20", "cat0", 10000, "003020", 1000000, 199)));
    }
}
