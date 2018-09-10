package com.lottoanalysis.models.lotterynumbergap;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LotteryNumberGapModel extends DrawModelBase {

    private int drawPositionIndex;
    private int gapSizeLower, gapSizeUpper, hits, gamesOut, hitsAtGameOut, lastSeen;
    private LottoGame lottoGame;
    private AnalyzeMethod analyzeMethod;
    private String currentDay;
    private StringProperty position, analysisMethod, dayOfWeek;
    private List<LotteryNumberGapModel> lotteryNumberGapModelList;
    private List<Integer> gameOutHolder = new ArrayList<>();
    private List<Integer> gapNumberHolder = new ArrayList<>();
    private List<String> patternHolder = new ArrayList<>();

    private LotteryNumberGapModel(){
    }

    public LotteryNumberGapModel(LottoGame lottoGame){
        position = new SimpleStringProperty("Currently Analyzing Position 1");
        analysisMethod = new SimpleStringProperty("Analysis Method: Positional Numbers");
        dayOfWeek = new SimpleStringProperty("Day Of Week: All Days");
        this.lottoGame = lottoGame;
        drawPositionIndex = 0;
        currentDay = "All Days";
        analyzeMethod = AnalyzeMethod.DRAW_POSITION;
        this.lotteryNumberGapModelList = new ArrayList<>();
        analyze();
    }

    public List<Integer> getGapNumberHolder() {
        return gapNumberHolder;
    }

    public int[] getValues(){
        return new int[]{getGapSizeLower(),getGapSizeUpper()};
    }
    public List<Integer> getGameOutHolder() {
        return gameOutHolder;
    }

    public List<String> getPatternHolder() {
        return patternHolder;
    }

    public LottoGame getLottoGame() {
        return lottoGame;
    }

    public StringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set("Currently Analyzing Position " + position);
        this.drawPositionIndex =  Integer.parseInt(position)-1;
        analyzeDrawData();
        computeHitsAtGameOut();
        onModelChange(("DrawPosition"));
    }

    public StringProperty analysisMethodProperty() {
        return analysisMethod;
    }

    public void setAnalysisMethod(String analysisMethod) {
        this.analysisMethod.set("Analysis Method: " + analysisMethod);
        AnalyzeMethod analyzeMethod =
                Arrays.stream(AnalyzeMethod.values())
                        .filter( method -> method.getTitle().equals(analysisMethod))
                        .findAny().orElse(null);

        this.analyzeMethod = analyzeMethod;
        analyzeDrawData();
        computeHitsAtGameOut();
        onModelChange(("AnalysisMethod"));

    }

    public StringProperty dayOfWeekProperty() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek.set("Day Of Week: " + dayOfWeek.getFullDayName());
        this.currentDay = dayOfWeek.getDay();
        analyzeDrawData();
        computeHitsAtGameOut();
        onModelChange(("DayOfWeek"));
    }

    public int getHits() {
        return hits;
    }

    void setHits(int hits) {
        this.hits = hits;
    }

    public int getGamesOut() {
        return gamesOut;
    }

    void setGamesOut(int gamesOut) {
        this.gamesOut = gamesOut;
    }

    public int getHitsAtGameOut() {
        return hitsAtGameOut;
    }

    void setHitsAtGameOut(int hitsAtGameOut) {
        this.hitsAtGameOut = hitsAtGameOut;
    }

    public int getLastSeen() {
        return lastSeen;
    }

    void setLastSeen(int lastSeen) {
        this.lastSeen = lastSeen;
    }

    public List<LotteryNumberGapModel> getLotteryNumberGapModelList() {
        return lotteryNumberGapModelList;
    }

    public int getGapSizeLower() {
        return gapSizeLower;
    }

    private void setGapSizeLower(int gapSizeLower) {
        this.gapSizeLower = gapSizeLower;
    }

    public int getGapSizeUpper() {
        return gapSizeUpper;
    }

    private void setGapSizeUpper(int gapSizeUpper) {
        this.gapSizeUpper = gapSizeUpper;
    }

    // Methods
    public void analyze(){
        buildRanges();
        analyzeDrawData();
        computeHitsAtGameOut();
    }

    private void computeHitsAtGameOut() {
        for(LotteryNumberGapModel lotteryNumberGapModel : lotteryNumberGapModelList){
            final int currentGamesOut = lotteryNumberGapModel.getGamesOut();
            final Long hits = lotteryNumberGapModel.getGameOutHolder().stream().filter(i -> i == currentGamesOut).count();
            lotteryNumberGapModel.setHitsAtGameOut(hits.intValue());

            final int index = lotteryNumberGapModel.getGameOutHolder().lastIndexOf( currentGamesOut );
            final int seenLast = Math.abs( (index - lotteryNumberGapModel.getGameOutHolder().size()) -1 );
            lotteryNumberGapModel.setLastSeen( seenLast );
        }
    }

    private void analyzeDrawData() {

        int[][] historicalDrawData = new int[lottoGame.getPositionNumbersAllowed()][lottoGame.getDrawingData().size()];
        NumberPatternAnalyzer.loadUpPositionalNumbers(historicalDrawData, lottoGame.getDrawingData());

        int[][] data = null;
        if(!DayOfWeek.ALL.getFullDayName().equals(currentDay)){
            List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(currentDay))
                    .collect(Collectors.toList());
            historicalDrawData = filterDataBasedOnAnalyzeMethod(Drawing.convertDrawDataTo2DArray(drawResults), analyzeMethod);
        }
        else{
            historicalDrawData = filterDataBasedOnAnalyzeMethod(historicalDrawData, analyzeMethod);
        }

        plugNumbersIntoRanges(historicalDrawData);

    }

    private void plugNumbersIntoRanges(int[][] historicalDrawData) {

        resetData();

        if(drawPositionIndex==0){
            for(int j = 0; j < historicalDrawData[drawPositionIndex].length;j++){
                final int value = historicalDrawData[drawPositionIndex][j];
                for(LotteryNumberGapModel lotteryNumberGapModel : lotteryNumberGapModelList){
                    if(value >= lotteryNumberGapModel.getGapSizeLower() && value <= lotteryNumberGapModel.getGapSizeUpper()){
                        int hits = lotteryNumberGapModel.getHits();
                        lotteryNumberGapModel.setHits(++hits);
                        lotteryNumberGapModel.getPatternHolder().add("X");
                        lotteryNumberGapModel.getGapNumberHolder().add( value );
                        lotteryNumberGapModel.getGameOutHolder().add( lotteryNumberGapModel.gamesOut );
                        lotteryNumberGapModel.setGamesOut(0);
                    }
                    else {
                        int out = lotteryNumberGapModel.getGamesOut();
                        lotteryNumberGapModel.setGamesOut(++out);
                        lotteryNumberGapModel.getPatternHolder().add("");
                        lotteryNumberGapModel.getGameOutHolder().add(lotteryNumberGapModel.getGamesOut());
                    }
                }
            }
        }
        else {
            int index = drawPositionIndex;
            for(int i = 0; i < historicalDrawData[0].length; i++){
                int value = Math.abs( historicalDrawData[index-1][i] - historicalDrawData[index][i] );
                for(LotteryNumberGapModel lotteryNumberGapModel : lotteryNumberGapModelList){
                    if(value >= lotteryNumberGapModel.getGapSizeLower() && value <= lotteryNumberGapModel.getGapSizeUpper()){
                        int hits = lotteryNumberGapModel.getHits();
                        lotteryNumberGapModel.setHits(++hits);
                        lotteryNumberGapModel.getPatternHolder().add("X");
                        lotteryNumberGapModel.getGapNumberHolder().add( value );
                        lotteryNumberGapModel.getGameOutHolder().add( lotteryNumberGapModel.gamesOut );
                        lotteryNumberGapModel.setGamesOut(0);
                    }
                    else {
                        int out = lotteryNumberGapModel.getGamesOut();
                        lotteryNumberGapModel.setGamesOut(++out);
                        lotteryNumberGapModel.getPatternHolder().add("");
                        lotteryNumberGapModel.getGameOutHolder().add(lotteryNumberGapModel.getGamesOut());
                    }
                }
            }
        }
    }

    private void resetData() {
        lotteryNumberGapModelList.forEach( lotteryNumberGapModel -> {
            lotteryNumberGapModel.getPatternHolder().clear();
            lotteryNumberGapModel.getGameOutHolder().clear();
            lotteryNumberGapModel.setHits(0);
            lotteryNumberGapModel.setGamesOut(0);
            lotteryNumberGapModel.setLastSeen(0);
            lotteryNumberGapModel.setHitsAtGameOut(0);
        });
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

    private void buildRanges() {
        int minNumber = 1;
        int gameMaxNumber = getLottoGame().getMaxNumber();
        if(gameMaxNumber == 9){
            minNumber = 0;
        }

        int gapSize = getNeedGapSize();

        for(int i = minNumber; i <= gameMaxNumber; ){
            LotteryNumberGapModel lotteryNumberGapModel = new LotteryNumberGapModel();
            lotteryNumberGapModel.setGapSizeLower( i );
            lotteryNumberGapModel.setGapSizeUpper( (i+ gapSize) -1 );

            i += gapSize;

            if(lotteryNumberGapModel.getGapSizeUpper() > gameMaxNumber){
                lotteryNumberGapModel.setGapSizeUpper(gameMaxNumber);
            }

            lotteryNumberGapModelList.add( lotteryNumberGapModel );
        }
    }

    private int getNeedGapSize() {
        int size;
        int r = Math.round( getLottoGame().getMaxNumber()/ getLottoGame().getPositionNumbersAllowed() );
//        switch (getLottoGame().getMaxNumber()){
//            case 9:
//                size = 5;
//                break;
//            case 39:
//                size = 5;
//                break;
//            case 69:
//            case 70:
//                size = 8;
//                break;
//            case 47:
//                size = 8;
//                break;
//            default:
//                size = 5;
//                break;
//        }

        return r;
    }

}
