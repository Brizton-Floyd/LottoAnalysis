package model;

import model.DataFiles.LotteryGameConstants;

import java.util.*;

public class LottoBetSlipDefinitions {

    private  Map<String,Map<String,Object[]>> gamePayslipOrientation = new HashMap<>();

    public LottoBetSlipDefinitions(){
        loadMap();
    }

    private void loadMap() {
        gamePayslipOrientation.put(LotteryGameConstants.FANTASY_FIVE,new LinkedHashMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.POWERBALL, new LinkedHashMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.MEGA_MILLIONS, new LinkedHashMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.DAILY_PICK_3, new LinkedHashMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.DAILY_PICK_4, new LinkedHashMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.SUPPER_LOTTO_PLUS, new LinkedHashMap<>());
    }

    public Map<String, Object[]> getPayslipOrientation(String gameName){

        for(Map.Entry<String, Map<String, Object[]>> g : gamePayslipOrientation.entrySet()){

            Map<String, Object[]> lottoSpecificFile = g.getValue();
            loadInPaySlipOrientation(g.getKey(), lottoSpecificFile);
        }

        return gamePayslipOrientation.get(gameName);
    }

    private void loadInPaySlipOrientation(String key, Map<String, Object[]> lottoSpecificFile) {

        Integer[][] fantasyFive = new Integer[5][];
        fantasyFive[0] = new Integer[]{1, 2, 11, 12, 21, 22, 31, 32};
        fantasyFive[1] = new Integer[]{3, 4, 13, 14, 23, 24, 33, 34};
        fantasyFive[2] = new Integer[]{5, 6, 15, 16, 25, 26, 35, 36};
        fantasyFive[3] = new Integer[]{7, 8, 17, 18, 27, 28, 37, 38};
        fantasyFive[4] = new Integer[]{9, 10,19, 20, 29, 30, 39};


        Integer[][] dailyPick4 = new Integer[4][];
        dailyPick4[0] = new Integer[]{0,1,7,3,4};
        dailyPick4[1] = new Integer[]{2,5,6,8,9};
        dailyPick4[2] = new Integer[]{0,1,2,4,8};
        dailyPick4[3] = new Integer[]{3,5,6,7,9};

        Integer[][] dailyPick3 = new Integer[4][];
        dailyPick3[0] = new Integer[]{0,1,7,3,4};
        dailyPick3[1] = new Integer[]{2,5,6,8,9};
        dailyPick3[2] = new Integer[]{0,1,2,4,8};

        Integer[][] powerBall = new Integer[10][];
        powerBall[0] = new Integer[]{1, 11, 21, 31, 41, 51, 61};
        powerBall[1] = new Integer[]{2, 12, 22, 32, 42, 52, 62};
        powerBall[2] = new Integer[]{3, 13, 23, 33, 43, 53, 63};
        powerBall[3] = new Integer[]{4, 14, 24, 34, 44, 54, 64};
        powerBall[4] = new Integer[]{5, 15, 25, 35, 45, 55, 65};
        powerBall[5] = new Integer[]{6, 16, 26, 36, 46, 56, 66};
        powerBall[6] = new Integer[]{7, 17, 27, 37, 47, 57, 67};
        powerBall[7] = new Integer[]{8, 18, 28, 38, 48, 58, 68};
        powerBall[8] = new Integer[]{9, 19, 29, 39, 49, 59, 69};
        powerBall[9] = new Integer[]{10, 20, 30, 40, 50, 60};



        switch (key) {
            case LotteryGameConstants.FANTASY_FIVE:
                lottoSpecificFile.put("C1", new Object[]{fantasyFive[0], 5});
                lottoSpecificFile.put("C2", new Object[]{fantasyFive[1], 5});
                lottoSpecificFile.put("C3", new Object[]{fantasyFive[2], 5});
                lottoSpecificFile.put("C4", new Object[]{fantasyFive[3], 5});
                lottoSpecificFile.put("C5", new Object[]{fantasyFive[4], 5});
                break;

            case LotteryGameConstants.DAILY_PICK_4:
                lottoSpecificFile.put("C1",new Object[]{dailyPick4[0], 4});
                lottoSpecificFile.put("C2", new Object[]{dailyPick4[1], 4});
                lottoSpecificFile.put("C3", new Object[]{dailyPick4[2], 4});
                lottoSpecificFile.put("C4",new Object[]{dailyPick4[3], 4});
                break;
            case LotteryGameConstants.DAILY_PICK_3:
                lottoSpecificFile.put("C1",new Object[]{dailyPick3[0], 3});
                lottoSpecificFile.put("C2", new Object[]{dailyPick3[1], 3});
                lottoSpecificFile.put("C3", new Object[]{dailyPick3[2], 3});
                break;
            case LotteryGameConstants.POWERBALL:
                lottoSpecificFile.put("C1",new Object[]{powerBall[0], 10});
                lottoSpecificFile.put("C2", new Object[]{powerBall[1], 10});
                lottoSpecificFile.put("C3", new Object[]{powerBall[2], 10});
                lottoSpecificFile.put("C4",new Object[]{powerBall[3], 10});
                lottoSpecificFile.put("C5",new Object[]{powerBall[4], 10});
                lottoSpecificFile.put("C6", new Object[]{powerBall[5], 10});
                lottoSpecificFile.put("C7", new Object[]{powerBall[6], 10});
                lottoSpecificFile.put("C8",new Object[]{powerBall[7], 10});
                lottoSpecificFile.put("C9", new Object[]{powerBall[8], 10});
                lottoSpecificFile.put("C10",new Object[]{powerBall[9], 10});
                break;


        }
    }
}
