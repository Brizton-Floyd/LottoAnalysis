package com.lottoanalysis.ui.homeview;

import com.lottoanalysis.models.lottogames.LottoGame;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public interface HomeViewListener {

    AnchorPane onGameLoad();

    void executeBetSlipAnalysis();

    void presentLottoGameSelectionView();

    void loadGameDashBoard();

    void invokeDashBoard();

    void loadGameDashBoard(LottoGame lottoGame);

    void loadGameDashBoard(int id);

    void reloadViewsBasedOnId(Integer integer, Stage stage);
}
