package utils;

import model.Drawing;
import model.LotteryGame;

import java.util.*;

/**
 * Class is responsible for analyzing the number distribution for a lottery game according to its bet slip layout.
 * This class also will analyze which direction a number should go and specify to the user which way to select numbers
 * on the betslip according to the number position
 */
public class LottoBetSlipAnalyzer {

    private int[][] rowsAndColumns = null;
    private int indexInDrawingData;
    private double positionalAverage = 0.0;
    private List<Integer> numbersInPosition;
    private Map<Double, Map<String, Integer[]>> positionalAverageHitsTracker;

    public LottoBetSlipAnalyzer(LotteryGame lotteryGame, int indexInDrawingData) {

        this.indexInDrawingData = indexInDrawingData;
        setUpRowsAndColumns(lotteryGame);
        findPositionalAverage(lotteryGame);
        analyzeBetSlip(rowsAndColumns, numbersInPosition);
    }

    /**
     * This method will determine which row the user should play for the next drawing for the given lotto position
     *
     * @param rowsAndColumns
     * @param numbersInPosition
     */
    private void analyzeBetSlip(int[][] rowsAndColumns, List<Integer> numbersInPosition) {

        Map<Integer[], Map<String, Integer[]>> directionAnalysis = new HashMap<>();
        int[][] beginningAndEndValuesForRows = new int[rowsAndColumns.length][2];
        loadBeginningAndEndValues(beginningAndEndValuesForRows, rowsAndColumns);

        int recentNumberToHitInPos = numbersInPosition.get(numbersInPosition.size() - 1);
        int locationInMatrix = 0;
        int count = 0;
        boolean check = true;
        Integer[] newRow = null;

        for (int j = 0; j < numbersInPosition.size(); j++) {

            if (numbersInPosition.get(j) == recentNumberToHitInPos && j < numbersInPosition.size() - 1) {

                int nextNum = numbersInPosition.get(j + 1);

                for (int i = count; i < rowsAndColumns.length; i++) {

                    for (int k = 0; k < rowsAndColumns[i].length; k++) {

                        if ((Arrays.stream(rowsAndColumns[i]).anyMatch(x -> x == recentNumberToHitInPos))) {

                            if (check)
                                newRow = Arrays.stream(rowsAndColumns[i]).boxed().toArray(Integer[]::new);

                            check = false;
                            determineRowNextNumFallsIn(locationInMatrix, newRow, nextNum, directionAnalysis, beginningAndEndValuesForRows);
                            break;

                        } else {
                            locationInMatrix++;
                            break;
                        }
                    }
                    count++;
                }
            }
            count = 0;
            locationInMatrix = 0;
        }
    }

    /**
     * This method will populate a map with the direction the next draw number tends to fall after the current number is
     * drawn
     *
     * @param location
     * @param arrayVals
     * @param nextNum
     * @param directionAnalysis
     * @param beginningAndEndValuesForRows
     */
    private void determineRowNextNumFallsIn(int location, Integer[] arrayVals, int nextNum, Map<Integer[],
            Map<String, Integer[]>> directionAnalysis, int[][] beginningAndEndValuesForRows) {


        int currentIndex = location;

        if (!directionAnalysis.containsKey(arrayVals)) {
            directionAnalysis.put(arrayVals, new LinkedHashMap<>());
        }

        Map<String, Integer[]> directionIndicators = directionAnalysis.get(arrayVals);

        boolean numHitIncurrentArrayIndex = (nextNum >= arrayVals[0] && nextNum <= arrayVals[arrayVals.length - 1]);

        if (!numHitIncurrentArrayIndex) {

            for (int i = 0; i < beginningAndEndValuesForRows.length; i++) {

                for (int k = 0; k < beginningAndEndValuesForRows[i].length; ) {

                    int beginingValue = beginningAndEndValuesForRows[i][k];
                    int endValue = beginningAndEndValuesForRows[i][++k];

                    if (nextNum >= beginingValue && nextNum <= endValue) {

                        int index = currentIndex - i;
                        String direction = (index < 0) ? "Down " : "Up ";

                        if(!directionIndicators.containsKey(direction + Math.abs(index))){
                            directionIndicators.put(direction+Math.abs(index),new Integer[]{1,0});
                        }else {

                            Integer[] vals = directionIndicators.get(direction+Math.abs(index));
                            vals[0]++;
                            vals[1] = 0;
                            NumberPatternAnalyzer.incrementGamesOutForMatrix(directionIndicators,direction+Math.abs(index));
                        }

                        break;
                    }
                    else {
                        break;
                    }
                }
            }
        } else {

            if(!directionIndicators.containsKey("Equal")){
                directionIndicators.put("Equal",new Integer[]{1,0});
            }else{

                Integer[] vals = directionIndicators.get("Equal");
                vals[0]++;
                vals[1]=0;
                NumberPatternAnalyzer.incrementGamesOutForMatrix(directionIndicators,"Equal");
            }
        }
    }

