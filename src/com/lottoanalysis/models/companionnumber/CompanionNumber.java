package com.lottoanalysis.models.companionnumber;

import com.lottoanalysis.models.drawhistory.*;
import com.lottoanalysis.models.lottogames.LottoGame;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanionNumber extends DrawModelBase {

    private StringProperty position, analysisMethod, dayOfWeek,gameSpanProperty;
    private LottoGame lottoGame;
    private TotalWinningNumberAnalyzer totalWinningNumberAnalyzer;
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
        analyze();
    }

    public LottoGame getLottoGame() {
        return lottoGame;
    }

    public void setPosition(DrawPosition drawPosition) {
        this.position.set("Currently Analyzing Position " + Integer.toString( (drawPosition.getIndex() +1)));
        this.drawPosition = drawPosition;
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

    }

    public void analyze(){

    }
}
