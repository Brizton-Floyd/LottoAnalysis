package com.lottoanalysis.models.gameoutanalyzers;

import java.util.*;

public class GameOutHitGrouper {

    Map<String,Integer[]> gameOutGroupHolderMap = new TreeMap<>();

    public GameOutHitGrouper(){

        populateMap();

    }

    public Map<String, Integer[]> getGameOutGroupHolderMap() {
        return gameOutGroupHolderMap;
    }

    public void checkValueAgainstMap(int val){

        gameOutGroupHolderMap.forEach( (k,v) -> {

            String[] data = k.split(",");

            if(data.length > 1){

                int digitOne = Integer.parseInt( data[0] );
                int digitTwo = Integer.parseInt( data[1] );

                if( val >= digitOne && val <= digitTwo ){

                    v[0]++;
                    v[1] = 0;
                    incrementGamesOut(gameOutGroupHolderMap, k);
                }
            }
            else{

                if( val >= Integer.parseInt(data[0]) ){

                    v[0]++;
                    v[1]=0;
                    incrementGamesOut(gameOutGroupHolderMap,k);
                }
            }
        });
    }

    private void incrementGamesOut(Map<String,Integer[]> gameOutGroupHolderMap, String range){

        gameOutGroupHolderMap.forEach((k,v) -> {

            if(!k.equals(range)){

                v[1]++;
            }
        });
    }
    private void populateMap() {

        gameOutGroupHolderMap.put("0,3",new Integer[]{0,0});
        gameOutGroupHolderMap.put("4,7",new Integer[]{0,0});
        gameOutGroupHolderMap.put("8,11",new Integer[]{0,0});
        gameOutGroupHolderMap.put("12,16",new Integer[]{0,0});
        gameOutGroupHolderMap.put("17,20",new Integer[]{0,0});
        gameOutGroupHolderMap.put("21",new Integer[]{0,0});
    }


}
