package com.lottoanalysis.models.companionnumber;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.models.lottogames.LottoGame;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanionNumber extends DrawModelBase {

    private StringProperty position, analysisMethod, dayOfWeek,gameSpanProperty;
    private LottoGame lottoGame;
    private TotalWinningNumberAnalyzer totalWinningNumberAnalyzer;
    private LotteryNumberRange lotteryNumberRange;
    private AnalyzeMethod analyzeMethod;
    private DayOfWeek weekDay;
    private DrawPosition drawPosition;
    private int gameSpan;

    public CompanionNumber(LottoGame lottoGame){
        this.lottoGame = lottoGame;
        position = new SimpleStringProperty(("Currently Analyzing Position 1"));
        analysisMethod = new SimpleStringProperty(("Analysis Method: Positional Numbers"));
        dayOfWeek = new SimpleStringProperty(("Day Of Week: All Days"));
        gameSpanProperty = new SimpleStringProperty(("Current Game Out Span: 10"));
        gameSpan = 10;
        weekDay = DayOfWeek.ALL;
        analyzeMethod = AnalyzeMethod.DRAW_POSITION;
        drawPosition = DrawPosition.POS_ONE;
        analyze();
    }

    public LottoGame getLottoGame() {
        return lottoGame;
    }

    public LotteryNumberRange getLotteryNumberRange() {
        return lotteryNumberRange;
    }

    public TotalWinningNumberAnalyzer getTotalWinningNumberAnalyzer() {
        return totalWinningNumberAnalyzer;
    }

    public void setPosition(DrawPosition drawPosition) {
        this.position.set("Currently Analyzing Position " + Integer.toString( (drawPosition.getIndex() +1)));
        this.drawPosition = drawPosition;
        analyze();
        onModelChange("field");
    }

    public int getDrawPosition() {
        return drawPosition.getIndex() +1;
    }

    public int getGameSpan() {
        return gameSpan;
    }

    public void setGameSpan(int gameSpan) {
        this.gameSpan = gameSpan;
    }

    public StringProperty gameSpanPropertyProperty() {
        return gameSpanProperty;
    }

    public void setGameSpanProperty(String gameSpanProperty) {
        this.gameSpanProperty.set("Current Game Out Span: " + gameSpanProperty);
        analyze();
        onModelChange("field");

    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public StringProperty analysisMethodProperty() {
        return analysisMethod;
    }

    public void setAnalysisMethod(AnalyzeMethod analysisMethod) {
        this.analysisMethod.set("Analysis Method: " + analysisMethod.getTitle());
        this.analyzeMethod = analysisMethod;
        analyze();
        onModelChange("field");
    }

    public String getDayOfWeek() {
        return dayOfWeek.get();
    }

    public StringProperty dayOfWeekProperty() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek.set("Day Of Week: " + dayOfWeek.getFullDayName());
        this.weekDay = dayOfWeek;
        analyze();
        onModelChange("field");

    }

    public void setRangeWinningNumber(int rangeWinningNumber) {
        lotteryNumberRange.setDrawPosition(drawPosition);
        lotteryNumberRange.setRangeWinningNumber(rangeWinningNumber);
        lotteryNumberRange.redetermineCompanionHits( rangeWinningNumber );
        totalWinningNumberAnalyzer.setLottoNumber( lotteryNumberRange.getRangeWinningNumber() );
        totalWinningNumberAnalyzer.analyzeDrawDataVersionTwo();

        onModelChange("Companion");
    }

    private void analyze(){
        beginTotalWinningNumberAnalysisProcess();
        beginLotteryNumberRangeAnalysisProcess();
    }

    private void beginLotteryNumberRangeAnalysisProcess() {
        Range.resetIndex();

        if(lotteryNumberRange == null)
            lotteryNumberRange = new LotteryNumberRange(getLottoGame(),2);
        else{
            lotteryNumberRange.resetRangeHitData();
        }
        final int[][] historicalDrawData = totalWinningNumberAnalyzer.getHistoricalDrawData();
        lotteryNumberRange.setHistoricalData( historicalDrawData );
        lotteryNumberRange.setCurrentWinningLottoNumber( totalWinningNumberAnalyzer.getLottoNumber());
        lotteryNumberRange.setDrawPosition( drawPosition );
        lotteryNumberRange.analyze();
    }

    private void beginTotalWinningNumberAnalysisProcess() {
        if(totalWinningNumberAnalyzer == null)
            totalWinningNumberAnalyzer = new TotalWinningNumberAnalyzer();

        totalWinningNumberAnalyzer.setDrawingList(getLottoGame());
        totalWinningNumberAnalyzer.setDayOfWeek( weekDay );
        totalWinningNumberAnalyzer.setGameOutValue( gameSpan );
        totalWinningNumberAnalyzer.setAnalyzeMethod( analyzeMethod );
        totalWinningNumberAnalyzer.setDrawPosition( drawPosition );
        totalWinningNumberAnalyzer.analyzeDrawData();
    }
}
