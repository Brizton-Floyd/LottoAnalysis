package com.lottoanalysis.utilities.fileutilities;

import com.lottoanalysis.common.LotteryGameConstants;

import java.util.*;

public class OnlineFileUtility {


    private OnlineFileUtility(){}

    public static Map<String,String> getUrlPaths(){

        Map<String, String> lottoFilesPaths = new LinkedHashMap<>();

        lottoFilesPaths.put("Fantasy Five",LotteryGameConstants.CASH_FIVE_URL);
//        lottoFilesPaths.put("PowerBall", LotteryGameConstants.POWERBALL_URL );
//        lottoFilesPaths.put("Mega Millions",LotteryGameConstants.MEGA_MILLIONS_URL);

        return lottoFilesPaths;
    }
}
