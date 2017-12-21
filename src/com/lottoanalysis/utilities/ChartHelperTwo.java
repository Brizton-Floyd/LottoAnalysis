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
		
		// analyze games out information
		plugInNumbersForCurrentGamesOut();

        //print();
    }
	
	private static void plugInNumbersForCurrentGamesOut(){
	
		List<Integer> gamesOutIndexHolder = new ArrayList<>();
		groupHitInformation.forEach( (key,value) -> {
			
			Object[] data = value;
			List<Integer> arrayListData = (List<Integer>) data[4];
			for(int n = 0; n < arrayListData.size(); n++){
				if(arrayListData.get(n) == (int)data[2]){
			
					gamesOutIndexHolder.add(n);
					
				}
			}
			
			//System.out.println(Arrays.toString(gamesOutIndexHolder.toArray()));
			
			if(gamesOutIndexHolder.size() > 1 ){
		    	List<Integer> diferenceHolder = new ArrayList<>();
		    	for(int i = 0; i < gamesOutIndexHolder.size() - 1; i++){
		    	    
		    	    int dif = gamesOutIndexHolder.get( i + 1 ) - gamesOutIndexHolder.get(i);
		    	    diferenceHolder.add(dif);
		    	    
		    	}
		    	
		    	// compute the averate
		    	double avg = 0.0; 
		    	int sum = 0; 
		    	
		    	for(int num : diferenceHolder){
		    	    sum += num;
		    	}
		    	
		    	avg = sum / diferenceHolder.size();
		    	value[5] = avg;
		    	
		    	for(int num : diferenceHolder){
		    	    if(num > avg)
		    	        value[6] = (int)value[6] + 1;
		    	    else if( num < avg)
		    	        value[7] = (int)value[7] + 1;
		    	    else
		    	        value[8] = (int)value[8] + 1;
		    	}
		    	
		    	int index = arrayListData.lastIndexOf( arrayListData.get( arrayListData.size() - 1) );
		    	int currentGamesOut = index - gamesOutIndexHolder.get(gamesOutIndexHolder.size() - 1 );
		    	value[9] = currentGamesOut;
		    	
				//System.out.println(Arrays.toString(diferenceHolder.toArray()));
				
			}
	
			gamesOutIndexHolder.clear();

		});
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
        for(int i = minNumber; i < maxNumber; i++){

            if(count == groupDivisor){

                Integer[] groupArray = new Integer[groupDivisor];
                currentNumberInIteration = numberHolderList.get( numberHolderList.size() - 1 );

                for(int j = 0; j < numberHolderList.size(); j++){
                    groupArray[j] = numberHolderList.get(j);
                }
                // add array to groupmap. 0 index in object array will be the number hits for the group,
                // 1 index will be group hits, 2 index will games out, 3 index will be the hits at specified games out
                // and the 4 index will be an arraylist holding all game out data, index 5 will out avg skips, index 6 will be hits above avg,
                // index 7 will be hits below avg, index 8 will be
				// current games out for the specified out.
                groupHitInformation.put(groupArray,new Object[]{new ArrayList<Integer>(),0,0,0,new ArrayList<Integer>(),0.0,0,0,0,0});

                // reset counter backt to 1 and clear the list
                count = 0;
                numberHolderList.clear();
            }

            numberHolderList.add( i );
            count++;
        }
        // plug in last remaining numbers if there are any;
        int dif = Math.abs( currentNumberInIteration - maxNumber );
        Integer[] groupArray = new Integer[dif];
        for(int i = 0; i < groupArray.length; i++){
            currentNumberInIteration++;
            groupArray[i] = currentNumberInIteration;
        }

        groupHitInformation.put(groupArray,new Object[]{new ArrayList<Integer>(),0,0,0,new ArrayList<Integer>(),0.0,0,0,0,0});

    }
	
	public static List<Integer> extractAppropriatePosition(Map<String,Object[]> data, String group){

		List<Integer> numbers = new ArrayList<>();

		if( group.equals("1") ){

			Map.Entry<String,Object[]> res = data.entrySet().iterator().next();	

			return (List<Integer>) res.getValue()[0];

		}		

		for(Map.Entry<String,Object[]> positionData : data.entrySet()){

			if(positionData.getKey().equals(group)){

				numbers = (List<Integer>) positionData.getValue()[0];
				break;
			}
		}

		return numbers;
	}	
}
