package com.lottoanalysis.models;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;

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

    public void insertHitsAndGamesOutForLottoNumbers(List<Map<Integer,Integer[]>> data ){

        for(Map<Integer,Integer[]> numData : data){

            for(Map.Entry<Integer,Integer[]> entry : numData.entrySet()){

                if(lottoNumberHitTracker.containsKey( entry.getKey() )){

                    Integer[] hitInfo = lottoNumberHitTracker.get( entry.getKey() );
                    Integer[] incomingInfo = entry.getValue();
                    incomingInfo[0] = hitInfo[0];
                    incomingInfo[1] = hitInfo[1];
                }
                else{
                    Integer[] incomingInfo = entry.getValue();
                    incomingInfo[0] = -1;
                    incomingInfo[1] = -1;
                }
            }
        }
    }
}
