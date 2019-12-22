import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FileHandlerTest {

    @Test
    void readInventoryFromNotExistingFile() {
        FileHandler myParser = new FileHandler();
        Assertions.assertEquals(0, myParser.readInventoryFromCSV(Paths.get("not_existing_file.csv")).size());
    }

    @Test
    void readInventoryFromEmptyFile() {
        assertDoesNotThrow(() -> {
            FileHandler myParser = new FileHandler();
            String fileName = TestHelpers.createTmpFileWithContent("my_test_database_", ".csv", "");
            Assertions.assertEquals(0, myParser.readInventoryFromCSV(Paths.get(fileName)).size());
            TestHelpers.deleteTmpFile(fileName);
        });
    }

    @Test
    void readInventoryFromFileWithContent() {
        assertDoesNotThrow(() -> {
            FileHandler myParser = new FileHandler();
            String content =
                    "\"Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065\",\"Füllfederhalter & Kugelschreiber\",8,\"000001\",116,1571\n" + // from example
                    "\n" + // empty line -> ignore
                    "ZIPIT, Wildlings,Federmäppchen,24,000023,375,799\n" + // description with embedded , -> invalid number of fields -> ignore
                    "ZIPIT Wildlings,Federmäppchen,24,000023,,799\n" + // weight not specified -> ignore
                    "ZIPIT Wildlings,Federmäppchen,24,000023,375,\n" + // price not specified -> ignore
                    "\"GirlZone Geschenke für Mädchen - \"Gelstifte Set für Mädchen\"\",Füllfederhalter & Kugelschreiber,24,000026,432,1299\n" + // description with embedded "
                    "Dreikant-Buntstift - STABILO Trio dick kurz - 12er Pack - mit 12 verschiedenen Farben\",\"Stifte\",21,\"000012\",692,475\n";
            String fileName = TestHelpers.createTmpFileWithContent("my_test_database_", ".csv", content);
            Assertions.assertEquals(3, myParser.readInventoryFromCSV(Paths.get(fileName)).size());
            // TODO check content of items
            TestHelpers.deleteTmpFile(fileName);
        });
    }
}
