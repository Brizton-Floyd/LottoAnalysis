package com.lottoanalysis.ui.homeview;

import com.lottoanalysis.lottogames.LottoGame;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public interface HomeViewListener {

    AnchorPane onGameLoad();

    void executeBetSlipAnalysis();

    void loadGameDashBoard();

    void invokeDashBoard();

    void loadGameDashBoard(LottoGame lottoGame);
}
