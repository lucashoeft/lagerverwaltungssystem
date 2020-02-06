import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        Validierungstests.class,
        Fehlertests.class
})

public class InventoryTestSuite {
}