package com.lottoanalysis.models;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@SuppressWarnings("unchecked")
public class RemainderTracker {

    private Map<Integer, Object[]> remainderHolder = new TreeMap<>();

    public void insertRemainderAndLottoNumber(int remainder, int winningLottoNumber){

        if(!remainderHolder.containsKey(remainder)){

            Set<Integer> numbers = new TreeSet<>();
            numbers.add( winningLottoNumber );

            remainderHolder.put(remainder, new Object[]{1,0, numbers});
            incrementGamesOut(remainderHolder, remainder);
        }
        else
        {

            Object[] data = remainderHolder.get( remainder );
            data[0] = (int) data[0] + 1;
            data[1] = 0;
            ((Set<Integer>)data[2]).add(winningLottoNumber);
            incrementGamesOut(remainderHolder, remainder);
        }
    }

    private void incrementGamesOut(Map<Integer,Object[]> data, int remainder ){

        data.forEach( (k,v) -> {

            if(k != remainder){

                v[1] = (int)v[1] + 1;
            }
        });
    }
}
