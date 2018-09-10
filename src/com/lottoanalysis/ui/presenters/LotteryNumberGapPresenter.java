package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.lotterynumbergap.LotteryNumberGapModel;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.ui.lotterynumbergapview.LotteryNumberGapView;
import com.lottoanalysis.ui.presenters.base.BasePresenter;

import java.util.Set;

public class LotteryNumberGapPresenter extends BasePresenter<LotteryNumberGapView, LotteryNumberGapModel> {

    LotteryNumberGapPresenter(LottoGame model) {
        super(new LotteryNumberGapView(), new LotteryNumberGapModel(model));
        getModel().addListener((this));
        getView().setPresenter((this));
        bindToViewElements();
        getView().setUpUi();
        injectDataIntoView();
    }

    @Override
    public void handleOnModelChanged(String property) {

        switch (property){
            case "DrawPosition":
                injectDataIntoView();
                break;
            case"DayOfWeek":
                injectDataIntoView();
                break;
            case"AnalysisMethod":
                injectDataIntoView();
                break;
        }
    }

    public void setDrawPositin(DrawPosition drawPositin){
        getModel().setPosition(Integer.toString( (drawPositin.getIndex() +1)));
    }

    public void setAnalysisMethod(AnalyzeMethod analysisMethod){
        getModel().setAnalysisMethod(analysisMethod.getTitle());
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek){
        getModel().setDayOfWeek(dayOfWeek);
    }

    public int getNumberOfPositionsInGame(){
        return getModel().getLottoGame().getPositionNumbersAllowed();
    }

    public String getGameName(){
        return getModel().getLottoGame().getGameName();
    }

    public Set<String> returnDaysOfWeek() {
        return getModel().getLottoGame()
                .extractDaysOfWeekFromResults( getModel().getLottoGame().getDrawingData());
    }

    private void injectDataIntoView(){
        getView().injectDataIntoTable( getModel().getLotteryNumberGapModelList() );
    }
    private void bindToViewElements(){
        getView().getPositionLabel().textProperty().bind(getModel().positionProperty());
        getView().getAnalysisLabel().textProperty().bind(getModel().analysisMethodProperty());
        getView().getDayOfWeekLabel().textProperty().bind(getModel().dayOfWeekProperty());
    }
}
