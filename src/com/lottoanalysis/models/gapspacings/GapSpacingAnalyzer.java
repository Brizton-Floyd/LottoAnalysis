package com.lottoanalysis.models.gapspacings;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;

import javax.sound.sampled.Line;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public class GapSpacingAnalyzer {

    private Map<Integer, Object[]> lineSpacingBuckets = new TreeMap<>();
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
     * @param data
     */
    public void analyzeSpacings(int[] data) {

        bucketHitHolder.clear();
        int currentWinningNumber;

        if (data.length > 1)
            currentWinningNumber = data[data.length - 1];
        else
            currentWinningNumber = data[0];

        winningNumber = currentWinningNumber;

        int[] hitIndexes = IntStream.range(0, data.length - 1).filter(i -> data[i] == currentWinningNumber).toArray();
        List<Integer> dataAsList = Arrays.stream(data).boxed().collect(Collectors.toList());

        for (int i = 0; i < hitIndexes.length; i++) {

            int nextWinningNumber = dataAsList.get(hitIndexes[i] + 1);
            int dif = Math.abs(nextWinningNumber - currentWinningNumber) - 1;
            if (dif < 0)
                dif = 0;

            for (Map.Entry<Integer, Object[]> lineSpaceData : lineSpacingBuckets.entrySet()) {

                List<Integer> lineSpacingSpans = (List<Integer>) lineSpaceData.getValue()[1];
                if (lineSpacingSpans.contains(dif)) {

                    bucketHitHolder.add(lineSpaceData.getKey());
                    GameSpacingHitTracker gameSpacingHitTracker = (GameSpacingHitTracker) lineSpaceData.getValue()[0];
                    gameSpacingHitTracker.setGameOutHolder(gameSpacingHitTracker.getGamesOut());
                    gameSpacingHitTracker.setGamesOut(0);
                    gameSpacingHitTracker.setDoIncrementGamesOut(false);
                    int hits = gameSpacingHitTracker.getHits();
                    gameSpacingHitTracker.setHits(++hits);

                    gameSpacingHitTracker.insertValuesIntoMap(dif);

                } else {

                    GameSpacingHitTracker gameSpacingHitTracker = (GameSpacingHitTracker) lineSpaceData.getValue()[0];
                    gameSpacingHitTracker.setDoIncrementGamesOut(true);
                    if (gameSpacingHitTracker.isDoIncrementGamesOut()) {
                        int gamesOut = gameSpacingHitTracker.getGamesOut();
                        gameSpacingHitTracker.setGamesOut(++gamesOut);
                    }

                }
            }
        }

        calculateHitsAtGamesOut();

    }

    private void calculateHitsAtGamesOut() {

        for (Map.Entry<Integer, Object[]> lineSpaceData : lineSpacingBuckets.entrySet()) {


            GameSpacingHitTracker gameSpacingHitTracker = (GameSpacingHitTracker) lineSpaceData.getValue()[0];
            List<Integer> outHolderList = gameSpacingHitTracker.getGameOutHolder();
            int currentGamesOut = gameSpacingHitTracker.getGamesOut();

            Long hitOccurrenceCount = outHolderList.stream().filter( i -> i == currentGamesOut).count();
            gameSpacingHitTracker.setHitsAtGamesOut( hitOccurrenceCount.intValue() );

            // Find where out last Occurred
            int lastIndex = outHolderList.lastIndexOf(currentGamesOut);

            int lastSeen;
            if( lastIndex > -1)
                lastSeen = Math.abs(  lastIndex - outHolderList.size());
            else
                lastSeen = -1;

            gameSpacingHitTracker.setOutLastSeen(lastSeen);

            LineSpacingHitTracker lineSpacingHitTracker = gameSpacingHitTracker.getLineSpacingHitTracker();
            Map<Integer,LineSpacingHitTracker> lineSpacingHitTrackerMap = lineSpacingHitTracker.getLineSpacingHitTracker();

            lineSpacingHitTrackerMap.forEach((k,v) -> {

                List<Integer> outList = v.getOutHolderList();
                int gamesOut = v.getGamesOut();

                Long count = outList.stream().filter( i -> i == gamesOut).count();
                v.setHitsAtGamesOut(count.intValue());

                int index = outList.lastIndexOf(gamesOut);

                int lastSeenTwo;

                if(index > -1)
                    lastSeenTwo = Math.abs(  index - outList.size());
                else
                    lastSeenTwo = -1;

                v.setLastSeen(lastSeenTwo);
            });
        }
    }

    /**
     * @param low
     * @param high
     */
    public void formHitBuckets(int low, int high) {

        lineSpacingBuckets.clear();

        int dif = high - low;
        List<Integer> numHolder = new ArrayList<>();
        int count = 0;

        if (dif > 3) {

            for (int i = 0; i <= dif - 1; ) {

                if (numHolder.size() == 3) {

                    List<Integer> bucket = new ArrayList<>(numHolder);
                    lineSpacingBuckets.put(++count, new Object[]{new GameSpacingHitTracker(count), bucket});
                    numHolder.clear();

                } else {

                    numHolder.add(i);
                    i++;
                }
            }

        }

        if (numHolder.size() > 0) {

            List<Integer> bucket = new ArrayList<>(numHolder);
            lineSpacingBuckets.put(++count, new Object[]{new GameSpacingHitTracker(count), bucket});
        }

    }

    public class GameSpacingHitTracker {

        private int id;
        private int hits;
        private int gamesOut;
        private int hitsAtGamesOut;
        private int outLastSeen;
        private boolean doIncrementGamesOut = true;
        private List<Integer> gameOutHolder = new ArrayList<>();

        private LineSpacingHitTracker lineSpacingHitTracker = new LineSpacingHitTracker();

        public GameSpacingHitTracker(int id) {

            setId(id);
        }

        void insertValuesIntoMap(int val) {

            if(!lineSpacingHitTracker.getLineSpacingHitTracker().containsKey(val)){

                LineSpacingHitTracker tracker = new LineSpacingHitTracker();
                tracker.setHits(1);

                lineSpacingHitTracker.getLineSpacingHitTracker().put(val, tracker);
                tracker.incrementGamesOutForNonWinningSpacings( lineSpacingHitTracker.getLineSpacingHitTracker(), tracker);
            }
            else{

                LineSpacingHitTracker tracker = lineSpacingHitTracker.getLineSpacingHitTracker().get( val );
                List<Integer> outHolderList = tracker.getOutHolderList();
                outHolderList.add( tracker.getGamesOut() );

                tracker.setGamesOut(0);

                int hits = tracker.getHits();
                tracker.setHits(++hits);

                tracker.incrementGamesOutForNonWinningSpacings(lineSpacingHitTracker.getLineSpacingHitTracker(), tracker);
            }

        }

        public LineSpacingHitTracker getLineSpacingHitTracker() {
            return lineSpacingHitTracker;
        }

        public int getOutLastSeen() {
            return outLastSeen;
        }

        public void setOutLastSeen(int outLastSeen) {
            this.outLastSeen = outLastSeen;
        }

        public int getHitsAtGamesOut() {
            return hitsAtGamesOut;
        }

        public void setHitsAtGamesOut(int hitsAtGamesOut) {
            this.hitsAtGamesOut = hitsAtGamesOut;
        }

        public List<Integer> getGameOutHolder() {
            return gameOutHolder;
        }

        public void setGameOutHolder(int outValue) {
            this.gameOutHolder.add(outValue);
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

    public class LineSpacingHitTracker {

        private int hits;
        private int gamesOut;
        private int lastSeen;
        private int hitsAtGamesOut;

        private List<Integer> outHolderList = new ArrayList<>();

        private Map<Integer, LineSpacingHitTracker> lineSpacingHitTracker = new TreeMap<>();

        public Map<Integer, LineSpacingHitTracker> getLineSpacingHitTracker() {
            return lineSpacingHitTracker;
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

        public int getLastSeen() {
            return lastSeen;
        }

        public void setLastSeen(int lastSeen) {
            this.lastSeen = lastSeen;
        }

        public int getHitsAtGamesOut() {
            return hitsAtGamesOut;
        }

        public void setHitsAtGamesOut(int hitsAtGamesOut) {
            this.hitsAtGamesOut = hitsAtGamesOut;
        }

        public List<Integer> getOutHolderList() {
            return outHolderList;
        }

        public void setOutHolderList(int outValue) {
            this.outHolderList.add(outValue);
        }

        public void incrementGamesOutForNonWinningSpacings(Map<Integer, LineSpacingHitTracker> lineSpacingHitTracker, LineSpacingHitTracker tracker) {

            lineSpacingHitTracker.forEach((k,v) -> {

                if( v != tracker){

                    int gamesOut = v.getGamesOut();
                    v.setGamesOut(++gamesOut);
                }
            });
        }
    }
}
