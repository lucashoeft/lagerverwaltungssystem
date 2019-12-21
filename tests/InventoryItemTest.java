import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InventoryItemTest {

    @Test
    void toStringCSV() {
        InventoryItem myItem = new InventoryItem("Buntstifte Marke \"Premium\" Variante 2018", "Buntstifte", 100, "000000", 321.99, 2.99);
        Assertions.assertEquals("Buntstifte Marke \"Premium\" Variante 2018,Buntstifte,100,000000,321.99,2.99", myItem.toStringCSV());

        // check invalid items
        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", null, "000000", 321.99, 2.99);
        Assertions.assertEquals("", myItem.toStringCSV());

        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", 100, "000000", null, 2.99);
        Assertions.assertEquals("", myItem.toStringCSV());

        myItem = new InventoryItem("Buntstifte Marke \"Premium\", Variante 2018", "Buntstifte", 100, "000000", 321.99, null);
        Assertions.assertEquals("", myItem.toStringCSV());
    }

    @Test
    void isValid() {
        InventoryItem myItem = new InventoryItem(null, null, null, null, null, null);
        Assertions.assertFalse(myItem.isValid());

        myItem.description = "description";
        myItem.category = "category";
        myItem.stock = 0;
        myItem.location = "000000";
        myItem.weight = 0.1;
        myItem.price = 0.0;
        Assertions.assertTrue(myItem.isValid());

        myItem.description = "desc,ription";
        Assertions.assertFalse(myItem.isValid());

        myItem.description = null;
        Assertions.assertFalse(myItem.isValid());
        myItem.description = "description";

        myItem.category = "cate,gory";
        Assertions.assertFalse(myItem.isValid());

        myItem.category = null;
        Assertions.assertFalse(myItem.isValid());
        myItem.category = "category";

        myItem.stock = -1;
        Assertions.assertFalse(myItem.isValid());

        myItem.stock = null;
        Assertions.assertFalse(myItem.isValid());
        myItem.stock = 1;

        myItem.location = "00000";
        Assertions.assertFalse(myItem.isValid());

        myItem.location = null;
        Assertions.assertFalse(myItem.isValid());
        myItem.location = "000000";

        myItem.weight = 0.0;
        Assertions.assertFalse(myItem.isValid());

        myItem.weight = null;
        Assertions.assertFalse(myItem.isValid());
        myItem.weight = 0.1;

        myItem.price = -0.1;
        Assertions.assertFalse(myItem.isValid());

        myItem.price = null;
        Assertions.assertFalse(myItem.isValid());
        myItem.price = 0.0;

    }

    @Test
    void testEquals() {
        InventoryItem i = new InventoryItem("test1", "cat", 100, "001001", 10.0, 1.00);
        Assertions.assertFalse(i.equals(new InventoryItem("test1", "cat", 100, "001002", 10.0, 1.00))); // different location
        Assertions.assertFalse(i.equals(new InventoryItem("test2", "cat", 100, "001001", 10.0, 1.00))); // different article
        Assertions.assertTrue(i.equals(new InventoryItem("test1", "cat2", 100, "001001", 10.0, 1.00))); // different category
        Assertions.assertTrue(i.equals(new InventoryItem("test1", "cat", 100, "001001", 20.0, 1.00))); // different weight
        Assertions.assertTrue(i.equals(new InventoryItem("test1", "cat", 100, "001001", 10.0, 2.00))); // different price
        Assertions.assertFalse(i.equals(1)); // different class
    }
}