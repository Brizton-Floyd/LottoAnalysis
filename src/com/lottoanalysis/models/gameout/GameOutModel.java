package com.lottoanalysis.models.gameout;

import com.lottoanalysis.models.drawhistory.DrawModel;
import com.lottoanalysis.models.lottogames.LottoGame;
import javafx.beans.property.SimpleIntegerProperty;

@SuppressWarnings("unchecked")
public class GameOutModel extends DrawModel {

    private SimpleIntegerProperty gameRange;
    private GroupRange groupRange;
    private GameOutRange gameOutRange;

    public GameOutModel(LottoGame lottoGame) {
        super(lottoGame,(null),(null),(null));
        gameRange = new SimpleIntegerProperty(10);

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

    public GroupRange getGroupRange() {
        return groupRange;
    }

    public void analyze() {

        int[][] gameDrawValues = (int[][]) getLottoDrawData().get( getAnalyzeMethod().getIndex() );

        if( groupRange == null) {
            groupRange = new GroupRange(getGameRange(), getLottoGame().getMinNumber(), getGameMaxValue(), gameDrawValues, getDrawPosition(), getAnalyzeMethod());
        }

        groupRange.analyze();
    }

    public void reAnalyzeData(){

        int[][] gameDrawValues = (int[][]) getLottoDrawData().get( getAnalyzeMethod().getIndex() );

        groupRange = new GroupRange(getGameRange(), getLottoGame().getMinNumber(), getGameMaxValue(), gameDrawValues, getDrawPosition(),getAnalyzeMethod());
        groupRange.analyze();

    }

}
