package com.lottoanalysis.ui.homeview;

import com.lottoanalysis.models.lottogames.LottoGame;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@FunctionalInterface
public interface HomeViewListener {

    void handleViewEvent(EventSource eventSource);
}
