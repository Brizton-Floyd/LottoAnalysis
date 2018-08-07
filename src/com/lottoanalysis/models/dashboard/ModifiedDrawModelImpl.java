package com.lottoanalysis.models.dashboard;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class ModifiedDrawModelImpl implements ModifiedDrawModel{

    private Drawing drawing;

    public ModifiedDrawModelImpl(Drawing drawing){
        this.drawing = drawing;
    }

    @Override
    public String getDrawNumber() {
        return drawing.drawNumberProperty().get();
    }

    @Override
    public String getDrawDate() {
        return drawing.drawDateProperty().get();
    }

    @Override
    public ObservableList<StringProperty> getDrawPositions() {
        return null;
    }
}
