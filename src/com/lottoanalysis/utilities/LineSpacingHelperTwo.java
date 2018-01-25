package com.lottoanalysis.utilities;

import java.util.LinkedHashMap;
import java.util.*;
import java.util.Map;

@SuppressWarnings("unchecked")
public class LineSpacingHelperTwo {

    private static Map<Integer[], Object[]> gapGroupHolder = new LinkedHashMap<>();
    private static Map<String, Map<String, Object[]>> groupCompanionHitTracker = new LinkedHashMap<>();

    public static void analyze(List<Integer> positionNumbers, boolean include) {

        clear();
        plugDataIntoMap();

        analyzeNumbers(positionNumbers, include);
        print();
    }

    private static void analyzeNumbers(List<Integer> positionNumbers, boolean include) {

        int currentWinningNumber = positionNumbers.get(positionNumbers.size() - 1);
        List<String> patternHolder = new ArrayList<>();

        for (int i = 0; i < positionNumbers.size(); i++) {

            int lineSpacing;

            if(include)
              lineSpacing = computeDifference(positionNumbers.get(i), positionNumbers.get(i + 1));
            else
              lineSpacing = positionNumbers.get(i);

            gapGroupHolder.forEach((key, value) -> {

                List<Integer> data = Arrays.asList(key);
                if (data.contains(lineSpacing)) {

                    value[0] = (int) value[0] + 1;

                    Map<Integer,Integer[]> gamesOutTracker = (TreeMap<Integer,Integer[]>) value[3];
                    int gamesOut = (int)value[1];
                    if(!gamesOutTracker.containsKey(gamesOut)){

                        gamesOutTracker.put(gamesOut, new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(gamesOutTracker, gamesOut);
                    }else{
                        Integer[] info = gamesOutTracker.get( gamesOut );
                        info[0]++;
                        info[1]=0;
                        NumberAnalyzer.incrementGamesOut(gamesOutTracker,gamesOut);
                    }


                    value[1] = 0;

                    patternHolder.add(Arrays.toString(key));

                    Map<Integer, Integer[]> lineSpacingHolder = (TreeMap<Integer, Integer[]>) value[2];
                    if (!lineSpacingHolder.containsKey(lineSpacing)) {
                        lineSpacingHolder.put(lineSpacing, new Integer[]{1, 0});
                        NumberAnalyzer.incrementGamesOut(lineSpacingHolder, lineSpacing);
                    } else {
                        Integer[] lineSpacingData = lineSpacingHolder.get(lineSpacing);
                        lineSpacingData[0]++;
                        lineSpacingData[1] = 0;
                        NumberAnalyzer.incrementGamesOut(lineSpacingHolder, lineSpacing);
                    }

                    incrementGamesOut(gapGroupHolder, key);

                }
            });

        }

        String currentWinningPattern = patternHolder.get(patternHolder.size() - 1);
        groupCompanionHitTracker.put(currentWinningPattern, new LinkedHashMap<>());

        Map<String, Object[]> dataHolder = groupCompanionHitTracker.get(currentWinningPattern);

        String currentPattern;

        for (int i = 0; i < patternHolder.size() - 1; i++) {

            if (patternHolder.get(i).equals(currentWinningPattern)) {

                currentPattern = patternHolder.get(i + 1);

                if (!dataHolder.containsKey(patternHolder.get(i + 1))) {

                    dataHolder.put(patternHolder.get(i + 1), new Object[]{1, 0, 1, new TreeMap<Integer, Integer[]>()});

                    Map<Integer, Integer[]> map = (TreeMap<Integer, Integer[]>) dataHolder.get(patternHolder.get(i + 1))[3];
                    map.put((int) dataHolder.get(patternHolder.get(i + 1))[2], new Integer[]{1, 0});

                    resetCount(dataHolder, (int) dataHolder.get(patternHolder.get(i + 1))[2]);
                    incrementGamesOut(dataHolder, patternHolder.get(i + 1));
                    NumberAnalyzer.incrementGamesOut(map, (int) dataHolder.get(patternHolder.get(i + 1))[2]);
                } else {
                    Object[] da = dataHolder.get(patternHolder.get(i + 1));
                    da[0] = (int) da[0] + 1;
                    da[1] = 0;
                    incrementGamesOut(dataHolder, patternHolder.get(i + 1));

                    da[2] = (int) da[2] + 1;
                    int number = (int) da[2];

                    Map<Integer, Integer[]> map = (TreeMap<Integer, Integer[]>) dataHolder.get(patternHolder.get(i + 1))[3];

                    if (!map.containsKey(number)) {
                        map.put(number, new Integer[]{1, 0});

                        resetCount(dataHolder, number);
                        incrementGamesOut(dataHolder, currentPattern);
                        NumberAnalyzer.incrementGamesOut(map, number);

                    } else {

                        Integer[] dd = map.get(number);
                        dd[0]++;
                        dd[1] = 0;

                        resetCount(dataHolder, number);
                        NumberAnalyzer.incrementGamesOut(map, number);
                        incrementGamesOut(dataHolder, currentPattern);
                    }
                }
            }
        }

    }

    private static void resetCount(Map<String, Object[]> dataHolder, int number) {

        dataHolder.forEach((key, value) -> {

            int num = (int) value[2];
            if (num != number) {

                value[2] = 0;
            }
        });
    }

    private static void incrementGamesOut(Map<String, Object[]> data, String keyValue) {

        data.forEach((key, value) -> {

            if (!key.equals(keyValue)) {
                value[1] = (int) value[1] + 1;
            }
        });
    }

    private static void incrementGamesOut(Map<Integer[], Object[]> hitInformation, Integer[] arrayValues) {

        if (arrayValues != null) {

            hitInformation.forEach((key, value) -> {

                if (!Arrays.equals(key, arrayValues)) {

                    value[1] = (int) value[1] + 1;
                }

            });

        } else {

            hitInformation.forEach((key, value) -> {

                value[1] = (int) value[1] + 1;
            });
        }
    }

    public static int computeDifference(int numOne, int numTwo) {

//        String numOneAsString = Integer.toString(numOne);
//        String numTwoAsString = Integer.toString(numTwo);
//
//        numOne = (numOneAsString.length() > 1) ? Character.getNumericValue(numOneAsString.charAt(1)) : numOne;
//        numTwo = (numTwoAsString.length() > 1) ? Character.getNumericValue(numTwoAsString.charAt(1)) : numTwo;


        int dif = Math.abs(numOne - numTwo);

        switch (dif) {

            case 24:
                return 23;
            case 23:
                return 22;
            case 22:
                return 21;
            case 21:
                return 20;
            case 20:
                return 19;
            case 19:
                return 18;
            case 18:
                return 17;
            case 17:
                return 16;
            case 16:
                return 15;
            case 15:
                return 14;
            case 14:
                return 13;
            case 13:
                return 12;
            case 12:
                return 11;
            case 11:
                return 10;
            case 10:
                return 9;
            case 9:
                return 8;
            case 8:
                return 7;
            case 7:
                return 6;
            case 6:
                return 5;
            case 5:
                return 4;
            case 4:
                return 3;
            case 3:
                return 2;
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    private static void plugDataIntoMap() {

        gapGroupHolder.put(new Integer[]{0, 1, 2, 3, 4}, new Object[]{0, 0, new TreeMap<Integer, Integer[]>(), new TreeMap<Integer, Integer[]>()});
        gapGroupHolder.put(new Integer[]{5, 6, 7, 8, 9}, new Object[]{0, 0, new TreeMap<Integer, Integer[]>(), new TreeMap<Integer, Integer[]>()});
        gapGroupHolder.put(new Integer[]{10, 11, 12, 13, 14}, new Object[]{0, 0, new TreeMap<Integer, Integer[]>(), new TreeMap<Integer, Integer[]>()});
        gapGroupHolder.put(new Integer[]{15, 16, 17, 18, 19}, new Object[]{0, 0, new TreeMap<Integer, Integer[]>(), new TreeMap<Integer, Integer[]>()});
        gapGroupHolder.put(new Integer[]{20, 21, 22, 23, 24}, new Object[]{0, 0, new TreeMap<Integer, Integer[]>(), new TreeMap<Integer, Integer[]>()});

    }

    private static void print() {
        gapGroupHolder.forEach((key, value) -> {


            System.out.println("\n******************* Gap Group: " + Arrays.toString(key) + " *********************\n");
            System.out.println("Hits: " + value[0]);
            System.out.println("Group Games Out: " + value[1]);

            int g = (int)value[1];
            Integer[] gamesOut = ((TreeMap<Integer,Integer[]>)value[3]).get(g);
            if(gamesOut!=null) {

                System.out.println("Games Out Hits: " + gamesOut[0]);
                System.out.println("Hits Games Out: " + gamesOut[1]);

            }else {

                System.out.println("No hits at games out.");

            }

            ((Map<Integer, Integer[]>) value[2]).forEach((keyTwo, valueTwo) -> {

                System.out.println("\nLine Spacing: " + keyTwo);
                System.out.println("Hits: " + valueTwo[0]);
                System.out.println("Line Spacing Games Out: " + valueTwo[1] + "\n");
            });

        });
    }

    private static void clear() {
        gapGroupHolder.clear();
        groupCompanionHitTracker.clear();
    }
}
