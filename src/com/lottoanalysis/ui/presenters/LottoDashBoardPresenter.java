package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.dashboard.ModifiedDrawModelImpl;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardViewImpl;
import com.lottoanalysis.ui.modifieddrawview.ModifiedDrawViewImpl;
import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public class LottoDashBoardPresenter extends BasePresenter<LottoDashBoardViewImpl, LottoGame> implements LottoDashBoardListener{

    LottoDashBoardPresenter(LottoDashBoardViewImpl lottoDashBoardView, LottoGame dashboardModel){
        super(lottoDashBoardView, dashboardModel);

        getView().setPresenter( (this) );

        performViewStartUp();

    }

    @Override
    public void handleOnModelChanged(String property) {
        switch (property){

            case "list":
            case"delete":
                getModel().onModelChange("list");
                renableTableView();
                break;
        }
    }

    @Override
    public void handleViewEvent(String operation, Drawing drawing) {

        switch (operation){
            case "inject":
                injectLottoDrawData();
                break;
            case"showView":
                loadEditableDrawView(drawing);
                break;
        }
    }

    private void loadEditableDrawView(Drawing drawing) {

        ModifiedDrawModelImpl modifiedDrawModel = new ModifiedDrawModelImpl(drawing);
        modifiedDrawModel.addListener(this);

        ModifiedDrawPresenter modifiedDrawPresenter = new ModifiedDrawPresenter(new ModifiedDrawViewImpl(),
                                                                                modifiedDrawModel);

        modifiedDrawPresenter.setListener(this);
        modifiedDrawPresenter.show();

    }

    void renableTableView() {

        getView().tableViewRenabled();
    }

    private void performViewStartUp() {

        getView().setGameNameToLabel( getModel().getGameName() );
        getView().setJackPotLabel( getModel().getCurrentEstimatedJackpot() );
        getView().initialize();
    }

    private void injectLottoDrawData() {

        final ObservableList<Drawing> lottoDrawData = getModel().getDrawingData();
        getView().injectDataIntoTable( lottoDrawData );

    }

    AnchorPane presentView(){
        return getView().display();
    }

}
