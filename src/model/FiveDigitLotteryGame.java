package model;


import java.util.List;

public class FiveDigitLotteryGame extends LotteryGame{


    public FiveDigitLotteryGame(String game) {

        this(0,game,null);
    }
    public FiveDigitLotteryGame(int lottoId, String gameName, List<Drawing> drawingData) {
        super(lottoId, gameName, drawingData);
    }

    public FiveDigitLotteryGame() {

    }

    @Override
    public void saveLotteryDrawingInformation() {

    }

    @Override
    public LotteryGame loadGameData() {
        lottoId = repository.retrieveGameId(gameName);
        return repository.loadFiveDigitLottoData(lottoId);
    }


}
