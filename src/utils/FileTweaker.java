package utils;

import model.LotteryUrlPaths;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTweaker {

    private static List<String> lottoNames;

    public static void removeHeaderAndOverWrite() {

    }

    public static void overWriteFile(String game) throws IOException {

        LotteryUrlPaths paths = new LotteryUrlPaths();
        lottoNames = new ArrayList<>(paths.getPathFiles().keySet());

        int lineSkipCount = 1;

        String inputFileName = game + ".txt";
        String outPutFileName = game + "Ver2" + ".txt";

        try {

            File inputFile = new File(inputFileName);
            File outPutFile = new File(outPutFileName);

            try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(outPutFile))) {

                String line = null;
                while ((line = br.readLine()) != null) {

                    if (lineSkipCount < 6) {
                        lineSkipCount++;
                        continue;
                    }

                    bw.write(line);
                    bw.newLine();
                }
            }

            if (inputFile.delete()) {
                if (!outPutFile.renameTo(inputFile)) {
                    throw new IOException("Could not rename " + outPutFileName + " to " + inputFileName);
                }
            } else {
                throw new IOException("Could not delete original input file " + inputFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

