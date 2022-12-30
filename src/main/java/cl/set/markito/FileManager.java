package cl.set.markito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileManager extends MarkitoBaseUtilsValues implements IFileManager {
    /**
     * Recibe un archivo que lee y devuelve su contenido.
     * @param strFileInput: Pathname del archivo.
     * @return strData: Contenido del archivo.
     */
    public String ReadFileToString(String strFileInput) {
        // TODO: printf(ANSI_YELLOW+"ReadFileToString %s\n", strFileInput);
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(strFileInput), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
}