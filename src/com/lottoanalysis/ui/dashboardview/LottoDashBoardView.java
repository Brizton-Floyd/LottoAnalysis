package com.lottoanalysis.ui.dashboardview;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public interface LottoDashBoardView {

    //void subscribeListener(LottoDashBoardListener lottoDashBoardListener);

    void initialize();

    void setGameNameToLabel(String name);

    void setJackPotLabel( String jackpot );

    AnchorPane getView();

    void injectDataIntoTable(ObservableList<Drawing> lottoDrawData);

    void tableViewRenabled();
}
