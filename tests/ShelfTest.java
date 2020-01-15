import com.lagerverwaltung.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void testEquals() {
        Shelf myShelf11 = new Shelf(1, 11);
        Shelf myShelf12 = new Shelf(1, 12);
        Shelf myShelf2 = new Shelf(2, 0);
        assertEquals(myShelf11, myShelf11);
        assertNotEquals(1, myShelf11);
        assertEquals(myShelf11, myShelf12); // TODO: even if weight is different?
        assertNotEquals(myShelf11, myShelf2);
        assertNotEquals(myShelf11, 1);
    }
}