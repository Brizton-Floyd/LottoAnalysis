package com.lottoanalysis.utilities;

import java.util.*;

public class CompanionNumberFinder {

    private static Map<Integer, Map<Integer, Integer[]>> companionNumberHitTrackerMap = new TreeMap<>();
    private static Map<Integer,Map<Integer,Integer[]>> companionNumberFirstDigitMap = new TreeMap<>();
    private static  List<Integer> companionNumberFirstElementHitDigitHolder = new ArrayList<>();

    public static List<Integer> getCompanionNumberFirstElementHitDigitHolder() {

        return companionNumberFirstElementHitDigitHolder;
    }

    /*
    *
    *	Method will start the analyzing process for all incoming numbers
    */
    public static void analyzeIncomingInformation(List<Integer> positionNumbers) {

        clearMap();
        findRecentWinningLottoNumber(positionNumbers);
        findRecentWinningDigitNumber(companionNumberFirstElementHitDigitHolder);
        print();

    }

    private static void findRecentWinningDigitNumber(List<Integer> companionNumberFirstElementHitDigitHolder) {

        findRecentWinningLottoNumber(companionNumberFirstElementHitDigitHolder);
    }

    private static void print() {

        companionNumberHitTrackerMap.forEach((key, value) -> {

            System.out.println("Recent Winning Number: " + key);
            value.forEach((keyTwo, valueTwo) -> {

                System.out.println("Companion Number: " + keyTwo + "\t\tCompanion Hits and Games Out: " + Arrays.toString(valueTwo));
            });

        });

    }

    private static void clearMap() {

        if (companionNumberHitTrackerMap.size() > 0) {
            companionNumberHitTrackerMap.clear();
            companionNumberFirstElementHitDigitHolder.clear();
            companionNumberFirstDigitMap.clear();
        }
    }

    private static void findRecentWinningLottoNumber(List<Integer> numbers) {

        final int recentWinningNumber = numbers.get(numbers.size() - 1);

        plugCompanionNumbersIntoMap(recentWinningNumber, numbers);
    }

    private static void plugCompanionNumbersIntoMap(int recentWinningNumber, List<Integer> numbers) {

        Map<Integer,Map<Integer,Map<Integer,Integer[]>>> data =  new TreeMap<>();
        data.put(0,companionNumberHitTrackerMap);
        data.put(1,companionNumberFirstDigitMap);



        companionNumberHitTrackerMap.put(recentWinningNumber, new TreeMap<>());

        for (int i = 0; i < numbers.size() - 1; i++) {

            if (numbers.get(i) == recentWinningNumber) {

                int companionNumber = numbers.get(i + 1);
                Map<Integer, Integer[]> companionNumberData = companionNumberHitTrackerMap.get(recentWinningNumber);

                if (!companionNumberData.containsKey(companionNumber)) {

                    companionNumberData.put(companionNumber, new Integer[]{1, 0});
                    incrementGamesOut(companionNumberData, companionNumber);
                } else {

                    Integer[] companionNumberCountData = companionNumberData.get(companionNumber);
                    companionNumberCountData[0]++;
                    companionNumberCountData[1] = 0;
                    incrementGamesOut(companionNumberData, companionNumber);
                }

                // place numbers into hit tracker list
                Integer[] trackerData = companionNumberData.get(companionNumber);
                int digit = (Integer.toString(trackerData[0]).length() > 1) ? Character.getNumericValue(Integer.toString(trackerData[0]).charAt(0))
                                                                            : 0;
                getCompanionNumberFirstElementHitDigitHolder().add(digit);
            }

        }

    }

    private static void incrementGamesOut(Map<Integer, Integer[]> companionNumberData, int companionNumber) {

        companionNumberData.forEach((key, value) -> {

            if (key != companionNumber) {
                value[1]++;
            }
        });
    }

    private class CompanionNumberHitDigitAnalyzer {

        private List<Integer> numbers = new ArrayList<>();

        public CompanionNumberHitDigitAnalyzer() {

        }

        private void extractRelevantInformationFromMap(Map<Integer, Map<Integer, Integer[]>> incomingData) {
            // will set numbers in here after extracting data
			Map.Entry<Integer,Map<Integer,Integer[]>> data = incomingData.entrySet().iterator().next();

            Map<Integer,Integer[]> values = data.getValue();
			values.forEach( (key,val) -> {


			});
        }

        public List<Integer> getNumbers() {

            return numbers;
        }
    }
}
