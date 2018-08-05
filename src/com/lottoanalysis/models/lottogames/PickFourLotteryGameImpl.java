package com.lottoanalysis.models.lottogames;

/**
 * Created by briztonfloyd on 9/1/17.
 */
public class PickFourLotteryGameImpl extends LottoGame {

    public PickFourLotteryGameImpl(){
        setPositionNumbersAllowed(4);
    }
    @Override
    public void startThreadForJackpotRetrieval() {

        setCurrentEstimatedJackpot("No Jackpot Available");
    }
}
