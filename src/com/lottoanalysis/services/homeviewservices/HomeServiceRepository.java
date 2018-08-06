package com.lottoanalysis.services.homeviewservices;

import com.lottoanalysis.models.lottogames.LottoGame;

public interface HomeServiceRepository {

    LottoGame loadDefaultLotteryGame() throws Exception;

    LottoGame loadGameById(int id) throws Exception;
}
