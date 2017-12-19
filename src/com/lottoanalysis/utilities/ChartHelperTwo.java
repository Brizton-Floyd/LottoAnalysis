package com.lottoanalysis.utilities;

import java.util.*;
import com.lottoanalysis.lottoinfoandgames.*;
/**
 *
 *
 */
@SuppressWarnings("unchecked")
public class ChartHelperTwo {

    //First index in object array will be the number hits for the group,
    // second element will be group hits, third element will games out, fourth element will be the hits at specified games out
    // and the fifth element will be an arraylist holding all game out data.
    private static Map<Integer[],Object[]> groupHitInformation = new LinkedHashMap<>();

    /**
     *This Method will take in a lottery game instance and all positional hits for a given lottery number position
     */
    public static void processIncomingData( LotteryGame lotteryGame, int[] drawPositionNumbers, int groupSize){

        // Deterimine how to split the number for a given lottery game
        splitLotteryGameDrawSizeIntoGroups( lotteryGame, groupSize );

        // start assigning numbers for the given drawPosition to the correct group ranges
        assignNumbersToGroups( drawPositionNumbers );

        // determine hits at games out
        findHitsAtCurrentGamesOut();

        print();
    }
    private static void print(){


        Map<String,Object[]> data = getGroupHitInformation();

        data.forEach( (key,value) -> {
            System.out.println("Key: " + key + "\t\tValue: " + Arrays.toString(value));
            System.out.println("\n");
        });

    }
    public static Map<String,Object[]> getGroupHitInformation(){

        Map<String,Object[]> data = new LinkedHashMap<>();

        groupHitInformation.forEach( (key,value) -> {

            Integer[] array;

            if(key.length > 1){
                array = new Integer[2];
                array[0] = key[0];
                array[1] = key[ key.length - 1];

            }else{
                array = key.clone();
            }

            data.put(Arrays.toString(array), value);
        });

        return data;
    }

    private static void findHitsAtCurrentGamesOut(){

        groupHitInformation.forEach( (key,value) -> {

            int currentGameOut = (int) value[2];
            //System.out.println(currentGameOut);
            List<Integer> gameOutNumbers = (List<Integer>) value[4];
            gameOutNumbers.forEach( number -> {

                if(number == currentGameOut){
                    value[3] = (int) value[3] + 1;
                }
            });

        });

    }

    /**
     * Method assigns numbers for a given winning lottery position to perspective draw group
     */
    private static void assignNumbersToGroups( int[] drawPositionNumbers ){

        groupHitInformation.forEach( (key,value) -> {

            List<Integer> data = Arrays.asList(key);
            plugInNumbers(data,drawPositionNumbers,value);
        });
    }
    private static void plugInNumbers(List<Integer> arrayAsList, int[] drawNumbers, Object[] groupData){

        for(int i = 0; i < drawNumbers.length; i++){

            if(arrayAsList.contains( drawNumbers[i] ) ){
                // first array list will be numbers to hit in group
                ((ArrayList<Integer>)groupData[0]).add(drawNumbers[i]);
                groupData[1] = (int)groupData[1] + 1;
                // second array list will be the game out hits for the given group
                ((ArrayList<Integer>)groupData[4]).add((int)groupData[2]);
                groupData[2] = 0;
                //incrementGamesOut(arrayAsList,groupData,false);

            }else{
                incrementGamesOut(arrayAsList,groupData);
            }
        }
    }
    private static void incrementGamesOut(List<Integer> groupNumbers, Object[] groupData){

        groupHitInformation.forEach( (key,value) -> {

            String arrayAsString = Arrays.toString(key);
            Integer[] data = groupNumbers.toArray(new Integer[groupNumbers.size()]);
            String listAsArrayString = Arrays.toString(data);

            if(arrayAsString.equals( listAsArrayString ) ){
                value[2] = (int) value[2] + 1;
            }

        });

    }
    /**
     *Clears all contents from the group hit map
     */
    public static void clearGroupHitInformation(){

        if(groupHitInformation != null)
            groupHitInformation.clear();
    }

    /**
     * This method will take in a given lottery game and determine the drawsize. Once draw size is determined distict groups
     * will then be placed into a map.
     */
    private static void splitLotteryGameDrawSizeIntoGroups( LotteryGame game, int groupSize){

        int minNumber = game.getMinNumber();
        int maxNumber = game.getMaxNumber();
        int count = 0;
        int currentNumberInIteration = 0;

        // get the max number which will determine how to split the group size.
        int groupDivisor = (maxNumber > groupSize) ? groupSize : maxNumber / 2 ;

        List<Integer> numberHolderList = new ArrayList<>();

        // start setting up to populate group hit map
        for(int i = 0; i < maxNumber; i++){

            if(count == groupDivisor){

                Integer[] groupArray = new Integer[groupDivisor];
                currentNumberInIteration = numberHolderList.get( numberHolderList.size() - 1 );

                for(int j = 0; j < numberHolderList.size(); j++){
                    groupArray[j] = numberHolderList.get(j);
                }
                // add array to groupmap. 0 index in object array will be the number hits for the group,
                // 1 index will be group hits, 2 index will games out, 3 index will be the hits at specified games out
                // and the 4 index will be an arraylist holding all game out data.
                groupHitInformation.put(groupArray,new Object[]{new ArrayList<Integer>(),0,0,0,new ArrayList<Integer>()});

                // reset counter backt to 1 and clear the list
                count = 0;
                numberHolderList.clear();
            }

            numberHolderList.add( (i+1) );
            count++;
        }
        // plug in last remaining numbers if there are any;
        int dif = Math.abs( currentNumberInIteration - maxNumber );
        Integer[] groupArray = new Integer[dif];
        for(int i = 0; i < groupArray.length; i++){
            currentNumberInIteration++;
            groupArray[i] = currentNumberInIteration;
        }

        groupHitInformation.put(groupArray,new Object[]{new ArrayList<Integer>(),0,0,0,new ArrayList<Integer>()});

    }
}