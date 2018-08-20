package com.lottoanalysis.models.drawhistory;

import java.util.ArrayList;
import java.util.List;

public abstract class DrawModelBase {

    private List<ModelChanged> listeners = new ArrayList<>();

    public void addListener(ModelChanged drawHistoryModelChanged){
        if(!listeners.contains(drawHistoryModelChanged)) {
            listeners.add(0, drawHistoryModelChanged);
        }
    }

    public void onModelChange(String value){

        listeners.forEach(listener -> listener.handleOnModelChanged( value ));
    }
}
