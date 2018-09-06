package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.gameout.GameOutModel;
import com.lottoanalysis.ui.gamesoutview.GameOutListener;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;
import com.lottoanalysis.ui.gamesoutview.cells.GroupRangeGameOutViewCell;
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

        GroupRangeGameOutViewCell.processed = false;

        switch (property){
            case"drawPosition":
                getModel().reAnalyzeData();
                getModel().setRangeIndex( getModel().getGameOutRange().getRangeIndex() );
                loadViews();
                break;
            case"analyzeMethod":
                getModel().reAnalyzeData();
                getModel().setRangeIndex( getModel().getGameOutRange().getRangeIndex() );
                loadViews();
                break;
            case"gameRange":
                getModel().reAnalyzeData();
                getModel().setRangeIndex( getModel().getGameOutRange().getRangeIndex() );
                loadViews();
                break;
            case"groupRange":
                loadGameOutView();
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

    public void setRangeIndex(int rangeIndex) {
        getModel().setRangeIndex( rangeIndex );
    }
    private void onPageLoad() {

        getView().setGamePositionRange( getModel().getDrawResultSize() );
        getView().setGameName( getModel().getGameName() );
        getView().setGameMaxValue( getModel().getGameMaxValue() );
        getView().setWinningDrawNumbers( getModel().getCurrentWinningNumbers());
        getModel().analyze();
    }

    private void loadGameOutView() {
        //loadViews();
        getView().populateGameOutStackPane( getModel().getGameOutRange() );
    }

    private void loadViews() {

        getView().populateRangeTableView( getModel().getGroupRange() );
    }

    private void bindToViewElements() {

        getView().getPositionLabel().textProperty().bind( getModel().drawingPositionProperty() );
        getView().getAnalysisLabel().textProperty().bind( getModel().analysisMethProperty() );
    }

    AnchorPane getViewForDisplay(){
        return getView().display();
    }

}
