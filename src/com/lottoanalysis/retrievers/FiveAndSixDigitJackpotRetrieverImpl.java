package com.lottoanalysis.retrievers;

import com.lottoanalysis.interfaces.JackpotRetriever;
import com.lottoanalysis.lottogames.FiveDigitLotteryGameImpl;
import com.lottoanalysis.lottogames.LottoGame;
import javafx.concurrent.Task;

public class FiveAndSixDigitJackpotRetrieverImpl extends NotifyingThread implements JackpotRetriever{

    private String amount;

    @Override
    public String getEstimatedJackpot(String gameName) {
        return amount;
    }

    @Override
    public void doRun() {

        amount = "$ 5,000";
    }
}
