package com.lottoanalysis.utilities.analyzerutilites;

import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.utilities.linespacingutilities.LineSpacingHelperTwo;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * Class will hold a variety of static methods that are capable of performing different operations when it comes to analyzing
 * number patterns for a given lottery game.
 */
public class NumberPatternAnalyzer {

    private static Map<Integer, List<Integer>> multipleRanges = new LinkedHashMap<>();

    static {
        multipleRanges.put(7, new ArrayList<>());
        multipleRanges.put(5, new ArrayList<>());
        multipleRanges.put(3, new ArrayList<>());
        multipleRanges.put(2, new ArrayList<>());
        multipleRanges.put(1, new ArrayList<>());
    }

    private static List<Integer>[] gamesOut = new List[]{new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>()};

    /**
     * Method will analyze each lottery draw position and return a map containing arrays as the keys that hold ranges
     * for the chosen game. And a values array that tracks total hits, lottogames out, and map that
     * tracks how many times each number has been drawn in that group.
     *
     * @param positionVals
     * @param length
     * @param maxValue
     * @param minValue
     * @param lotteryGame
     * @return
     */
    public static Map<Integer[], Object[]> findNumberGroupLikelyToHit(int[] positionVals, int length, int minValue, int maxValue, LottoGame lotteryGame) {

        int minVal = minValue;
        int maxVal = (maxValue < minVal) ? Math.abs(minVal + (minVal - lotteryGame.getMaxNumber())) : maxValue;
        Integer[] range = null;

        Map<Integer[], Object[]> gameRanges = new LinkedHashMap<>();

        // load up map with the ranges
        int count = 0;
        boolean flag = true;
        for (int i = minVal; i <= maxVal; ) {

            if (length >= 5 && count % 4 == 0 && flag) {

                range = new Integer[4];
                while (count < 4) {
                    range[count] = i++;
                    ++count;
                }

                if (i > maxVal) {
                    i -= count;
                    count = 0;
                    flag = false;
                }
            } else {

                range = new Integer[4];
                while (count < 4) {
                    range[count] = i++;
                    ++count;
                }

                if (i > maxVal) {
                    i -= count;
                    count = 0;
                    flag = false;
                }

            }

            if (!flag) {

                range = new Integer[Math.abs(i - maxVal) + 1];
                while (count < range.length) {
                    range[count] = i++;
                    ++count;
                }

            }

            gameRanges.put(range, new Object[]{0, 0, new HashMap<Integer, Integer>(), 0});
            count = 0;

        }

        for (int i = 0; i < positionVals.length; i++) {

            for (Map.Entry<Integer[], Object[]> p : gameRanges.entrySet()) {
                Integer[] data = p.getKey();
                if (Arrays.asList(data).contains(positionVals[i])) {
                    Object[] value = gameRanges.get(data);
                    value[0] = (int) value[0] + 1;
                    value[1] = 0;
                    Map<Integer, Integer> numberData = (Map<Integer, Integer>) value[2];
                    if (!numberData.containsKey(positionVals[i])) {
                        numberData.put(positionVals[i], 1);
                    } else {
                        int numberCount = numberData.get(positionVals[i]);
                        numberData.put(positionVals[i], ++numberCount);
                    }
                    gameRanges.put(data, value);
                    incrementGamesOutForAllOtherGroups(data, gameRanges);
                    break;
                }
            }

        }

        int lastDigitNum = positionVals[positionVals.length - 1];
        List<Integer> lastDigitPrecedingNumbers = new LinkedList<>();

        for (int i = 0; i < positionVals.length; i++) {

            if (positionVals[i] == lastDigitNum && i < positionVals.length - 1) {
                int nextNumAfterLastDigitNum = positionVals[i + 1];
                lastDigitPrecedingNumbers.add(nextNumAfterLastDigitNum);
            }
        }

        for (Map.Entry<Integer[], Object[]> pp : gameRanges.entrySet()) {

            List<Integer> nums = Arrays.asList(pp.getKey());
            Object[] allData = pp.getValue();

            for (int i = 0; i < lastDigitPrecedingNumbers.size(); i++) {

                if (nums.contains(lastDigitPrecedingNumbers.get(i))) {
                    allData[3] = (int) allData[3] + 1;
                    gameRanges.put(pp.getKey(), allData);
                }
            }
        }

        List<Integer> largestVals = new ArrayList<>();
        Map<Integer[], Object[]> values = new HashMap<>();
        for (Map.Entry<Integer[], Object[]> pp : gameRanges.entrySet()) {
            Object[] vals = pp.getValue();
            largestVals.add((int) vals[3]);
            Collections.sort(largestVals);
        }


        for (Map.Entry<Integer[], Object[]> pp : gameRanges.entrySet()) {
            Object[] allData = pp.getValue();
            if ((int) allData[3] == largestVals.get(largestVals.size() - 1)) {
                values.put(pp.getKey(), allData);
                break;
            }
        }

        return values;
    }

