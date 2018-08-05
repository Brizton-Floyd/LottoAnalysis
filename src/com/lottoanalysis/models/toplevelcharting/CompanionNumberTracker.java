package com.lottoanalysis.models.toplevelcharting;

import com.lottoanalysis.models.comparators.CompanionNumberComparator;

import java.util.*;
import java.util.stream.IntStream;

public class CompanionNumberTracker {

    private Map<Integer,Map<Integer,CompanionNumberData>> companionNumberTracker = new TreeMap<>();
    private GameHitLenghtTracker gameHitLenghtTracker;

    public CompanionNumberTracker(){

        gameHitLenghtTracker = new GameHitLenghtTracker();
    }

    public void sortMapData( Map<Integer,Map<Integer,CompanionNumberData>> data){

        Iterator<Integer> keyIterator = data.keySet().iterator();
        while (keyIterator.hasNext()){

            final int key = keyIterator.next();
            List<Map.Entry<Integer,CompanionNumberData>> entries = new ArrayList<>(data.get(key).entrySet());
            entries.sort( new CompanionNumberComparator());
            data.clear();

            data.put(key, new LinkedHashMap<>());
            Map<Integer,CompanionNumberData> values = data.get(key);
            for(Map.Entry<Integer,CompanionNumberData> dataEntry : entries){

               values.put(dataEntry.getKey(), dataEntry.getValue());
            }
        }

    }

    public void findCompanionNumberForRecentWinningNumber(List<Integer> data){

        final int recentWinningNumber = data.get( data.size() - 1);
        companionNumberTracker.put(recentWinningNumber, new TreeMap<>());

        int[] recentWinningLottoNumberHitIndexes = IntStream.range(0, data.size() - 1)
                                                    .filter( i -> data.get(i) == recentWinningNumber).toArray();

        Map<Integer,CompanionNumberData> companionMap = companionNumberTracker.get(recentWinningNumber);
        for(int number : recentWinningLottoNumberHitIndexes){

            int nextNextWinningNumber = data.get( number + 1);
            if(!companionMap.containsKey(nextNextWinningNumber)){

                CompanionNumberData data1 = new CompanionNumberData();
                data1.setHits(1);

                gameHitLenghtTracker.computeIntLengthAndAddToMap( data1.getHits() );

                incrementGamesOut(companionMap,nextNextWinningNumber);
                companionMap.put(nextNextWinningNumber, data1);
            }
            else
            {
                CompanionNumberData companionNumberData = companionMap.get(nextNextWinningNumber);
                int hits = companionNumberData.getHits();
                companionNumberData.setHits( ++hits );
                companionNumberData.setGamesOut(0);

                gameHitLenghtTracker.computeIntLengthAndAddToMap( companionNumberData.getHits() );

                incrementGamesOut(companionMap,nextNextWinningNumber);
            }
        }
    }

    private void incrementGamesOut(Map<Integer,CompanionNumberData> data, int number){

        data.forEach((k,v) -> {

            if(k != number){

                int gamesOut = v.getGamesOut();
                v.setGamesOut( ++gamesOut );
            }
        });
    }

    public Map<Integer, Map<Integer, CompanionNumberData>> getCompanionNumberTracker() {
        return companionNumberTracker;
    }

    public GameHitLenghtTracker getGameHitLenghtTracker() {
        return gameHitLenghtTracker;
    }

    public class CompanionNumberData {

        private int hits;
        private int gamesOut;

        public int getHits() {
            return hits;
        }

        public int getGamesOut() {
            return gamesOut;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public void setGamesOut(int gamesOut) {
            this.gamesOut = gamesOut;
        }
    }
}
