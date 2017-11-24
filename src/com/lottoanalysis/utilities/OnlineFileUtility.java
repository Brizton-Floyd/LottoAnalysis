package com.lottoanalysis.utilities;

import com.lottoanalysis.common.LotteryGameConstants;

import java.util.*;

public class OnlineFileUtility {


    private OnlineFileUtility(){}

    public static Map<String,String> getUrlPaths(){

        Map<String, String> lottoFilesPaths = new LinkedHashMap<>();

        lottoFilesPaths.put("Fantasy Five",LotteryGameConstants.FANTASY_FIVE_URL);
        lottoFilesPaths.put("PowerBall", LotteryGameConstants.POWERBALL_URL );
        lottoFilesPaths.put("Mega Millions",LotteryGameConstants.MEGA_MILLIONS_URL);
        lottoFilesPaths.put("Super Lotto Plus", LotteryGameConstants.SUPER_LOTTO_PLUS_URL);
        lottoFilesPaths.put("Daily Pick4", LotteryGameConstants.PICK_4_URL);
        lottoFilesPaths.put("Daily Pick3", LotteryGameConstants.PICK_3_URL);


        return lottoFilesPaths;
    }
}
