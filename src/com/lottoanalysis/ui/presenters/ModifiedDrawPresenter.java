package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.dashboard.ModifiedDrawModel;
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

    public ModifiedDrawPresenter(ModifiedDrawView modifiedDrawView, ModifiedDrawModel modifiedDrawModel){

        this.modifiedDrawView = modifiedDrawView;
        this.modifiedDrawModel = modifiedDrawModel;

        modifiedDrawView.initializeView(modifiedDrawModel.getDrawNumber(),
                                        modifiedDrawModel.getDrawDate(),
                                        modifiedDrawModel.getDrawPositions());

    }

    @Override
    public void setListener(LottoDashBoardListener listener) {
        this.lottoDashBoardPresenter = listener;
    }

    @Override
    public void show() {

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setTitle("Draw Update");

        Scene scene = new Scene(modifiedDrawView.getView());
        stage.setScene(scene);

        stage.show();

        stage.setOnHiding( event -> {
            lottoDashBoardPresenter.renableTableView();
        });
    }
}
