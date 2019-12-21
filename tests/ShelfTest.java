import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void testEquals() {
        Shelf myShelf11 = new Shelf(1, 1.1);
        Shelf myShelf12 = new Shelf(1, 1.2);
        Shelf myShelf2 = new Shelf(2, 0.0);
        Assertions.assertEquals(true, myShelf11.equals(myShelf11));
        Assertions.assertEquals(false, myShelf11.equals(1));
        Assertions.assertEquals(true, myShelf11.equals(myShelf12)); // TODO: even if weight is different?
        Assertions.assertEquals(false, myShelf11.equals(myShelf2));
    }
}