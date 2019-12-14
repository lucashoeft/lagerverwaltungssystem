import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CSVParserTest {

    private String createTmpFileWithContent(String prefix, String suffix, String content) {
        String fileName = "";
        try {
            File tmpFile = File.createTempFile(prefix, suffix);
            PrintWriter pw = new PrintWriter(new FileWriter(tmpFile));
            pw.print(content);
            pw.close();
            fileName = tmpFile.getCanonicalPath();
        }
        catch (IOException e) { }
        System.out.printf("create tmp file: %s%n", fileName);
        return fileName;
    }

    private void deleteTmpFile(String fileName) {
        System.out.printf("delete tmp file: %s%n", fileName);
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) { }
    }

    @Test
    void readInventoryFromNotExistingFile() {
        CSVParser myParser = new CSVParser();
        Assertions.assertEquals(0, myParser.readInventoryFromCSV(Paths.get("not_existing_file.csv")).size());
    }

    @Test
    void readInventoryFromEmptyFile() {
        assertDoesNotThrow(() -> {
            CSVParser myParser = new CSVParser();
            String fileName = createTmpFileWithContent("my_test_database_", ".csv", "");
            Assertions.assertEquals(0, myParser.readInventoryFromCSV(Paths.get(fileName)).size());
            deleteTmpFile(fileName);
        });
    }

    @Test
    void readInventoryFromFileWithEmptyLine() {
        assertDoesNotThrow(() -> {
            CSVParser myParser = new CSVParser();
            String fileName = createTmpFileWithContent("my_test_database_", ".csv", "\n");
            Assertions.assertEquals(0, myParser.readInventoryFromCSV(Paths.get(fileName)).size());
            deleteTmpFile(fileName);
        });
    }
}
