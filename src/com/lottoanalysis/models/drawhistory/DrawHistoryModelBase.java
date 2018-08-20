package com.lottoanalysis.models.drawhistory;

import java.util.ArrayList;
import java.util.List;

public abstract class DrawHistoryModelBase {

    private List<DrawHistoryModelChanged> listerners = new ArrayList<>();

    public void addListener(DrawHistoryModelChanged drawHistoryModelChanged){
        listerners.add( drawHistoryModelChanged );
    }

    void onModelChange(String value){

        listerners.forEach(listener -> listener.handleOnModelChanged( value ));
    }
}
