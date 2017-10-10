package line_chart_helper;

import javafx.scene.control.Label;
import model.LotteryGame;
import org.omg.PortableInterceptor.INACTIVE;
import utils.NumberPatternAnalyzer;


import java.util.*;
import java.util.List;

public class LineRetracementAnalyzer {

    private static int currentWinningNumber;
    private static int nextWinningNumber;
    private static LotteryGame game;
    private static List<Integer> numberIndexes;
    private static int[] numbers;

    public static Map<String, Integer[]> findAboveAndBelowHitsForPrecedingDigit(int[] positionNumbers) {

        numbers = positionNumbers;

        Map<String, Integer[]> aboveLowerTracker = new TreeMap<>();
        loadTracker(aboveLowerTracker);

        currentWinningNumber = positionNumbers[positionNumbers.length - 1];
        nextWinningNumber = 0;

        // Add numbers to list
        List<Integer> numberData = new ArrayList<>();
        for (int num : positionNumbers) {
            numberData.add(num);
        }

        // add all indexes of current winning positionNumbers
        numberIndexes = new ArrayList<>();
        int count = 0;
        for (int num : numberData) {
            if (num == currentWinningNumber) {
                numberIndexes.add(count++);
            } else {
                count++;
            }
        }

        //System.out.print(numberData.size());
        int index = numberIndexes.get(numberIndexes.size() - 2);

        nextWinningNumber = numberData.get(index + 1);

        for (int i = 0; i < positionNumbers.length; i++) {

            if (i < positionNumbers.length - 1 && positionNumbers[i] == currentWinningNumber) {

                int nextNum = positionNumbers[i + 1];
                if (nextNum > nextWinningNumber) {

                    Integer[] aboveData = aboveLowerTracker.get("Above");
                    aboveData[0]++;
                    aboveData[1] = 0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(aboveLowerTracker, "Above");
                } else if (nextNum < nextWinningNumber) {
                    Integer[] belowData = aboveLowerTracker.get("Below");
                    belowData[0]++;
                    belowData[1] = 0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(aboveLowerTracker, "Below");
                } else {
                    Integer[] equalData = aboveLowerTracker.get("Equal");
                    equalData[0]++;
                    equalData[1] = 0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(aboveLowerTracker, "Equal");

                }
            }
        }

        return aboveLowerTracker;
    }

    public static int getCurrentWinningNumber() {
        return currentWinningNumber;
    }

    public static int getNextWinningNumber() {
        return nextWinningNumber;
    }

    private static void loadTracker(Map<String, Integer[]> aboveLowerTracker) {
        aboveLowerTracker.put("Above", new Integer[]{0, 0});
        aboveLowerTracker.put("Below", new Integer[]{0, 0});
        aboveLowerTracker.put("Equal", new Integer[]{0, 0});
    }

    public static Object[] populateAbovePreviousRanges(int nextWinningNumber, LotteryGame game) {

        Object[] listData = new Object[2];
        int trueAmountOfGroups;
        int difBetweenNumberAndGameMax = Math.abs(game.getMaxNumber() - nextWinningNumber);
        int roughAmountOfGroups = difBetweenNumberAndGameMax / 5;

        List<Label[]> aboveData = null;

        if (roughAmountOfGroups > 0) {
            boolean equalsDiff = (roughAmountOfGroups * 5 == difBetweenNumberAndGameMax);
            if (equalsDiff) {
                trueAmountOfGroups = roughAmountOfGroups;
                aboveData = new ArrayList<>();
                loadArraysIntoList(aboveData, trueAmountOfGroups);
                loadLabelsIntoArray(aboveData, nextWinningNumber, "Up", game);
            } else {
                int extraGroupsNeeded = difBetweenNumberAndGameMax - (roughAmountOfGroups * 5);
                if (extraGroupsNeeded < 5) {
                    trueAmountOfGroups = roughAmountOfGroups + 1;
                    aboveData = new ArrayList<>();
                    loadArraysIntoList(aboveData, trueAmountOfGroups);
                    loadLabelsIntoArray(aboveData, nextWinningNumber, "Up", game);
                }
            }
        } else {
            trueAmountOfGroups = 1;
            aboveData = new ArrayList<>();
            loadArraysIntoList(aboveData, trueAmountOfGroups);
            loadLabelsIntoArray(aboveData, nextWinningNumber, "Up", game);
        }


        List<Label[]> belowData = populateBelowPreviousRanges(nextWinningNumber, game);
        if (analyzeLabelsForCorrectness(aboveData, belowData, game)) {
            listData[0] = aboveData;
            listData[1] = belowData;
        }

        return listData;
    }

