package com.lottoanalysis.models.gameout;

import com.lottoanalysis.models.drawhistory.DrawModel;
import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.drawhistory.DrawPositions;
import javafx.beans.property.SimpleIntegerProperty;

import javax.swing.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class GameOutModel extends DrawModel {

    private SimpleIntegerProperty gameRange;

    public GameOutModel(LottoGame lottoGame) {
        super(lottoGame,(null),(null),(null));
    }

    public int getGameMaxValue(){
        return getLottoGame().getMaxNumber();
    }

    public int getGameRange() {
        return gameRange.get();
    }

    public SimpleIntegerProperty gameRangeProperty() {
        return gameRange;
    }

    public void setGameRange(int gameRange) {

        if(this.gameRange == null)
            this.gameRange = new SimpleIntegerProperty();

        this.gameRange.set(gameRange);
        onModelChange( ("gameRange") );

    }
}
