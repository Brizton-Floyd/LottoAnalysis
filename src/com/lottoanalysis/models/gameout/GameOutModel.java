package com.lottoanalysis.models.gameout;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.models.drawhistory.DrawPositions;

import java.util.List;

@SuppressWarnings("unchecked")
public class GameOutModel {

    private Object[] gameDrawInformation;
    private DrawPositions drawPosition;

    public GameOutModel( Object[] gameDrawInformation ) {

        this.gameDrawInformation = gameDrawInformation;
    }

    public int getDrawSize(){
        final List<Object> objectList = (List<Object>)gameDrawInformation[1];
        return  ((int[][])objectList.get(0)).length;
    }

    public String getGameName(){
        return ((LottoGame)gameDrawInformation[0]).getGameName();
    }

    public DrawPositions getDrawPosition() {
        return drawPosition;
    }

    public void setDrawPosition(DrawPositions drawPosition) {
        this.drawPosition = drawPosition;
    }

    public int getGameMaxValue(){
        return ((LottoGame)gameDrawInformation[0]).getMaxNumber();
    }
}
