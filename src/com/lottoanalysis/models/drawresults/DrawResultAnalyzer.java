package com.lottoanalysis.models.drawresults;

import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.drawhistory.TotalWinningNumberTracker;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;
import java.util.stream.Collectors;

public class DrawResultAnalyzer extends DrawModelBase implements GamesInPast {

    private int gameSpan;
    private TotalWinningNumberTracker totalWinningNumberTracker;
    private LottoGame lottoGame;
    private DrawPosition drawPosition;
    private List<DrawResultPosition> drawResultPositions = new ArrayList<>();
    // Simple String Property for binding purposes
    private StringProperty drawPositionProperty;

    public DrawResultAnalyzer(LottoGame lottoGame) {
        this.lottoGame = lottoGame;
        this.drawPosition = DrawPosition.POS_ONE;
        gameSpan = 10;
        drawPositionProperty = new SimpleStringProperty("Currently Analyzing Draw Position 1");
        analyze();
    }

    private void analyze() {
        drawResultPositions.clear();
        loadDrawResultPositionList();
        loadHistoricalDrawResultPositionLotteryNumbers();
        analyzeWhichColumnsProducesWinners();
        analyzeHitPatternBasedOnGameSpan();
    }

    public void setDrawPosition(DrawPosition drawPosition) {
        this.drawPosition = drawPosition;
        setDrawPositionProperty((drawPosition.getIndex() + 1) + "");
        clearPositionHashMap();
        analyzeWhichColumnsProducesWinners();
        onModelChange("DrawPosition");
    }

    private void clearPositionHashMap() {
        for (DrawResultPosition drawResultPosition : drawResultPositions) {
            if (drawResultPosition.getDrawPositionColumnMap() != null) {
                drawResultPosition.getDrawPositionColumnMap().clear();
            }
        }
    }

    private void analyzeWhichColumnsProducesWinners() {
        final int gamesToGoBackInHistory = getSpan().getSpanValue();
        List<List<Integer>> lotteryResultList = new ArrayList<>();
        final int len = drawResultPositions.get(0).getLotteryResultPositionList().size();

        for (int i = 0; i < len; i++) {
            List<Integer> drawResult = new ArrayList<>();
            for (int j = 0; j < drawResultPositions.size(); j++) {
                final int winningNumber = drawResultPositions.get(j).getLotteryResultPositionList().get(i);
                drawResult.add(winningNumber);
            }

            if (lotteryResultList.size() == gamesToGoBackInHistory) {
                final int currentWinningNumberInPosition = drawResult.get(drawPosition.getIndex());
                final int[] gameOutAndWinningColumnArray = scanPastResultsForGamesOutAndWinningColumn(lotteryResultList, currentWinningNumberInPosition);
                if (gameOutAndWinningColumnArray[1] == -1) {
                    gameOutAndWinningColumnArray[1] = drawPosition.getIndex();
                }
                DrawResultPosition drawResultPosition = drawResultPositions.get(drawPosition.getIndex());
                drawResultPosition.increasePositionColumnHit(gameOutAndWinningColumnArray, currentWinningNumberInPosition);

                lotteryResultList.remove(0);
                lotteryResultList.add(drawResult);
            } else {
                lotteryResultList.add(drawResult);
            }
        }
    }

    private int[] scanPastResultsForGamesOutAndWinningColumn(List<List<Integer>> lotteryResultList, int currentWinningNumberInPosition) {

        int gamesOut = 0;
        int winningColumn = -1;

        for (int i = lotteryResultList.size() - 1; i >= 0; i--) {
            List<Integer> drawResult = lotteryResultList.get(i);
            if (drawResult.contains(currentWinningNumberInPosition)) {
                winningColumn = drawResult.indexOf(currentWinningNumberInPosition);
                break;
            } else {
                gamesOut++;
            }
        }

        return new int[]{gamesOut, winningColumn};
    }

