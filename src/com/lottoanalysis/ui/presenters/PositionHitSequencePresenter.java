package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.drawresults.DrawResultAnalyzer;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.ui.positionhitsequenceview.PositionHitSequenceView;
import com.lottoanalysis.ui.presenters.base.BasePresenter;

public class PositionHitSequencePresenter extends BasePresenter<PositionHitSequenceView, DrawResultAnalyzer> {

    PositionHitSequencePresenter(LottoGame lottoGame){
        super(new PositionHitSequenceView(), new DrawResultAnalyzer(lottoGame));
        getModel().addListener((this));
        getView().setPresenter((this));
        bindToUiElements();
        getView().setUpUi();
        reloadViews();
    }

    @Override
    public void handleOnModelChanged(String property) {
        switch (property){
            case "DrawPosition":
                reloadViews();
                break;
        }

    }

    public void setDrawPosition(DrawPosition drawPosition){
        getModel().setDrawPosition( drawPosition );
    }

    public int getAmountOfDrawPositionsAllowed(){
        return getModel().getPositionsAllowed();
    }

    public String getGameName() {
       return getModel().getGameName();
    }

    private void reloadViews(){
        getView().reloadViews( getModel() );
    }
    private void bindToUiElements(){
        getView().getPositionLabel().textProperty().bind(getModel().drawPositionPropertyProperty());
    }
}
