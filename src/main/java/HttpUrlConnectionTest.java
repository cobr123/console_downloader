import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by r.tabulov on 05.09.2016.
 */
public class HttpUrlConnectionTest {
    public static void main(String[] args) throws IOException {
        final URL url = new URL("http://stackoverflow.com/");
        try (InputStream stream = new BufferedInputStream(url.openStream())) {
            Files.copy(stream, Paths.get("d:\\test.html"));
        }
    }
}

