package com.lottoanalysis.ui.presenters;

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
                break;
        }
    }

    @Override
    public void handleViewEvents(String action) {

        switch (action){
            case"load":
                onPageLoad();
                break;
        }
    }

    @Override
    public void onDrawPositionChange(DrawPositions drawPosition) {

        getModel().setDrawPositions( drawPosition );
    }

    private void onPageLoad() {

        getView().setGamePositionRange( getModel().getDrawResultSize() );
        getView().setGameName( getModel().getGameName() );
        getView().setGameMaxValue( 39 );
    }

    private void bindToViewElements() {

        getView().getPositionLabel().textProperty().bind( getModel().drawingPositionProperty() );
    }

    AnchorPane getViewForDisplay(){
        return getView().display();
    }
}
