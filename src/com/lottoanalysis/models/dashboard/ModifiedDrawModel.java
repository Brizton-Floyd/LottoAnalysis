package com.lottoanalysis.models.dashboard;

import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.Map;

public interface ModifiedDrawModel{

    String getDrawNumber();

    String getDrawDate();

    int getId();

    ObservableList<StringProperty> getDrawPositions();

    void updateList(Map<Integer, String> valAndKeys);

    void setUpdateMethod(CrudOperation crudOperation);
}
