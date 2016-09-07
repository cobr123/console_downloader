package com.example.downloader;

import com.google.common.util.concurrent.RateLimiter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by r.tabulov on 06.09.2016.
 */
final public class DownloadItem {
    final String url;
    final List<String> fileNames;
    final String saveDir;
    private long downloadedBytes;

    public DownloadItem(final String url, final List<String> fileNames, final String saveDir) {
        this.url = url;
        this.fileNames = fileNames;
        this.saveDir = saveDir;
    }

    public void saveByLimit(final RateLimiter limiter) {
        try {
            final URL url = new URL(this.url);
            final String fileToSave = saveDir + File.pathSeparator + fileNames.get(0);
            try (InputStream is = url.openStream()) {
                if (limiter == null) {
                    Files.copy(is, Paths.get(fileToSave));
                } else {
                    try (InputStream stream = new BufferedInputStream(new LimitedInputStream(is, limiter))) {
                        Files.copy(stream, Paths.get(fileToSave));
                    }
                }
            }
            downloadedBytes = new File(fileToSave).length();
            for (int i = 1; i < fileNames.size(); ++i) {
                Files.copy(Paths.get(fileToSave), Paths.get(saveDir + File.pathSeparator + fileNames.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }
}
