package com.lottoanalysis.models.gameout;

import com.lottoanalysis.models.drawhistory.DrawModel;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.List;

@SuppressWarnings("unchecked")
public class GameOutModel extends DrawModel {

    private SimpleIntegerProperty gameRange;
    private GroupRange groupRange;
    private GameOutRange gameOutRange;
    private NumberDistanceCalculator numberDistanceCalculator;

    public GameOutModel(LottoGame lottoGame) {
        super(lottoGame,(null),(null),(null));
        gameRange = new SimpleIntegerProperty(10);
    }

    public NumberDistanceCalculator getNumberDistanceCalculator() {
        return numberDistanceCalculator;
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

    public GameOutRange getGameOutRange() {
        return gameOutRange;
    }

    public void analyze() {
        Range.resetIndex();

        int[][] gameDrawValues = getDrawData();

        //if( groupRange == null) {
            groupRange = new GroupRange(getGameRange(), getLottoGame().getMinNumber(), getGameMaxValue(), gameDrawValues, getDrawPosition(), getAnalyzeMethod());
        //}
        groupRange.analyze();
    }

    public void reAnalyzeData(){

        Range.resetIndex();

        int[][] gameDrawValues = getDrawData();

        groupRange = new GroupRange(getGameRange(), getLottoGame().getMinNumber(), getGameMaxValue(), gameDrawValues, getDrawPosition(),getAnalyzeMethod());
        groupRange.analyze();

    }

    public String getCurrentWinningNumbers() {

        StringBuilder stringBuilder = new StringBuilder();
        int size = getLottoGame().getDrawingData().size();
        Drawing drawing = getLottoGame().getDrawingData().get(size - 1);

        for (SimpleStringProperty number : drawing.getDrawNumbers()){
            stringBuilder.append( number.get() ).append("-");
        }

        int index = stringBuilder.lastIndexOf("-");
        stringBuilder.setCharAt(index,' ');

        return stringBuilder.toString().trim();
    }

    public void setRangeIndex(int rangeIndex) {
        getGroupRange().setRangeIndex( rangeIndex );
        performGameOutAnalysis();
        onModelChange("groupRange");
    }

    private void performGameOutAnalysis() {

        Range.resetIndex();
        List<List<String>> lottoNumberHitDistrubutions = groupRange.getLottoNumberHitDistrubutions(getGroupRange().getRangeIndex());

        gameOutRange = new GameOutRange(lottoNumberHitDistrubutions);
        gameOutRange.analyze();

    }
}
