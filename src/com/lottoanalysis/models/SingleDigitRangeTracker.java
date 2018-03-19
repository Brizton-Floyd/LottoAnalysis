package com.lottoanalysis.models;

import java.util.*;

@SuppressWarnings("unchecked")
public class SingleDigitRangeTracker {

    private Map<String,SingleDigitRangeTracker> data = new LinkedHashMap<>();
    private Map<String,SingleDigitRangeTracker> tracker = new LinkedHashMap<>();
    private Set<Integer> lottoNumberHolder = new TreeSet<>();

    private int hits = -1;
    private int gamesOut = -1;
    private int hitsAtGamesOut = -1;
    private int gameOutLastAppearance = -1;

    public SingleDigitRangeTracker(){

    }

    // getters and setters

    public Set<Integer> getLottoNumberHolder() {
        return lottoNumberHolder;
    }

    public Map<String, SingleDigitRangeTracker> getData() {
        return data;
    }

    public void setData(Map<String, SingleDigitRangeTracker> data) {
        this.data = data;
    }

    public void setTracker(Map<String, SingleDigitRangeTracker> tracker) {
        this.tracker = tracker;
    }

    public Map<String, SingleDigitRangeTracker> getTracker() {
        return tracker;
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

        }else{

            SingleDigitRangeTracker tracker1 = data.get(direction);

            Integer[] keyData = getAppropriateKey(lottoNumber);
            tracker1.setTrackerData(keyData,Integer.parseInt(lottoNumber));

        }

    }
    private void setTrackerData(Integer[] data, int lottoNumber) {

        if(!tracker.containsKey(Arrays.toString(data))){
            tracker.put(Arrays.toString(data), new SingleDigitRangeTracker());

            SingleDigitRangeTracker tracker1 = tracker.get(Arrays.toString(data));
            tracker1.setHits(1);
            tracker1.setGamesOut(0);
            tracker1.lottoNumberHolder.add(lottoNumber);

            incrementGamesOutForDigitRanges(tracker, data);
        }
        else{
            SingleDigitRangeTracker tracker1= tracker.get(Arrays.toString(data));
            int hits = tracker1.getHits();
            tracker1.setHits(++hits);
            tracker1.setGamesOut(0);
            tracker1.lottoNumberHolder.add(lottoNumber);

            incrementGamesOutForDigitRanges(tracker,data);
        }

    }
    private void incrementGamesOutForDigitRanges(Map<String,SingleDigitRangeTracker> data, Integer[] keys){

        data.forEach( (k,v) -> {

            if(!k.equals(Arrays.toString(keys))){
                int outs = v.getGamesOut();
                v.setGamesOut(++outs);
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
