package com.lottoanalysis.utilities.gameoutviewutilities;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;

public class GameOutLottoHitFinder {

    private Map<Integer,Integer[]> gameOutTracker = new TreeMap<>();
    private Map<Integer,Integer[]> lottoNumberHitTracker = new LinkedHashMap<>();

    public GameOutLottoHitFinder(int i, int i1, List<Integer> numList) {

        loadLottoNumberMap(i,i1);
        analyze( numList );
        populateGameOutInfoForLottoNumbers();
        sortData();
    }

    public Map<Integer, Integer[]> getLottoNumberHitTracker() {
        return lottoNumberHitTracker;
    }

    private void sortData() {

        List<Map.Entry<Integer,Integer[]>> sorted = new ArrayList<>(lottoNumberHitTracker.entrySet());
        sorted.sort((o1, o2) -> {

            Integer o1Hits = o1.getValue()[2];
            Integer o2Hits = o2.getValue()[2];

            int result = o1Hits.compareTo( o2Hits );
            if(result > 0){return -1;}
            else if( result < 0){return 1;}
            else {return 0;}
        });

        lottoNumberHitTracker.clear();

        for(Map.Entry<Integer,Integer[]> values : sorted){

            lottoNumberHitTracker.put(values.getKey(), values.getValue());
        }
    }

    private void populateGameOutInfoForLottoNumbers() {

        lottoNumberHitTracker.forEach( (k,v) -> {

            if( gameOutTracker.containsKey(v[1])){

                Integer[] gameOutData = gameOutTracker.get( v[1] );
                v[2] = gameOutData[0];
                v[3] = gameOutData[1];
            }
        });
    }

    private void analyze(List<Integer> numList) {

        numList.forEach( lottoNumber -> {

            Integer[] lottoNumberInfo = lottoNumberHitTracker.get( lottoNumber );
            lottoNumberInfo[0] ++;

            if(!gameOutTracker.containsKey( lottoNumberInfo[1] )){

                gameOutTracker.put( lottoNumberInfo[1], new Integer[]{1,0} );
                NumberAnalyzer.incrementGamesOut(gameOutTracker, lottoNumberInfo[1]);
            }
            else
            {
                Integer[] gameOutInfo = gameOutTracker.get( lottoNumberInfo[1] );
                gameOutInfo[0]++;
                gameOutInfo[1] = 0;
                NumberAnalyzer.incrementGamesOut( gameOutTracker, lottoNumberInfo[1] );
            }
            lottoNumberInfo[1] = 0;

            incrementGames( lottoNumberHitTracker, lottoNumber);
        });
    }

    private void incrementGames(Map<Integer, Integer[]> lottoNumberHitTracker, Integer lottoNumber) {

        lottoNumberHitTracker.forEach((number,value) -> {

            if(number != lottoNumber){
                value[1]++;
            }
        });
    }

    private void loadLottoNumberMap(int i, int i1) {
        // index 0: Lotto Num hits,
        //index 1: Lotto Num Games Out
        // index 2: Game out hits amongst all numbers
        // index 3: Last time current games out hit
        for(int j = i; j <= i1; j++){

            lottoNumberHitTracker.put(j, new Integer[]{0,0,-1,-1});
        }
    }
}
