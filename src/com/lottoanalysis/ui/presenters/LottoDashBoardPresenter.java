package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.dashboard.ModifiedDrawModelImpl;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardView;
import com.lottoanalysis.ui.modifieddrawview.ModifiedDrawViewImpl;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public class LottoDashBoardPresenter implements LottoDashBoardListener{

    private LottoDashBoardView lottoDashBoardView;
    private LottoGame dashboardModel;
    private HomeViewPresenter homeViewListener;

    LottoDashBoardPresenter(LottoDashBoardView lottoDashBoardView, LottoGame dashboardModel){

        this.lottoDashBoardView = lottoDashBoardView;
        this.dashboardModel = dashboardModel;
        lottoDashBoardView.subscribeListener( (this) );

        performViewStartUp();

    }

    @Override
    public void handleViewEvent(String operation) {

        switch (operation){
            case "inject":
                injectLottoDrawData();
                break;
        }
    }


    void setListener(HomeViewPresenter homeViewListener) {
        this.homeViewListener = homeViewListener;
    }

    @Override
    public void notifyOfCompletion() {
        homeViewListener.reloadDashBoard();
    }

    @Override
    public void loadEditableDrawView(Drawing drawing) {

        ModifiedDrawPresenter modifiedDrawPresenter = new ModifiedDrawPresenter(new ModifiedDrawViewImpl(),
                                                                                new ModifiedDrawModelImpl(drawing));
        modifiedDrawPresenter.setListener(this);
        modifiedDrawPresenter.show();

    }

    void renableTableView() {

        lottoDashBoardView.tableViewRenabled();
    }

    private void performViewStartUp() {

        lottoDashBoardView.setGameNameToLabel( this.dashboardModel.getGameName() );
        lottoDashBoardView.setJackPotLabel( this.dashboardModel.getCurrentEstimatedJackpot() );
        lottoDashBoardView.initialize();
    }

    private void injectLottoDrawData() {

        final ObservableList<Drawing> lottoDrawData = dashboardModel.getDrawingData();
        lottoDashBoardView.injectDataIntoTable( lottoDrawData );

    }

    AnchorPane presentView(){
        return lottoDashBoardView.getView();
    }

}
