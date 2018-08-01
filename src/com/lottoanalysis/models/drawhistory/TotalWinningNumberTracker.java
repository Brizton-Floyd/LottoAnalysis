package com.lottoanalysis.models.drawhistory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class TotalWinningNumberTracker {

    private int totalHits;
    private int gamesOut;
    private Map<String,TotalWinningNumberTracker> totalWinningNumberTrackerMap = new LinkedHashMap<>();
    private static List<Integer> hitHolder = new ArrayList<>();

    // Getters

    public int getTotalHits() {
        return totalHits;
    }

    private void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public int getGamesOut() {
        return gamesOut;
    }

    private void setGamesOut(int gamesOut) {
        this.gamesOut = gamesOut;
    }

    public Map<String, TotalWinningNumberTracker> getTotalWinningNumberTrackerMap() {

        Iterator<String> iterator = totalWinningNumberTrackerMap.keySet().iterator();
        while (iterator.hasNext())
        {
            final String key = iterator.next();
            final TotalWinningNumberTracker totalWinningNumberTracker = totalWinningNumberTrackerMap.get(key);
            if(totalWinningNumberTracker.getTotalHits() == 0){
                iterator.remove();
            }
        }
        return totalWinningNumberTrackerMap;
    }

    // Methods

    public static Double getAverage(){
        return new BigDecimal(hitHolder.stream().mapToDouble(val -> val).average().getAsDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue();

    }

    public void analyze(int[][] historicalDrawData, final int gameSpan, final int totalPositions) {

        assignValuesTotalWinningNumberMap( totalPositions );
        computeWinningNumbersBasedOnGameSpan( gameSpan, historicalDrawData );
    }

    private void computeWinningNumbersBasedOnGameSpan(int gameSpan, int[][] historicalDrawData) {

        // create list to hold a series of draws
        List<Integer[]> drawList = new ArrayList<>();

        for(int i = 0; i < historicalDrawData[0].length; i++){

            Integer[] drawHolder = new Integer[historicalDrawData.length];
            for(int j = 0; j < drawHolder.length; j++){
                drawHolder[j] = historicalDrawData[j][i];
            }

            // add draw array to drawlist
            drawList.add(drawHolder);

            // check to see if the list size is equal to gamespan + 1
            if(drawList.size() == gameSpan+1){
                final int count = findWinningNumbersPresentWithinSpan( drawList );
                plugCountIntoMap( count, historicalDrawData.length );

                // add count to hit holder map
                hitHolder.add(count);

                // remove the first index to keep in alignment with gamespan +1
                drawList.remove(0);
            }
        }
    }

    private void plugCountIntoMap(int count, int size) {

        final String hitRepresentationString = String.format("%s of %s",count, size);
        for(Map.Entry<String,TotalWinningNumberTracker> trackerEntry : totalWinningNumberTrackerMap.entrySet()){

            if(trackerEntry.getKey().equals(hitRepresentationString)){
                trackerEntry.getValue().setGamesOut(0);
                int hitCount = trackerEntry.getValue().getTotalHits();
                trackerEntry.getValue().setTotalHits(++hitCount);

                incrementGamesOut(totalWinningNumberTrackerMap,hitRepresentationString);
                break;
            }

        }
    }

    private void incrementGamesOut(Map<String, TotalWinningNumberTracker> totalWinningNumberTrackerMap, String hitRepresentationString) {

        for(Map.Entry<String, TotalWinningNumberTracker> totalWinningNumberTrackerEntry : totalWinningNumberTrackerMap.entrySet()){
            if(!totalWinningNumberTrackerEntry.getKey().equals(hitRepresentationString)){
               int gamesOut =  totalWinningNumberTrackerEntry.getValue().getGamesOut();
               totalWinningNumberTrackerEntry.getValue().setGamesOut(++gamesOut);
            }
        }
    }

    private int findWinningNumbersPresentWithinSpan(List<Integer[]> drawList) {

        // extract the current winning lotto draw
        final Integer[] currentWinningDraw = drawList.get( drawList.size() - 1 ) ;

        // remove current draw so its not part of the analysis
        drawList.remove( currentWinningDraw );

        // add total span of numbers to set to make all numbers unique
        final Set<Integer> spanOfNumbers = new HashSet<>();

        for (Integer[] aDrawList : drawList) {
            spanOfNumbers.addAll(Arrays.asList(aDrawList));
        }

        final Long count = Arrays.stream( currentWinningDraw ).mapToInt(Integer::intValue).filter(spanOfNumbers::contains).count();

        // add removed drawing back to list
        drawList.add( currentWinningDraw );

        return count.intValue();
    }

    private void assignValuesTotalWinningNumberMap(int totalPositions) {

        // clear contents from structures before adding new draw content
        clearDataStructureContent();

        for(int i = 0; i <= totalPositions; i++) {

            totalWinningNumberTrackerMap.put(String.format("%s of %s",i,totalPositions), new TotalWinningNumberTracker());
        }
    }

    private void clearDataStructureContent() {
        totalWinningNumberTrackerMap.clear();
        hitHolder.clear();
    }


}
