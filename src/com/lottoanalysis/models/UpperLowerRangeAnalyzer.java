package com.lottoanalysis.models;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.utilities.analyzerutilites.SplitDigitAnalyzer;

import java.util.*;
import java.util.stream.Collectors;

import static sun.misc.Version.print;

public class UpperLowerRangeAnalyzer {

    private int drawIndex;
    private int[][] drawData;
    private int[] range;
    private int rangeHits;
    private int rangGamesOut;
    private int rangeHitAtGamesOut;
    private int gameOutLastAppearance;
    private List<Integer> gameOutHolder;

    private LottoNumberTracker lottoNumberTracker;
    private RemainderTracker remainderTracker;
    private SingleDigitRangeTracker singleDigitRangeTracker;
    private UpperLowerRangeAnalyzer[] upperLowerRangeAnalyzers;

    public UpperLowerRangeAnalyzer(){
        gameOutHolder = new ArrayList<>();
        lottoNumberTracker = new LottoNumberTracker();
        remainderTracker = new RemainderTracker();
        singleDigitRangeTracker = new SingleDigitRangeTracker();

    }
    public UpperLowerRangeAnalyzer(int[][] drawData, int drawIndex, LottoGame lottoGame){

        this.drawData = drawData;
        this.drawIndex = drawIndex;
        upperLowerRangeAnalyzers = new UpperLowerRangeAnalyzer[]{new UpperLowerRangeAnalyzer(),new UpperLowerRangeAnalyzer()};

        determineHighAndLowRanges(lottoGame);
        determineRangeLottoNumbersHit();
        determineRangeHitsAtGamesOut();
        printData();
    }

    private void printData() {

        String[] directions = {"Lower","Upper"};

        for( int i = 0; i < upperLowerRangeAnalyzers.length; i++){

            UpperLowerRangeAnalyzer analyzer = upperLowerRangeAnalyzers[i];

            System.out.printf("\n%s Half Numbers: %s\n",directions[i],Arrays.toString(analyzer.getRange()));
            System.out.printf("\n%25s %s %15s %s %25s %s %15s\n","Hits:",analyzer.getRangeHits(),"Games Out:",analyzer.getRangGamesOut(),
                    "Hits At Games Out:", analyzer.getRangeHitAtGamesOut(),"Game Out Last Seen: ");


        }
    }

    // getters and setters

    public List<Integer> getGameOutHolder() {
        return gameOutHolder;
    }

    public int[] getRange() {
        return range;
    }

    public void setRange(int[] range) {
        this.range = range;
    }

    public int getRangeHits() {
        return rangeHits;
    }

    public void setRangeHits(int rangeHits) {
        this.rangeHits = rangeHits;
    }

    public int getRangGamesOut() {
        return rangGamesOut;
    }

    public void setRangGamesOut(int rangGamesOut) {
        this.rangGamesOut = rangGamesOut;
    }

    public int getRangeHitAtGamesOut() {
        return rangeHitAtGamesOut;
    }

    public void setRangeHitAtGamesOut(int rangeHitAtGamesOut) {
        this.rangeHitAtGamesOut = rangeHitAtGamesOut;
    }

    public int getGameOutLastAppearance() {
        return gameOutLastAppearance;
    }

    public void setGameOutLastAppearance(int gameOutLastAppearance) {
        this.gameOutLastAppearance = gameOutLastAppearance;
    }

    // Methods

    private void determineRangeHitsAtGamesOut() {

        for(UpperLowerRangeAnalyzer analyzer : upperLowerRangeAnalyzers){

            int gamesOut = analyzer.getRangGamesOut();
            long count = analyzer.getGameOutHolder().stream().filter(out -> out  == gamesOut).count();

            int index = analyzer.getGameOutHolder().lastIndexOf( gamesOut );
            int gamesAgo = Math.abs( analyzer.getGameOutHolder().size() - index );

            analyzer.setRangeHitAtGamesOut((int)count);
            analyzer.setGameOutLastAppearance( gamesAgo );

        }
    }

    private void determineHighAndLowRanges(LottoGame game)
    {
        int maxNumber = game.getMaxNumber();

        int half = (game.getMaxNumber() == 9) ? (maxNumber+1)/2 : maxNumber / 2;
        List<List<Integer>> upperLowerNumbers = new LinkedList<>();
        upperLowerNumbers.add(new ArrayList<>());
        upperLowerNumbers.add(new ArrayList<>());

        for(int i = game.getMinNumber(); i <= maxNumber; i++)
        {
            if(i < half)
            {
                List<Integer> lower = upperLowerNumbers.get(0);
                lower.add(i);
            }
            else
            {
                List<Integer> upper = upperLowerNumbers.get(1);
                upper.add(i);
            }
        }

        // plug data into map
        Integer[] lower = upperLowerNumbers.get(0).toArray(new Integer[upperLowerNumbers.get(0).size()]);
        Integer[] upper = upperLowerNumbers.get(1).toArray(new Integer[upperLowerNumbers.get(1).size()]);

        upperLowerRangeAnalyzers[0].setRange( Arrays.stream( lower ).mapToInt(i -> i).toArray() );
        upperLowerRangeAnalyzers[1].setRange( Arrays.stream( upper ).mapToInt(i -> i).toArray() );
    }

    private void determineRangeLottoNumbersHit(){

        String direction = "";

        for(int i = 0; i < drawData[drawIndex].length; i++){

            String lottNumber = drawData[drawIndex][i] + "";
            int remainder = Integer.parseInt(lottNumber) % 3;

            for (int k = 0; k < upperLowerRangeAnalyzers.length; k++) {

                List<Integer> rangeNumbers = Arrays.stream(upperLowerRangeAnalyzers[k].getRange()).boxed()
                                                                                        .collect(Collectors.toList());

                if (rangeNumbers.contains(Integer.parseInt(lottNumber))) {

                    direction = (k == 0)? "Lower" : "Upper";

                    int hits = upperLowerRangeAnalyzers[k].getRangeHits();

                    List<Integer> gameOutHolder = upperLowerRangeAnalyzers[k].getGameOutHolder();

                    gameOutHolder.add(upperLowerRangeAnalyzers[k].getRangGamesOut());
                    upperLowerRangeAnalyzers[k].setRangeHits(++hits);
                    upperLowerRangeAnalyzers[k].setRangGamesOut(0);

                    upperLowerRangeAnalyzers[k].singleDigitRangeTracker.populateDataMap(direction,lottNumber);
                    upperLowerRangeAnalyzers[k].lottoNumberTracker.insertNumberAndIncrementHits(Integer.parseInt(lottNumber));
                    upperLowerRangeAnalyzers[k].remainderTracker.insertRemainderAndLottoNumber(remainder,Integer.parseInt(lottNumber));



                } else {

                    int gamesOut = upperLowerRangeAnalyzers[k].getRangGamesOut();
                    upperLowerRangeAnalyzers[k].setRangGamesOut(++gamesOut);

                }
            }

        }
    }
}
