package com.lottoanalysis.models.drawhistory;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.controllers.LottoDashboardController;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;
import javafx.beans.property.SimpleStringProperty;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class DrawModel extends DrawModelBase {

    private Comparator<Map.Entry<Integer, Integer[]>> hitComparatory = ((o1, o2) -> o2.getValue()[0].compareTo(o1.getValue()[0]));

    private Map<Integer, Integer[]> firstDigitValueHolderMap = new LinkedHashMap<>();
    private Map<Integer, LottoNumberGameOutTracker> lottoNumberGameOutTrackerMap;

    private List<Object> lottoDrawData;
    private SimpleStringProperty gameName, drawingPosition, analysisMeth, weekDay;
    private int[][] historicalDrawData;
    private int gameSpan;
    private AnalyzeMethod analyzeMethod;
    private DrawPosition drawPosition;
    private DayOfWeek dayOfWeek;

    private TotalWinningNumberTracker totalWinningNumberTracker;
    private LottoNumberGameOutTracker lottoNumberGameOutTracker;
    private SumGroupAnalyzer sumGroupAnalyzer;
    private LottoGame lottoGame;
    private boolean dayOfWeekPopulationNeeded;

    public DrawModel(LottoGame lottoGame, TotalWinningNumberTracker totalWinningNumberTracker,
                     LottoNumberGameOutTracker lottoNumberGameOutTracker, SumGroupAnalyzer sumGroupAnalyzer){

        this.lottoGame = lottoGame;
        lottoDrawData = new ArrayList<>();

        populateDrawData();

        this.totalWinningNumberTracker = totalWinningNumberTracker;
        this.lottoNumberGameOutTracker = lottoNumberGameOutTracker;
        this.sumGroupAnalyzer = sumGroupAnalyzer;
        this.gameSpan = 5;
        this.analyzeMethod = AnalyzeMethod.DRAW_POSITION;
        this.drawPosition = DrawPosition.POS_ONE;
        this.dayOfWeek = DayOfWeek.ALL;
        this.dayOfWeekPopulationNeeded = true;
        this.drawingPosition = new SimpleStringProperty();
        setDrawingPosition((drawPosition.getIndex() + 1) + "");
        this.analysisMeth = new SimpleStringProperty();
        setAnalysisMeth(analyzeMethod.getTitle());
        weekDay = new SimpleStringProperty();
        setWeekDay(dayOfWeek.getDay());

        this.gameName = new SimpleStringProperty(lottoGame.getGameName());

    }

    public DrawModel(Object[] currentDrawInformation, TotalWinningNumberTracker totalWinningNumberTracker,
                     LottoNumberGameOutTracker lottoNumberGameOutTracker, SumGroupAnalyzer sumGroupAnalyzer) {

        this.totalWinningNumberTracker = totalWinningNumberTracker;
        this.lottoNumberGameOutTracker = lottoNumberGameOutTracker;
        this.sumGroupAnalyzer = sumGroupAnalyzer;
        this.gameSpan = 5;
        this.analyzeMethod = AnalyzeMethod.DRAW_POSITION;
        this.drawPosition = DrawPosition.POS_ONE;
        this.dayOfWeek = DayOfWeek.ALL;
        this.dayOfWeekPopulationNeeded = true;
        this.lottoGame = (LottoGame) currentDrawInformation[0];
        this.drawingPosition = new SimpleStringProperty();
        setDrawingPosition((drawPosition.getIndex() + 1) + "");
        this.analysisMeth = new SimpleStringProperty();
        setAnalysisMeth(analyzeMethod.getTitle());
        weekDay = new SimpleStringProperty();
        setWeekDay(dayOfWeek.getDay());
        this.gameName = new SimpleStringProperty(lottoGame.getGameName());
        this.lottoDrawData = (List<Object>) currentDrawInformation[1];
        this.historicalDrawData = (int[][]) lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex());

    }

    protected int[][] getDrawData(){

        if(!DayOfWeek.ALL.equals(dayOfWeek) && !AnalyzeMethod.MULTIPLES.equals(analyzeMethod)){
            List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                    .collect(Collectors.toList());

            int[][] convertDrawDataTo2DArray = Drawing.convertDrawDataTo2DArray(drawResults);
            historicalDrawData = filterDataBasedOnAnalyzeMethod(convertDrawDataTo2DArray, analyzeMethod);
        }
        else{
            historicalDrawData = (int[][]) lottoDrawData.get(analyzeMethod.getIndex());
        }

        return historicalDrawData;
    }

    private void populateDrawData() {

        historicalDrawData = new int[lottoGame.getPositionNumbersAllowed()][lottoGame.getDrawingData().size()];
        NumberPatternAnalyzer.loadUpPositionalNumbers(historicalDrawData, lottoGame.getDrawingData());

        lottoDrawData.add(historicalDrawData);
        lottoDrawData.add(NumberPatternAnalyzer.findDeltaNumbers(historicalDrawData));
        lottoDrawData.add(NumberPatternAnalyzer.findPositionalSums(historicalDrawData));
        lottoDrawData.add(NumberPatternAnalyzer.lineSpacings(historicalDrawData));
        lottoDrawData.add(NumberPatternAnalyzer.computeRemainders(historicalDrawData));
        lottoDrawData.add(NumberPatternAnalyzer.getLastDigits(historicalDrawData));
        lottoDrawData.add(NumberPatternAnalyzer.findMultiples(historicalDrawData));

    }

    // getters


    public List<Object> getLottoDrawData() {
        return lottoDrawData;
    }

    public String getDrawingPosition() {
        return drawingPosition.get();
    }

    public void setDrawingPosition(String drawingPosition) {

        this.drawingPosition.set(String.format("Currently Analyzing Position %s",drawingPosition));
    }

    public String getAnalysisMeth() {
        return analysisMeth.get();
    }

    public SimpleStringProperty analysisMethProperty() {
        return analysisMeth;
    }

    private void setAnalysisMeth(String analysisMeth) {
        this.analysisMeth.set(String.format("Analyzing By \t\" %s \" ",analysisMeth));
    }

    public String getWeekDay() {
        return weekDay.get();
    }

    public SimpleStringProperty weekDayProperty() {
        return weekDay;
    }

    private void setWeekDay(String weekDay) {
        this.weekDay.set("Day: " + weekDay);
    }

    public SimpleStringProperty drawingPositionProperty() {
        return drawingPosition;
    }

    public DrawPosition getDrawPosition() {
        return drawPosition;
    }

    public void setGameSpan(int gameSpan) {
        this.gameSpan = gameSpan;
        onModelChange("gameSpan");
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        setWeekDay(dayOfWeek.getFullDayName());
        onModelChange("dayOfWeek");

    }

    public boolean isDayOfWeekPopulationNeeded() {
        return dayOfWeekPopulationNeeded;
    }

    public void setDayOfWeekPopulationNeeded(boolean dayOfWeekPopulationNeeded) {
        this.dayOfWeekPopulationNeeded = dayOfWeekPopulationNeeded;
    }

    public void setAnalyzeMethod(AnalyzeMethod analyzeMethodIndex) {
        this.analyzeMethod = analyzeMethodIndex;
        setAnalysisMeth(analyzeMethod.getTitle());
        onModelChange("analyzeMethod");

    }

    public void setDrawPosition(DrawPosition drawPosition) {
        this.drawPosition = drawPosition;
        setDrawingPosition( (drawPosition.getIndex() + 1) + "");
        onModelChange("drawPosition");

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

        int len;
        if(LotteryGameConstants.MEGA_MILLIONS.equals(lottoGame.getGameName()) || LotteryGameConstants.POWERBALL.equals(lottoGame.getGameName()) ||
                LotteryGameConstants.SUPPER_LOTTO_PLUS.equals(lottoGame.getGameName())) {

            len = 6;

        }
        else {
            len = ((int[][]) lottoDrawData.get(0)).length;
        }

        return len;
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
     *
     * @param text
     * @return returns the correct select position index
     */
    private int extractDrawIndexFromText(String text) {

        if (text.equals(""))
            return -1;

        String[] textData = text.split(" ");
        final int drawIndex;

        if (text.length() > 1) {
            drawIndex = Integer.parseInt(textData[1].trim()) - 1;
        } else {
            drawIndex = Integer.parseInt(textData[0].trim());
        }
        return drawIndex;
    }

    public void analyzeDrawData() {

        analyzeDrawData(getGameSpan());
    }

    /**
     * Method will carry out all functions needed to analyze historical draw data based on the game span
     *
     * @param gameSpan
     */
    private void analyzeDrawData(final int gameSpan) {

        if (AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle()) ||
                AnalyzeMethod.REMAINDER == analyzeMethod || AnalyzeMethod.POSITIONAL_SUMS == analyzeMethod ||
                AnalyzeMethod.GROUP_ANALYSIS == analyzeMethod) {

            if (dayOfWeek != DayOfWeek.ALL) {

                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray(drawResults);

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, AnalyzeMethod.DRAW_POSITION);
            } else {
                historicalDrawData = (int[][]) lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex());
            }
        } else {
            if (dayOfWeek != DayOfWeek.ALL) {

                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray(drawResults);

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, analyzeMethod);
            } else {
                historicalDrawData = (int[][]) lottoDrawData.get(analyzeMethod.getIndex());
            }
        }

        totalWinningNumberTracker.analyze(historicalDrawData, gameSpan);
        performAnalysisOnFirstDigitBasedOnDrawPosition();
        computeGameOutForLottoNumbers();
        analyzeLottoNumberSumGroupHits();
    }

    private int[][] filterDataBasedOnAnalyzeMethod(int[][] convertedData, AnalyzeMethod analyzeMethod1) {

        int[][] data;

        switch (analyzeMethod1) {

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
        final Boolean numberDivideCheckNeeded;
        if (AnalyzeMethod.MULTIPLES == analyzeMethod || AnalyzeMethod.REMAINDER == analyzeMethod
                || AnalyzeMethod.POSITIONAL_SUMS == analyzeMethod || AnalyzeMethod.GROUP_ANALYSIS == analyzeMethod) {

            if (dayOfWeek == DayOfWeek.ALL) {
                drawPositionData = historicalDrawData[drawPosition.getIndex()];
                numberDivideCheckNeeded = (AnalyzeMethod.REMAINDER == analyzeMethod) ? Boolean.FALSE : Boolean.TRUE;
            } else {
                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray(drawResults);

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, AnalyzeMethod.DRAW_POSITION);
                drawPositionData = historicalDrawData[drawPosition.getIndex()];
                numberDivideCheckNeeded = (AnalyzeMethod.REMAINDER == analyzeMethod) ? Boolean.FALSE : Boolean.TRUE;
            }
        } else {

            if (dayOfWeek == DayOfWeek.ALL) {
                drawPositionData = historicalDrawData[drawPosition.getIndex()];
                numberDivideCheckNeeded = null;
            } else {
                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray(drawResults);

                // Now determine which analyze method to apply to results
                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, analyzeMethod);
                drawPositionData = historicalDrawData[drawPosition.getIndex()];
                numberDivideCheckNeeded = null;
            }
        }

        Set<Integer> currentValidNumbers = getLottoNumberGameOutTrackerMap().keySet();
        if (AnalyzeMethod.POSITIONAL_SUMS != analyzeMethod && AnalyzeMethod.GROUP_ANALYSIS != analyzeMethod) {
            sumGroupAnalyzer.analyze(currentValidNumbers, drawPositionData, numberDivideCheckNeeded, lottoGame, analyzeMethod);
        } else {
            sumGroupAnalyzer.analyzeSums(currentValidNumbers, drawPositionData, (AnalyzeMethod.GROUP_ANALYSIS == analyzeMethod));
        }

        if (analyzeMethod.getTitle().equals(AnalyzeMethod.MULTIPLES.getTitle())) {
            // Extract numbers from list that do not meet game out criteria
            Collection<SumGroupAnalyzer> sumGroupAnalyzers = sumGroupAnalyzer.getGroupAnalyzerMap().values();
            lottoNumberGameOutTracker.extractNumbersNotMeetingGameSpanCriteria(sumGroupAnalyzers, getGameSpan());
        }

    }

    private void gameOutIncrementer(int hitCount, Map<Integer, Integer[]> dataStructure) {

        dataStructure.forEach((k, v) -> {

            if (k != hitCount) {
                v[1]++;
            }
        });
    }

    private void performAnalysisOnFirstDigitBasedOnDrawPosition() {

        // Clear map of any previous existing data
        firstDigitValueHolderMap.clear();

        // first determine which data we need to access out of the given options and then retrieve that data
        final int[][] analysisMethodDrawData;

        if (dayOfWeek == DayOfWeek.ALL) {
            analysisMethodDrawData = (int[][]) ((AnalyzeMethod.MULTIPLES == analyzeMethod ||
                    AnalyzeMethod.REMAINDER == analyzeMethod || AnalyzeMethod.POSITIONAL_SUMS == analyzeMethod
                    || AnalyzeMethod.GROUP_ANALYSIS == analyzeMethod) ?
                    lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex()) :
                    lottoDrawData.get(analyzeMethod.getIndex()));
        } else {
            List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                    .collect(Collectors.toList());

            int[][] convertedData = Drawing.convertDrawDataTo2DArray(drawResults);

            AnalyzeMethod method = (analyzeMethod == AnalyzeMethod.MULTIPLES ||
                    analyzeMethod == AnalyzeMethod.REMAINDER ||
                    analyzeMethod == AnalyzeMethod.POSITIONAL_SUMS ||
                    analyzeMethod == AnalyzeMethod.GROUP_ANALYSIS) ? AnalyzeMethod.DRAW_POSITION : analyzeMethod;

            analysisMethodDrawData = filterDataBasedOnAnalyzeMethod(convertedData, method);
        }

        // now we need to get the correct draw index from the 2D array
        final int[] positionData = analysisMethodDrawData[drawPosition.getIndex()];

        int digit;
        for (int i = 0; i < positionData.length; i++) {
            final String numAsString = positionData[i] + "";
            if (numAsString.length() == 1) {
                digit = 0;
            } else {
                digit = Character.getNumericValue(numAsString.charAt(0));
            }

            // plug value into first Digit value holder map
            if (!firstDigitValueHolderMap.containsKey(digit)) {
                firstDigitValueHolderMap.put(digit, new Integer[]{1, 0});
                gameOutIncrementer(digit, firstDigitValueHolderMap);
            } else {
                Integer[] hitAndGameOutData = firstDigitValueHolderMap.get(digit);
                hitAndGameOutData[0]++;
                hitAndGameOutData[1] = 0;
                gameOutIncrementer(digit, firstDigitValueHolderMap);
            }
        }

        final List<Map.Entry<Integer, Integer[]>> sortedList = new ArrayList<>(firstDigitValueHolderMap.entrySet());
        sortedList.sort(hitComparatory);

        firstDigitValueHolderMap.clear();
        sortedList.forEach(val -> {
            firstDigitValueHolderMap.put(val.getKey(), val.getValue());
        });

    }

    private void computeGameOutForLottoNumbers() {

        // Assign the current analysis to the draw Data
        if (AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle())
                || AnalyzeMethod.REMAINDER == analyzeMethod
                || AnalyzeMethod.POSITIONAL_SUMS == analyzeMethod
                || AnalyzeMethod.GROUP_ANALYSIS == analyzeMethod && dayOfWeek == DayOfWeek.ALL) {

            historicalDrawData = (int[][]) lottoDrawData.get(AnalyzeMethod.DRAW_POSITION.getIndex());
        } else if (AnalyzeMethod.MULTIPLES.getTitle().equals(analyzeMethod.getTitle()) || AnalyzeMethod.REMAINDER.getTitle().equals(analyzeMethod.getTitle())
                || AnalyzeMethod.POSITIONAL_SUMS.getTitle().equals(analyzeMethod.getTitle()) || AnalyzeMethod.GROUP_ANALYSIS == analyzeMethod
                && dayOfWeek != DayOfWeek.ALL) {
            List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                    .collect(Collectors.toList());

            int[][] convertedData = Drawing.convertDrawDataTo2DArray(drawResults);

            historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, AnalyzeMethod.DRAW_POSITION);
        } else {

            if (dayOfWeek == DayOfWeek.ALL) {
                historicalDrawData = (int[][]) lottoDrawData.get(analyzeMethod.getIndex());
            } else {
                List<Drawing> drawResults = lottoGame.getDrawingData().stream().filter(game -> game.getDrawDate().contains(dayOfWeek.getDay()))
                        .collect(Collectors.toList());

                int[][] convertedData = Drawing.convertDrawDataTo2DArray(drawResults);

                historicalDrawData = filterDataBasedOnAnalyzeMethod(convertedData, analyzeMethod);
            }
        }

        lottoNumberGameOutTracker.analyzeGamesOutForLottoNumbers(historicalDrawData, lottoGame);
        lottoNumberGameOutTracker.analyzePositionalHitsAndGamesOut(historicalDrawData[drawPosition.getIndex()]);
        lottoNumberGameOutTracker.getLottoNumbersBasedOnGameSpan(gameSpan);
        lottoNumberGameOutTracker.analyzePositionalHitsAndGamesOut(historicalDrawData[drawPosition.getIndex()]);

        lottoNumberGameOutTrackerMap = lottoNumberGameOutTracker.getLottoNumberGameOutInfoMap();
    }

    public List<Integer> extractDefaultResults() {

        for (Map.Entry<Integer[], SumGroupAnalyzer> entry : sumGroupAnalyzer.getGroupAnalyzerMap().entrySet()) {
            return entry.getValue().getLottoNumberInSumRangeHolder();
        }

        return null;
    }

    public List<Integer> findGapsBetweenWinningNumbers(List<Integer> integers) {

        List<Integer> gapHolder = new ArrayList<>();

        for (int i = 1; i < integers.size(); i++) {

            int dif = Math.abs(integers.get(i - 1) - integers.get(i));
            gapHolder.add(dif);

        }

        return gapHolder;
    }

    public List<Integer> getLottoNumbersInSumRangeHolder( String value ){

        for(Map.Entry<Integer[], SumGroupAnalyzer> entry : getSumGroupAnalyzer().getGroupAnalyzerMap().entrySet()) {

            Set<Integer> numericValues = new HashSet<>(entry.getValue().getLottoNumberInSumRangeHolder());
            if (Arrays.toString(numericValues.toArray()).equals(value)) {
                return  entry.getValue().getLottoNumberInSumRangeHolder();

            }

        }

        return null;
    }
}