    /**
     * Method will analyze all labels for correctness
     *
     * @param aboveData
     * @param belowData
     * @return
     */
    private static boolean analyzeLabelsForCorrectness(List<Label[]> aboveData, List<Label[]> belowData, LotteryGame game) {
        boolean result = false;
        int count = 0;
        for (Iterator<Label[]> label = aboveData.iterator(); label.hasNext(); ) {
            Label[] aboveLabel = label.next();
            String[] aboveLabelData = aboveLabel[0].getText().trim().split("[ \\t]+");

            int num = Integer.parseInt(aboveLabelData[0]);
            int otherNum = Integer.parseInt(aboveLabelData[2]);

            if (num > game.getMaxNumber() || otherNum > game.getMaxNumber()) {

                String newText;

                if (num > game.getMaxNumber()) {
                    label.remove();

                } else {

                    newText = (aboveLabelData[0].equals( Integer.toString( game.getMaxNumber() ))) ? aboveLabelData[0] :
                                                                        aboveLabelData[0] + " - " + game.getMaxNumber();
                    aboveLabel[0].setText(newText);
                    result = true;
                }

            }else{
                count++;
            }
        }

        count = 0;

        for (Iterator<Label[]> label = belowData.iterator(); label.hasNext(); ) {
            Label[] belowLabel = label.next();
            String[] belowLabelData = belowLabel[0].getText().trim().split("[ \\t]+");

            int num = Integer.parseInt(belowLabelData[0]);
            int otherNum = Integer.parseInt(belowLabelData[2]);

            if(num == game.getMinNumber()) {


                count++;
                result = true;
            }

            if (num < game.getMinNumber() || otherNum < game.getMinNumber()) {

                String newText;

                if ( num < game.getMinNumber()  ) {

                    if(count == 0) {
                        newText = (num < game.getMinNumber() && otherNum > game.getMinNumber()) ? game.getMinNumber() + " - " + otherNum :
                                Integer.toString(game.getMinNumber());

                        belowLabel[0].setText(newText);
                        result = true;
                        count++;
                    }
                    else{
                        label.remove();
                    }
                }

            }
        }

        return result;
    }

    private static List<Label[]> populateBelowPreviousRanges(int nextWinningNumber, LotteryGame game) {

        int trueAmountOfGroups;
        int difBetweenNumberAndGameMax = Math.abs(game.getMinNumber() - nextWinningNumber);
        int roughAmountOfGroups = difBetweenNumberAndGameMax / 5;

        List<Label[]> labelData = null;

        if (roughAmountOfGroups > 0) {
            boolean equalsDiff = (roughAmountOfGroups * 5 == difBetweenNumberAndGameMax);
            if (equalsDiff) {
                trueAmountOfGroups = roughAmountOfGroups;
                labelData = new ArrayList<>();
                loadArraysIntoList(labelData, trueAmountOfGroups);
                loadLabelsIntoArray(labelData, nextWinningNumber, "Down", game);
            } else {
                int extraGroupsNeeded = difBetweenNumberAndGameMax - (roughAmountOfGroups * 5);
                if (extraGroupsNeeded < 5) {
                    trueAmountOfGroups = roughAmountOfGroups + 1;
                    labelData = new ArrayList<>();
                    loadArraysIntoList(labelData, trueAmountOfGroups);
                    loadLabelsIntoArray(labelData, nextWinningNumber, "Down", game);
                }
            }
        } else {
            trueAmountOfGroups = 1;
            labelData = new ArrayList<>();
            loadArraysIntoList(labelData, trueAmountOfGroups);
            loadLabelsIntoArray(labelData, nextWinningNumber, "Down", game);
        }

        return labelData;
    }