    /**
     * Method will take in a two dimensional array and convert each number in each position to its corresponding delta
     * number sets. It will then return a two dimensional array containing delta number values for each position.
     *
     * @param positionalNumbers
     * @return
     */
    public static int[][] findDeltaNumbers(int[][] positionalNumbers) {

        int[][] data = new int[positionalNumbers.length][positionalNumbers[0].length];

        for (int i = 0; i < positionalNumbers.length; i++) {

            for (int k = 0; k < positionalNumbers[i].length; k++) {

                if (i == 0)
                    data[i][k] = positionalNumbers[i][k];
                else
                    data[i][k] = Math.abs(positionalNumbers[i - 1][k] - positionalNumbers[i][k]);
            }

        }

        return data;
    }

    /**
     * Method increments lottogames out for all other values exect for the specified data parameter
     *
     * @param data
     * @param s
     */
    private static void incrementGamesOutForAllOtherGroups(Integer[] data, Map<Integer[], Object[]> s) {

        for (Map.Entry<Integer[], Object[]> d : s.entrySet()) {

            if (!d.getKey().equals(data)) {
                Object[] gamesOutVal = d.getValue();
                gamesOutVal[1] = (int) gamesOutVal[1] + 1;
            }
        }
    }

    /**
     * Accepts the entire number history for a position and calculates the average value for that position. Returns a map
     * containing the average for position and the above and below average hits for that position.
     *
     * @param values
     * @return
     */
    public static Object[] computePositionalAvgAboveBelowHits(int[] values) {

        double average = 0.0;
        int sum = 0;
        Map<Integer, Map<String, Integer[]>> data = new HashMap<>();

        for (int val : values) {
            sum += val;
        }

        average = sum / values.length;
        int avgRounded = (int) Math.round(average) + 10;
        Map<String, Integer[]> aboveBelowCounts = new HashMap<>();
        aboveBelowCounts.put("Above", new Integer[]{0, 0});
        aboveBelowCounts.put("Below", new Integer[]{0, 0});
        aboveBelowCounts.put("Equal", new Integer[]{0, 0});

        data.put(avgRounded, aboveBelowCounts);

        int count = 0;
        for (int val : values) {

            if (count == 0) {
                count++;
                continue;
            }

            if (val > avgRounded) {
                Map<String, Integer[]> p = data.get(avgRounded);
                Integer[] value = p.get("Above");
                value[0]++;
                value[1] = 0;
                p.put("Above", value);

                Integer[] belowValues = p.get("Below");
                belowValues[1]++;
                p.put("Below", belowValues);

                Integer[] equalValues = p.get("Equal");
                equalValues[1]++;
                p.put("Equal", equalValues);


            } else if (val < avgRounded) {
                Map<String, Integer[]> p = data.get(avgRounded);
                Integer[] value = p.get("Below");
                value[0]++;
                value[1] = 0;
                p.put("Below", value);

                Integer[] aboveValues = p.get("Above");
                aboveValues[1]++;
                p.put("Above", aboveValues);

                Integer[] equalValues = p.get("Equal");
                equalValues[1]++;
                p.put("Equal", equalValues);


            } else {
                Map<String, Integer[]> p = data.get(avgRounded);
                Integer[] value = p.get("Equal");
                value[0]++;
                value[1] = 0;
                p.put("Equal", value);

                Integer[] aboveValues = p.get("Above");
                aboveValues[1]++;
                p.put("Above", aboveValues);

                Integer[] belowValues = p.get("Below");
                belowValues[1]++;
                p.put("Below", belowValues);

            }
            count = 0;
        }

        int minVal = 1;
        int maxVal = 0;
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> d = data.keySet().iterator();
        while (d.hasNext()) {
            Map<String, Integer[]> r = data.get(d.next());
            Map<String, Integer[]> rr = r;
            for (Map.Entry<String, Integer[]> dd : r.entrySet()) {
                int[] ddd = Arrays.stream(dd.getValue()).mapToInt(i -> i).toArray();
                list.add(ddd[0]);
                Collections.sort(list);
            }

            int num = list.get(list.size() - 1);
            for (Map.Entry<String, Integer[]> dd : rr.entrySet()) {
                int[] ddd = Arrays.stream(dd.getValue()).mapToInt(i -> i).toArray();
                if (ddd[0] == num && dd.getKey().equals("Above")) {

                    minVal = avgRounded + 1;

                } else if (ddd[0] == num && dd.getKey().equals("Below")) {
                    minVal = 1;
                    maxVal = avgRounded - 1;
                } else if (ddd[0] == num && dd.getKey().equals("Equal")) {
                    minVal = avgRounded;
                    maxVal = avgRounded;
                }
            }
        }

        return new Object[]{avgRounded, minVal, maxVal, data};
    }

