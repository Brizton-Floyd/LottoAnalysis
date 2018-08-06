package com.lottoanalysis.ui.gameselection;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public interface GameSelectionView {

    void initializeListener(GameSelectionViewListener gameSelectionViewListener);

    void initializeView();

    void setMenuBarItems(List<String> values);

    AnchorPane view();

    void bindToProgressAndMessage(ReadOnlyDoubleProperty readOnlyDoubleProperty, ReadOnlyStringProperty readOnlyStringProperty);

    void bindToMessage(ReadOnlyStringProperty readOnlyStringProperty);
}
