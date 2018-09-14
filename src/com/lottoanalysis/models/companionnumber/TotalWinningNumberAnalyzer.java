package com.lottoanalysis.models.companionnumber;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TotalWinningNumberAnalyzer {

    private DayOfWeek dayOfWeek;
    private AnalyzeMethod analyzeMethod;
    private DrawPosition drawPosition;
    private LottoGame lottoGame;
    private int gameOutValue, lottoNumber, totalWinninPositionHits;
    private double averageWinningNumberAtGamesOut;
    private LotteryNumberTracker lotteryNumberTracker;
    private int[][] historicalDrawData;

    TotalWinningNumberAnalyzer(){lottoNumber=-1;}
    public String getAverageWinningNumberAtGamesOut(){
        return String.format("%.2f", averageWinningNumberAtGamesOut);
    }

    public int[][] getHistoricalDrawData() {
        return historicalDrawData;
    }

    void setDrawingList(LottoGame lottoGame) {
        this.lottoGame = lottoGame;
    }

    int getLottoNumber() {
        return lottoNumber;
    }

    public int getTotalWinninPositionHits() {
        return totalWinninPositionHits;
    }

    public void setDrawPosition(DrawPosition drawPosition) {
        this.drawPosition = drawPosition;
    }

    void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    void setAnalyzeMethod(AnalyzeMethod analyzeMethod) {
        this.analyzeMethod = analyzeMethod;
    }

    void setGameOutValue(int gameOutValue) {
        this.gameOutValue = gameOutValue;
    }

    public void analyzeDrawDataVersionTwo(){
        historicalDrawData = new int[lottoGame.getPositionNumbersAllowed()][lottoGame.getDrawingData().size()];
        historicalDrawData = populateHistoricalDrawData(historicalDrawData);
        findTotalPositionHits(historicalDrawData[drawPosition.getIndex()]);
    }
    public void analyzeDrawData() {

        historicalDrawData = new int[lottoGame.getPositionNumbersAllowed()][lottoGame.getDrawingData().size()];
        historicalDrawData = populateHistoricalDrawData(historicalDrawData);
        findTotalPositionHits(historicalDrawData[drawPosition.getIndex()]);
        findAverageHits( historicalDrawData );
        lotteryNumberTracker.filterHistoricalDrawData( historicalDrawData, lotteryNumberTracker );
    }

    private void findTotalPositionHits(int[] historicalDrawDataa) {
        if(lottoNumber < 0){
            for(int i = historicalDrawDataa.length -1; i >= 0; i--){
                if(historicalDrawDataa[i] > -1){
                    lottoNumber = historicalDrawDataa[i];
                    break;
                }
            }
        }
        Long count = Arrays.stream(historicalDrawDataa).filter(number -> number == lottoNumber ).count();
        totalWinninPositionHits = count.intValue();
    }

    private void findAverageHits(int[][] historicalDrawData) {
        final List<Integer> hitTotalHolder = new ArrayList<>();
        final List<Integer[]> drawHolder = new ArrayList<>();
        lotteryNumberTracker = new LotteryNumberTracker(lottoGame.getMinNumber(), lottoGame.getMaxNumber());

        for(int i = 0; i < historicalDrawData[0].length; i++){
            Integer[] drawResult = new Integer[historicalDrawData.length];
            for(int j = 0; j < drawResult.length; j++){
                drawResult[j] = historicalDrawData[j][i];
            }

            if(drawHolder.size() == gameOutValue){
                final int totalWinningNumbersPresentInPast = findTotalWinningNumbers( drawResult, drawHolder );
                hitTotalHolder.add( totalWinningNumbersPresentInPast );
                drawHolder.remove((0));
                drawHolder.add( drawResult );
                lotteryNumberTracker.incrementGamesOutForNonWinningNumbers( drawResult, lotteryNumberTracker.getLotteryNumberTrackerList());
            }
            else{
                drawHolder.add(drawResult);
                lotteryNumberTracker.incrementGamesOutForNonWinningNumbers( drawResult, lotteryNumberTracker.getLotteryNumberTrackerList());
            }
        }

        averageWinningNumberAtGamesOut = hitTotalHolder.stream().mapToDouble(Integer::intValue).average().orElse(Double.NaN);
    }

    private int findTotalWinningNumbers(Integer[] drawResult, List<Integer[]> drawHolder) {
        int winningNumberCount = 0;
        Set<Integer> numberHolder = new HashSet<>();
        for(Integer[] integers : drawHolder){
            numberHolder.addAll(Arrays.asList(integers));
        }

        for(Integer integer : drawResult){
            if(numberHolder.contains(integer)){
                winningNumberCount++;
            }
        }

        return winningNumberCount;
    }

    private int[][] populateHistoricalDrawData(int[][] historicalDrawData) {
        List<Drawing> drawingList;
        int[][] data;

        if(!DayOfWeek.ALL.equals(dayOfWeek)){
            drawingList = lottoGame.getDrawingData().stream().filter( drawing -> drawing.getDrawDate().contains(dayOfWeek.getDay()))
                    .collect(Collectors.toList());
            historicalDrawData = new int[lottoGame.getPositionNumbersAllowed()][drawingList.size()];
            NumberPatternAnalyzer.loadUpPositionalNumbers(historicalDrawData, drawingList);
            data = filterDataBasedOnAnalyzeMethod(historicalDrawData, analyzeMethod);
        }
        else {
            NumberPatternAnalyzer.loadUpPositionalNumbers(historicalDrawData, lottoGame.getDrawingData());
            data = filterDataBasedOnAnalyzeMethod(historicalDrawData, analyzeMethod);
        }

        return data;
    }

    private int[][] filterDataBasedOnAnalyzeMethod(int[][] convertedData, AnalyzeMethod analyzeMethod1) {

        int[][] data;

        switch (analyzeMethod1) {

            case DELTA_NUMBERS:
                data = NumberPatternAnalyzer.findDeltaNumbers(convertedData);
                break;
            case LAST_DIGIT:
                data = NumberPatternAnalyzer.getLastDigits(convertedData);
                break;
            case REMAINDER:
                data = NumberPatternAnalyzer.computeRemainders(convertedData);
                break;
            case POSITIONAL_SUMS:
                data = NumberPatternAnalyzer.findPositionalSums(convertedData);
                break;
            case LINE_SPACINGS:
                data = NumberPatternAnalyzer.lineSpacings(convertedData);
                break;
            default:
                data = convertedData;

        }
        return data;
    }

    public void setLottoNumber(int lottoNumber) {
        this.lottoNumber = lottoNumber;
    }

    public int getLottoNumberGamesOut(){
        return lotteryNumberTracker.getGamesOutForLottoNumber();
    }

    public class LotteryNumberTracker {
        List<LotteryNumberTracker> lotteryNumberTrackerList = new ArrayList<>();
        private int lottoNum;
        private int gamesOut;
        
        private LotteryNumberTracker(final int lottoNum){
            this.lottoNum = lottoNum;
            gamesOut = 0;
        }
        LotteryNumberTracker(final int min, final int max){
            for(int i = min; i <= max; i++){
                lotteryNumberTrackerList.add(new LotteryNumberTracker(i));
            }
        }

        public List<LotteryNumberTracker> getLotteryNumberTrackerList() {
            return lotteryNumberTrackerList;
        }

        public int getLottoNum() {
            return lottoNum;
        }

        public void setGamesOut(int gamesOut) {
            this.gamesOut = gamesOut;
        }

        public int getGamesOut() {
            return gamesOut;
        }

        void incrementGamesOutForNonWinningNumbers(Integer[] drawResult, List<LotteryNumberTracker> lotteryNumberTrackerList) {
            List<Integer> currentWinningNumbers = Arrays.asList(drawResult);

            for (LotteryNumberTracker lotteryNumberTracker : lotteryNumberTrackerList) {
                if (!currentWinningNumbers.contains(lotteryNumberTracker.lottoNum)) {
                    int out = lotteryNumberTracker.gamesOut;
                    lotteryNumberTracker.setGamesOut(++out);
                } else {
                    lotteryNumberTracker.setGamesOut(0);
                }
            }

        }

        void filterHistoricalDrawData(int[][] historicalDrawData, LotteryNumberTracker lotteryNumberTracker) {
            List<LotteryNumberTracker> numberTrackerList = lotteryNumberTracker.getLotteryNumberTrackerList()
                                                            .stream().filter(lottoTracker -> lottoTracker.gamesOut > gameOutValue)
                                                            .collect(Collectors.toList());

            for(LotteryNumberTracker tracker : numberTrackerList){

                for (int[] aHistoricalDrawData : historicalDrawData) {

                    List<Integer> values = Arrays.stream(aHistoricalDrawData).boxed().collect(Collectors.toList());
                    final int index = values.indexOf(tracker.lottoNum);
                    if (index > 0) {

                        final int[] indexes = IntStream.range(0,aHistoricalDrawData.length)
                                                       .filter(i -> aHistoricalDrawData[i] == tracker.lottoNum)
                                                       .toArray();
                        for(int i : indexes){
                            aHistoricalDrawData[i] = -1;
                        }
                    }

                }
            }
        }

        int getGamesOutForLottoNumber() {
            for(LotteryNumberTracker lotteryNumberTracker : lotteryNumberTrackerList){
                if(lotteryNumberTracker.lottoNum == lottoNumber)
                    return lotteryNumberTracker.getGamesOut();

            }
            return -1;
        }
    }
}
