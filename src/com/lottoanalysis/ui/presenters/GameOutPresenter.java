package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.models.drawhistory.DrawPositions;
import com.lottoanalysis.models.gameout.GameOutModel;
import com.lottoanalysis.ui.gamesoutview.GameOutListener;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;

public class GameOutPresenter implements GameOutListener {

    private GameOutModel gameOutModel;
    private GameOutViewImpl gameOutView;

    public GameOutPresenter(GameOutModel gameOutModel, GameOutViewImpl gameOutView){

        this.gameOutModel = gameOutModel;
        this.gameOutView = gameOutView;

        gameOutView.setListener( this );
        gameOutView.initializeViewElements();
    }

    @Override
    public void onDrawPositionChange(DrawPositions drawPosition) {

        gameOutModel.setDrawPosition( drawPosition );
        gameOutView.setDrawposition( gameOutModel.getDrawPosition().getIndex()  );
    }

    @Override
    public void onPageLoad() {

        gameOutView.setGamePositionRange( gameOutModel.getDrawSize() );
        gameOutView.setGameName( Drawing.splitGameName(gameOutModel.getGameName()) );
        gameOutView.setGameMaxValue( gameOutModel.getGameMaxValue() );
    }

    public GameOutViewImpl getViewForDisplay(){
        return gameOutView;
    }
}
