package com.lottoanalysis.models.gameout;

import com.lottoanalysis.models.drawhistory.DrawModel;
import com.lottoanalysis.models.drawhistory.DrawModelBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public abstract class Range {

    List<Range> ranges = new ArrayList<>();
    private List<Integer> rangeGameOutHolder = new ArrayList<>();
    private List<Integer> gameOutHolder = new ArrayList<>();
    private List<Integer> rangeWinningNumberHolder = new ArrayList<>();
    private Map<Integer, List<String>> lottoNumberMap = new TreeMap<>();
    private int lowerBound, upperBound, hits, gamesOut, hitsAtGamesOut, gameOutLastSeen, range, minNumber, maxNumber,rangeIndex;
    private double avgSkips;
    private int[] defaultUpperLowerBounds = new int[2];
    private static int index;

    public Range() {
    }

    public Range(int range, int minNumber, int maxNumber) {
        this.range = range;
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        defaultUpperLowerBounds[0] = minNumber;
        defaultUpperLowerBounds[1] = maxNumber;
    }

     int[] getDefaultUpperLowerBounds() {
        return defaultUpperLowerBounds;
    }

    public int getRangeIndex() {
        return rangeIndex;
    }

    public void setRangeIndex(int rangeIndex) {
        this.rangeIndex = rangeIndex;
    }

    public int[] getUpperLowerBoundAsArray() {
        if(upperBound > 0)
            return new int[]{lowerBound, upperBound};
        return new int[]{lowerBound};
    }

    public List<Integer> getRangeWinningNumberHolder() {
        return rangeWinningNumberHolder;
    }

    public List<Integer> getRangeGameOutHolder() {
        return rangeGameOutHolder;
    }

    public Map<Integer, List<String>> getLottoNumberMap() {
        return lottoNumberMap;
    }

    public ObservableList<Set<Map.Entry<Integer, List<String>>>> getLottoNumberDistroMap() {

        ObservableList<Set<Map.Entry<Integer, List<String>>>> values = FXCollections.observableArrayList();
        values.add(lottoNumberMap.entrySet());

        return values;
    }

    void setRangeGameOutHolder(List<Integer> rangeGameOutHolder) {
        this.rangeGameOutHolder = rangeGameOutHolder;
    }

    public List<Range> getRanges() {
        return ranges;
    }

    void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public  void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
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

    public int getGameOutLastSeen() {
        return gameOutLastSeen;
    }

    public void setGameOutLastSeen(int gameOutLastSeen) {
        this.gameOutLastSeen = gameOutLastSeen;
    }

    public int getRange() {
        return range;
    }

    void setRange(int range) {
        this.range = range;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public List<Integer> getGameOutHolder() {
        return gameOutHolder;
    }

    public double getAvgSkips() {
        return avgSkips;
    }

    void setAvgSkips(double avgSkips) {
        this.avgSkips = avgSkips;
    }

    protected void validateUpperBoundsForOverflow() {

        for (Range range : getRanges()) {

            if (range.upperBound > defaultUpperLowerBounds[1]) {
                range.setUpperBound(defaultUpperLowerBounds[1]);
            }
        }

    }

    protected void validateUpperBoundsForOverflow(List<Range> ranges) {

        for (Range range : ranges) {

            if (range.upperBound > defaultUpperLowerBounds[1]) {
                range.setUpperBound(defaultUpperLowerBounds[1]);
            }
        }

    }
    public abstract void analyze();

    protected abstract void computeRangeUpperLowerBound();

    void computeHitsAtGamesOut() {

        for (Range range : getRanges()) {

            int currentGameOut = range.getGamesOut();
            Long counter = range.getRangeGameOutHolder().stream().filter(out -> out == currentGameOut).count();
            range.setHitsAtGamesOut(counter.intValue());
        }
    }

    protected <T extends Range> void computeHitsAtGamesOut(List<T> ranges) {

        for (Range range : ranges) {

            range.getRanges().forEach( range1 -> {
                int currentGameOut = range1.getGamesOut();
                Long counter = range1.getRangeGameOutHolder().stream().filter(out -> out == currentGameOut).count();
                range1.setHitsAtGamesOut(counter.intValue());
            });

        }
    }
    void findLastOccurenceOfGameOut() {

        getRanges().forEach(range -> {

            final int currentGamesOut = range.getGamesOut();
            List<Integer> gameOutHolder = range.getRangeGameOutHolder();
            if(gameOutHolder.size() > 1)
                System.out.println(gameOutHolder.get(gameOutHolder.size()-1));
            int lastIndex = gameOutHolder.lastIndexOf( currentGamesOut );
            int lastSeen =  Math.abs(lastIndex - gameOutHolder.size()) -1;
            range.setGameOutLastSeen(lastSeen);

        });
    }

    protected <T extends Range> void findLastOccurenceOfGameOut(List<T> ranges) {

        ranges.forEach(range -> {

            range.getRanges().forEach(range1 -> {
                final int currentGamesOut = range1.getGamesOut();
                List<Integer> gameOutHolder = range1.getRangeGameOutHolder();
                if(gameOutHolder.size() > 1)
                    System.out.println(gameOutHolder.get(gameOutHolder.size()-1));
                int lastIndex = gameOutHolder.lastIndexOf( currentGamesOut );
                int lastSeen =  Math.abs(lastIndex - gameOutHolder.size()) -1;
                range1.setGameOutLastSeen(lastSeen);
            });
        });
    }

    void incrementHitsForAppropriateRange(int positionNumber) {
        ranges.forEach(range1 -> {

            if (positionNumber >= range1.getLowerBound() && positionNumber <= range1.getUpperBound() ||
                    Math.abs(range1.getLowerBound() - positionNumber)  == 1) {
                int hits = range1.getHits();
                range1.setHits(++hits);
                range1.getRangeGameOutHolder().add(range1.getGamesOut());
                int num = (positionNumber >= 0) ? positionNumber : 0;
                range1.getGameOutHolder().add( num );
                range1.setGamesOut(0);
            }
            else if( positionNumber >= range1.getLowerBound() && range1.getUpperBound()==0){
                int hits = range1.getHits();
                range1.setHits(++hits);
                range1.getRangeGameOutHolder().add(range1.getGamesOut());
                range1.getGameOutHolder().add( positionNumber );
                range1.setGamesOut(0);
            }
            else {
                int out = range1.getGamesOut();
                range1.setGamesOut(++out);
            }
        });
    }

    protected <T extends Range> void incrementHitsForAppropriateRange(List<T> ranges, int positionNumber) {

        for(Range range1 : ranges){

            if (positionNumber >= range1.getLowerBound() && positionNumber <= range1.getUpperBound()) {
                int hits = range1.getHits();
                range1.setHits(++hits);
                range1.getRangeGameOutHolder().add(range1.getGamesOut());
                range1.getRangeWinningNumberHolder().add(positionNumber);
                range1.getGameOutHolder().add( range1.getGamesOut() );
                range1.setGamesOut(0);
            }
            else if( positionNumber >= range1.getLowerBound() && range1.getUpperBound()==0){
                int hits = range1.getHits();
                range1.setHits(++hits);
                range1.getRangeGameOutHolder().add(range1.getGamesOut());
                range1.getGameOutHolder().add( range1.getGamesOut() );
                range1.getRangeWinningNumberHolder().add(positionNumber);
                range1.setGamesOut(0);
            }
            else {
                int out = range1.getGamesOut();
                range1.setGamesOut(++out);
            }
        }
    }


    public void resetLowerUpperBound() {

        setMinNumber(defaultUpperLowerBounds[0]);
        setMaxNumber(defaultUpperLowerBounds[1]);
    }

    void placeNumberAndGameOutInMap(int positionNumber, List<Map<Integer, List<String>>> winningLottoNumberList) {

        for (Range range : getRanges()) {

            if (positionNumber >= range.lowerBound && positionNumber <= range.upperBound) {

                if (range.lottoNumberMap.size() == 0) {
                    populateMap(range, range.getLowerBound(), range.getUpperBound());
                }

                range.lottoNumberMap.forEach((k, v) -> {

                    for (Map<Integer, List<String>> integerMap : winningLottoNumberList) {

                        for (Map.Entry<Integer, List<String>> entry : integerMap.entrySet()) {

                            if (k == entry.getKey().intValue()) {
                                range.lottoNumberMap.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                });
            }
        }
    }

    private void populateMap(Range range, int lowerBound, int upperBound) {

        for (int i = lowerBound; i <= upperBound; i++) {
            range.lottoNumberMap.put(i, new ArrayList<>());
        }
    }

    public void assignNewNumberToLowerUpperBound() {

        defaultUpperLowerBounds[0] = getMinNumber();
        defaultUpperLowerBounds[1] = getMaxNumber();
    }

    public void setIndex() {
        rangeIndex = index++;
    }

    public static void resetIndex() {
        index = 0;
    }
}