    private static void loadLabelsIntoArray(List<Label[]> labelData,
                                            int nextWinningNumber, String direction, LotteryGame game) {

        int startingDigit;

        if (direction.equals("Up"))
            startingDigit = nextWinningNumber + 1;
        else
            startingDigit = nextWinningNumber - 1;

        int count = 0;

        if (direction.equals("Up")) {

            for (int i = 0; i < labelData.size(); i++) {

                //  if(startingDigit < game.getMaxNumber()) {
                Label label = new Label(String.format("%1s - %2s", startingDigit, (startingDigit + 5)));
                label.setOnMouseEntered(event -> {

                });
                Label[] data = labelData.get(i);


                startingDigit += 6;

                data[0] = label;
                //  }

            }
        } else {

            for (int i = 0; i < labelData.size(); i++) {
                Label label = new Label(String.format("%1s - %2s", (startingDigit - 5), startingDigit));
                label.setOnMouseEntered(event -> {
                });
                Label[] data = labelData.get(i);

                startingDigit -= 6;

                data[0] = label;

            }


        }

    }

    private static void loadArraysIntoList(List<Label[]> labelData, int size) {

        for (int i = 0; i < size; i++) {
            Label[] data = new Label[1];
            labelData.add(data);
        }
    }

    public static Map<Integer,Integer[]> calculateLineSpacings() {

        Map<Integer, Integer[]> data = new TreeMap<>();
        Map<Integer, Integer[]> gamesOutHitTracker = new TreeMap<>();

        List<Integer> allNumbers = new ArrayList<>();

        if(numbers == null && numberIndexes == null )
            return null;


        for(int i : numbers) {
            allNumbers.add(i);
        }


        for(int i = 0; i < numberIndexes.size(); i++){

            if(i < numberIndexes.size() - 1) {
                //int currentWinningNumber = allNumbers.get(numberIndexes.get(i));
                int nextWinningNumber = allNumbers.get(numberIndexes.get(i) + 1);

               // int compareNumber = Math.abs(currentWinningNumber - nextWinningNumber);

                //if (compareNumber > 0) {

                    //int lineSpaceBetweenNumber = Math.abs((currentWinningNumber - nextWinningNumber)) - 1;
                    if (!data.containsKey(nextWinningNumber)) {

                        data.put(nextWinningNumber, new Integer[]{1, 0});
                        NumberAnalyzer.incrementGamesOut(data, nextWinningNumber);

                    } else {

                        Integer[] vals = data.get(nextWinningNumber);
                        vals[0]++;

                        if( !gamesOutHitTracker.containsKey(vals[1]) ){

                            gamesOutHitTracker.put( vals[1], new Integer[]{1,0});
                            NumberAnalyzer.incrementGamesOut( gamesOutHitTracker, vals[1] );

                        }
                        else{
                            Integer[] gamesOutData = gamesOutHitTracker.get( vals[1] );
                            gamesOutData[0]++;
                            gamesOutData[1] = 0;
                            NumberAnalyzer.incrementGamesOut( gamesOutHitTracker, vals[1] );
                        }

                        vals[1] = 0;
                        NumberAnalyzer.incrementGamesOut(data, nextWinningNumber);
                    }
               // }
//                else {
//
//                    int newNum = compareNumber - 1;
//                    if(!data.containsKey(newNum)){
//
//                        data.put( newNum, new Integer[]{1,0} );
//                        NumberAnalyzer.incrementGamesOut( data, newNum );
//
//                    }else{
//
//                        Integer[] vals = data.get( newNum );
//                        vals[0]++;
//
//                        if( !gamesOutHitTracker.containsKey( vals[1]) ) {
//
//                            gamesOutHitTracker.put( vals[1], new Integer[]{1,0});
//                            NumberAnalyzer.incrementGamesOut( gamesOutHitTracker, vals[1] );
//                        }
//                        else{
//
//                            Integer[] gamesOutData = gamesOutHitTracker.get( vals[1] );
//                            gamesOutData[0]++;
//                            gamesOutData[1] = 0;
//                            NumberAnalyzer.incrementGamesOut( gamesOutHitTracker, vals[1] );
//                        }
//
//                        vals[1] = 0;
//                        NumberAnalyzer.incrementGamesOut( data, newNum );
//                    }
//                }
            }
        }
        return data;
    }
}
