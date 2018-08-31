package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.gameout.GameOutModel;
import com.lottoanalysis.ui.gamesoutview.GameOutListener;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;
import com.lottoanalysis.ui.presenters.base.BasePresenter;
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
                getModel().reAnalyzeData();
                loadViews();
                System.out.println(getModel().getDrawPosition());
                break;
            case"analyzeMethod":
                getModel().reAnalyzeData();
                loadViews();
                System.out.println(getModel().getAnalyzeMethod());
                break;
            case"gameRange":
                getModel().reAnalyzeData();
                loadViews();
                break;
        }
    }

    @Override
    public void handleViewEvent(String action) {

        switch (action){
            case"load":
                onPageLoad();
                break;
            case"loadViews":
                loadViews();
                break;
        }
    }

    public void setDrawPosition(DrawPosition drawPosition) {

        getModel().setDrawPosition( drawPosition );
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
        getModel().analyze();

    }


    private void loadViews() {

        getView().populateRangeTableView( getModel().getGroupRange() );
    }

    private void bindToViewElements() {

        getView().getPositionLabel().textProperty().bind( getModel().drawingPositionProperty() );
    }

    AnchorPane getViewForDisplay(){
        return getView().display();
    }

}
