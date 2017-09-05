package utils;

import model.LotteryGame;
import model.LotteryUrlPaths;

import java.util.*;

/**
 * Class will hold a variety of static methods that are capable of performing different operations when it comes to analyzing
 * number patterns for a given lottery game.
 */
public class NumberPatternAnalyzer {

    /**
     * Method will analyze each lottery draw position and return a map containing arrays as the keys that hold ranges
     * for the chosen game. And a values array that tracks total hits, games out, and map that
     * tracks how many times each number has been drawn in that group.
     *
     * @param positionVals
     * @param length
     * @param maxValue
     * @param minValue
     * @param lotteryGame
     * @return
     */
    public static Map<Integer[], Object[]> findNumberGroupLikelyToHit(int[] positionVals, int length, int minValue, int maxValue, LotteryGame lotteryGame) {

        int minVal = minValue;
        int maxVal = (maxValue < minVal) ? Math.abs(minVal + (minVal - lotteryGame.getMaxNumber())) : maxValue;
        Integer[] range = null;

        Map<Integer[], Object[]> gameRanges = new LinkedHashMap<>();

        // load up map with the ranges
        int count = 0;
        boolean flag = true;
        for (int i = minVal; i <= maxVal; ) {

            if ( length >= 5 && count % 4 == 0 && flag) {

                range = new Integer[4];
                while (count < 4) {
                    range[count] = i++;
                    ++count;
                }

                if(i > maxVal) {
                    i -= count;
                    count = 0;
                    flag = false;
                }
            }
            else{

                range = new Integer[4];
                while (count < 4) {
                    range[count] = i++;
                    ++count;
                }

                if(i > maxVal) {
                    i -= count;
                    count = 0;
                    flag = false;
                }

            }

            if(!flag){

                range = new Integer[Math.abs(i - maxVal) + 1];
                while (count < range.length) {
                    range[count] = i++;
                    ++count;
                }

            }

            gameRanges.put(range, new Object[]{0, 0, new HashMap<Integer, Integer>(),0});
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

        for(int i = 0; i < positionVals.length; i++){

            if(positionVals[i] == lastDigitNum && i < positionVals.length - 1){
                int nextNumAfterLastDigitNum = positionVals[i + 1];
                lastDigitPrecedingNumbers.add(nextNumAfterLastDigitNum);
            }
        }

        for(Map.Entry<Integer[],Object[]> pp : gameRanges.entrySet()){

            List<Integer> nums = Arrays.asList(pp.getKey());
            Object[] allData = pp.getValue();

            for(int i = 0; i < lastDigitPrecedingNumbers.size(); i++){

                if(nums.contains(lastDigitPrecedingNumbers.get(i))) {
                    allData[3] = (int) allData[3] + 1;
                    gameRanges.put(pp.getKey(), allData);
                }
            }
        }

        List<Integer> largestVals = new ArrayList<>();
        Map<Integer[], Object[]> values = new HashMap<>();
        for(Map.Entry<Integer[],Object[]> pp : gameRanges.entrySet()){
            Object[] vals = pp.getValue();
            largestVals.add((int)vals[3]);
            Collections.sort(largestVals);
        }


        for(Map.Entry<Integer[],Object[]> pp : gameRanges.entrySet()){
            Object[] allData = pp.getValue();
            if((int)allData[3] == largestVals.get(largestVals.size()-1)){
                values.put(pp.getKey(),allData);
                break;
            }
        }

        return values;
    }

    /**
     * Method will take in a two dimensional array and convert each number in each position to its corresponding delta
     * number sets. It will then return a two dimensional array containing delta number values for each position.
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
     * Method increments games out for all other values exect for the specified data parameter
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

            if(count == 0) {
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
            count=0;
        }

        int minVal = 1;
        int maxVal = 0;
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> d = data.keySet().iterator();
        while (d.hasNext()){
            Map<String, Integer[]> r = data.get(d.next());
            Map<String, Integer[]> rr = r;
            for(Map.Entry<String, Integer[]> dd : r.entrySet()){
                int[] ddd = Arrays.stream(dd.getValue()).mapToInt(i -> i).toArray();
                list.add(ddd[0]);
                Collections.sort(list);
            }

            int num = list.get(list.size() - 1);
            for(Map.Entry<String, Integer[]> dd : rr.entrySet()){
                int[] ddd = Arrays.stream(dd.getValue()).mapToInt(i -> i).toArray();
                if(ddd[0] == num && dd.getKey().equals("Above")){

                    minVal = avgRounded + 1;

                }
                else if(ddd[0] == num && dd.getKey().equals("Below")){
                    minVal = 1;
                    maxVal = avgRounded - 1;
                }
                else if(ddd[0] == num && dd.getKey().equals("Equal")){
                    minVal = avgRounded;
                    maxVal = avgRounded;
                }
            }
        }

        return new Object[]{avgRounded,minVal,maxVal,data};
    }
}
