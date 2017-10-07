package line_chart_helper;

import java.util.*;

/**
 * Created by briztonfloyd on 9/23/17.
 */
@SuppressWarnings("Unchecked")
public class NumberAnalyzer {

    public static Map<Integer, Map<String, Object[]>> findAverageAndGamesOut(int[] number, boolean inflateAvg) {

        int sum = 0;
        for (int num : number) {
            sum += num;
        }

        int average = (inflateAvg) ? (int) Math.round(Math.ceil(sum / number.length)) + 3 :
                (int) Math.round(Math.ceil(sum / number.length)) + 1;

        Map<Integer, Map<String, Object[]>> data = new HashMap<>();
        data.put(average, new HashMap<>());

        for (int val : number) {

            if (val >= average) {

                Map<String, Object[]> allData = data.get(average);
                if (!allData.containsKey("Above")) {
                    allData.put("Above", new Object[]{1, 0, new ArrayList<>(), 0, 0});
                    incrementGamesOut(allData, "Above");
                } else {
                    Object[] d = allData.get("Above");
                    ((ArrayList<Integer>) d[2]).add((int) d[1]);
                    d[0] = (int) d[0] + 1;
                    d[1] = 0;
                    incrementGamesOut(allData, "Above");
                }
            } else if (val < average) {
                Map<String, Object[]> belowData = data.get(average);
                if (!belowData.containsKey("Below")) {
                    belowData.put("Below", new Object[]{1, 0, new ArrayList<>(), 0, 0});
                    incrementGamesOut(belowData, "Below");
                } else {
                    Object[] d = belowData.get("Below");
                    ((ArrayList<Integer>) d[2]).add((int) d[1]);
                    d[0] = (int) d[0] + 1;
                    d[1] = 0;
                    incrementGamesOut(belowData, "Below");
                }

            }
        }

        // See what happens most often at the current games out
        for (Map.Entry<Integer, Map<String, Object[]>> d : data.entrySet()) {
            Map<String, Object[]> dd = d.getValue();

            for (Map.Entry<String, Object[]> values : dd.entrySet()) {

                ArrayList<Integer> ddd = (ArrayList<Integer>) values.getValue()[2];

                for (int num = 0; num < ddd.size(); num++) {

                    if (ddd.get(num) == (int) values.getValue()[1]) {

                        if (num < ddd.size() - 1) {
                            int num1 = ddd.get(num + 1);
                            if (num1 == 0) {
                                Object[] mm = values.getValue();
                                mm[3] = (int) mm[3] + 1;
                                mm[4] = 0;
                            } else {

                                Object[] mm = values.getValue();
                                mm[4] = (int) mm[4] + 1;

                            }
                        }
                    }
                }
            }
        }


        int num = number[number.length - 1];
        int numTwo = number[number.length - 2];
        int lesscount = 0;
        int greatercount = 0;
        int equalcount = 0;

        int count = 0;
        for (int i = 0; i < number.length; i++) {
            if (number[i] == num && i < number.length - 1) {
                if (number[i + 1] > numTwo)
                    greatercount++;
                else if (number[i + 1] < numTwo)
                    lesscount++;
                else
                    equalcount++;

                count++;
            }

        }
        return data;
    }

    public static void incrementGamesOut(Map<String, Object[]> data, String direction) {

        for (Map.Entry<String, Object[]> d : data.entrySet()) {

            if (!d.getKey().equals(direction)) {

                Object[] dd = d.getValue();
                dd[1] = (int) dd[1] + 1;
            }
        }

    }

    public static Map<Integer, Integer[]> findHitsAndGamesOutForRemainder(int[] secondElement) {
        Map<Integer, Integer[]> data = new TreeMap<>();

       // int recentDigit = secondElement[secondElement.length - 1];

        for (int i = 0; i < secondElement.length; i++) {

            //if (secondElement[i] == recentDigit) {

                if (i < secondElement.length - 1) {
                    int remainder = secondElement[i + 1] % 3;
                    if (!data.containsKey(remainder)) {
                        data.put(remainder, new Integer[]{1, 0, 0});
                        incrementGamesOut(data, remainder);
                    } else {
                        Integer[] values = data.get(remainder);
                        values[0]++;
                        values[2] = values[1];
                        values[1] = 0;
                        incrementGamesOut(data, remainder);
                    }
                }
            //}
        }

        return data;
    }

