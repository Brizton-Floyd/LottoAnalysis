package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.dashboard.ModifiedDrawModel;
import com.lottoanalysis.services.dashboardservices.LottoDashboardRepositoryImpl;
import com.lottoanalysis.services.dashboardservices.LottoDashboardService;
import com.lottoanalysis.services.dashboardservices.LottoDashboardServiceImpl;
import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;
import com.lottoanalysis.ui.modifieddrawview.ModifiedDrawListener;
import com.lottoanalysis.ui.modifieddrawview.ModifiedDrawView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Map;

import static com.lottoanalysis.services.dashboardservices.enums.CrudOperation.*;

public class ModifiedDrawPresenter implements ModifiedDrawListener {

    private ModifiedDrawView modifiedDrawView;
    private ModifiedDrawModel modifiedDrawModel;
    private LottoDashBoardPresenter lottoDashBoardPresenter;
    private Stage stage = new Stage(StageStyle.DECORATED);

    public ModifiedDrawPresenter(ModifiedDrawView modifiedDrawView, ModifiedDrawModel modifiedDrawModel){

        this.modifiedDrawView = modifiedDrawView;
        this.modifiedDrawModel = modifiedDrawModel;

        modifiedDrawView.initializeView(modifiedDrawModel.getDrawNumber(),
                                        modifiedDrawModel.getDrawDate(),
                                        modifiedDrawModel.getDrawPositions());

        modifiedDrawView.setListener(this);

    }

    @Override
    public void handleViewEvent(CrudOperation crudOperation) {
        switch (crudOperation){

            case READ:
                break;
            case UPDATE:
                invokeService(UPDATE);
                lottoDashBoardPresenter.notifyOfCompletion();
                break;
            case CREATE:
                break;
            case DELETE:
                invokeService(DELETE);
                lottoDashBoardPresenter.notifyOfCompletion();
                break;
        }
    }

    @Override
    public void updateModelList(Map<Integer, String> valAndKeys) {
        modifiedDrawModel.updateList( valAndKeys );
    }

    private void invokeService(CrudOperation crudOperation) {

        LottoDashboardService lottoDashboardService = new LottoDashboardServiceImpl(modifiedDrawModel,new LottoDashboardRepositoryImpl());

        if(CrudOperation.UPDATE == crudOperation) {

            if (lottoDashboardService.executeUpdate()) {
                stage.close();
            }

        }
        else if(CrudOperation.DELETE == crudOperation){

            lottoDashboardService.executeDelete();
            stage.close();
        }
    }

    public void setListener(LottoDashBoardPresenter listener) {
        this.lottoDashBoardPresenter = listener;
    }

    @Override
    public void show() {

        stage.setResizable(false);
        stage.setTitle("Update Drawing");

        Scene scene = new Scene(modifiedDrawView.getView());
        stage.setScene(scene);

        stage.show();

        stage.setOnHiding( event -> {
            lottoDashBoardPresenter.renableTableView();
        });
    }
}
