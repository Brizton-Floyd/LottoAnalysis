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
        int count = 0;

        try {

            File inputFile = new File(inputFileName);
            File outPutFile = new File(outPutFileName);

            try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(outPutFile))) {

                String line;
                if(!inputFileName.equals("Daily Pick3.txt")) {
                    while ((line = br.readLine()) != null) {

                        if (lineSkipCount < 6) {
                            lineSkipCount++;
                            continue;
                        }

                        bw.write(line);
                        bw.newLine();
                    }
                }else{

                    while ((line = br.readLine()) != null) {

                        if (lineSkipCount < 6) {
                            lineSkipCount++;
                            continue;
                        }

                        if(count % 2 == 0) {
                            bw.write(line);
                            bw.newLine();
                        }
                        count++;
                    }
                }
            }

            inputFile.delete();
//            if (inputFile.delete()) {
//                if (!outPutFile.renameTo(inputFile)) {
//                    throw new IOException("Could not rename " + outPutFileName + " to " + inputFileName);
//                }
//            } else {
//                throw new IOException("Could not delete original input file " + inputFileName);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String trimStateAbrrFromGameName(String gameName) {

        String[] nameString = gameName.split("\\s");
        String gameRealName = "";
        for(int i = 1; i < nameString.length; i++){
            gameRealName += nameString[i];
        }

        return gameRealName;
    }
}

