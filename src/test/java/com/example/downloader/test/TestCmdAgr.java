package com.example.downloader.test;

import com.example.downloader.Main;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

/**
 * Created by r.tabulov on 06.09.2016.
 */
final public class TestCmdAgr {
    public static void main(String[] args) throws IOException, ParseException {
        Main.main("-n 5 -l 2000k -o output_folder -f links.txt".split("\\s"));
    }
}
