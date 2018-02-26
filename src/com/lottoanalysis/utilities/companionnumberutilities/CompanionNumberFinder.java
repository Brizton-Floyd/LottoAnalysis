package com.lottoanalysis.utilities.companionnumberutilities;

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

        List<Integer> numbers = new ArrayList<>(companionNumberFirstElementHitDigitHolder);
       // System.out.println("Number List Size: " + numbers.size());
        companionNumberFirstElementHitDigitHolder.clear();
        
        findRecentWinningLottoNumber(numbers);
    }

    private static void print() {

        companionNumberHitTrackerMap.forEach((key, value) -> {

            System.out.println("\nRecent Winning Number: " + key);
            value.forEach((keyTwo, valueTwo) -> {

                System.out.println("Companion Number: " + keyTwo + "\t\tCompanion Hits and Games Out: " + Arrays.toString(valueTwo));
            });

        });

        companionNumberFirstDigitMap.forEach((key,value) -> {

            System.out.println("\nCurrent Winning Companion Number Hit Digit: " + key);
            value.forEach((keyTwo,valueTwo) -> {

                System.out.println("Companion Number For Hit Digit: " + keyTwo + "\t\tHits and Games Out: " + Arrays.toString(valueTwo));
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

        if(numbers.size() > 1 ){
            final int recentWinningNumber = numbers.get(numbers.size() - 1);
    
            plugCompanionNumbersIntoMap(recentWinningNumber, numbers);
        }
        
    }

    private static void plugCompanionNumbersIntoMap(int recentWinningNumber, List<Integer> numbers) {

        Map<Integer,Map<Integer,Map<Integer,Integer[]>>> data =  new TreeMap<>();
        data.put(0,companionNumberHitTrackerMap);
        data.put(1,companionNumberFirstDigitMap);

		Map<Integer,Map<Integer,Integer[]>> dataTwo = null;
		for(Map.Entry<Integer,Map<Integer,Map<Integer,Integer[]>>> entry : data.entrySet()){

			if(entry.getValue().size() == 0){
				dataTwo = entry.getValue();
				break;
			}
		}

        dataTwo.put(recentWinningNumber, new TreeMap<>());

        for (int i = 0; i < numbers.size() - 1; i++) {

            if (numbers.get(i) == recentWinningNumber) {
               // System.out.println("Before: " + numbers.get(i));
                int companionNumber = numbers.get(i + 1);
                //System.out.println("After: " + companionNumber);
                Map<Integer, Integer[]> companionNumberData = dataTwo.get(recentWinningNumber);

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
               // System.out.println(Integer.toString(trackerData[0]));
                int digit = (Integer.toString(trackerData[0]).length() > 1) ? Character.getNumericValue(Integer.toString(trackerData[0]).charAt(0)) : 0;
                getCompanionNumberFirstElementHitDigitHolder().add(digit);
                //System.out.println(digit + "\tHit with number: " + companionNumber);
            
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

}
