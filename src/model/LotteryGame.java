package model;


import javafx.collections.ObservableList;
import model.DataFiles.LotteryRepository;

import java.util.List;

public abstract class LotteryGame {

    protected int lottoId;
    protected String gameName;
    protected ObservableList<Drawing> drawingData;

    protected LotteryRepository repository = new LotteryRepository(null);

    public LotteryGame() {

    }
    public LotteryGame(int lottoId, String gameName, ObservableList<Drawing> drawingData){

        this.lottoId = lottoId;
        this.gameName = gameName;
        this.drawingData = drawingData;
    }

    public int getLottoId() {
        return lottoId;
    }

    public void setLottoId(int lottoId) {
        this.lottoId = lottoId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ObservableList<Drawing> getDrawingData() {
        return drawingData;
    }

    public void setDrawingData(ObservableList<Drawing> drawingData) {
        this.drawingData = drawingData;
    }

    public abstract void saveLotteryDrawingInformation();
    public abstract LotteryGame loadGameData();
}
