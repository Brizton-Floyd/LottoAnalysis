package com.lottoanalysis.ui.homeview;

import com.lottoanalysis.models.lottogames.LottoGame;
import javafx.scene.layout.AnchorPane;

public interface HomeViewListener {

    AnchorPane onGameLoad();

    void executeBetSlipAnalysis();

    void loadGameDashBoard();

    void invokeDashBoard();

    void loadGameDashBoard(LottoGame lottoGame);
}
