import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHelpers {

    static String createTmpFileWithContent(String prefix, String suffix, String content) {
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

    static void deleteTmpFile(String fileName) {
        System.out.printf("delete tmp file: %s%n", fileName);
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) { }
    }

}
