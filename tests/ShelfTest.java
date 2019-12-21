import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void testEquals() {
        Shelf myShelf11 = new Shelf(1, 1.1);
        Shelf myShelf12 = new Shelf(1, 1.2);
        Shelf myShelf2 = new Shelf(2, 0.0);
        assertEquals(myShelf11, myShelf11);
        assertNotEquals(1, myShelf11);
        assertEquals(myShelf11, myShelf12); // TODO: even if weight is different?
        assertNotEquals(myShelf11, myShelf2);
    }
}