    /**
     * Method will load in the beginning and end values for each row in the matrix
     *
     * @param beginningAndEndValuesForRows
     * @param rowsAndColumns
     */
    private void loadBeginningAndEndValues(int[][] beginningAndEndValuesForRows, int[][] rowsAndColumns) {

        for (int i = 0; i < beginningAndEndValuesForRows.length; i++) {

            for (int j = 0; j < rowsAndColumns[i].length; j++) {

                beginningAndEndValuesForRows[i][j] = rowsAndColumns[i][j];
                beginningAndEndValuesForRows[i][j + 1] = rowsAndColumns[i][rowsAndColumns[i].length - 1];
                break;

            }
        }
    }

    /**
     * Method will analyze the draw position and analyze what the overall average is for that position.
     *
     * @param lotteryGame
     */
    private void findPositionalAverage(LotteryGame lotteryGame) {

        if (numbersInPosition == null)
            numbersInPosition = new LinkedList<>();

        int sum = 0;

        for (Drawing drawing : lotteryGame.getDrawingData()) {

            if (indexInDrawingData == 2)
                numbersInPosition.add(Integer.parseInt(drawing.getPosOne()));
            else if (indexInDrawingData == 3)
                numbersInPosition.add(Integer.parseInt(drawing.getPosTwo()));
            else if (indexInDrawingData == 4)
                numbersInPosition.add(Integer.parseInt(drawing.getPosThree()));
            else if (indexInDrawingData == 5)
                numbersInPosition.add(Integer.parseInt(drawing.getPosFour()));
            else if (indexInDrawingData == 6)
                numbersInPosition.add(Integer.parseInt(drawing.getPosFive()));
        }
        for (int num : numbersInPosition)
            sum += num;

        positionalAverage = (sum / numbersInPosition.size()) + 4;

        trackAboveBelowAndEqualToAverageHits(positionalAverage);
    }

    /**
     * Method will track above, below, and at average hits for the position
     *
     * @param positionalAverage
     */
    private void trackAboveBelowAndEqualToAverageHits(double positionalAverage) {

        if (positionalAverageHitsTracker == null)
            positionalAverageHitsTracker = new HashMap<>();

        positionalAverageHitsTracker.put(positionalAverage, new HashMap<>());

        for (int num : numbersInPosition) {

            if (num > positionalAverage) {
                Map<String, Integer[]> upVals = positionalAverageHitsTracker.get(positionalAverage);
                if (!upVals.containsKey("Up")) {
                    upVals.put("Up", new Integer[]{1, 0});
                } else {
                    Integer[] vals = upVals.get("Up");
                    vals[0]++;
                    vals[1] = 0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(upVals, "Up");
                    positionalAverageHitsTracker.put(positionalAverage, upVals);
                }
            } else if (num < positionalAverage) {
                Map<String, Integer[]> lessVals = positionalAverageHitsTracker.get(positionalAverage);
                if (!lessVals.containsKey("Less")) {
                    lessVals.put("Less", new Integer[]{1, 0});
                } else {
                    Integer[] vals = lessVals.get("Less");
                    vals[0]++;
                    vals[1] = 0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(lessVals, "Less");
                    positionalAverageHitsTracker.put(positionalAverage, lessVals);
                }
            } else if (num == positionalAverage) {
                Map<String, Integer[]> equalVals = positionalAverageHitsTracker.get(positionalAverage);
                if (!equalVals.containsKey("Equal")) {
                    equalVals.put("Equal", new Integer[]{1, 0});
                } else {
                    Integer[] vals = equalVals.get("Equal");
                    vals[0]++;
                    vals[1] = 0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(equalVals, "Equal");
                    positionalAverageHitsTracker.put(positionalAverage, equalVals);
                }
            }

        }
    }

    /**
     * Method will populate a two dimension array according to the amount of numbers in the specific lottery game.
     *
     * @param lotteryGame
     */
    private void setUpRowsAndColumns(LotteryGame lotteryGame) {

        // get the min and max numbers
        int lowNum = lotteryGame.getMinNumber();
        int maxNum = lotteryGame.getMaxNumber();

        int numberOfRowsNeeded = maxNum / 9;
        int remainderRowIfNeeded = (maxNum - (numberOfRowsNeeded * 9)) > 0 ? 1 : 0;
        int trueAmountOfRowsNeeded = numberOfRowsNeeded + remainderRowIfNeeded;

        int slots = (maxNum - (numberOfRowsNeeded * 9));
        int remainingSlots = maxNum - slots;

        rowsAndColumns = new int[trueAmountOfRowsNeeded][];
        int count = 0;
        for (int i = lowNum; i <= maxNum; ) {

            int[] columnSize;
            if ((numberOfRowsNeeded * 9) == maxNum) {

                columnSize = new int[9];
                rowsAndColumns[count] = columnSize;
            } else if (i <= remainingSlots) {
                columnSize = new int[9];
                rowsAndColumns[count] = columnSize;
            } else {
                columnSize = new int[slots];
                rowsAndColumns[count] = columnSize;
            }

            for (int k = 0; k < rowsAndColumns[count].length; k++) {

                rowsAndColumns[count][k] = i++;
            }
            count++;
        }

    }
}
