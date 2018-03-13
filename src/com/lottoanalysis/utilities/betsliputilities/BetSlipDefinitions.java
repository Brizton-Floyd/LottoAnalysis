package com.lottoanalysis.utilities.betsliputilities;

import java.util.HashMap;
import java.util.Map;

public class BetSlipDefinitions {

    private static Map<String,Integer[][]> definitinHolder = new HashMap<>();
    static {
        definitinHolder.put("FantasyFive",fantasyFiveDefinitionsTwo());
        definitinHolder.put("Powerball", powerBallDefinitions());
        definitinHolder.put("Pick4", pickThreeAnFourDefinitions());
        definitinHolder.put("Pick3", pickThreeAnFourDefinitions());
    }


    public Integer[][] getDefinitionFile(String gameName) {

        return definitinHolder.get(gameName);
    }

    private static Integer[][] fantasyFiveDefinitions(){

        return new Integer[][]{
                {1,5,9,13,17,21,25,29,33,37},
                {2,6,10,14,18,22,26,30,34,38},
                {3,7,11,15,19,23,27,31,35,39},
                {4,8,12,16,20,24,28,32,36}
        };
    }
    private static Integer[][] fantasyFiveDefinitionsTwo(){

        return new Integer[][]{
                {1,2,3,4,5,6,7,8,9,10},
                {11,12,13,14,15,16,17,18,19,20},
                {21,22,23,24,25,26,27,28,29,30},
                {31,32,33,34,35,36,37,38,39}
        };
    }
    private static Integer[][] pickThreeAnFourDefinitions(){

        return new Integer[][]{
                {0,1},
                {2,3},
                {4,5},
                {6,7},
                {8,9}
        };
    }
    private static Integer[][] powerBallDefinitions(){

        return new Integer[][]{
                {1,11,21,31,41,51,61},
                {2,12,22,32,42,52,62},
                {3,13,23,33,43,53,63},
                {4,14,24,34,44,54,64},
                {5,15,25,35,45,55,65},
                {6,16,26,36,46,56,66},
                {7,17,27,37,47,57,67},
                {8,18,28,38,48,58,68},
                {9,19,29,39,49,59,69},
                {10,20,30,40,50,60}
        };
    }
}
