package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class will hold a variety of static methods that are capable of performing different operations when it comes to analyzing
 * number patterns for a given lottery game.
 */
public class NumberPatternAnalyzer {

    /**
     * Method will analyze each lottery draw position and return a map containing arrays as the keys that hold ranges
     * 1 to 16 equally divided into 4 arrays. And a values array that tracks total hits, games out, and map that
     * tracks how many times each delta number has been drawn in that group.
     * @param positionVals
     * @return
     */
    public static Map<Integer[], Object[]> findDeltaNumberGroupLikelyToHit(int[] positionVals) {

        final int minVal = 1;
        final int maxVal = 16;

        Map<Integer[], Object[]> gameRanges = new LinkedHashMap<>();

        // load up map with the ranges
        int count = 0;
        for (int i = minVal; i <= maxVal; ) {
            Integer[] range = new Integer[4];
            while (count < 4) {
                range[count] = i++;
                ++count;
            }
            gameRanges.put(range, new Object[]{0, 0, new HashMap<Integer, Integer>()});
            count = 0;
        }

        for (int i = 0; i < positionVals.length; i++) {

            for (Map.Entry<Integer[], Object[]> p : gameRanges.entrySet()) {
                Integer[] data = p.getKey();
                if (Arrays.asList(data).contains(positionVals[i])) {
                    Object[] value = gameRanges.get(data);
                    value[0] = (int)value[0] + 1;
                    value[1] = 0;
                    Map<Integer, Integer> numberData = (Map<Integer, Integer>)value[2];
                    if(!numberData.containsKey(positionVals[i])){
                        numberData.put(positionVals[i],1);
                    }
                    else{
                        int numberCount = numberData.get(positionVals[i]);
                        numberData.put(positionVals[i],++numberCount);
                    }
                    gameRanges.put(data, value);
                    incrementGamesOutForAllOtherGroups(data, gameRanges);
                    break;
                }
            }

        }

        return gameRanges;
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
     * @param data
     * @param s
     */
    private static void incrementGamesOutForAllOtherGroups(Integer[] data, Map<Integer[], Object[]> s) {

        for (Map.Entry<Integer[], Object[]> d : s.entrySet()) {

            if (!d.getKey().equals(data)) {
                Object[] gamesOutVal = d.getValue();
                gamesOutVal[1] = (int)gamesOutVal[1] + 1;
            }
        }
    }
}
