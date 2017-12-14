package com.lottoanalysis.utilities;

import java.util.*;

public class GamesOutAnalyzerHelper{

    private static Map<Integer, Object[]> gamesOutAndHitTracker = new TreeMap<>();
    private static List<Integer[]> drawHolder = new ArrayList<>();
    private static int numberHitCount;
    private static Object[] lottoNumSpecificData;

    public static void analyze( int[][] positionValues, int drawPosition, String dropDownMenuValue ){
        fillDrawHolder( positionValues.length, positionValues );
        placeNumbersIntoTracker( positionValues );
        loadUpLottoNumSpecificData( positionValues, drawPosition, Integer.parseInt(dropDownMenuValue));
    }

    /**
     * Returns an object Array consisting of three elements. With index: 0 being the lottoNumber hit count, index: 1
     * the amount of games the lotto number is out
     * and index: 2 being the last position the lotto number hit in.
     */
    public static Object[] getLottoNumSpecificData(){
        return lottoNumSpecificData;
    }

    private static void loadUpLottoNumSpecificData(int[][] drawPositions, int index, int lottoNumber){
        int count = 0;

        List<Integer> nums = new ArrayList<>();

        for( int num : drawPositions[index] ){

            if(num == lottoNumber)
                count++;

        }
        lottoNumSpecificData = new Object[3];
        lottoNumSpecificData[0] = count;
        lottoNumSpecificData[1] = gamesOutAndHitTracker.get( lottoNumber )[0];
        lottoNumSpecificData[2] = gamesOutAndHitTracker.get( lottoNumber )[1];
    }

    /**
     * Method will take in all positions that make up a lotto number. And analyze how many games out lotto number is. As well as the last position
     * the lotto number hit in.
     *@param values
     */
    private static void placeNumbersIntoTracker( int[][] values ){

        Map<Integer,Integer> numberAndIndexHolder = new TreeMap<>();
        Set<Integer> numbersDueForIncrement = new HashSet<>();

        for( int i = 1; i < drawHolder.size() ; i++){

            Integer[] previousDrawResult = drawHolder.get( i-1);
            Integer[] currentDrawResult = drawHolder.get(  i );

            List<Integer> previousWinningNumbers = Arrays.asList( previousDrawResult);
            List<Integer> currentWinningNumbers = Arrays.asList( currentDrawResult );

            if(numbersDueForIncrement.size() > 0)
                removeWinningNumbersFromSetIfPresent( numbersDueForIncrement, currentWinningNumbers);

            for( int j = 0; j < previousWinningNumbers.size(); j++ ){

                if( !currentWinningNumbers.contains( previousWinningNumbers.get(j) )) {

                    if( !gamesOutAndHitTracker.containsKey(previousWinningNumbers.get(j))){
                        gamesOutAndHitTracker.put(previousWinningNumbers.get(j), new Object[]{0,"Pos: " + (j+1)});
                    }
                    numbersDueForIncrement.add( previousWinningNumbers.get(j) );
                }
            }

            incrementGamesOut( numbersDueForIncrement, gamesOutAndHitTracker);

            for(int j = 0; j < currentWinningNumbers.size(); j++){

                if( !gamesOutAndHitTracker.containsKey( currentWinningNumbers.get(j) )){
                    gamesOutAndHitTracker.put(currentWinningNumbers.get(j), new Object[]{0, "Pos: " + (j+1)} );
                }else{
                    Object[] dataValue = gamesOutAndHitTracker.get( currentWinningNumbers.get(j) );
                    dataValue[0] = 0;
                    dataValue[1] = "Pos: " + (j+1);
                }
            }
        }
    }

    /**
     * Method will remove a lotto number from the a set of non winnng numbers. If the lotto number is now a winner.
     *@param nums
     *@param winningNums
     */
    private static void removeWinningNumbersFromSetIfPresent(Set<Integer> nums, List<Integer> winningNums){

        for(Iterator<Integer> n = nums.iterator(); n.hasNext();){

            if(winningNums.contains( n.next() ) ){
                n.remove();
            }
        }
    }

    /**
     * Method will increment all games out applicable to the lotto numbers contained in a set.
     *@param numsDueForIncrement
     *@param data
     */
    private static void incrementGamesOut(Set<Integer> numsDueForIncrement, Map<Integer,Object[]> data){

        for(Iterator<Integer> numsToIncrementIterator = numsDueForIncrement.iterator(); numsToIncrementIterator.hasNext();){

            Integer num = numsToIncrementIterator.next();
            Object[] values = data.get( num );
            values[0] = (int)values[0]+1;

        }
    }

    /**
     * Method will take all numbers in each draw position and form a draw sequence for each index.
     *@param length
     *@param drawPositions
     */
    private static void fillDrawHolder( int length, int[][] drawPositions ){
        int totalElements = drawPositions[0].length;
        int count = 0;
        int[] drawPosition;

        while( count < totalElements){
            drawPosition = new int[ length ];
            for( int i = 0; i < drawPositions.length; i++ ){

                drawPosition[i] = drawPositions[i][count];

            }
            Integer[] p = Arrays.stream( drawPosition ).boxed().toArray(Integer[]::new);
            drawHolder.add( p );
            count++;
        }

    }
}