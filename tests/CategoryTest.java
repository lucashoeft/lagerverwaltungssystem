
import com.lagerverwaltung.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void testEquals() {
        Category myCategory = new Category("cat1", 0);
        Assertions.assertFalse(myCategory.equals(1));
        Assertions.assertFalse(myCategory.equals(new Category("cat2", 0)));
        Assertions.assertTrue(myCategory.equals(new Category("cat1", 0)));
        Assertions.assertTrue(myCategory.equals(new Category("cat1", 2)));
        Assertions.assertTrue(myCategory.equals(myCategory));
    }
}