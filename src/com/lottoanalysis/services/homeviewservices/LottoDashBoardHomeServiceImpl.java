package com.lottoanalysis.services.homeviewservices;

import com.lottoanalysis.models.lottogames.LottoGame;

public class LottoDashBoardHomeServiceImpl implements LottoDashBoardHomeService {

    private HomeServiceRepository homeServiceRepository;

    public LottoDashBoardHomeServiceImpl(HomeServiceRepository homeServiceRepository){

        this.homeServiceRepository = homeServiceRepository;
    }

    @Override
    public LottoGame getDefaultGame() {

        try {

            return homeServiceRepository.loadDefaultLotteryGame();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
