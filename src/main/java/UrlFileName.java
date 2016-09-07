/**
 * Created by r.tabulov on 06.09.2016.
 */
final public class UrlFileName {
    private String url;
    private String fileName;

    public UrlFileName(String[] urlAndFileName) {
        url = urlAndFileName[0];
        fileName = urlAndFileName[1];
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }
}
