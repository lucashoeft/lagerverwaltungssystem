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
        Assertions.assertEquals(false, myItem.isValid());

        myItem.description = "description";
        myItem.category = "category";
        myItem.stock = 0;
        myItem.location = "000000";
        myItem.weight = 0.1;
        myItem.price = 0.0;
        Assertions.assertEquals(true, myItem.isValid());

        myItem.description = "desc,ription";
        Assertions.assertEquals(false, myItem.isValid());

        myItem.description = null;
        Assertions.assertEquals(false, myItem.isValid());
        myItem.description = "description";

        myItem.category = "cate,gory";
        Assertions.assertEquals(false, myItem.isValid());

        myItem.category = null;
        Assertions.assertEquals(false, myItem.isValid());
        myItem.category = "category";

        myItem.stock = -1;
        Assertions.assertEquals(false, myItem.isValid());

        myItem.stock = null;
        Assertions.assertEquals(false, myItem.isValid());
        myItem.stock = 1;

        myItem.location = "00000";
        Assertions.assertEquals(false, myItem.isValid());

        myItem.location = null;
        Assertions.assertEquals(false, myItem.isValid());
        myItem.location = "000000";

        myItem.weight = 0.0;
        Assertions.assertEquals(false, myItem.isValid());

        myItem.weight = null;
        Assertions.assertEquals(false, myItem.isValid());
        myItem.weight = 0.1;

        myItem.price = -0.1;
        Assertions.assertEquals(false, myItem.isValid());

        myItem.price = null;
        Assertions.assertEquals(false, myItem.isValid());
        myItem.price = 0.0;

    }
}