package com.lottoanalysis.ui.homeview;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public interface HomeView {

    void injectView(AnchorPane pane);

    AnchorPane getView();

    void enableButtonAndDisableDashboardButton();
}
