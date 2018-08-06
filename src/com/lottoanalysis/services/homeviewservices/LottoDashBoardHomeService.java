package com.lottoanalysis.services.homeviewservices;

import com.lottoanalysis.models.lottogames.LottoGame;

public interface LottoDashBoardHomeService {

    LottoGame getDefaultGame();

    LottoGame loadById(int id);
}
