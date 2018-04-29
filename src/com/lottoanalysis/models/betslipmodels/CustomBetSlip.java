package com.lottoanalysis.models.betslipmodels;

import javafx.beans.property.SimpleStringProperty;

public class CustomBetSlip {

    private SimpleStringProperty gameOutValue;

    public CustomBetSlip(String value){

        gameOutValue = new SimpleStringProperty(value);
    }

    public String getGameOutValue() {
        return gameOutValue.get();
    }

}
