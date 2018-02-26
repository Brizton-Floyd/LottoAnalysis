package com.lottoanalysis.utilities.analyzerutilites;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("unchecked")
public class LineRetracementFinder {

    private static Map<String, Object[]> retracementData = new HashMap<>();

    public static void analyze(int currentNumber, int[] numbers) {

        Clear();
        loadData();
        analyzeDrawdata(currentNumber, numbers);
    }

    private static void loadData() {

        retracementData.put("Above", new Object[]{0, 0, new TreeMap<>()});
        retracementData.put("Below", new Object[]{0, 0, new TreeMap<>()});
        retracementData.put("Even", new Object[]{0, 0, new TreeMap<>()});
    }

    private static void Clear() {

        retracementData.clear();
    }

    private static void analyzeDrawdata(int currentNumber, int[] numbers) {

        int currentWinningNumber = currentNumber;
        int previousWinningNumber = numbers[numbers.length - 1];

        // lets determine what happens when number retraces to previous number
        for (int i = 0; i < numbers.length - 2; i++) {

            if (numbers[i] == currentWinningNumber && numbers[i + 1] == previousWinningNumber) {

                int nextNumber = numbers[i + 2];

                if (nextNumber > previousWinningNumber) {
                    Object[] data = retracementData.get("Above");
                    data[0] = (int) data[0] + 1;
                    data[1] = 0;

                    Map<Integer, Integer[]> numberData = (Map<Integer, Integer[]>) data[2];
                    if (!numberData.containsKey(nextNumber)) {
                        numberData.put(nextNumber, new Integer[]{1, 0});
                        NumberAnalyzer.incrementGamesOut(numberData, nextNumber);
                    } else {
                        Integer[] dataTwo = numberData.get(nextNumber);
                        dataTwo[0]++;
                        dataTwo[1] = 0;
                        NumberAnalyzer.incrementGamesOut(numberData, nextNumber);
                    }

                    NumberAnalyzer.incrementGamesOut(retracementData, "Above");

                } else if (nextNumber < previousWinningNumber) {
                    Object[] data = retracementData.get("Below");
                    data[0] = (int) data[0] + 1;
                    data[1] = 0;

                    Map<Integer, Integer[]> numberData = (Map<Integer, Integer[]>) data[2];
                    if (!numberData.containsKey(nextNumber)) {
                        numberData.put(nextNumber, new Integer[]{1, 0});
                        NumberAnalyzer.incrementGamesOut(numberData, nextNumber);
                    } else {
                        Integer[] dataTwo = numberData.get(nextNumber);
                        dataTwo[0]++;
                        dataTwo[1] = 0;
                        NumberAnalyzer.incrementGamesOut(numberData, nextNumber);
                    }

                    NumberAnalyzer.incrementGamesOut(retracementData, "Below");
                } else {
                    Object[] data = retracementData.get("Even");
                    data[0] = (int) data[0] + 1;
                    data[1] = 0;

                    Map<Integer,Integer[]> numberData = (Map<Integer, Integer[]>) data[2];
                    if(!numberData.containsKey(nextNumber)){
                        numberData.put(nextNumber,new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(numberData,nextNumber);
                    }
                    else{
                        Integer[] dataTwo = numberData.get(nextNumber);
                        dataTwo[0]++;
                        dataTwo[1] = 0;
                        NumberAnalyzer.incrementGamesOut(numberData,nextNumber);
                    }

                    NumberAnalyzer.incrementGamesOut(retracementData, "Even");
                }
            }

        }

    }
}
