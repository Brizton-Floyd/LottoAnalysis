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

public class ModifiedDrawPresenter implements ModifiedDrawListener {

    private ModifiedDrawView modifiedDrawView;
    private ModifiedDrawModel modifiedDrawModel;
    private LottoDashBoardListener lottoDashBoardPresenter;
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
    public void invokeService(CrudOperation crudOperation) {

        LottoDashboardService lottoDashboardService = new LottoDashboardServiceImpl(modifiedDrawModel,new LottoDashboardRepositoryImpl());

        if(CrudOperation.UPDATE == crudOperation) {

            if (lottoDashboardService.executeUpdate()) {

                lottoDashBoardPresenter.reloadViewAfterUpdate();
                stage.close();
            }

        }
        else if(CrudOperation.DELETE == crudOperation){

            lottoDashboardService.executeDelete();
            lottoDashBoardPresenter.reloadViewAfterDelete();
            stage.close();
        }
    }

    @Override
    public void setListener(LottoDashBoardListener listener) {
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
