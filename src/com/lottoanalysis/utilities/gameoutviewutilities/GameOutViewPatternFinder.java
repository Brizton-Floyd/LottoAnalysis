package com.lottoanalysis.utilities.gameoutviewutilities;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Class will analyze each positioni to determine which game out range is due to hit for a given position
 */
public class GameOutViewPatternFinder {

    private static Map<Integer,Map<Integer[],Object[]>> positionnalData = new LinkedHashMap<>();

    public static void analyze(int[][] positionalNumbers){

        clear();
        determineNumberGamesOut( positionalNumbers);
    }


    @SuppressWarnings("unchecked")
    private static void determineNumberGamesOut(int[][] positionalNumbers) {

        Map<Integer,Object[]> numberSequenceHistoryHolder = new LinkedHashMap<>();

        for(int i = 0; i < positionalNumbers.length; i++){

            for(int j = 0; j < positionalNumbers[i].length; j++){

                int lottoNumber = positionalNumbers[i][j];

                if(!numberSequenceHistoryHolder.containsKey(lottoNumber)){
                    numberSequenceHistoryHolder.put(lottoNumber,new Object[]{new ArrayList<String>(),0});

                    List<String> data = (List<String>)numberSequenceHistoryHolder.get(lottoNumber)[0];
                    data.add("Pos: " + i + " Out: " + 0);
                    incrementGamesOut(numberSequenceHistoryHolder, lottoNumber);
                }
                else{

                    Object[] data = numberSequenceHistoryHolder.get(lottoNumber);

                    List<String> sequence = (List<String>)data[0];
                    sequence.add("Pos: " + i + " Out: " + data[1]);

                    data[1] = 0;
                    incrementGamesOut(numberSequenceHistoryHolder,lottoNumber);
                }
            }
        }


    }

    private static void incrementGamesOut(Map<Integer, Object[]> data, Integer number) {

        data.forEach( (key,val) -> {

            if(key != number){
                val[1] = (int) val[1] + 1;
            }
        });
    }

    private static void incrementGamesOut(Map<Integer,Integer> data, int number){

        data.forEach( (key, value) -> {

            if( key != number){
                value = value + 1;
                data.put(key, value);
            }

        });
    }

    private static void clear(){
        positionnalData.clear();
    }

    private static Map<Integer[],Object[]> getPositionalData(){

        Map<Integer[],Object[]> data = new LinkedHashMap<>();
        data.put(new Integer[]{0,5},new Object[]{new ArrayList<Integer>(),0,0});
        data.put(new Integer[]{6,10},new Object[]{new ArrayList<Integer>(),0,0});
        data.put(new Integer[]{11,15},new Object[]{new ArrayList<Integer>(),0,0});
        data.put(new Integer[]{16,20},new Object[]{new ArrayList<Integer>(),0,0});
        data.put(new Integer[]{21,25},new Object[]{new ArrayList<Integer>(),0,0});
        data.put(new Integer[]{26},new Object[]{new ArrayList<Integer>(),0,0});


        return data;
    }
}
