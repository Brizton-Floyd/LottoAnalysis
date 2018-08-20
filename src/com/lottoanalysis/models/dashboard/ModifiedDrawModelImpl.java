package com.lottoanalysis.models.dashboard;

import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

public class ModifiedDrawModelImpl extends DrawModelBase implements ModifiedDrawModel{

    private Drawing drawing;
    private CrudOperation crudOperation;

    public ModifiedDrawModelImpl(Drawing drawing){
        this.drawing = drawing;
    }

    @Override
    public String getDrawNumber() {
        return drawing.drawNumberProperty().get();
    }

    @Override
    public int getId() {
        return drawing.drawIdProperty().get();
    }

    @Override
    public String getDrawDate() {
        return drawing.drawDateProperty().get();
    }

    @Override
    public ObservableList<StringProperty> getDrawPositions() {
        return FXCollections.observableArrayList( drawing.getDrawNumbers() );
    }

    @Override
    public void setUpdateMethod(CrudOperation crudOperation) {
        this.crudOperation = crudOperation;

        switch (crudOperation){
            case DELETE:
                onModelChange("delete");
                break;
            case UPDATE:
                onModelChange("list");
                break;
        }
    }

    public CrudOperation getCrudOperation() {
        return crudOperation;
    }

    @Override
    public void updateList(Map<Integer, String> valAndKeys) {

        ObservableList<StringProperty> data = getDrawPositions();

        for(Map.Entry<Integer,String> entry : valAndKeys.entrySet()){

            data.get(entry.getKey()).set(entry.getValue());

        }

        //onModelChange("list");
    }
}
