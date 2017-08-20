package model;

import javafx.concurrent.Task;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class will be responsible for performing extensive work that requires additonal time to run
 * this will allow the UI to remain responsive
 */

public class DataDownLoader extends Task<Void> {

    // Url field
    private String url;

    public DataDownLoader(String url) {
        this.url = url;
    }

    @Override
    protected Void call() throws Exception {

        String ext = url.substring(url.lastIndexOf("."), url.length());

        // Open url connection
        URLConnection connection = new URL(url).openConnection();

        // establish how long our file will be
        long fileLength = connection.getContentLength();

        // use try block as it will automatically close all streams
        try (InputStream is = connection.getInputStream(); OutputStream os = Files.newOutputStream(Paths.get("" + ext))) {

            long nread = 0L;
            byte[] buf = new byte[8192];
            int n;

            while ((n = is.read(buf)) > 0) {
                os.write(buf, 0, n);
                nread += n;
                updateProgress(nread, fileLength);
            }
        }

        return null;
    }

    @Override
    protected void failed() {

    }

    @Override
    protected void succeeded() {

    }
}
