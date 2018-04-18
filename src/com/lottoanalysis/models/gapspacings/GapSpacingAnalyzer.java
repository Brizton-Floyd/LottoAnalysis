package com.lottoanalysis.models.gapspacings;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public class GapSpacingAnalyzer {

    private Map<Integer,Object[]> lineSpacingBuckets = new TreeMap<>();
    private List<Integer> bucketHitHolder = new ArrayList<>();

    private int winningNumber;

    public Map<Integer, Object[]> getLineSpacingBuckets() {
        return lineSpacingBuckets;
    }

    public int getWinningNumber() {
        return winningNumber;
    }

    public List<Integer> getBucketHitHolder() {
        return bucketHitHolder;
    }

    /**
     *
     * @param data
     */
    public void analyzeSpacings( int[] data ){

        bucketHitHolder.clear();
        int currentWinningNumber;

        if(data.length > 1)
            currentWinningNumber = data[ data.length - 1 ];
        else
            currentWinningNumber = data[0];

        winningNumber = currentWinningNumber;

        int[] hitIndexes = IntStream.range(0, data.length - 1).filter(i -> data[i] == currentWinningNumber).toArray();
        List<Integer> dataAsList = Arrays.stream(data).boxed().collect(Collectors.toList());

        for(int i = 0; i < hitIndexes.length; i++){

            int nextWinningNumber = dataAsList.get( hitIndexes[i] + 1 );
            int dif = Math.abs( nextWinningNumber - currentWinningNumber) - 1;
            if(dif < 0 )
                dif = 0;

            for(Map.Entry<Integer,Object[]> lineSpaceData : lineSpacingBuckets.entrySet()){

                List<Integer> lineSpacingSpans = (List<Integer>) lineSpaceData.getValue()[1];
                if(lineSpacingSpans.contains( dif )){

                    bucketHitHolder.add( lineSpaceData.getKey() );
                    GameSpacingHitTracker gameSpacingHitTracker = (GameSpacingHitTracker) lineSpaceData.getValue()[0];
                    gameSpacingHitTracker.setGamesOut(0);
                    gameSpacingHitTracker.setDoIncrementGamesOut(false);
                    int hits = gameSpacingHitTracker.getHits();
                    gameSpacingHitTracker.setHits( ++hits );

                    gameSpacingHitTracker.insertValuesIntoMap(dif);

                }
                else{

                    GameSpacingHitTracker gameSpacingHitTracker = (GameSpacingHitTracker) lineSpaceData.getValue()[0];
                    gameSpacingHitTracker.setDoIncrementGamesOut(true);
                    if(gameSpacingHitTracker.isDoIncrementGamesOut()) {
                        int gamesOut = gameSpacingHitTracker.getGamesOut();
                        gameSpacingHitTracker.setGamesOut(++gamesOut);
                    }

                }
            }
        }

    }

    /**
     *
     * @param low
     * @param high
     */
    public void formHitBuckets(int low, int high){

        lineSpacingBuckets.clear();

        int dif = high - low;
        List<Integer> numHolder = new ArrayList<>();
        int count = 0;

        if(dif > 3){

            for(int i = 0; i <= dif-1;){

                if(numHolder.size() == 3){

                    List<Integer> bucket = new ArrayList<>(numHolder);
                    lineSpacingBuckets.put(++count, new Object[]{new GameSpacingHitTracker(count), bucket});
                    numHolder.clear();

                }else{

                    numHolder.add(i);
                    i++;
                }
            }

        }

        if(numHolder.size() > 0){

            List<Integer> bucket = new ArrayList<>(numHolder);
            lineSpacingBuckets.put(++count, new Object[]{new GameSpacingHitTracker(count), bucket});        }

    }

    public class GameSpacingHitTracker{

        private int id;
        private int hits;
        private int gamesOut;
        private boolean doIncrementGamesOut = true;
        private Map<Integer,Integer[]> lineSpacingHitTracker = new TreeMap<>();


        public GameSpacingHitTracker(int id){

            setId(id);
        }

        void insertValuesIntoMap( int val ){

            if(!lineSpacingHitTracker.containsKey(val)){
                lineSpacingHitTracker.put(val, new Integer[]{1,0});
                NumberAnalyzer.incrementGamesOut(lineSpacingHitTracker, val);
            }
            else{

                Integer[] data = lineSpacingHitTracker.get(val);
                data[0]++;
                data[1] = 0;
                NumberAnalyzer.incrementGamesOut(lineSpacingHitTracker,val);
            }
        }
        public Map<Integer, Integer[]> getLineSpacingHitTracker() {
            return lineSpacingHitTracker;
        }

        public int getId() {
            return id;
        }

        private void setId(int id) {
            this.id = id;
        }

        public boolean isDoIncrementGamesOut() {
            return doIncrementGamesOut;
        }

        public void setDoIncrementGamesOut(boolean doIncrementGamesOut) {
            this.doIncrementGamesOut = doIncrementGamesOut;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public int getGamesOut() {
            return gamesOut;
        }

        public void setGamesOut(int gamesOut) {
            this.gamesOut = gamesOut;
        }
    }
}
