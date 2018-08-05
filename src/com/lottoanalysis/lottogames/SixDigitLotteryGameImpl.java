package com.lottoanalysis.lottogames;

import com.lottoanalysis.interfaces.JackpotRetriever;
import com.lottoanalysis.interfaces.ThreadCompleteListener;
import com.lottoanalysis.retrievers.FiveAndSixDigitJackpotRetrieverImpl;
import com.lottoanalysis.tasks.NotifyingThread;

/**
 * Created by briztonfloyd on 8/31/17.
 */
public class SixDigitLotteryGameImpl extends LottoGame implements ThreadCompleteListener{

    private NotifyingThread jackpotThread;

    public SixDigitLotteryGameImpl(){
        setPositionNumbersAllowed(6);
    }

    @Override
    public void startThreadForJackpotRetrieval() {

        jackpotThread = new FiveAndSixDigitJackpotRetrieverImpl(gameName);
        jackpotThread.addListener(this);
        jackpotThread.start();

        try {
            jackpotThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {

        jackpotThread.removeListener(this);
        String val = ((FiveAndSixDigitJackpotRetrieverImpl)jackpotThread).getEstimatedJackpot("");
        this.setCurrentEstimatedJackpot(val);

    }
}
