package com.lottoanalysis.models.dashboard;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface ModifiedDrawModel {

    String getDrawNumber();

    String getDrawDate();

    int getId();

    ObservableList<StringProperty> getDrawPositions();



}