    private static void incrementGamesOut(Map<Integer, Integer[]> data, int remainder) {

        for (Map.Entry<Integer, Integer[]> dataTwo : data.entrySet()) {

            if (dataTwo.getKey() != remainder) {
                Integer[] values = dataTwo.getValue();
                values[1]++;
            }
        }
    }


    public static Map<Integer, Map<Integer, Integer[]>> findLastDigitsThatLastHitInRanges(int[] secondElement) {

        Map<Integer, Map<Integer, Integer[]>> data = new TreeMap<>();

        int recentDigit = secondElement[secondElement.length - 1];

        for (int i = 0; i < secondElement.length; i++) {

            if (secondElement[i] == recentDigit) {

                if (i < secondElement.length - 1) {
                    int remainder = secondElement[i + 1] % 3;
                    if (!data.containsKey(remainder)) {
                        data.put(remainder, new TreeMap<>());

                        Map<Integer, Integer[]> numbers = data.get(remainder);
                        if (!numbers.containsKey(secondElement[i + 1])) {
                            numbers.put(secondElement[i + 1], new Integer[]{1, 0});
                            incrementGamesOut(numbers, secondElement[i + 1]);
                        }

                    } else {
                        Map<Integer, Integer[]> values = data.get(remainder);
                        if (!values.containsKey(secondElement[i + 1])) {
                            values.put(secondElement[i + 1], new Integer[]{1, 0});
                            incrementGamesOut(values, secondElement[i + 1]);
                        } else {
                            Integer[] valuesTwo = values.get(secondElement[i + 1]);
                            valuesTwo[0]++;
                            valuesTwo[1] = 0;
                            incrementGamesOut(values, secondElement[i + 1]);
                        }
                    }
                }
            }
        }

        return data;
    }


    /**
     * Method determines what column numbers hit in together for a given drawing
     * @param drawNumbers
     * @param betslipMatrix
     */
    public static void determineColumnsNumbersHitIn(int[][] drawNumbers, int[][] betslipMatrix){

        Map<String, Object[]> numberAndPositionLocation = new LinkedHashMap<>();

        for(int i = 0; i < drawNumbers.length; i++){

            for(int k = 0; k < drawNumbers[i].length; k++){

                String columnNumber = determineColumnForCurrentNumber(drawNumbers[i][k], betslipMatrix);
                String position = "Position " + (i + 1);

                if(!numberAndPositionLocation.containsKey(position)){
                    List<String> list = new ArrayList<>();
                    list.add(columnNumber);
                    numberAndPositionLocation.put( position, new Object[]{list, new HashMap<String,Integer[]>(),
                        new HashMap<String,Map<String,Integer[]>>()} );
                }
                else{
                    List<String> positionData = (List<String>) numberAndPositionLocation.get(position)[0];
                    positionData.add(columnNumber);
                }
            }
        }

        // The below for loops are of no importance they are simply there to output the information the datastructures are holding so you can get a better
        // representation of the data contained within them
        for(Map.Entry<String,Object[]> vals : numberAndPositionLocation.entrySet()){
            List<String> columnHits = (List<String>) vals.getValue()[0];
            Map<String, Integer[]> columnOddOrEvenHitss = (Map<String, Integer[]>) vals.getValue()[1];
            Map<String,Map<String,Integer[]>> companionHitTracker = (Map<String,Map<String,Integer[]>>) vals.getValue()[2];

            //System.out.println(String.format("%1s \t Column %2s\n",vals.getKey(), columnHits));

            loadColumnOddOrEvenHits(columnHits,columnOddOrEvenHitss);
            loadInCompaionHitsForCurrentWinningColumn( columnHits, companionHitTracker );

            for(Map.Entry<String, Integer[]> dd : columnOddOrEvenHitss.entrySet()){

                System.out.println(String.format("Key %1s \t Value %2s\n",dd.getKey(), Arrays.toString( dd.getValue() )));

            }

            for(Map.Entry<String,Map<String,Integer[]>> ddd : companionHitTracker.entrySet()){
                Map<String,Integer[]> internalMap = ddd.getValue();
                for(Map.Entry<String,Integer[]> dddd : internalMap.entrySet()){
                    System.out.println(String.format("Companion Column %1s \t Value %2s\n",dddd.getKey(), Arrays.toString( dddd.getValue() )));
                }
            }
        }
    }
    /**
     *Method Takes in the most recent winning digit and searched through the entire history to find companion hits for the recent winning digit
     *@param columnHits
     *@param companionHitTracker
     */
    private static void loadInCompaionHitsForCurrentWinningColumn( List<String> columnHits, Map<String,Map<String,Integer[]>> companionHitTracker){

        String recentDigit = columnHits.get(columnHits.size() - 1);
        companionHitTracker.put(recentDigit, new TreeMap<>());

        for(int i = 0; i < columnHits.size(); i++){

            if(recentDigit.equals(columnHits.get(i))){

                if(i < columnHits.size() -1){
                    String followingDigit = columnHits.get(i + 1);
                    Map<String,Integer[]> data = companionHitTracker.get(recentDigit);
                    if(!data.containsKey(followingDigit)){
                        data.put(followingDigit, new Integer[]{1,0});
                        incrementGameOuts(data, followingDigit);
                    }
                    else{
                        Integer[] companionData = data.get( followingDigit );
                        companionData[0]++;
                        companionData[1] = 0;
                        incrementGameOuts( data, followingDigit );
                    }
                }
            }

        }

    }

