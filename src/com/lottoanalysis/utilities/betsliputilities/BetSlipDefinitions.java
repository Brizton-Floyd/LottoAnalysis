package com.lottoanalysis.utilities.betsliputilities;

import java.util.HashMap;
import java.util.Map;

public class BetSlipDefinitions {

    private static Map<String,Integer[][]> definitinHolder = new HashMap<>();
    static {
        definitinHolder.put("FantasyFive",fantasyFiveDefinitionsFour());
        definitinHolder.put("Powerball", powerBallDefinitions());
        definitinHolder.put("Pick4", pickThreeAnFourDefinitions());
        definitinHolder.put("Pick3", pickThreeAnFourDefinitions());
        definitinHolder.put("SuperLottoPlus", superLottoPlusDefinitions());
    }

    private static Integer[][] superLottoPlusDefinitions() {

        return new Integer[][]{
                {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19},
                {20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39},
                {40,41,42,43,44,45,46,47}
        };
    }


    public static Integer[][] getDefinitionFile(String gameName) {

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
    private static Integer[][] fantasyFiveDefinitionsThree(){

        return new Integer[][]{
                {1,6,9, 14,17,22,25,30,33,38},
                {2,5,10,13,18,21,26,29,34,37},
                {3,8,11,16,19,24,27,32,35,39},
                {4,7,12,15,20,23,28,31,36}
        };
    }
    private static Integer[][] fantasyFiveDefinitionsFour(){

        return new Integer[][]{
                {1,2,3,4,5,6,7,8,9},
                {10,11,12,13,14,15,16,17,18,19},
                {20,21,22,23,24,25,26,27,28,29},
                {30,31,32,33,34,35,36,37,38,39}
        };
    }
    private static Integer[][] fantasyFiveDefinitionsFive(){

        return new Integer[][]{
                {1,2,3,4,5,6,7,8,9},
                {10,11,12,13,14,15,16,17,18,19},
                {20,21,22,23,24,25,26,27,28,29},
                {30,31,32,33,34,35,36,37,38,39}
        };
    }
    private static Integer[][] fantasyFiveDefinitionsTwo(){

        return new Integer[][]{
                {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19},
                {20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39}
        };
    }
    private static Integer[][] pickThreeAnFourDefinitions(){

        return new Integer[][]{
                {0,1,2,3,4},
                {5,6,7,8,9}
        };
    }
    private static Integer[][] powerBallDefinitions(){

        return new Integer[][]{
                {1,2,3,4,5,6,7,8,9},
                {10,11,12,13,14,15,16,17,18,19},
                {20,21,22,23,24,25,26,27,28,29},
                {30,31,32,33,34,35,36,37,38,39},
                {40,41,42,43,44,45,46,47,48,49},
                {50,51,52,53,54,55,56,57,58,59},
                {60,61,62,63,64,65,66,67,68,69}
        };
    }
}
