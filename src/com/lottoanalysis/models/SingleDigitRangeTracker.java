package com.lottoanalysis.models;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class SingleDigitRangeTracker {

    private Map<String,SingleDigitRangeTracker> data = new LinkedHashMap<>();
    private Map<String,Object[]> tracker = new LinkedHashMap<>();

    private int hits;
    private int gamesOut;
    private int hitsAtGamesOut;
    private int gameOutLastAppearance;

    public SingleDigitRangeTracker(){

    }

    // getters and setters
    public Map<String, SingleDigitRangeTracker> getData() {
        return data;
    }

    public void setData(Map<String, SingleDigitRangeTracker> data) {
        this.data = data;
    }

    public void setTracker(Map<String, Object[]> tracker) {
        this.tracker = tracker;
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

    public int getHitsAtGamesOut() {
        return hitsAtGamesOut;
    }

    public void setHitsAtGamesOut(int hitsAtGamesOut) {
        this.hitsAtGamesOut = hitsAtGamesOut;
    }

    public int getGameOutLastAppearance() {
        return gameOutLastAppearance;
    }

    public void setGameOutLastAppearance(int gameOutLastAppearance) {
        this.gameOutLastAppearance = gameOutLastAppearance;
    }

    public void populateDataMap(String direction, String lottoNumber){

        if(!data.containsKey(direction)){

            Integer[] keyData = getAppropriateKey( lottoNumber );
            data.put(direction, new SingleDigitRangeTracker());

            SingleDigitRangeTracker tracker1 = data.get( direction );
            tracker1.setTrackerData( keyData, Integer.parseInt(lottoNumber) );
            tracker1.setGamesOut(0);
            tracker1.setHits(1);

            // create method to increment games out
            incrementGamesOut(tracker1.getData(),direction);


        }else{

            SingleDigitRangeTracker tracker1 = data.get(direction);
            int hits = tracker1.getHits();
            tracker1.setHits(++hits);
            tracker1.setGamesOut(0);

            Integer[] keyData = getAppropriateKey(lottoNumber);
            tracker1.setTrackerData(keyData,Integer.parseInt(lottoNumber));

            // create method to increment games out
            incrementGamesOut(tracker1.getData(),direction);
        }

    }
    private void setTrackerData(Integer[] data, int lottoNumber) {

        if(!tracker.containsKey(Arrays.toString(data))){
            tracker.put(Arrays.toString(data), new Object[]{1,0, new TreeSet<Integer>()});
            Set<Integer> vals = (Set<Integer>) tracker.get(Arrays.toString(data))[2];
            vals.add(lottoNumber);

            incrementGamesOutForDigitRanges(tracker, data);
        }
        else{
            Object[] d = tracker.get(Arrays.toString(data));
            d[0] = (int)d[0] + 1;
            d[1]=0;
            Set<Integer> vals = (Set<Integer>) tracker.get(Arrays.toString(data))[2];
            vals.add(lottoNumber);

            incrementGamesOutForDigitRanges(tracker,data);
        }

    }
    private void incrementGamesOut(Map<String,SingleDigitRangeTracker> data, String direction){

        data.forEach((k,v) -> {

            if(!k.equals(direction)){

                int out = v.getGamesOut();
                v.setGamesOut(++out);
            }
        });
    }
    private void incrementGamesOutForDigitRanges(Map<String,Object[]> data, Integer[] keys){

        data.forEach( (k,v) -> {

            if(!k.equals(Arrays.toString(keys))){
                v[1] = (int)v[1] + 1;
            }
        });
    }
    private Integer[] getAppropriateKey(String lottoNumber) {

        final List<Integer> lowRangeVals = new ArrayList<>(Arrays.asList(0,1,2,3,4));
        final List<Integer> highRangeVals = new ArrayList<>(Arrays.asList(5,6,7,8,9));

        int lastWinningDigit = (lottoNumber.length() > 1) ? Character.getNumericValue(lottoNumber.charAt(1)) :
                Character.getNumericValue(lottoNumber.charAt(0));

        if(lowRangeVals.contains(lastWinningDigit)){

            return Arrays.stream(lowRangeVals.stream().mapToInt(i ->i).toArray()).boxed().toArray(Integer[]::new);
        }
        else {
            return Arrays.stream(highRangeVals.stream().mapToInt(i ->i).toArray()).boxed().toArray(Integer[]::new);
        }

    }

}
