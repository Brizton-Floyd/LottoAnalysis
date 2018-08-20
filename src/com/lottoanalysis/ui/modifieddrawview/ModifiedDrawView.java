package com.lottoanalysis.ui.modifieddrawview;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public interface ModifiedDrawView {

    void initializeView(String gamNumber, String drawDate, ObservableList<StringProperty> drawPositions);

    AnchorPane getView();
}