    public static Map<Integer,Object[]> findElementValuesForMatrix(int[] positionalNumber) {

        // Map will hold matrix directions
        Map<Integer[], Map<String, Integer[]>> matrixDirectionValue = new LinkedHashMap<>();
        loadDataIntoMap(matrixDirectionValue);

        Map<Integer, Map<Integer, Integer>> companionNumbersForElementOne = new HashMap<>();

        for (int i = 0; i < positionalNumber.length; i++) {

            String val = Integer.toString(positionalNumber[i]);
            if (val.length() >= 2) {
                int val2 = Integer.parseInt(Character.toString(val.charAt(1)));
                if (!companionNumbersForElementOne.containsKey(val2)) {
                    Map<Integer, Integer> data = new HashMap<>();
                    data.put(Integer.parseInt(Character.toString(val.charAt(0))), 1);
                    companionNumbersForElementOne.put(val2, data);
                } else {
                    Map<Integer, Integer> dataTwo = companionNumbersForElementOne.get(val2);
                    if (!dataTwo.containsKey(Integer.parseInt(Character.toString(val.charAt(0))))) {
                        dataTwo.put(Integer.parseInt(Character.toString(val.charAt(0))), 1);
                    } else {

                        int val3 = dataTwo.get(Integer.parseInt(Character.toString(val.charAt(0))));
                        dataTwo.put(Integer.parseInt(Character.toString(val.charAt(0))), ++val3);
                        companionNumbersForElementOne.put(val2, dataTwo);
                    }
                }
            } else {

                int val2 = Integer.parseInt(Character.toString(val.charAt(0)));
                if (!companionNumbersForElementOne.containsKey(val2)) {
                    Map<Integer, Integer> data = new HashMap<>();
                    data.put(0, 1);
                    companionNumbersForElementOne.put(val2, data);
                } else {
                    Map<Integer, Integer> dataTwo = companionNumbersForElementOne.get(val2);

                    if (!dataTwo.containsKey(0)) {
                        dataTwo.put(0, 1);

                    } else {
                        int val3 = dataTwo.get(0);
                        dataTwo.put(0, ++val3);
                        companionNumbersForElementOne.put(val2, dataTwo);
                    }

                }
            }
        }
        // Determine which row is likely to come up in the next drawing

        Integer[][] valuesArrays = new Integer[][]{{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9}};

        for (int i = 0; i < positionalNumber.length; i++) {

            String numInStringFormat = Integer.toString(positionalNumber[positionalNumber.length - 1]);
            int num;

            if (numInStringFormat.length() > 1)
                num = Integer.parseInt(Character.toString(numInStringFormat.charAt(1)));
            else
                num = Integer.parseInt(Character.toString(numInStringFormat.charAt(0)));

            int secNum = 0;
            if (Integer.toString(positionalNumber[i]).length() > 1) {
                secNum = Integer.parseInt(String.valueOf(Integer.toString(positionalNumber[i]).charAt(1)));
            } else {
                secNum = Integer.parseInt(String.valueOf(Integer.toString(positionalNumber[i]).charAt(0)));
            }

            if (secNum == num) {

                for (Map.Entry<Integer[], Map<String, Integer[]>> dd : matrixDirectionValue.entrySet()) {

                    Map<String, Integer[]> matrixMap = dd.getValue();
                    Integer[] key = dd.getKey();

                    if (!Arrays.asList(key).contains(num))
                        continue;

                    if (i < positionalNumber.length - 1) {


                        String val = Integer.toString(positionalNumber[i + 1]);
                        if (val.length() > 1) {
                            num = Integer.parseInt(Character.toString(val.charAt(1)));
                        } else {
                            num = Integer.parseInt(Character.toString(val.charAt(0)));
                        }

                        if (Arrays.equals(key, valuesArrays[1])) {
                            rowTwoAlgorithm(matrixDirectionValue, num, matrixMap, key);
                            break;
                        } else if (Arrays.equals(key, valuesArrays[0])) {

                            rowOneAlgorithm(matrixDirectionValue, num, matrixMap, key);
                            break;
                        } else if (Arrays.equals(key, valuesArrays[2])) {

                            rowThreeAlgorithm(matrixDirectionValue, num, matrixMap, key);
                            break;

                        }
                    }
                }
            }
        }

        return analyzeNumberDataAndReturnBestRowAndElementZeroDigit(matrixDirectionValue,positionalNumber);
    }