    private void loadHistoricalDrawResultPositionLotteryNumbers() {
        final int columns = lottoGame.getPositionNumbersAllowed();
        final int rows = lottoGame.getDrawingData().size();

        int[][] allPositionHistoricalDrawData = new int[columns][rows];
        NumberPatternAnalyzer.loadUpPositionalNumbers(allPositionHistoricalDrawData, lottoGame.getDrawingData());

        //int[][] allPositionHistoricalDrawData2 = NumberPatternAnalyzer.loadUpPositionalNumbers(allPositionHistoricalDrawData);

        for (DrawResultPosition drawResultPosition : drawResultPositions) {
            final int drawPositionIndex = drawResultPosition.getDrawPositionIndex();
            final int[] positionDrawResultData = allPositionHistoricalDrawData[drawPositionIndex];
            drawResultPosition.getLotteryResultPositionList().addAll(
                    Arrays.stream(positionDrawResultData).boxed().collect(Collectors.toList())
            );
        }

        totalWinningNumberTracker = new TotalWinningNumberTracker();
        totalWinningNumberTracker.analyze( allPositionHistoricalDrawData, gameSpan );
    }

    public DrawResultPosition getDrawResultPosition(){

        return drawResultPositions.stream()
                .filter(position -> position.getDrawPositionIndex() == drawPosition.getIndex()).findAny().orElse(null);
    }

    /**
     * Method is responsible for loading the list with the amount of needed positions for the given lottery game
     */
    private void loadDrawResultPositionList() {
        final int numberOfPositionsAllowed = lottoGame.getPositionNumbersAllowed();
        for (int i = 0; i < numberOfPositionsAllowed; i++) {
            drawResultPositions.add(new DrawResultPosition(i));
        }
    }

    public StringProperty drawPositionPropertyProperty() {
        return drawPositionProperty;
    }

    private void setDrawPositionProperty(String drawPositionProperty) {
        this.drawPositionProperty.set("Currently Analyzing Draw Position " + drawPositionProperty);
    }

    @Override
    public Span getSpan() {
        return Span.FIFTEEN;
    }

    public List<DrawResultPosition> getDrawResultPositions() {
        return drawResultPositions;
    }

    public int getPositionsAllowed() {
        return lottoGame.getPositionNumbersAllowed();
    }

    public String getGameName() {
        return lottoGame.getGameName();
    }

    public int getGameSpan() {
        return gameSpan;
    }

    public void setGameSpan(int gameSpan) {
        this.gameSpan = gameSpan;
        clearPositionHashMap();
        analyze();
        onModelChange("Span");
    }

    public TotalWinningNumberTracker getTotalWinningNumberTracker() {
        return totalWinningNumberTracker;
    }

    private void refreshDrawResultList(DrawResultPosition drawResultPos, int position) {
        DrawResultPosition drawResultPosition = getDrawResultPositions().get(position);
        if(drawResultPos != null){
            drawResultPos.getLotteryResultPositionList().clear();
            drawResultPos.getLotteryResultPositionList().addAll( filter( drawResultPosition.getLotteryResultPositionList() ) );
        }
    }

    private Collection<? extends Integer> filter(List<Integer> lotteryResultPositionList) {
        List<Integer> data = new ArrayList<>();
        Set<Integer> uniqueValues = new LinkedHashSet<>(lotteryResultPositionList.subList(lotteryResultPositionList.size()-gameSpan, lotteryResultPositionList.size()));
        System.out.println(drawPosition.getIndex());
        for(int val : getDrawResultPositions().get(drawPosition.getIndex()).getLotteryResultPositionList()){
            if(uniqueValues.contains(val)){
                data.add( val );
            }
        }
        return data;
    }

    private void analyzeHitPatternBasedOnGameSpan() {
        refreshDrawResultList(null, drawPosition.getIndex());
    }

    public void refreshDrawResultList(DrawResultPosition position) {
        refreshDrawResultList(position, position.getDrawPositionIndex()-1);
    }
}
