package com.lottoanalysis.ui.homeview;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public interface HomeViewListener {

    AnchorPane onGameLoad();

    void executeBetSlipAnalysis();
}
