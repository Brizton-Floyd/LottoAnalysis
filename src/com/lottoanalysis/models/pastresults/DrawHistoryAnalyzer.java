package com.lottoanalysis.models.pastresults;

import com.lottoanalysis.controllers.LottoDashboardController;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.lottogames.drawing.Drawing;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class DrawHistoryAnalyzer {

    private Comparator<Map.Entry<Integer,Integer[]>> hitComparatory = ((o1,o2) -> o1.getValue()[1].compareTo(o2.getValue()[1]));

    private Map<Integer,Integer[]> gameSpanHitMap = new TreeMap<>();
    private Map<Integer,Integer[]> firstDigitValueHolderMap = new LinkedHashMap<>();
    private Map<Integer,LottoNumberGameOutTracker> lottoNumberGameOutTrackerMap;
    private List<Integer> multipleHolderList;

    private List<Object> lottoDrawData;
    private SimpleStringProperty gameName;
    private int[][] historicalDrawData;
    private int gameSpan;
    private AnalyzeMethod analyzeMethod;
    private DrawPositions drawPositions;
    private DayOfWeek dayOfWeek;

    private TotalWinningNumberTracker totalWinningNumberTracker;
    private LottoNumberGameOutTracker lottoNumberGameOutTracker;
    private SumGroupAnalyzer sumGroupAnalyzer;
    private LottoGame lottoGame;
    public boolean dayOfWeekPopulationNeeded;

    public DrawHistoryAnalyzer() { }

    public DrawHistoryAnalyzer(Object[] currentDrawInformation, TotalWinningNumberTracker totalWinningNumberTracker,
                               LottoNumberGameOutTracker lottoNumberGameOutTracker, SumGroupAnalyzer sumGroupAnalyzer) {

        this.totalWinningNumberTracker = totalWinningNumberTracker;
        this.lottoNumberGameOutTracker = lottoNumberGameOutTracker;
        this.sumGroupAnalyzer = sumGroupAnalyzer;
        this.gameSpan = 5;
        this.analyzeMethod = AnalyzeMethod.DRAW_POSITION;
        this.drawPositions = DrawPositions.POS_ONE;
        this.dayOfWeek = DayOfWeek.ALL;
        dayOfWeekPopulationNeeded = true;
        this.lottoGame = (LottoGame) currentDrawInformation[0];
        this.gameName = new SimpleStringProperty(lottoGame.getGameName());
        this.lottoDrawData = (List<Object>) currentDrawInformation[1];
        historicalDrawData = (int[][]) lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex());

        analyzeDrawData( getGameSpan() );

    }

    // getters


    public DrawPositions getDrawPositions() {
        return drawPositions;
    }



    public void setGameSpan(int gameSpan) {
        this.gameSpan = gameSpan ;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isDayOfWeekPopulationNeeded() {
        return dayOfWeekPopulationNeeded;
    }

    public void setDayOfWeekPopulationNeeded(boolean dayOfWeekPopulationNeeded) {
        this.dayOfWeekPopulationNeeded = dayOfWeekPopulationNeeded;
    }

    public void setAnalyzeMethod(AnalyzeMethod analyzeMethodIndex) {
        this.analyzeMethod = analyzeMethodIndex;
    }

    public void setDrawPositions(DrawPositions drawPositions) {
        this.drawPositions = drawPositions;
    }

    public TotalWinningNumberTracker getTotalWinningNumberTracker() {
        return totalWinningNumberTracker;
    }

    public LottoNumberGameOutTracker getLottoNumberGameOutTracker() {
        return lottoNumberGameOutTracker;
    }

    public String getGameName() {
        return gameName.get();
    }

    public int getDrawResultSize() {

        return ((int[][])lottoDrawData.get(0)).length;
    }

    public int[][] getHistoricalDrawData() {
        return historicalDrawData;
    }

    public LottoGame getLottoGame() {
        return lottoGame;
    }

    public int getGameSpan() {
        return gameSpan;
    }

    public AnalyzeMethod getAnalyzeMethod() {
        return analyzeMethod;
    }


    public Map<Integer, Integer[]> getFirstDigitValueHolderMap() {
        return firstDigitValueHolderMap;
    }

    public Map<Integer, LottoNumberGameOutTracker> getLottoNumberGameOutTrackerMap() {
        return lottoNumberGameOutTrackerMap;
    }

    public SumGroupAnalyzer getSumGroupAnalyzer() {


        return sumGroupAnalyzer;
    }

    // methods

    /**
     * Method will extract the draw index from the String parameter.
     * @param text
     * @return returns the correct select position index
     */
    private int extractDrawIndexFromText(String text) {

        if(text.equals(""))
            return - 1;

        String[] textData = text.split(" ");
        final int drawIndex;

        if(text.length() > 1)
        {
            drawIndex = Integer.parseInt( textData[1].trim() ) - 1;
        }
        else
        {
            drawIndex = Integer.parseInt( textData[0].trim() );
        }
        return drawIndex;
    }

    public void analyzeDrawData(){

        analyzeDrawData(getGameSpan());
    }
    /**
     * Method will carry out all functions needed to analyze historical draw data based on the game span
     * @param gameSpan
     */
    public void analyzeDrawData(final int gameSpan){

        final int numberOfPositions = getHistoricalDrawData().length;
        if(AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle())){

            if(dayOfWeek != DayOfWeek.ALL){

                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter( game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                                                                                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray( drawResults );

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, AnalyzeMethod.DRAW_POSITION);
            }
            else
            {
                historicalDrawData = (int[][]) lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex());
            }
        }
        else {
            if(dayOfWeek != DayOfWeek.ALL){

                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter( game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray( drawResults );

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, analyzeMethod);
            }
            else {
                historicalDrawData = (int[][]) lottoDrawData.get(analyzeMethod.getIndex());
            }
        }

        totalWinningNumberTracker.analyze(getHistoricalDrawData(), gameSpan, numberOfPositions);
        performAnalysisOnFirstDigitBasedOnDrawPosition();
        computeGameOutForLottoNumbers();
        analyzeLottoNumberSumGroupHits();
    }

    private int[][] filterDataBasedOnAnalyzeMethod(int[][] convertedData, AnalyzeMethod analyzeMethod1) {

        int[][] data;

        switch (analyzeMethod1){

            case DELTA_NUMBERS:
                data = NumberPatternAnalyzer.findDeltaNumbers(convertedData);
                break;
            case MULTIPLES:
                data = LottoDashboardController.findMultiples(convertedData);
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

    private void analyzeLottoNumberSumGroupHits() {

        // get the correct array from 2D array based on the current position being analyzed
        final int[] drawPositionData;
        final boolean numberDivideCheckNeeded;
        if(AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle()) ){

            if(dayOfWeek == DayOfWeek.ALL) {
                drawPositionData = historicalDrawData[drawPositions.getIndex()];
                numberDivideCheckNeeded = Boolean.TRUE;
            }
            else
            {
                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter( game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray( drawResults );

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, AnalyzeMethod.DRAW_POSITION);
                drawPositionData = historicalDrawData[drawPositions.getIndex()];
                numberDivideCheckNeeded = Boolean.TRUE;
            }
        }
        else {

            if(dayOfWeek == DayOfWeek.ALL) {
                drawPositionData = historicalDrawData[drawPositions.getIndex()];
                numberDivideCheckNeeded = Boolean.FALSE;
            }
            else
            {
                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter( game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray( drawResults );

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, analyzeMethod);
                drawPositionData = historicalDrawData[drawPositions.getIndex()];
                numberDivideCheckNeeded = Boolean.FALSE;
            }
        }

        Set<Integer> currentValidNumbers = getLottoNumberGameOutTrackerMap().keySet();
        sumGroupAnalyzer.analyze(currentValidNumbers, drawPositionData, numberDivideCheckNeeded, lottoGame );

        if(analyzeMethod.getTitle().equals(AnalyzeMethod.MULTIPLES.getTitle())) {
            // Extract numbers from list that do not meet game out criteria
            Collection<SumGroupAnalyzer> sumGroupAnalyzers = sumGroupAnalyzer.getGroupAnalyzerMap().values();
            lottoNumberGameOutTracker.extractNumbersNotMeetingGameSpanCriteria(sumGroupAnalyzers, getGameSpan());
        }

    }

    private void gameOutIncrementer(int hitCount, Map<Integer,Integer[]> dataStructure) {

        dataStructure.forEach((k,v) -> {

            if(k != hitCount)
            {
                v[1]++;
            }
        });
    }

    private void performAnalysisOnFirstDigitBasedOnDrawPosition() {

        // Clear map of any previous existing data
        firstDigitValueHolderMap.clear();

        // first determine which data we need to access out of the given options and then retrieve that data
        final int[][] analysisMethodDrawData;

        if(dayOfWeek == DayOfWeek.ALL) {
            analysisMethodDrawData = (int[][]) (AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle()) ?
                    lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex()) :
                    lottoDrawData.get(analyzeMethod.getIndex()));
        }
        else
        {
            List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter( game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                    .collect(Collectors.toList());

            int[][] convertedData = Drawing.convertDrawDataTo2DArray( drawResults );

            AnalyzeMethod method = (analyzeMethod == AnalyzeMethod.MULTIPLES) ? AnalyzeMethod.DRAW_POSITION : analyzeMethod;

            analysisMethodDrawData = filterDataBasedOnAnalyzeMethod(convertedData, method);
        }

        // now we need to get the correct draw index from the 2D array
        final int[] positionData = analysisMethodDrawData[drawPositions.getIndex()];

        int digit;
        for(int i = 0; i < positionData.length; i++){
            final String numAsString = positionData[i] + "";
            if(numAsString.length() == 1) {
                digit = 0;
            }
            else{
                digit = Character.getNumericValue( numAsString.charAt(0) );
            }

            // plug value into first Digit value holder map
            if(!firstDigitValueHolderMap.containsKey(digit)){
                firstDigitValueHolderMap.put(digit, new Integer[]{1,0});
                gameOutIncrementer(digit, firstDigitValueHolderMap);
            }
            else{
                Integer[] hitAndGameOutData = firstDigitValueHolderMap.get(digit);
                hitAndGameOutData[0]++;
                hitAndGameOutData[1] = 0;
                gameOutIncrementer(digit, firstDigitValueHolderMap);
            }
        }

        final List<Map.Entry<Integer,Integer[]>> sortedList = new ArrayList<>(firstDigitValueHolderMap.entrySet());
        sortedList.sort(hitComparatory);

        firstDigitValueHolderMap.clear();
        sortedList.forEach(val -> {
            firstDigitValueHolderMap.put(val.getKey(), val.getValue());
        });

    }


    public void computeGameOutForLottoNumbers()
    {

        // Assign the current analysis to the draw Data
        if(AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle()) && dayOfWeek == DayOfWeek.ALL){
            historicalDrawData = (int[][])lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex());
        }
        else if(AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle()) && dayOfWeek != DayOfWeek.ALL)
        {
            List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter( game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                    .collect(Collectors.toList());

            int[][] convertedData = Drawing.convertDrawDataTo2DArray( drawResults );

            historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, AnalyzeMethod.DRAW_POSITION);
        }
        else {

            if(dayOfWeek == DayOfWeek.ALL) {
                historicalDrawData = (int[][]) lottoDrawData.get(analyzeMethod.getIndex());
            }
            else{
                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter( game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray( drawResults );

                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, analyzeMethod);
            }
        }

        lottoNumberGameOutTracker.analyzeGamesOutForLottoNumbers(historicalDrawData, lottoGame);
        lottoNumberGameOutTracker.analyzePositionalHitsAndGamesOut(historicalDrawData[drawPositions.getIndex()]);
        lottoNumberGameOutTracker.getLottoNumbersBasedOnGameSpan( gameSpan );
        lottoNumberGameOutTracker.analyzePositionalHitsAndGamesOut(historicalDrawData[drawPositions.getIndex()]);

        lottoNumberGameOutTrackerMap = lottoNumberGameOutTracker.getLottoNumberGameOutInfoMap();
    }

    public List<Integer> extractDefaultResults() {

        for(Map.Entry<Integer[],SumGroupAnalyzer> entry : sumGroupAnalyzer.getGroupAnalyzerMap().entrySet()){
            return entry.getValue().getLottoNumberInSumRangeHolder();
        }

        return null;
    }
}