    private static Map<Integer,Object[]> analyzeNumberDataAndReturnBestRowAndElementZeroDigit
            (Map<Integer[], Map<String, Integer[]>> matrixDirectionValue, int[] positionalNumbers) {


        int lastDigit;
        String num = Integer.toString(positionalNumbers[positionalNumbers.length - 1]);
        if(num.length() > 1)
            lastDigit = Integer.parseInt(Character.toString(num.charAt(1)));

        else
            lastDigit = Integer.parseInt(Character.toString(num.charAt(0)));

        int count = 0;
        for(Map.Entry<Integer[], Map<String,Integer[]>> dd : matrixDirectionValue.entrySet()){
            Map<String,Integer[]> data = dd.getValue();

            for(Map.Entry<String, Integer[]> ddd : data.entrySet()){

                if(!Arrays.asList(dd.getKey()).contains(lastDigit))
                    break;

                Integer[] integers = ddd.getValue();
                integers[2] = (integers[2] / integers[0]) + 2;

                for(int i = 0; i < gamesOut[count].size(); i++){

                    if(gamesOut[count].size() != integers[0]){
                        i--;
                        count++;
                        continue;
                    }
                    if(gamesOut[count].get(i) > integers[2])
                        integers[3]++;
                    else if (gamesOut[count].get(i) < integers[2])
                        integers[4]++;
                    else
                        integers[5]++;

                }
                count=0;
            }
        }


        return null;
    }

    private static void rowThreeAlgorithm(Map<Integer[], Map<String, Integer[]>> matrixDirectionValue, int num, Map<String, Integer[]> matrixMap, Integer[] key) {
        if (num >= key[0] && num <= key[key.length - 1]) {

            Integer[] sameData = matrixMap.get("Equal");
            gamesOut[0].add(sameData[1]);
            sameData[2] += sameData[1];
            sameData[0]++;
            sameData[1] = 0;
            matrixMap.put("Equal", sameData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "Equal");

        } else if (num < key[0] && num >= 4) {
            Integer[] downData = matrixMap.get("UpOne");
            gamesOut[1].add(downData[1]);
            downData[2] += downData[1];
            downData[0]++;
            downData[1] = 0;
            matrixMap.put("UpOne", downData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "UpOne");
        } else if (num < 4) {
            Integer[] upData = matrixMap.get("UpTwo");
            gamesOut[2].add(upData[1]);
            upData[2] += upData[1];
            upData[0]++;
            upData[1] = 0;
            matrixMap.put("UpTwo", upData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "UpTwo");
        }
    }

