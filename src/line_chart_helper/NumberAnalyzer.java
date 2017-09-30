package line_chart_helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by briztonfloyd on 9/23/17.
 */
public class NumberAnalyzer {

    public static Map<Integer, Map<String, Object[]>> findAverageAndGamesOut(int[] number, boolean inflateAvg) {

        int sum = 0;
        for (int num : number) {
            sum += num;
        }

        int average = (inflateAvg) ? (int) Math.round(Math.ceil(sum / number.length)) + 6 :
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

    private static void incrementGamesOut(Map<String, Object[]> data, String direction) {

        for (Map.Entry<String, Object[]> d : data.entrySet()) {

            if (!d.getKey().equals(direction)) {

                Object[] dd = d.getValue();
                dd[1] = (int) dd[1] + 1;
            }
        }

    }

    public static Map<Integer, Integer[]> findHitsAndGamesOutForRemainder(int[] secondElement) {
        Map<Integer, Integer[]> data = new TreeMap<>();

        // int recentDigit = secondElement[secondElement.length -1];

        for (int i = 0; i < secondElement.length; i++) {

            // if(secondElement[i] == recentDigit){

            if (i < secondElement.length - 1) {
                int remainder = secondElement[i + 1] % 3;
                if (!data.containsKey(remainder)) {
                    data.put(remainder, new Integer[]{1, 0});
                    incrementGamesOut(data, remainder);
                } else {
                    Integer[] values = data.get(remainder);
                    values[0]++;
                    values[1] = 0;
                    incrementGamesOut(data, remainder);
                }
            }
            // }
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
}
