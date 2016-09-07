import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by r.tabulov on 06.09.2016.
 */
final public class LimitedInputStream extends InputStream {
    private final InputStream is;
    private final RateLimiter limiter;

    public LimitedInputStream(final InputStream stream, final RateLimiter limiter) {
        this.is = stream;
        this.limiter = limiter;
    }

    @Override
    public int read() throws IOException {
        final int count = is.read();
        limiter.acquire(count);
        return count;
    }
}
