package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardView;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public class LottoDashBoardPresenter implements LottoDashBoardListener{

    private LottoDashBoardView lottoDashBoardView;
    private LottoGame dashboardModel;

    public LottoDashBoardPresenter(LottoDashBoardView lottoDashBoardView, LottoGame dashboardModel){

        this.lottoDashBoardView = lottoDashBoardView;
        this.dashboardModel = dashboardModel;

        lottoDashBoardView.subscribeListener( (this) );

        performViewStartUp();
    }

    @Override
    public void performViewStartUp() {

        lottoDashBoardView.setGameNameToLabel( this.dashboardModel.getGameName() );
        lottoDashBoardView.setJackPotLabel( this.dashboardModel.getCurrentEstimatedJackpot() );
        lottoDashBoardView.initialize();
    }

    @Override
    public void injectLottoDrawData() {

        final ObservableList<Drawing> lottoDrawData = dashboardModel.getDrawingData();
        lottoDashBoardView.injectDataIntoTable( lottoDrawData );

    }

    public AnchorPane presentView(){
        return lottoDashBoardView.getView();
    }

}
