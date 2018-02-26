package com.lottoanalysis.utilities.betsliputilities;

import com.lottoanalysis.lottogames.drawing.Drawing;
import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.utilities.gameoutviewutilities.GamesOutPositionTracker;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;

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
    private List<Object> allInformationForRowAndPosition;
    private Map<Integer[], Map<String, Integer[]>> rowDirections;

    private Map<Double, Map<String, Integer[]>> positionalAverageHitsTracker;

    public LottoBetSlipAnalyzer(LotteryGame lotteryGame, int indexInDrawingData) {

        allInformationForRowAndPosition = new ArrayList<>();

        this.indexInDrawingData = indexInDrawingData;
        //rowsAndColumns = setUpRowsAndColumns(lotteryGame.getGameName());
        setUpRowsAndColumns(lotteryGame);
        findPositionalAverage(lotteryGame);
        int[] rowToPlay = analyzeBetSlip(rowsAndColumns, numbersInPosition);
        analyzeRemainderDueToHit(rowToPlay,numbersInPosition);
        //finalizeDataForDisplay(rowToPlay, numbersInPosition );
        determineGamesOutForNumbersAndLastPositionNumbersHitIn(lotteryGame);

    }

    private void analyzeRemainderDueToHit(int[] rowToPlay, List<Integer> numbersInPosition) {
        List<Integer> numbersInRow = new ArrayList<>();
        Map<Integer, Object[]> remainderAndNumbers = new TreeMap<>();
        Map<Integer,Boolean> numberFlags = new TreeMap<>();

        for(int num : rowToPlay){
            numbersInRow.add(num);
            numberFlags.put(num,false);
        }

        for(int num : numbersInPosition){

            if(numbersInRow.contains(num)){

                boolean result = numberFlags.get(num);
                result = true;
                numberFlags.put(num, result);

                int remainder = num % 3;

                if(!remainderAndNumbers.containsKey(remainder)){

                    Map<Integer, Integer[]> data = new TreeMap<>();
                    remainderAndNumbers.put(remainder, new Object[]{new TreeMap<Integer,Integer[]>(), 0,0});

                    if(!data.containsKey(num)){
                        data.put(num, new Integer[]{1, 0});
                        incrementgamesOut(data, num);

                        Object[] objects = remainderAndNumbers.get(remainder);
                        objects[0] = data;
                        objects[1] = (int)objects[1] + 1;
                        objects[2] = 0;
                        remainderAndNumbers.put(remainder, objects );
                    }
                    else{
                        Object[] objects = remainderAndNumbers.get(remainder);
                        Map<Integer, Integer[]> innerHashmap = (Map<Integer, Integer[]>)objects[0];
                        Integer[] vals = innerHashmap.get(num);
                        vals[0]++;
                        vals[1] = 0;
                        incrementgamesOut(innerHashmap, num);
                    }

                    incrementMultipleGamesOut(remainderAndNumbers, remainder);

                }
                else{

                    Object[] objects = remainderAndNumbers.get(remainder);
                    objects[1] = (int)objects[1]+1;
                    objects[2] = 0;

                    Map<Integer, Integer[]> data = (Map<Integer, Integer[]>)objects[0];

                    if(!data.containsKey(num)){
                        data.put(num, new Integer[]{1,0});
                        incrementgamesOut(data, num);
                        objects[0] = data;
                        remainderAndNumbers.put(remainder, objects);
                    }else{
                        Integer[] gameInfo = data.get(num);
                        gameInfo[0]++;
                        gameInfo[1] = 0;
                        incrementgamesOut(data, num);
                        data.put(num, gameInfo);
                        objects[0] = data;
                        remainderAndNumbers.put(remainder, objects);
                    }

                    incrementMultipleGamesOut(remainderAndNumbers, remainder);
                }
            }
        }

        for(Map.Entry<Integer, Boolean> d : numberFlags.entrySet()){
            boolean val = d.getValue();
            if(!val){
                int num = d.getKey();
                int remainder = num % 3;
                if(remainderAndNumbers.containsKey(remainder)){
                    Object[] data = remainderAndNumbers.get(remainder);
                    Map<Integer, Integer[]> numbers = (Map<Integer, Integer[]>)data[0];
                    if(!numbers.containsKey(num)){
                        numbers.put(num, new Integer[]{0,0});
                    }
                }
            }
        }
        allInformationForRowAndPosition.add(rowToPlay);
        allInformationForRowAndPosition.add(remainderAndNumbers);
    }

    private void incrementMultipleGamesOut(Map<Integer, Object[]> remainderAndNumbers, int remainder) {

        for(Map.Entry<Integer,Object[]> dd : remainderAndNumbers.entrySet()){

            if(dd.getKey() != remainder) {
                Object[] data = dd.getValue();
                data[2] = (int)data[2] + 1;
            }

        }
    }

    private int[][] setUpRowsAndColumns(String gameName) {

        Map<String, Integer[][]> gamePayslipFormation = new HashMap<>();

        Integer[][] payslipSetUp = {{1,5,9,13,17,21,25,29,33,37}, {2,6,10,14,18,22,26,30,34,38},
                                    {3,7,11,15,19,23,27,31,35,39},{4,8,12,16,20,24,28,32,36}};

        if(!gamePayslipFormation.containsKey(gameName)){

            if(gameName.equals("FantasyFive"))
                gamePayslipFormation.put(gameName, payslipSetUp);
        }

        int[][] data = new int[gamePayslipFormation.get(gameName).length][];

        for(int i = 0; i < gamePayslipFormation.get(gameName).length; i++){

            data[i] = new int[gamePayslipFormation.get(gameName)[i].length];

            for(int k = 0; k < gamePayslipFormation.get(gameName)[i].length; k++){
                data[i][k] = gamePayslipFormation.get(gameName)[i][k];
            }
        }

        return data;
    }

    /**
     * Method will return a list of objects containing all the information about the selected row in order to make
     * an informed decision of which numbers to play for the next draw.
     * @return
     */
    public List<Object> getAllInformationForRowAndPosition() {
        return allInformationForRowAndPosition;
    }

    /**
     * Method will return a suggestion of the best numbers to play for the given draw position. The return object will
     * contain a map with a detail count of how many ties the given digit has hit in the given draw position. The result
     * set will also contain a lottogames out counter for each digit and the last position the number was drawn in
     * @return
     */
    public Map<Double, Map<String, Integer[]>> getPositionalAverageHitsTracker() {
        return positionalAverageHitsTracker;
    }
    private void determineGamesOutForNumbersAndLastPositionNumbersHitIn(LotteryGame lotteryGame) {

        Set<Integer> allKeyvals = new TreeSet<>();

        int[] datas  = (int[]) allInformationForRowAndPosition.get(0);
        for(int a : datas)
            allKeyvals.add(a);

        GamesOutPositionTracker tracker = new GamesOutPositionTracker(allKeyvals, lotteryGame);

        Map<Integer, Object[]> data = tracker.getAnalysisForCurrentRow();

        //determineLastElementValueToPlay();
        allInformationForRowAndPosition.add(data);
        allInformationForRowAndPosition.add(rowDirections);
        allInformationForRowAndPosition.add(rowsAndColumns);
        allInformationForRowAndPosition.add(positionalAverageHitsTracker);
    }

    private void determineLastElementValueToPlay() {
        int number = numbersInPosition.get(numbersInPosition.size() - 1);
        Map<Integer,Map<Integer,Integer[]>> values = new TreeMap<>();
        List<Integer> elementValues = new LinkedList<>();
        int elementValue;

        for(int num : numbersInPosition){

            if(Integer.toString(num).length() > 1)
                num = Integer.parseInt(Character.toString(Integer.toString(num).charAt(1)));

            elementValues.add(num);
        }


        boolean isGreaterThanOne = (Integer.toString(number).length() > 1);
        if(isGreaterThanOne)
            elementValue = Integer.parseInt(Character.toString(Integer.toString(number).charAt(1)));
        else
            elementValue = number;

        values.put(elementValue, new TreeMap<>());

        for(int i = 0; i < elementValues.size(); i++){

            if( i < elementValues.size() - 1 && elementValues.get(i) == elementValue){
                Map<Integer, Integer[]> data = values.get(elementValue);
                if(!data.containsKey(elementValues.get(i + 1))){
                    data.put(elementValues.get(i +1), new Integer[]{1,0});
                }
                else {
                    Integer[] val = data.get(elementValues.get(i +1));
                    val[0]++;
                    val[1] = 0;
                    incrementgamesOut(data, elementValues.get(i +1));
                }
            }
        }
    }

    private void incrementgamesOut(Map<Integer, Integer[]> data, Integer integer) {

        for(Map.Entry<Integer,Integer[]> d : data.entrySet()){

            if(d.getKey() != integer){
                Integer[] dd = d.getValue();
                dd[1]++;
            }
        }
    }

    private void finalizeDataForDisplay(int[] rowToPlay, List<Integer> numbersInPosition) {

        double average = 0;

        for(double avg : positionalAverageHitsTracker.keySet()){
            average = avg;
        }

        String direction = "";
        List<Integer> counts = new ArrayList<>();
        for(Map.Entry<Double, Map<String, Integer[]>> rangeHits : positionalAverageHitsTracker.entrySet()){
            Map<String, Integer[]> directs = rangeHits.getValue();
            for(Map.Entry<String, Integer[]> r : directs.entrySet()){
                Integer[] hits = r.getValue();
                counts.add(hits[0]);
                Collections.sort(counts);
            }

            for(Map.Entry<String, Integer[]> uu : directs.entrySet()){
                if(uu.getValue()[0] == counts.get(counts.size() - 1)){
                    direction = uu.getKey();
                    break;
                }
            }
        }

        List<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < rowToPlay.length; i++){

            if(direction.equals("Up")){
                if(rowToPlay[i] > average) {
                    numbers.add(rowToPlay[i]);
                    Collections.sort(numbers);
                }
            }
            else if(direction.equals("Less")){
                if(rowToPlay[i] < average) {
                    numbers.add(rowToPlay[i]);
                    Collections.sort(numbers);
                }
            }
            else if(direction.equals("Equals")){
                if(rowToPlay[i] == average) {
                    numbers.add(rowToPlay[i]);
                    break;
                }
            }
        }

        int index = 0;
        int[] modifiedRowOfNumbers = new int[numbers.size()];
        Map<Integer,Integer> numberAndHitTracker = new TreeMap<>();

        for(int n : numbers){
            numberAndHitTracker.put(n, 0);
            modifiedRowOfNumbers[index++] = n;
        }

        for(int num : numbersInPosition){
            if(numberAndHitTracker.containsKey(num)){
                int n = numberAndHitTracker.get(num);
                n++;
                numberAndHitTracker.put(num, n);
            }
        }

        allInformationForRowAndPosition.add(numberAndHitTracker);
    }


    /**
     * This method will determine which row the user should play for the next drawing for the given lotto position
     *
     * @param rowsAndColumns
     * @param numbersInPosition
     */
    private int[] analyzeBetSlip(int[][] rowsAndColumns, List<Integer> numbersInPosition) {

        Map<Integer[], Map<String, Integer[]>> directionAnalysis = new HashMap<>();
        int[][] beginningAndEndValuesForRows = new int[rowsAndColumns.length][2];
        loadBeginningAndEndValues(beginningAndEndValuesForRows, rowsAndColumns);

        int recentNumberToHitInPos = numbersInPosition.get(numbersInPosition.size() - 1);
        int locationInMatrix = 0;
        int count = 0;
        boolean check = true;
        Integer[] newRow = null;

        // Get the last index of the current winning number and remove from the list
        int lastIndex = numbersInPosition.lastIndexOf(recentNumberToHitInPos);
        numbersInPosition.remove( lastIndex );

        for (int j = 0; j < numbersInPosition.size(); j++) {

            // if current winning number is in the list after being removed proceed
            if ( numbersInPosition.contains(recentNumberToHitInPos) ) {

                // since the current winning number is in the list add the number back at its last index of removal
                // Only if j is equal to zero
                if(j == 0)
                    numbersInPosition.add(lastIndex, recentNumberToHitInPos);

                if (numbersInPosition.get(j) == recentNumberToHitInPos && j < numbersInPosition.size() - 1) {

                    int nextNum = numbersInPosition.get(j + 1);

                    for (int i = count; i < rowsAndColumns.length; i++) {

                        for (int k = 0; k < rowsAndColumns[i].length; k++) {

                            if ((Arrays.stream(rowsAndColumns[i]).anyMatch(x -> x == recentNumberToHitInPos))) {

                                if (check)
                                    newRow = Arrays.stream(rowsAndColumns[i]).boxed().toArray(Integer[]::new);

                                check = false;
                                determineRowNextNumFallsIn(locationInMatrix, newRow, nextNum, directionAnalysis,
                                        beginningAndEndValuesForRows);
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
            else{

                return rowsAndColumns[0];
            }
        }

        rowDirections = directionAnalysis;

        // get total hits for each row and sort from least to greatest
        int[] rowToPlayForNextDraw = null;
        List<Integer> hitCounter = new ArrayList<>();

        for(Map.Entry<Integer[], Map<String, Integer[]>> dd : directionAnalysis.entrySet()){
            Map<String, Integer[]> directions = dd.getValue();
            for (Iterator<String> it = directions.keySet().iterator(); it.hasNext(); ) {
                Integer[] d = directions.get(it.next());
                hitCounter.add(d[0]);
                Collections.sort(hitCounter);
            }

            // determine which row is equal the greatest hits in the list
            for(Map.Entry<String, Integer[]> values : directions.entrySet()){

                if(hitCounter.get(hitCounter.size() -1 ) == values.getValue()[0]){
                    String direction = values.getKey();
                    if(direction.equals("Equal")){
                        rowToPlayForNextDraw = Arrays.stream(dd.getKey()).mapToInt(Integer::intValue).toArray();
                    }else {

                        int[] arr = Arrays.stream(dd.getKey()).mapToInt(Integer::intValue).toArray();
                        List allArray = Arrays.asList(rowsAndColumns);
                        int index = 0;

                        for(Object p : allArray){
                            int[] pp = (int[])p;
                            if(Arrays.equals(pp,arr)){
                                index = allArray.indexOf(pp);
                                break;
                            }
                        }

                        int directionIndicator = directionMapper(direction);
                        boolean lessThanZero = (directionIndicator < 0);
                        if(lessThanZero){
                            directionIndicator *= -1;
                            rowToPlayForNextDraw = rowsAndColumns[index + directionIndicator];

                        }else{
                            rowToPlayForNextDraw = rowsAndColumns[index - directionIndicator];
                        }


                    }

                    break;
                }
            }
        }

        // Determine what sum is due to hit for the given row
        return rowToPlayForNextDraw;
    }

    private void determineSumLikelyToHit(int[] rowToPlayForNextDraw) {

        List<Integer> rowNumbers = new ArrayList<>();
        Map<Integer,Integer[]> columnSums = new HashMap<>();
        int sum;
        for(int num : rowToPlayForNextDraw){
            if(Integer.toString(num).length() > 1){
                int elementZero =  Integer.parseInt(Character.toString(Integer.toString(num).charAt(0)));
                int elementOne = Integer.parseInt(Character.toString(Integer.toString(num).charAt(1)));
                sum = elementOne + elementZero;

            }else{
                sum = num;
            }

            columnSums.put(sum, new Integer[]{0,0});
            rowNumbers.add(num);
        }

        for(int num : numbersInPosition){
            if(rowNumbers.contains(num)){

                if(Integer.toString(num).length() > 1){
                    int elementZero =  Integer.parseInt(Character.toString(Integer.toString(num).charAt(0)));
                    int elementOne = Integer.parseInt(Character.toString(Integer.toString(num).charAt(1)));
                    sum = elementOne + elementZero;

                }else{
                    sum = num;
                }

                Integer[] vals = columnSums.get(sum);
                vals[0]++;
                vals[1] = 0;
                incrementgamesOut(columnSums, sum);
            }
        }

    }

    private int directionMapper(String direction) {

        switch (direction){

            case "Up 1":
                return 1;
            case "Up 2":
                return 2;
            case "Up 3":
                return 3;
            case "Up 4":
                return 4;
            case "Up 5":
                return 5;
            case "Up 6":
                return 6;
            case "Up 7":
                return 7;
            case "Down 1":
                return -1;
            case "Down 2":
                return -2;
            case "Down 3":
                return -3;
            case "Down 4":
                return -4;
            case "Down 5":
                return -5;
            case "Down 6":
                return -6;
            case "Down 7":
                return -7;

        }
        return 0;
    }

    /**
     * This method will populate a map with the direction the next draw number tends to fall after the current number is
     * drawn
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
            else if (indexInDrawingData == 7)
                numbersInPosition.add(Integer.parseInt(drawing.getBonusNumber()));

        }
        for (int num : numbersInPosition)
            sum += num;

        positionalAverage = (lotteryGame.getMaxNumber() == 9) ? (sum / numbersInPosition.size()) : (sum / numbersInPosition.size()) + 4;

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

        int lowNum;
        int maxNum;
        // get the min and max numbers
        if(indexInDrawingData < 7) {
            lowNum = lotteryGame.getMinNumber();
            maxNum = lotteryGame.getMaxNumber();
        }
        else{
            if(lotteryGame.getGameName().toLowerCase().equals("powerball")){
                lowNum = 1;
                maxNum = 26;
            }
            else if(lotteryGame.getGameName().toLowerCase().equals("mega millions")){
                lowNum = 1;
                maxNum = 15;
            }
            else{
                lowNum = 1;
                maxNum = 27;
            }
        }
        int divisor;
        int numberOfRowsNeeded;
        int remainderRowIfNeeded;
        int trueAmountOfRowsNeeded;
        int slots;
        int remainingSlots;

        if(maxNum == 9){

            divisor = 5;
            numberOfRowsNeeded = maxNum / divisor;
            remainderRowIfNeeded = (maxNum - (numberOfRowsNeeded * divisor)) > 0 ? 1 : 0;
            trueAmountOfRowsNeeded = numberOfRowsNeeded + remainderRowIfNeeded;
            slots = (maxNum - (numberOfRowsNeeded * divisor));
            remainingSlots = maxNum - slots;

        }else{
            divisor = (int)Math.ceil(maxNum / 2.0);
            numberOfRowsNeeded = maxNum / divisor;
            remainderRowIfNeeded = (maxNum - (numberOfRowsNeeded * divisor)) > 0 ? 1 : 0;
            trueAmountOfRowsNeeded = numberOfRowsNeeded + remainderRowIfNeeded;
            slots = (maxNum - (numberOfRowsNeeded * divisor));
            remainingSlots = maxNum - slots;
        }

        rowsAndColumns = new int[trueAmountOfRowsNeeded][];
        int count = 0;
        for (int i = lowNum; i <= maxNum; ) {

            int[] columnSize;
            if ((numberOfRowsNeeded * divisor) == maxNum) {

                columnSize = new int[divisor];
                rowsAndColumns[count] = columnSize;
            } else if (i <= remainingSlots) {
                columnSize = new int[divisor];
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
