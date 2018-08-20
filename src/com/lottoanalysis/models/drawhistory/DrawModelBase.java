package com.lottoanalysis.models.drawhistory;

import java.util.ArrayList;
import java.util.List;

public abstract class DrawModelBase {

    private List<ModelChanged> listeners = new ArrayList<>();

    public void addListener(ModelChanged drawHistoryModelChanged){
        listeners.add( drawHistoryModelChanged );
    }

    void onModelChange(String value){

        listeners.forEach(listener -> listener.handleOnModelChanged( value ));
    }
}
