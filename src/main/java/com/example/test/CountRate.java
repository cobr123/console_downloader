package com.example.test;

import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by r.tabulov on 05.09.2016.
 */
public class CountRate {
    private long start;
    private long cost;
    private String url;
    private long transferred = 0;

    CountRate(final String url) {
        this.url = url;
    }

    public static void main(String[] args) {
        final RateLimiter limiter = RateLimiter.create(4 * 1024 * 1024);
        final List<CountRate> list = new ArrayList<>();
        list.add(new CountRate("url1"));
        list.add(new CountRate("url2"));
        list.add(new CountRate("url3"));
        list.add(new CountRate("url4"));
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        try {
            forkJoinPool.submit(() ->
                    //parallel task here, for example
                    list.parallelStream().forEach(item -> item.readByLimit(limiter))
            ).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void readByLimit(final RateLimiter limiter) {
        try {
            try (final InfInputStream in = new InfInputStream()) {
                int count;
                start = System.currentTimeMillis();
                while ((count = in.read()) != -1) {
                    limiter.acquire(count);
                    transferred(count, in.size);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transferred(int length, long remain) {
        // Yet other length bytes has been transferred since the last time
        // this
        // method was called
        transferred += length;
        // System.out.println(length + " bytes");
        long tmpCost = System.currentTimeMillis() - start;
        if (tmpCost > cost) {
            cost = tmpCost;
            if (cost > 0 && cost % 1000 == 0) {
                final long transf = transferred;
                final long rate = transf / (cost / 1000);
//                System.out.printf("\rRead %,d MB, speed: %,d MB/s%n", transferred / 1024 / 1024, transferred / cost / 1000);
                System.out.println(url + ": Read " + toStr(transf) + ", remain " + toStr(remain) + ", speed: " + toStr(rate) + "/s");
            }
        }
    }

    public static String toStr(final long transf) {
        if (transf > 1_000_000_000) {
            return String.valueOf(transf / 1_000_000_000) + "gb";
        } else if (transf > 1_000_000) {
            return String.valueOf(transf / 1_000_000) + "mb";
        } else if (transf > 1_000) {
            return String.valueOf(transf / 1_000) + "kb";
        } else {
            return String.valueOf(transf) + "b";
        }
    }
}
