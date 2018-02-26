package com.lottoanalysis.lottoinfoandgames;

import com.lottoanalysis.lottoinfoandgames.lottogames.FiveDigitLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickFourLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickThreeLotteryGame;

public class LotteryGameFactory extends AbstractFactory{

    @Override
    public LottoGame getLotteryGame(String game) {

        if(game.equalsIgnoreCase("three")){

            return new PickThreeLotteryGame();
        }
        if(game.equalsIgnoreCase("five")){

            return new FiveDigitLotteryGame();
        }
        if(game.equalsIgnoreCase("four")){

            return new PickFourLotteryGame();
        }

        return null;
    }
}
