package com.lottoanalysis.models.gameoutanalyzers;

import com.lottoanalysis.comparators.HitComparator;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;

public class GameOutHitGrouper {

    Map<String,Integer[]> gameOutGroupHolderMap = new LinkedHashMap<>();
    Map<Integer,Integer[]> gameOutTracker = new LinkedHashMap<>();

    public GameOutHitGrouper(){

        populateMap();

    }

    public Map<Integer, Integer[]> getGameOutTracker(String range) {

        Map<Integer,Integer[]> modifiedData = new LinkedHashMap<>();

        String[] ranges = range.split(",");
        if(ranges.length > 1) {
            int min = Integer.parseInt(ranges[0]);
            int max = Integer.parseInt(ranges[1]);

            gameOutTracker.forEach((k,v) -> {

                if(k >= min && k <= max ){
                    modifiedData.put(k, v);
                }

            });
        }
        else{
            gameOutTracker.forEach((k,v) -> {

                if( k >= Integer.parseInt(ranges[0]) ) {
                    modifiedData.put(k, v);
                }

            });
        }



        List<Map.Entry<Integer,Integer[]>> sorted = new ArrayList<>(modifiedData.entrySet());
        sorted.sort( new HitComparator() );

        modifiedData.clear();

        sorted.forEach(val -> {

            modifiedData.put(val.getKey(), val.getValue());
        });

        return modifiedData;
    }

    public Map<String, Integer[]> getGameOutGroupHolderMap() {

        List<Map.Entry<String,Integer[]>> sorted = new ArrayList<>(gameOutGroupHolderMap.entrySet());
        sorted.sort((o1, o2) -> {

            Integer o1Hits = o1.getValue()[0];
            Integer o2Hits = o2.getValue()[0];

            int result = o1Hits.compareTo( o2Hits );
            if(result > 0){return -1;}
            else if( result < 0){return 1;}
            else {return 0;}
        });

        gameOutGroupHolderMap.clear();

        sorted.forEach( val -> {

            gameOutGroupHolderMap.put(val.getKey(), val.getValue());
        });
        return gameOutGroupHolderMap;
    }

    public void checkValueAgainstMap(int val){

        plugValIntoIndividualGameOutTracker( val );

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

    private void plugValIntoIndividualGameOutTracker(int val) {

        if(!gameOutTracker.containsKey(val)){

            gameOutTracker.put(val, new Integer[]{1,0});
            NumberAnalyzer.incrementGamesOut(gameOutTracker, val);
        }
        else{
            Integer[] data = gameOutTracker.get( val );
            data[0]++;
            data[1] = 0;
            NumberAnalyzer.incrementGamesOut(gameOutTracker, val);
        }
    }

    private void incrementGamesOut(Map<String,Integer[]> gameOutGroupHolderMap, String range){

        gameOutGroupHolderMap.forEach((k,v) -> {

            if(!k.equals(range)){

                v[1]++;
            }
        });
    }
    private void populateMap() {

        gameOutGroupHolderMap.put("0,5",new Integer[]{0,0});
        gameOutGroupHolderMap.put("6,11",new Integer[]{0,0});
        gameOutGroupHolderMap.put("12,17",new Integer[]{0,0});
        gameOutGroupHolderMap.put("18,23",new Integer[]{0,0});
        gameOutGroupHolderMap.put("24,29",new Integer[]{0,0});
        gameOutGroupHolderMap.put("30,",new Integer[]{0,0});
    }


}
