package com.lottoanalysis.models.lottogames;

import com.lottoanalysis.interfaces.ThreadCompleteListener;
import com.lottoanalysis.models.lottogames.retrievers.FiveAndSixDigitJackpotRetrieverImpl;
import com.lottoanalysis.models.tasks.NotifyingThread;

public class FiveDigitLotteryGameImpl extends LottoGame implements ThreadCompleteListener{

    private NotifyingThread jackpotThread;

    public FiveDigitLotteryGameImpl(){
        setPositionNumbersAllowed(5);
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
        setCurrentEstimatedJackpot(val);

    }
}
