package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.dashboard.ModifiedDrawModelImpl;
import com.lottoanalysis.services.dashboardservices.LottoDashboardRepositoryImpl;
import com.lottoanalysis.services.dashboardservices.LottoDashboardService;
import com.lottoanalysis.services.dashboardservices.LottoDashboardServiceImpl;
import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import com.lottoanalysis.ui.modifieddrawview.ModifiedDrawListener;
import com.lottoanalysis.ui.modifieddrawview.ModifiedDrawViewImpl;
import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Map;

public class ModifiedDrawPresenter extends BasePresenter<ModifiedDrawViewImpl, ModifiedDrawModelImpl> implements ModifiedDrawListener {

    private LottoDashBoardPresenter lottoDashBoardPresenter;
    private Stage stage = new Stage(StageStyle.DECORATED);

    ModifiedDrawPresenter(ModifiedDrawViewImpl modifiedDrawView, ModifiedDrawModelImpl modifiedDrawModel){

        super(modifiedDrawView, modifiedDrawModel);

        getView().setPresenter( (this) );
        getModel().addListener((this));

        modifiedDrawView.initializeView(modifiedDrawModel.getDrawNumber(),
                                        modifiedDrawModel.getDrawDate(),
                                        modifiedDrawModel.getDrawPositions());

    }

    @Override
    public void handleOnModelChanged(String property) {

        switch (property){

            case "list":
                invokeService(getModel().getCrudOperation());
                break;
            case"delete":
                invokeService(getModel().getCrudOperation());
                break;
        }
    }

    @Override
    public void updateCrudeOpertaion(CrudOperation crudOperation) {
        getModel().setUpdateMethod( crudOperation );
    }

    @Override
    public void updateModelList(Map<Integer, String> valAndKeys) {
        getModel().updateList( valAndKeys );
    }

    private void invokeService(CrudOperation crudOperation) {

        LottoDashboardService lottoDashboardService = new LottoDashboardServiceImpl(getModel(),new LottoDashboardRepositoryImpl());

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

    void setListener(LottoDashBoardPresenter listener) {
        this.lottoDashBoardPresenter = listener;
    }

    @Override
    public void show() {

        stage.setResizable(false);
        stage.setTitle("Update Drawing");

        Scene scene = new Scene(getView().display());
        stage.setScene(scene);

        stage.show();

        stage.setOnHiding( e -> lottoDashBoardPresenter.renableTableView() );

    }
}