    private static void rowOneAlgorithm(Map<Integer[], Map<String, Integer[]>> matrixDirectionValue, int num, Map<String, Integer[]> matrixMap, Integer[] key) {
        if (num >= key[0] && num <= key[key.length - 1]) {
            Integer[] sameData = matrixMap.get("Equal");
            gamesOut[0].add(sameData[1]);
            sameData[2] += sameData[1];
            sameData[0]++;
            sameData[1] = 0;
            matrixMap.put("Equal", sameData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "Equal");

        } else if (num > key[key.length - 1] && num <= 7) {
            Integer[] downData = matrixMap.get("DownOne");
            gamesOut[1].add(downData[1]);
            downData[2] += downData[1];
            downData[0]++;
            downData[1] = 0;
            matrixMap.put("DownOne", downData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "DownOne");
        } else if (num > 7 && num <= 9) {
            Integer[] upData = matrixMap.get("DownTwo");
            gamesOut[2].add(upData[1]);
            upData[2] += upData[1];
            upData[0]++;
            upData[1] = 0;
            matrixMap.put("DownTwo", upData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "DownTwo");
        }
    }

    private static void rowTwoAlgorithm(Map<Integer[], Map<String, Integer[]>> matrixDirectionValue, int num, Map<String, Integer[]> matrixMap, Integer[] key) {
        if (num >= key[0] && num <= key[key.length - 1]) {
            Integer[] sameData = matrixMap.get("Equal");
            gamesOut[0].add(sameData[1]);
            sameData[2] += sameData[1];
            sameData[0]++;
            sameData[1] = 0;
            matrixMap.put("Equal", sameData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "Equal");
        } else if (num > key[key.length - 1]) {
            Integer[] downData = matrixMap.get("Down");
            gamesOut[1].add(downData[1]);
            downData[2] += downData[1];
            downData[0]++;
            downData[1] = 0;
            matrixMap.put("Down", downData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "Down");
        } else if (num < key[0]) {
            Integer[] upData = matrixMap.get("Up");
            gamesOut[2].add(upData[1]);
            upData[2] += upData[1];
            upData[0]++;
            upData[1] = 0;
            matrixMap.put("Up", upData);
            matrixDirectionValue.put(key, matrixMap);

            incrementGamesOutForMatrix(matrixMap, "Up");
        }
    }

    public static void incrementGamesOutForMatrix(Map<String, Integer[]> data, String direction) {

        for (Map.Entry<String, Integer[]> d : data.entrySet()) {

            String key = d.getKey();

            if (!d.getKey().equals(direction)) {
                Integer[] vals = d.getValue();
                vals[1]++;
                data.put(key, vals);
            }
        }

    }

    private static void loadDataIntoMap(Map<Integer[], Map<String, Integer[]>> data) {

        Integer[][] directions = new Integer[][]{

                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {8, 9}

        };

        String[] rowOneData = {"Equal", "DownOne", "DownTwo"};
        String[] rowTwoData = {"Up", "Equal", "Down"};
        String[] rowThreeData = {"Equal", "UpOne", "UpTwo"};

        String[][] stringDirection = new String[][]{

                rowOneData,
                rowTwoData,
                rowThreeData
        };

        data.put(directions[0], new LinkedHashMap<>());
        data.put(directions[1], new LinkedHashMap<>());
        data.put(directions[2], new LinkedHashMap<>());

        int count = 0;

        for (Map.Entry<Integer[], Map<String, Integer[]>> dd : data.entrySet()) {

            Map<String, Integer[]> d = dd.getValue();

            for (int i = count; i < directions.length; i++) {

                for (int k = 0; k < stringDirection[i].length; k++) {

                    d.put(stringDirection[i][k], new Integer[]{0, 0, 0, 0, 0, 0});
                }
                data.put(directions[i], d);
                count++;
                break;
            }
        }
    }

    /**
     * Method finds the sum of each digit for a given draw index
     * @param positionalNumbers
     * @return
     */
    public static int[][] findPositionalSums(int[][] positionalNumbers) {

        int[][] data = new int[positionalNumbers.length][];
        int sum = 0;

        for(int i = 0; i < positionalNumbers.length; i++){

            int[] positionData = new int[positionalNumbers[i].length];

            for(int k = 0; k < positionalNumbers[i].length; k++){

                String res = positionalNumbers[i][k] + "";
                if(res.length() > 1){
                    int indexOne = Integer.parseInt(Character.toString(res.charAt(0)));
                    int indexTwo = Integer.parseInt(Character.toString(res.charAt(1)));

                    sum = indexOne + indexTwo;
                    positionData[k] = sum;

                }else {
                    positionData[k] = positionalNumbers[i][k];
                }
            }

            data[i] = positionData;
        }

        return data;
    }

