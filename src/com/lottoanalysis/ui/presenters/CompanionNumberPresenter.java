package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.companionnumber.CompanionNumber;
import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.ui.companionnumberview.CompanionNumberView;
import com.lottoanalysis.ui.presenters.base.BasePresenter;

import java.util.Set;

public class CompanionNumberPresenter extends BasePresenter<CompanionNumberView, CompanionNumber> {

    CompanionNumberPresenter(CompanionNumber companionNumber){
        super(new CompanionNumberView(), companionNumber);
        getModel().addListener((this));
        getView().setPresenter((this));
        bindToViewElements();
        getView().setUpUi();

    }

    @Override
    public void handleOnModelChanged(String property) {

    }

    public int getNumberOfPositionsInGame() {
        return getModel().getLottoGame().getPositionNumbersAllowed();
    }

    public void setDrawPosition(DrawPosition position) {
        getModel().setPosition(position);
    }

    public void setGameSpan(int gameSpan) {
        getModel().setGameSpan(gameSpan);
        getModel().setGameSpanProperty(getModel().getGameSpan()+"");
    }

    public String getGameName() {
        return getModel().getLottoGame().getGameName();
    }

    public void setAnalysisMethod(AnalyzeMethod analyzeMethod) {
        getModel().setAnalysisMethod(analyzeMethod);
    }

    public Set<String> returnDaysOfWeek() {
        return getModel().getLottoGame()
                .extractDaysOfWeekFromResults( getModel().getLottoGame().getDrawingData());    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        getModel().setDayOfWeek(dayOfWeek);

    }

    private void bindToViewElements(){
        getView().getPositionLabel().textProperty().bind(getModel().positionProperty());
        getView().getAnalysisLabel().textProperty().bind(getModel().analysisMethodProperty());
        getView().getDayOfWeekLabel().textProperty().bind(getModel().dayOfWeekProperty());
        getView().getGameSpanLabel().textProperty().bind(getModel().gameSpanPropertyProperty());
    }
}
