package com.lottoanalysis.utilities;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.io.*;
import java.text.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.time.*;

public class FileTweaker {

    private static List<String> lottoNames;
    private static String startTime = "19:30:20";
    private static String endTime = "10:00:12";

    public static void removeHeaderAndOverWrite() {

    }

    public static void overWriteFile(String game) throws IOException {

        lottoNames = new ArrayList<>(OnlineFileUtility.getUrlPaths().keySet());

        int lineSkipCount = 0;

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

                    if(isTimeBetweenTwoTime(startTime,endTime)) {
                        while ((line = br.readLine()) != null) {

                            if (lineSkipCount < 6) {
                                lineSkipCount++;
                                continue;
                            }

                            if (count % 2 == 0) {
                                bw.write(line);
                                bw.newLine();
                            }
                            count++;
                        }
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

    private static boolean isTimeBetweenTwoTime(String initialTime, String finalTime) throws java.text.ParseException {

        boolean valid = false;

        LocalTime date6 = LocalTime.now();
        String dateAsString = date6.toString();
        String currentTime = dateAsString.substring(0,dateAsString.lastIndexOf("."));

        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

        if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg))
        {

            //Start Time
            java.util.Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);

            //Current Time
            java.util.Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);

            //End Time
            java.util.Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);

            if (finalTime.compareTo(initialTime) < 0)
            {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }

            java.util.Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0) && actualTime.before(calendar2.getTime()))
            {
                valid = true;
                return valid;

            } else {
                throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
            }
        }

        return valid;
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

