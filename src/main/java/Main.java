import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by r.tabulov on 06.09.2016.
 */
final public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        final Options options = new Options();
        options.addOption("n", true, "количество одновременно качающих потоков (1,2,3,4....)");
        options.addOption("l", true, "общее ограничение на скорость скачивания, для всех потоков, размерность - байт/секунда, можно использовать суффиксы k,m (k=1024, m=1024*1024)");
        options.addOption("f", true, "путь к файлу со списком ссылок");
        options.addOption("o", true, "имя папки, куда складывать скачанные файлы");

        final Downloader downloader = new Downloader();
        final CommandLineParser parser = new BasicParser();
        final CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("n")) {
            int threadCnt = Integer.valueOf(cmd.getOptionValue("n"));
            downloader.setThreadCnt(threadCnt);
        }
        if (cmd.hasOption("l")) {
            long rateLimit = 0;
            String rateLimitStr = cmd.getOptionValue("l");
            switch (rateLimitStr.toLowerCase().charAt(rateLimitStr.length() - 1)) {
                case 'k':
                    rateLimit = Long.valueOf(rateLimitStr.substring(0, rateLimitStr.length() - 2)) * 1024;
                    break;
                case 'm':
                    rateLimit = Long.valueOf(rateLimitStr.substring(0, rateLimitStr.length() - 2)) * 1024 * 1024;
                    break;
                case 'g':
                    rateLimit = Long.valueOf(rateLimitStr.substring(0, rateLimitStr.length() - 2)) * 1024 * 1024 * 1024;
                    break;
                default:
                    rateLimit = Long.valueOf(rateLimitStr);
            }
            downloader.setRateLimit(rateLimit);
        }
        if (cmd.hasOption("f")) {
            final String filesList = cmd.getOptionValue("f");
            final List<String> urlList = Files.readAllLines(Paths.get(filesList));
            final Map<String, Set<String>> uniq = urlList.stream()
                    .map(line -> new UrlFileName(line.split("\\s")))
                    .collect(Collectors.groupingBy(UrlFileName::getUrl, Collectors.mapping(UrlFileName::getFileName, Collectors.toSet())))
                    ;
            downloader.setUrlAndFile(uniq);
        }
        if (cmd.hasOption("o")) {
            final String saveDir = cmd.getOptionValue("o");
            downloader.setSaveDir(saveDir);
        }
        downloader.run();
        System.out.println("время работы:");
        System.out.println("количество скачанных байт:");
    }
}
