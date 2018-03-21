package com.lottoanalysis.models;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.Map;
import java.util.TreeMap;

public class FirstDigitTracker {

    private Map<Integer,Integer[]> digitTracker = new TreeMap<>();

    public Map<Integer, Integer[]> getDigitTracker() {
        return digitTracker;
    }

    public void analyzeFirstDigitAndInsert(int digit){

        String digitAsString = digit + "";
        digit = (digitAsString.length() > 1) ? Character.getNumericValue(digitAsString.charAt(0)) : 0;

        if(!digitTracker.containsKey(digit)){
            digitTracker.put(digit, new Integer[]{1,0});
            NumberAnalyzer.incrementGamesOut(digitTracker,digit);
        }
        else{

            Integer[] data = digitTracker.get(digit);
            data[0]++;
            data[1] = 0;
            NumberAnalyzer.incrementGamesOut(digitTracker,digit);
        }
    }
}
