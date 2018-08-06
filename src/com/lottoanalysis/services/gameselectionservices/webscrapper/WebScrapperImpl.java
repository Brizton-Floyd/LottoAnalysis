package com.lottoanalysis.services.gameselectionservices.webscrapper;

import com.lottoanalysis.models.tasks.NotifyingThread;
import com.lottoanalysis.ui.gameselection.GameSelectionViewListener;
import com.lottoanalysis.utilities.fileutilities.FileTweaker;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class WebScrapperImpl extends Task<Void> implements WebScrapper {

    private BufferedReader br;
    private BufferedWriter bw;
    private GameSelectionViewListener gameSelectionViewListener;
    private Map<String, String> filePathsAndLotteryNames;

    public WebScrapperImpl(Map<String, String> data, GameSelectionViewListener gameSelectionViewListener){
        this.filePathsAndLotteryNames = data;
        this.gameSelectionViewListener = gameSelectionViewListener;
    }

    @Override
    protected Void call() throws Exception {

        System.out.print(Thread.currentThread().getName());
        updateMessage("Gathering Game Files From Internet...");

        int n;
        long nread = 0L;
        long fileLength = 0L;
        byte[] buf = new byte[8192];

        for (Map.Entry<String, String> data : filePathsAndLotteryNames.entrySet()) {

            URLConnection connection = new URL(data.getValue()).openConnection();
            fileLength += connection.getContentLength();
        }

        for (Map.Entry<String, String> data : filePathsAndLotteryNames.entrySet()) {

            // update lotto status label
            updateMessage("Updating " + data.getKey() + " Lotto Files");

            // Open url connection
            URLConnection connection = new URL(data.getValue()).openConnection();

            // use try block as it will automatically close all streams
            try (InputStream is = connection.getInputStream();
                 OutputStream os = Files.newOutputStream(Paths.get(data.getKey() + ".txt"))) {

                while ((n = is.read(buf)) > 0) {
                    os.write(buf, 0, n);
                    nread += n;
                    updateProgress(nread, fileLength);
                }

                // Remove the unwanted file heading at the top of the txt files
                updateMessage("Removing Unwanted Header From " + data.getKey() + " File");
                FileTweaker.overWriteFile(data.getKey());
            }

        }
        return null;
    }

    @Override
    protected void succeeded() {
        gameSelectionViewListener.invokeDbService();
    }
}