    /**
     * Method finds the sum of each digit for a given draw index
     * @param positionalNumbers
     * @return
     */
    public static int[][] lineSpacings(int[][] positionalNumbers) {

        int[][] data = new int[positionalNumbers.length][];
        int sum = 0;

        for(int i = 0; i < positionalNumbers.length; i++){

            int[] positionData = new int[positionalNumbers[i].length-1];

            for(int k = 0; k < positionalNumbers[i].length-1; k++){

                int res = LineSpacingHelperTwo.computeDifference(positionalNumbers[i][k],positionalNumbers[i][k+1]);
                positionData[k] = res;
            }

            data[i] = positionData;
        }

        return data;
    }


    /**
     * Method finds the sum of each digit for a given draw index
     * @param positionalNumbers
     * @return
     */
    public static int[][] computeRemainders(int[][] positionalNumbers) {

        int[][] data = new int[positionalNumbers.length][];
        int sum = 0;

        for(int i = 0; i < positionalNumbers.length; i++){

            int[] positionData = new int[positionalNumbers[i].length];

            for(int k = 0; k < positionalNumbers[i].length; k++){

                int num = positionalNumbers[i][k];
                int remainder = num % 3;
                positionData[k] = remainder;
            }

            data[i] = positionData;
        }

        return data;
    }

    public static int[][] getLastDigits(int[][] positionalNumbers) {

        int[][] lastDigits = new int[positionalNumbers.length][positionalNumbers[0].length];

        for(int i = 0; i < positionalNumbers.length; i++){

            for(int j = 0; j < positionalNumbers[i].length; j++){

                String numAsString = positionalNumbers[i][j] + "";
                lastDigits[i][j] = (numAsString.length() >  1) ? Character.getNumericValue( numAsString.charAt(1)) :
                        Character.getNumericValue(numAsString.charAt(0));
            }
        }

        return lastDigits;
    }

    public static List<Integer> getLastDigits(List<Integer> positionalNumbers) {

        List<Integer> data = new ArrayList<>();

        for(int i = 0; i < positionalNumbers.size(); i++){

                String numAsString = positionalNumbers.get(i) + "";
                int val = (numAsString.length() >  1) ? Character.getNumericValue( numAsString.charAt(1)) :
                        Character.getNumericValue(numAsString.charAt(0));
                data.add(val);
        }

        return data;
    }

    /**
     * Loader that injects values into perspective draw positions from a Lotterygame object.
     *
     * @param positionalNumbers
     * @param drawingData
     */
    public static void loadUpPositionalNumbers(int[][] positionalNumbers, List<Drawing> drawingData) {

        for (int i = 0; i < drawingData.size(); i++) {

            if (positionalNumbers.length == 3) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
            } else if (positionalNumbers.length == 4) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
            } else if (positionalNumbers.length == 5) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
                positionalNumbers[4][i] = Integer.parseInt(drawingData.get(i).posFiveProperty().get());
            } else if (positionalNumbers.length == 6) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
                positionalNumbers[4][i] = Integer.parseInt(drawingData.get(i).posFiveProperty().get());
                positionalNumbers[5][i] = Integer.parseInt(drawingData.get(i).bonusNumberProperty().get());

            }


        }
    }

    public static int[][] findMultiples(int[][] positionalNumbers) {
        int[][] data = new int[positionalNumbers.length][positionalNumbers[0].length];
        for(int i = 0; i < positionalNumbers.length; i++){

            for(int j = 0; j < positionalNumbers[i].length; j++){

                data[i][j] = getMultiple( positionalNumbers[i][j]);
            }
        }

        return data;
    }

    private static int getMultiple(int i) {

        for(Map.Entry<Integer,List<Integer>> entry : multipleRanges.entrySet()){

            if(i % entry.getKey() == 0  ) {

                if(!entry.getValue().contains(i)){
                    entry.getValue().add( i );
                }

                return entry.getKey();
            }
        }

        return -1;
    }
}
