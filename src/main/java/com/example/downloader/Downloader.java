package com.example.downloader;

import com.google.common.util.concurrent.RateLimiter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Created by r.tabulov on 06.09.2016.
 */
final public class Downloader {
    private int threadCnt = 1;
    private long rateLimit = 0;
    private String saveDir;
    private Map<String, Set<String>> urlAndFile;


    public void run() {
        if (urlAndFile == null) return;
        final RateLimiter limiter = (rateLimit == 0) ? null : RateLimiter.create(rateLimit);
        final List<DownloadItem> list = urlAndFile.keySet()
                .stream()
                .map(url -> new DownloadItem(url, urlAndFile.get(url).stream().collect(Collectors.toList()), saveDir))
                .collect(Collectors.toList());
        if (list.size() == 0) return;
        final ForkJoinPool forkJoinPool = new ForkJoinPool(threadCnt);
        try {
            forkJoinPool.submit(() ->
                    //parallel task here, for example
                    list.parallelStream().forEach(item -> item.saveByLimit(limiter))
            ).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setThreadCnt(int threadCnt) {
        this.threadCnt = threadCnt;
    }

    public void setRateLimit(long rateLimit) {
        this.rateLimit = rateLimit;
    }

    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    public void setUrlAndFile(Map<String, Set<String>> urlAndFile) {
        this.urlAndFile = urlAndFile;
    }
}
