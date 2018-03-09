package com.lottoanalysis.lottogames;


import com.lottoanalysis.interfaces.JackpotRetriever;
import com.lottoanalysis.interfaces.ThreadCompleteListener;
import com.lottoanalysis.retrievers.FiveAndSixDigitJackpotRetrieverImpl;
import com.lottoanalysis.retrievers.NotifyingThread;

public class FiveDigitLotteryGameImpl extends LottoGame implements ThreadCompleteListener{

    private JackpotRetriever retriever;
    NotifyingThread jackpotThread;
    public FiveDigitLotteryGameImpl() throws InterruptedException {

        jackpotThread = new FiveAndSixDigitJackpotRetrieverImpl();
        jackpotThread.addListener(this);
        jackpotThread.start();
        jackpotThread.join();
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {

        String val = ((FiveAndSixDigitJackpotRetrieverImpl)jackpotThread).getEstimatedJackpot("");
        this.setCurrentEstimatedJackpot(val);
    }
}
