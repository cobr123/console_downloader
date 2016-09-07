import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by r.tabulov on 05.09.2016.
 */
public class InfInputStream extends InputStream {
    long size = 1024*1024*10;

    @Override
    public int read() throws IOException {
        if(size <= 0){
            return -1;
        }
        size = size - 255;
        return 255;
    }
}
