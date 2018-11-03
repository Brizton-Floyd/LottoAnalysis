package com.lottoanalysis.models.gameout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NumberDistanceCalculator {

    private GameOutRange gameOutRange;
    private int hits, gamesOut, lowerRange, upperRange;
    private List<Integer> distanceValues = new ArrayList<>();
    private List<NumberDistanceCalculator> numberDistanceMapListHolder = new ArrayList<>();

    private NumberDistanceCalculator() {
    }

    public NumberDistanceCalculator(GameOutRange gameOutRange) {
        this.gameOutRange = gameOutRange;
        findUpperLowerValues();
    }

    public List<NumberDistanceCalculator> numberDistributionData() {
        return numberDistanceMapListHolder;
    }

    public String getUpperLowerAsArrayString(){
        return Arrays.toString(new Integer[]{lowerRange, upperRange});
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

    public int getLowerRange() {
        return lowerRange;
    }

    public void setLowerRange(int lowerRange) {
        this.lowerRange = lowerRange;
    }

    public int getUpperRange() {
        return upperRange;
    }

    public void setUpperRange(int upperRange) {
        this.upperRange = upperRange;
    }

    public List<Integer> getDistanceValues() {
        return distanceValues;
    }

    public void setDistanceValues(List<Integer> distanceValues) {
        this.distanceValues = distanceValues;
    }

    private void findUpperLowerValues() {
        final int[] upperLowerBoundAsArray = gameOutRange.getUpperLowerBoundAsArray();
        if (upperLowerBoundAsArray.length == 1) {
            numberDistanceMapListHolder.add(new NumberDistanceCalculator());
        } else {
            for (int i = upperLowerBoundAsArray[0]; i < upperLowerBoundAsArray[1]; i++) {
                NumberDistanceCalculator numberDistanceCalculator = new NumberDistanceCalculator();
                numberDistanceCalculator.setLowerRange(i);
                numberDistanceCalculator.setUpperRange(i + 4);
                numberDistanceMapListHolder.add(numberDistanceCalculator);
                i += 4;
            }

            analyze();
        }
    }

    @SuppressWarnings("unchecked")
    private void analyze() {
        boolean breakOuter = false;
        List<Integer> gameOutData = new ArrayList<>(gameOutRange.getGameOutHolder());
        if (gameOutData.size() > 1) {
            for (int i = 0; i < gameOutData.size(); i++) {
                //final int difference = Math.abs(gameOutData.get(i) - gameOutData.get(i - 1));
                for (NumberDistanceCalculator numberDistanceCalculator : numberDistanceMapListHolder) {
                    if (gameOutData.get(i) >= numberDistanceCalculator.getLowerRange() && gameOutData.get(i) <= numberDistanceCalculator.getUpperRange()) {
                        int gHits = numberDistanceCalculator.getHits();
                        gHits++;
                        numberDistanceCalculator.setHits( gHits );
                        numberDistanceCalculator.getDistanceValues().add( gameOutData.get(i));
                        numberDistanceCalculator.setGamesOut(0);
                        incrementGamesOut(numberDistanceMapListHolder, numberDistanceCalculator);
                        break;
                    }
                }
            }
        }
    }

    private void incrementGamesOut(List<NumberDistanceCalculator> numberDistanceMapListHolder, NumberDistanceCalculator key) {

        numberDistanceMapListHolder.forEach(numberDistanceCalculator -> {

            if(numberDistanceCalculator != key){
                int gamesOut = numberDistanceCalculator.getGamesOut();
                gamesOut++;
                numberDistanceCalculator.setGamesOut(gamesOut);
            }
        });
    }
}
