package com.lottoanalysis.models.gameoutanalyzers;

import java.util.*;

public class GameOutHitGrouper {

    Map<Integer,Integer[]> gameOutGroupHolderMap = new TreeMap<>();

    public GameOutHitGrouper(){

        populateMap();

    }

    public Map<Integer, Integer[]> getGameOutGroupHolderMap() {
        return gameOutGroupHolderMap;
    }

    public void checkValueAgainstMap(int val){

        if( gameOutGroupHolderMap.containsKey(val) ){

            Integer[] data = gameOutGroupHolderMap.get( val );
            data[0]++;
            data[1] = 0;
            incrementGamesOut(gameOutGroupHolderMap, val);
        }
    }

    private void incrementGamesOut(Map<Integer,Integer[]> data, int winningGameOut){

        data.forEach((k,v) -> {

            if(k != winningGameOut){

                v[1]++;
            }
        });
    }
    private void populateMap() {


        for(int i = 0; i <= 100; i++){

            gameOutGroupHolderMap.put(i, new Integer[]{0,0});
        }
    }

}
