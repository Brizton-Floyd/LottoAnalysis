package com.lottoanalysis.models.toplevelcharting;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;

public class GameHitLenghtTracker {

    private Map<Integer,LenTrackerData> lengthTracker = new TreeMap<>();

    public void computeIntLengthAndAddToMap(int number){

        String numAsString = number + "";

        if(!lengthTracker.containsKey(numAsString.length())){

            LenTrackerData lenTrackerData = new LenTrackerData();
            lenTrackerData.setHits(1);
            lenTrackerData.setGamesOut(0);


            incrementGamesOut(lengthTracker,numAsString.length());
            LenTrackerData.LenRemainderTracker remainderTracker = lenTrackerData.getLenRemainderTracker();

            remainderTracker.insertRemainder(numAsString.length(), numAsString);

            lengthTracker.put(numAsString.length(), lenTrackerData);
        }
        else
        {
            LenTrackerData hitData = lengthTracker.get(numAsString.length());
            int hits = hitData.getHits();
            hitData.setHits( ++hits );
            hitData.setGamesOut(0);

            LenTrackerData.LenRemainderTracker lenRemainderTracker = hitData.getLenRemainderTracker();
            lenRemainderTracker.insertRemainder(numAsString.length(), numAsString);
            incrementGamesOut(lengthTracker, numAsString.length());
        }

    }

    public Map<Integer, LenTrackerData> getLengthTracker() {
        return lengthTracker;
    }

    private void incrementGamesOut(Map<Integer,LenTrackerData> data, int number){

        data.forEach( (k,v) -> {

            if(k != number){
                int gamesOut = v.getGamesOut();
                v.setGamesOut(++gamesOut);
            }
        });
    }

    public class LenTrackerData{

        private int hits;
        private int gamesOut;
        private LenRemainderTracker lenRemainderTracker = new LenRemainderTracker();

        public LenRemainderTracker getLenRemainderTracker() {
            return lenRemainderTracker;
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

        public class LenRemainderTracker{

            private int hits;
            private int gamesOut;
            private Map<Integer,LenRemainderTracker> hitLengthMap = new TreeMap<>();
            private Map<Integer,Integer[]> hitValueMap = new TreeMap<>();

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

            public Map<Integer, LenRemainderTracker> getHitLengthMap() {
                return hitLengthMap;
            }

            public Map<Integer, Integer[]> getHitValueMap() {
                return hitValueMap;
            }

            public void insertRemainder(int len, String number){

                int remainder = len % 3;
                int remainderTwo = (number.length() > 1) ? computeSum(number): Character.getNumericValue(
                                                                                 number.charAt(0)) % 3;

                if(!hitLengthMap.containsKey(remainder)){

                    LenRemainderTracker lenRemainderTracker = new LenRemainderTracker();
                    lenRemainderTracker.setHits(1);

                    incrementGamesOut(hitLengthMap,remainder);
                    hitLengthMap.put(remainder, lenRemainderTracker );

                    if(!hitValueMap.containsKey(remainderTwo)){

                        hitValueMap.put(remainderTwo, new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(hitValueMap, remainderTwo);
                    }
                    else{

                        Integer[] hitValueData = hitValueMap.get( remainderTwo );
                        hitValueData[0]++;
                        hitValueData[1] = 0;
                        NumberAnalyzer.incrementGamesOut(hitValueMap, remainderTwo);
                    }
                }
                else
                {
                    LenRemainderTracker lenRemainderTracker = hitLengthMap.get( remainder );
                    int currentHits = lenRemainderTracker.getHits();
                    lenRemainderTracker.setHits( ++ currentHits);
                    lenRemainderTracker.setGamesOut(0);

                    incrementGamesOut(hitLengthMap,remainder);

                    if(!hitValueMap.containsKey(remainderTwo)){

                        hitValueMap.put(remainderTwo, new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(hitValueMap, remainderTwo);
                    }
                    else{

                        Integer[] hitValueData = hitValueMap.get( remainderTwo );
                        hitValueData[0]++;
                        hitValueData[1] = 0;
                        NumberAnalyzer.incrementGamesOut(hitValueMap, remainderTwo);
                    }

                }
            }

            private void incrementGamesOut(Map<Integer,LenRemainderTracker> data, int remainder){

                data.forEach((k,v) -> {

                    if(k != remainder){

                        int outs = v.getGamesOut();
                        v.setGamesOut( ++outs );
                    }
                });
            }
        }
    }

    private int computeSum(String number) {

        int sum = 0;
        String[] data = number.split("");
//        for( String s : data){
//
//            sum += Integer.parseInt(s);
//        }

        return Math.abs(Integer.parseInt( data[data.length-1] )) % 3;
    }
}
