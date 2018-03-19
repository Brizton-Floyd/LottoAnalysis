package com.lottoanalysis.models;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.Map;
import java.util.TreeMap;

public class LottoNumberTracker {

    private Map<Integer,Integer[]> lottoNumberHitTracker = new TreeMap<>();

    public void insertNumberAndIncrementHits( int number ) {

        if (!lottoNumberHitTracker.containsKey(number)) {

            lottoNumberHitTracker.put(number, new Integer[]{1, 0});
            NumberAnalyzer.incrementGamesOut(lottoNumberHitTracker, number);
        }
        else{
            Integer[] data = lottoNumberHitTracker.get( number );
            data[0]++;
            data[1] = 0;
            NumberAnalyzer.incrementGamesOut(lottoNumberHitTracker,number);
        }
    }
}
