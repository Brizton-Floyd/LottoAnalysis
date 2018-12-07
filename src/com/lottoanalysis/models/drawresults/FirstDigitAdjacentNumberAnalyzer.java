package com.lottoanalysis.models.drawresults;

import java.util.*;

public class FirstDigitAdjacentNumberAnalyzer {

    private DrawResultPosition drawResultPosition;

    private int firstDigit, hits, gamesOut, lastSeen, hitsAtGamesOut;

    private List<Integer> gameOutHolderList;
    private List<Integer> adjacentNumberHolderList;
    private List<FirstDigitAdjacentNumberAnalyzer> firstDigitAdjacentNumberAnalyzerList;

    private FirstDigitAdjacentNumberAnalyzer(){
        gameOutHolderList = new ArrayList<>();
        adjacentNumberHolderList = new ArrayList<>();
    }

    public FirstDigitAdjacentNumberAnalyzer( DrawResultPosition drawResultPosition ){
        this.drawResultPosition = drawResultPosition;
        firstDigitAdjacentNumberAnalyzerList = new ArrayList<>();
        analyze();
    }


    private void analyze() {
        Set<Integer> firstDigitSet = getFirstDigitSet();
        createFirstDigitAnalyzers( firstDigitSet );
        calculateHitsAtGamesOutAndLastSeen();
    }

    private void calculateHitsAtGamesOutAndLastSeen() {
        for(FirstDigitAdjacentNumberAnalyzer firstDigitAdjacentNumberAnalyzer : firstDigitAdjacentNumberAnalyzerList){
            int gamesOut = firstDigitAdjacentNumberAnalyzer.getGamesOut();
            long count = firstDigitAdjacentNumberAnalyzer.gameOutHolderList.stream().filter(integer -> integer == gamesOut).count();
            firstDigitAdjacentNumberAnalyzer.hitsAtGamesOut = (int) count;

            int gamesAgo = Math.abs( firstDigitAdjacentNumberAnalyzer.gameOutHolderList.lastIndexOf( firstDigitAdjacentNumberAnalyzer.gamesOut ) -
                           firstDigitAdjacentNumberAnalyzer.gameOutHolderList.size());
            firstDigitAdjacentNumberAnalyzer.lastSeen = gamesAgo;
        }
    }

    private void createFirstDigitAnalyzers(Set<Integer> firstDigitSet) {
        firstDigitAdjacentNumberAnalyzerList.clear();
        for(Integer firstDigit : firstDigitSet){
            FirstDigitAdjacentNumberAnalyzer firstDigitAdjacentNumberAnalyzer = new FirstDigitAdjacentNumberAnalyzer();
            firstDigitAdjacentNumberAnalyzer.firstDigit = firstDigit;
            firstDigitAdjacentNumberAnalyzerList.add( firstDigitAdjacentNumberAnalyzer );
        }

        List<Integer> drawResultsForPostion = drawResultPosition.getLotteryResultPositionList();
        for(Integer lottoNumber : drawResultsForPostion) {
            String lottoNumberAsString = Integer.toString(lottoNumber);
            int firstDigit, lastDigit;
            if (lottoNumberAsString.length() > 1) {
                firstDigit = Character.getNumericValue(lottoNumberAsString.charAt(0));
                lastDigit = Character.getNumericValue(lottoNumberAsString.charAt(1));
            } else {
                firstDigit = 0;
                lastDigit = Character.getNumericValue(lottoNumberAsString.charAt(0));
            }

            for(FirstDigitAdjacentNumberAnalyzer firstDigitAdjacentNumberAnalyzer : firstDigitAdjacentNumberAnalyzerList){
                if(firstDigitAdjacentNumberAnalyzer.firstDigit == firstDigit){
                    firstDigitAdjacentNumberAnalyzer.hits++;
                    firstDigitAdjacentNumberAnalyzer.gameOutHolderList.add( firstDigitAdjacentNumberAnalyzer.gamesOut );
                    firstDigitAdjacentNumberAnalyzer.gamesOut = 0;
                    firstDigitAdjacentNumberAnalyzer.adjacentNumberHolderList.add( lastDigit );
                }else{
                    firstDigitAdjacentNumberAnalyzer.gamesOut++;
                }
            }
        }
    }

    private Set<Integer> getFirstDigitSet() {
        List<Integer> drawResultsForPostion = drawResultPosition.getLotteryResultPositionList();
        Set<Integer> firstDigitSet = new HashSet<>();

        for(Integer lottoNumber : drawResultsForPostion) {
            String lottoNumberAsString = Integer.toString(lottoNumber);
            int firstDigit;
            if (lottoNumberAsString.length() > 1) {
                firstDigit = Character.getNumericValue(lottoNumberAsString.charAt(0));
            } else {
                firstDigit = 0;
            }
            firstDigitSet.add( firstDigit );
        }
        return firstDigitSet;
    }

    public List<FirstDigitAdjacentNumberAnalyzer> getFirstDigitAdjacentNumberAnalyzerList() {
        return firstDigitAdjacentNumberAnalyzerList;
    }

    public int getFirstDigit() {
        return firstDigit;
    }

    public int getHits() {
        return hits;
    }

    public int getGamesOut() {
        return gamesOut;
    }

    public int getLastSeen() {
        return lastSeen;
    }

    public List<Integer> getAdjacentNumberHolderList() {
        return adjacentNumberHolderList;
    }

    public int getHitsAtGamesOut() {
        return hitsAtGamesOut;
    }
}