    /**
     *Method Takes in a list of Integers representing winning columns the drawposition hit in and a map holding odd and even value representing odd and even columns hits, and games out.
     *@param columnHits
     *@param columnOddOrEvenHits
     */
    private static void loadColumnOddOrEvenHits(List<String> columnHits, Map<String, Integer[]> columnOddOrEvenHits){

        for(int i = 0; i < columnHits.size(); i++){

            int num = Integer.parseInt(columnHits.get(i));
            if(num % 2 == 1){
                if(!columnOddOrEvenHits.containsKey("Odd")){

                    columnOddOrEvenHits.put("Odd", new Integer[]{1,0});
                    incrementGameOuts(columnOddOrEvenHits, "Odd");
                }
                else{
                    Integer[] oddValues = columnOddOrEvenHits.get("Odd");
                    oddValues[0]++;
                    oddValues[1] = 0;
                    incrementGameOuts(columnOddOrEvenHits, "Odd");

                }
            }
            else{
                if(!columnOddOrEvenHits.containsKey("Even")){

                    columnOddOrEvenHits.put("Even", new Integer[]{1,0});
                    incrementGameOuts(columnOddOrEvenHits, "Even");

                }
                else{
                    Integer[] oddValues = columnOddOrEvenHits.get("Even");
                    oddValues[0]++;
                    oddValues[1] = 0;
                    incrementGameOuts(columnOddOrEvenHits, "Even");
                }
            }
        }
    }

    /**
     *Method simply increments games out for all columns that did not yield the correct winning digit for the most recent drawing
     *@param data
     *@param value
     */
    private static void incrementGameOuts(Map<String,Integer[]> data, String value){

        for(Map.Entry<String, Integer[]> dataAndValue : data.entrySet()){

            if(!dataAndValue.getKey().equals(value)){

                Integer[] arrayData = dataAndValue.getValue() ;
                arrayData[1]++;

            }

        }

    }

    /**
     * Method determines the column the passed in value has hit in.
     * @param lottoNumber
     * @param betslipMatrix
     */
    private static String determineColumnForCurrentNumber(int lottoNumber, int[][] betslipMatrix){

        List<Integer> betSlipColumns = new ArrayList<>();
        int columnIndex = 0;

        for(int i = 0; i < betslipMatrix.length; i++){

            for(int k = 0; k < betslipMatrix[i].length; k++){
                betSlipColumns.add(betslipMatrix[i][k]);
            }

            // check to see if value is located in the current row that was to just added to the ArrayList
            if(betSlipColumns.contains(lottoNumber)){
                columnIndex = (i + 1);
                break;
            }
        }

        return columnIndex + "";
    }

    /**
     *
     * @param number
     * @return
     */
    public static Map<Integer,Integer[]> findRemainderDueToHitInPosition(int[] number) {

        Map<Integer, Integer[]> data = new TreeMap<>();
        int lastDigit = number[number.length-1];

        for(int i =0; i < number.length; i++){

            //if((lastDigit == number[i]) && i < number.length - 1) {

                int remainder = number[i ] % 3;

                if (!data.containsKey(remainder)) {
                    data.put(remainder, new Integer[]{1, 0});
                    incrementGamesOut(data, remainder);
                } else {
                    Integer[] remainderData = data.get(remainder);
                    remainderData[0]++;
                    remainderData[1] = 0;
                    incrementGamesOut(data, remainder);
                }
           // }
        }
        return data;
    }
}
