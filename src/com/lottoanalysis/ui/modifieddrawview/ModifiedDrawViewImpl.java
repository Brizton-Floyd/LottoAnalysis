package com.lottoanalysis.ui.modifieddrawview;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ModifiedDrawViewImpl extends AnchorPane implements ModifiedDrawView {

    private ModifiedDrawListener modifiedDrawListener;

    public ModifiedDrawViewImpl(){
        setPrefHeight(500);
        setStyle("-fx-background-color:#515B51;");
        setPrefWidth(300);
    }
    @Override
    public void setListener(ModifiedDrawListener modifiedDrawListener) {
        this.modifiedDrawListener = modifiedDrawListener;
    }

    @Override
    public void initializeView(String gamNumber, String drawDate, ObservableList<StringProperty> drawPositions) {

        TextArea textArea = new TextArea();
        textArea.setText(String.format("Game Number: %s\nDraw Date: %s \n",gamNumber,drawDate));

        getChildren().add(textArea);

        AnchorPane.setLeftAnchor(textArea,5.0);
        AnchorPane.setTopAnchor(textArea,5.0);
    }

    @Override
    public AnchorPane getView() {
        return this;
    }
}
