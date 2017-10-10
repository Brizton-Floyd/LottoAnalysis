package model;

import model.DataFiles.LotteryGameConstants;

import java.util.*;

public class LottoBetSlipDefinitions {

    private  Map<String,Map<String,Object[]>> gamePayslipOrientation = new HashMap<>();
    private Map<String,Integer[][]> gamePaySlipMatrix = new HashMap<>();
    private int[][] gamePaySlip;
    private String gameName;

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

        for(Map.Entry<String, Map<String, Object[]>> g : gamePayslipOrientation.entrySet()){

            Map<String, Object[]> lottoSpecificFile = g.getValue();
            gameName = g.getKey();
            loadInPaySlipOrientation(g.getKey(), lottoSpecificFile);
        }

    }

    public Map<String, Object[]> getPayslipOrientation(String gameName){

        return gamePayslipOrientation.get(gameName);
    }

    private int[][] mapIntegerToInt(Integer[][] data) {

        int[][] values = new int[data.length][];

        for(int i = 0; i < data.length; i++){
            int[] vals = new int[data[i].length];

            for(int k = 0; k < data[i].length; k++){

                vals[k] = data[i][k];
            }

            values[i] = vals;
        }

        return values;
    }

    public int[][] getGamePaySlip(String game) {

        Integer[][] data = gamePaySlipMatrix.get(gameName);

        int[][] intData = mapIntegerToInt(data);

        return intData;
    }

    private int[][] getGame2DMatrix(){

        int[][] data = null;

        switch (gameName) {

            case LotteryGameConstants.FANTASY_FIVE:
                data = new int[4][];
                data[0] = new int[]{1, 5, 9, 13, 17, 21, 25, 29, 33, 37};
                data[1] = new int[]{2, 6, 10, 14, 18, 22, 26, 30, 34, 38};
                data[2] = new int[]{3, 7, 11, 15, 19, 23, 27, 31, 35, 39};
                data[3] = new int[]{4, 8, 12, 16, 20, 24, 28, 32, 36};

//                Integer[][] dataTwo = mapToIntegerArray(data);
//                gamePaySlipMatrix.put(LotteryGameConstants.FANTASY_FIVE, dataTwo);
            break;

            case LotteryGameConstants.DAILY_PICK_4:
                data = new int[4][];
                data[0] = new int[]{0, 4, 8};
                data[1] = new int[]{1, 5, 9};
                data[2] = new int[]{2, 6};
                data[3] = new int[]{3, 7};

//                Integer[][] pick4 = (Integer[][]) mapToIntegerArray(data);
//                gamePaySlipMatrix.put(LotteryGameConstants.DAILY_PICK_4, pick4);

                break;

            case LotteryGameConstants.DAILY_PICK_3:
                data = new int[3][];
                data[0] = new int[]{0, 3, 6, 9};
                data[1] = new int[]{1, 4, 7};
                data[2] = new int[]{2, 5, 8};

//                Integer[][] pick3 = (Integer[][]) mapToIntegerArray(data);
//                gamePaySlipMatrix.put(LotteryGameConstants.DAILY_PICK_3, pick3);

                break;

            case LotteryGameConstants.POWERBALL:
                data = new int[10][];
                data[0] = new int[]{1, 11, 21, 31, 41, 51, 61};
                data[1] = new int[]{2, 12, 22, 32, 42, 52, 62};
                data[2] = new int[]{3, 13, 23, 33, 43, 53, 63};
                data[3] = new int[]{4, 14, 24, 34, 44, 54, 64};
                data[4] = new int[]{5, 15, 25, 35, 45, 55, 65};
                data[5] = new int[]{6, 16, 26, 36, 46, 56, 66};
                data[6] = new int[]{7, 17, 27, 37, 47, 57, 67};
                data[7] = new int[]{8, 18, 28, 38, 48, 58, 68};
                data[8] = new int[]{9, 19, 29, 39, 49, 59, 69};
                data[9] = new int[]{10, 20, 30, 40, 50, 60};

//                Integer[][] powerball = mapToIntegerArray(data);
//                gamePaySlipMatrix.put(LotteryGameConstants.POWERBALL, powerball);

                break;
        }

        return data;
    }

    private Integer[][] mapToIntegerArray(int[][] dataValues) {

        Integer[][] data = new Integer[dataValues.length][];


        for(int i = 0; i < dataValues.length; i++){
            int[] values = new int [dataValues[i].length];

            for(int k = 0; k < dataValues[i].length; k++){
                values[k] = dataValues[i][k];

            }
            data[i] = Arrays.stream(values).boxed().toArray(Integer[]::new);
        }

        return data;
    }

    private void loadInPaySlipOrientation(String key, Map<String, Object[]> lottoSpecificFile) {

        switch (key) {
            case LotteryGameConstants.FANTASY_FIVE:

                gamePaySlip = new int[4][];

                gamePaySlip[0] = getGame2DMatrix()[0];
                gamePaySlip[1] = getGame2DMatrix()[1];
                gamePaySlip[2] = getGame2DMatrix()[2];
                gamePaySlip[3] = getGame2DMatrix()[3];

                lottoSpecificFile.put("C1", new Object[]{gamePaySlip[0], 4});
                lottoSpecificFile.put("C2", new Object[]{gamePaySlip[1], 4});
                lottoSpecificFile.put("C3", new Object[]{gamePaySlip[2], 4});
                lottoSpecificFile.put("C4", new Object[]{gamePaySlip[3], 4});
                break;

            case LotteryGameConstants.DAILY_PICK_4:

                gamePaySlip = new int[4][];

                gamePaySlip[0] = getGame2DMatrix()[0];
                gamePaySlip[1] = getGame2DMatrix()[1];
                gamePaySlip[2] = getGame2DMatrix()[2];
                gamePaySlip[3] = getGame2DMatrix()[3];

                lottoSpecificFile.put("C1",new Object[]{gamePaySlip[0], 4});
                lottoSpecificFile.put("C2", new Object[]{gamePaySlip[1], 4});
                lottoSpecificFile.put("C3", new Object[]{gamePaySlip[2], 4});
                lottoSpecificFile.put("C4",new Object[]{gamePaySlip[3], 4});
                break;
            case LotteryGameConstants.DAILY_PICK_3:

                gamePaySlip = new int[3][];

                gamePaySlip[0] = getGame2DMatrix()[0];
                gamePaySlip[1] = getGame2DMatrix()[1];
                gamePaySlip[2] = getGame2DMatrix()[2];

                lottoSpecificFile.put("C1",new Object[]{gamePaySlip[0], 3});
                lottoSpecificFile.put("C2", new Object[]{gamePaySlip[1], 3});
                lottoSpecificFile.put("C3", new Object[]{gamePaySlip[2], 3});
                break;
            case LotteryGameConstants.POWERBALL:

                gamePaySlip = new int[10][];

                gamePaySlip[0] = getGame2DMatrix()[0];
                gamePaySlip[1] = getGame2DMatrix()[1];
                gamePaySlip[2] = getGame2DMatrix()[2];
                gamePaySlip[3] = getGame2DMatrix()[3];
                gamePaySlip[4] = getGame2DMatrix()[4];
                gamePaySlip[5] = getGame2DMatrix()[5];
                gamePaySlip[6] = getGame2DMatrix()[6];
                gamePaySlip[7] = getGame2DMatrix()[7];
                gamePaySlip[8] = getGame2DMatrix()[8];
                gamePaySlip[9] = getGame2DMatrix()[9];

                lottoSpecificFile.put("C1",new Object[]{gamePaySlip[0], 10});
                lottoSpecificFile.put("C2", new Object[]{gamePaySlip[1], 10});
                lottoSpecificFile.put("C3", new Object[]{gamePaySlip[2], 10});
                lottoSpecificFile.put("C4",new Object[]{gamePaySlip[3], 10});
                lottoSpecificFile.put("C5",new Object[]{gamePaySlip[4], 10});
                lottoSpecificFile.put("C6", new Object[]{gamePaySlip[5], 10});
                lottoSpecificFile.put("C7", new Object[]{gamePaySlip[6], 10});
                lottoSpecificFile.put("C8",new Object[]{gamePaySlip[7], 10});
                lottoSpecificFile.put("C9", new Object[]{gamePaySlip[8], 10});
                lottoSpecificFile.put("C10",new Object[]{gamePaySlip[9], 10});
                break;


        }
    }

    public void lodMatrixIntoMap(String gameName) {
        this.gameName = gameName;
        Integer[][] data = mapToIntegerArray( getGame2DMatrix() );

        this.gamePaySlipMatrix.put(gameName, data);
    }
}
