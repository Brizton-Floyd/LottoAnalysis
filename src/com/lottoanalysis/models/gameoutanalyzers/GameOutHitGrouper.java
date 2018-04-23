package com.lottoanalysis.models.gameoutanalyzers;

import com.lottoanalysis.comparators.HitComparator;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;
import java.util.stream.IntStream;

public class GameOutHitGrouper {

    Map<String,Object[]> gameOutGroupHolderMap = new LinkedHashMap<>();
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

    public Map<String, Object[]> getGameOutGroupHolderMap() {

        List<Map.Entry<String,Object[]>> sorted = new ArrayList<>(gameOutGroupHolderMap.entrySet());
        sorted.sort((o1, o2) -> {

            Integer o1Hits = (Integer) o1.getValue()[0];
            Integer o2Hits = (Integer) o2.getValue()[0];

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

            List<Integer> outHitHolder = (List<Integer>)v[2];

            String[] data = k.split(",");

            if(data.length > 1){

                int digitOne = Integer.parseInt( data[0] );
                int digitTwo = Integer.parseInt( data[1] );

                if( val >= digitOne && val <= digitTwo ){

                    outHitHolder.add(val);
                    v[0] = (int)v[0] + 1;
                    v[1] = 0;
                    incrementGamesOut(gameOutGroupHolderMap, k);
                }
            }
            else{

                if( val >= Integer.parseInt(data[0]) ){

                    outHitHolder.add(val);
                    v[0] = (int) v[0] + 1;
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

    private void incrementGamesOut(Map<String,Object[]> gameOutGroupHolderMap, String range){

        gameOutGroupHolderMap.forEach((k,v) -> {

            if(!k.equals(range)){

                v[1] = (int)v[1] + 1;
            }
        });
    }
    private void populateMap() {

        gameOutGroupHolderMap.put("0,20",new Object[]{0,0, new ArrayList<Integer>()});
        gameOutGroupHolderMap.put("21,40",new Object[]{0,0, new ArrayList<Integer>()});
        gameOutGroupHolderMap.put("41,60",new Object[]{0,0, new ArrayList<Integer>()});
        gameOutGroupHolderMap.put("61,",new Object[]{0,0, new ArrayList<Integer>()});
    }


}
