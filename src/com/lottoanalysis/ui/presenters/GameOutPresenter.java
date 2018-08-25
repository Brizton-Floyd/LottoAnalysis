package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DrawModel;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.models.drawhistory.DrawPositions;
import com.lottoanalysis.models.gameout.GameOutModel;
import com.lottoanalysis.ui.gamesoutview.GameOutListener;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;
import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.AnchorPane;

public class GameOutPresenter extends BasePresenter<GameOutViewImpl, GameOutModel> implements GameOutListener {

    GameOutPresenter(GameOutModel lottoGame){
        super(new GameOutViewImpl(), lottoGame);

        getView().setPresenter((this));
        getModel().addListener((this));

        bindToViewElements();
        getView().setUpUi();
    }

    @Override
    public void handleOnModelChanged(String property) {

        switch (property){
            case"drawPosition":
                System.out.println(getModel().getDrawPositions());
                break;
            case"analyzeMethod":
                System.out.println(getModel().getAnalyzeMethod());
                break;
            case"gameRange":
                System.out.println(getModel().getGameRange());
                break;
        }
    }

    @Override
    public void handleViewEvent(String action) {

        switch (action){
            case"load":
                onPageLoad();
                break;
        }
    }

    public void setDrawPosition(DrawPositions drawPosition) {

        getModel().setDrawPositions( drawPosition );
    }

    public void setAnalyzeMethod(AnalyzeMethod analyzeMethod) {
        getModel().setAnalyzeMethod( analyzeMethod );
    }

    public void setGroupRange(String range) {
        getModel().setGameRange(Integer.parseInt(range));
    }

    private void onPageLoad() {

        getView().setGamePositionRange( getModel().getDrawResultSize() );
        getView().setGameName( getModel().getGameName() );
        getView().setGameMaxValue( getModel().getGameMaxValue() );
    }

    private void bindToViewElements() {

        getView().getPositionLabel().textProperty().bind( getModel().drawingPositionProperty() );
    }

    AnchorPane getViewForDisplay(){
        return getView().display();
    }

}
