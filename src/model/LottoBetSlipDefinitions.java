package model;

import model.DataFiles.LotteryGameConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LottoBetSlipDefinitions {

    private  Map<String,Map<String,Object[]>> gamePayslipOrientation = new HashMap<>();

    public LottoBetSlipDefinitions(){
        loadMap();
    }

    private void loadMap() {
        gamePayslipOrientation.put(LotteryGameConstants.FANTASY_FIVE,new TreeMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.POWERBALL, new TreeMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.MEGA_MILLIONS, new TreeMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.DAILY_PICK_3, new TreeMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.DAILY_PICK_4, new TreeMap<>());
        gamePayslipOrientation.put(LotteryGameConstants.SUPPER_LOTTO_PLUS, new TreeMap<>());
    }

    public Map<String, Object[]> getPayslipOrientation(String gameName){

        for(Map.Entry<String, Map<String, Object[]>> g : gamePayslipOrientation.entrySet()){

            Map<String, Object[]> lottoSpecificFile = g.getValue();
            loadInPaySlipOrientation(g.getKey(), lottoSpecificFile);
        }

        return gamePayslipOrientation.get(gameName);
    }

    private void loadInPaySlipOrientation(String key, Map<String, Object[]> lottoSpecificFile) {

        Integer[][] fantasyFive = new Integer[4][];
        fantasyFive[0] = new Integer[]{1,5,9,13,17,21,25,29,33,37};
        fantasyFive[1] = new Integer[]{2,6,10,14,18,22,26,30,34,38};
        fantasyFive[2] = new Integer[]{3,7,11,15,19,23,27,31,35,39};
        fantasyFive[3] = new Integer[]{4,8,12,16,20,24,28,32,36};

        if(key.equals(LotteryGameConstants.FANTASY_FIVE)){
            lottoSpecificFile.put("C1",new Object[]{fantasyFive[0], 4});
            lottoSpecificFile.put("C2", new Object[]{fantasyFive[1], 4});
            lottoSpecificFile.put("C3", new Object[]{fantasyFive[2], 4});
            lottoSpecificFile.put("C4",new Object[]{fantasyFive[3], 4});
        }

    }
}
