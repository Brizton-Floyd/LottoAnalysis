package com.lottoanalysis.services.homeviewservices;

import com.lottoanalysis.lottogames.LottoGame;

public interface HomeServiceRepository {

    LottoGame loadDefaultLotteryGame() throws Exception;
}
