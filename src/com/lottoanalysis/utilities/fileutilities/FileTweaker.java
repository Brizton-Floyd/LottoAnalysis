package com.lottoanalysis.utilities.fileutilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTweaker {

    private static List<String> lottoNames;

    public static void removeHeaderAndOverWrite() {

    }

    public static void overWriteFile(String game) throws IOException {


        lottoNames = new ArrayList<>(OnlineFileUtility.getUrlPaths().keySet());

        String inputFileName = game + ".txt";
        String outPutFileName = game + "Ver2" + ".txt";

        try {

            File inputFile = new File(inputFileName);
            File outPutFile = new File(outPutFileName);

            try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(outPutFile))) {

                String line;
                if(!inputFileName.equals("Daily Pick3.txt")) {
                    while ((line = br.readLine()) != null) {

                        String[] drawString = line.split("\\s+");

                        if (drawString[0].matches("[0-9]+")) {
                            bw.write(line);
                            bw.newLine();
                        }

                    }
                }else{

                    List<String> pick3EveningHolder = new ArrayList<>();

                    while ((line = br.readLine()) != null) {

                        String[] drawString = line.split("\\s+");

                        if (drawString[0].matches("[0-9]+")) {
                            if(Integer.parseInt(drawString[0]) % 2 == 1)
                                pick3EveningHolder.add(line);
                        }

                    }

                    pick3EveningHolder.forEach( string -> {
                        try {
                            bw.write(string);
                            bw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
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

