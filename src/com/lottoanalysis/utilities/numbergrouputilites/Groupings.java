package com.lottoanalysis.utilities.numbergrouputilites;
import java.util.*;

public class Groupings{
	
	// Define fields up here 
	private static Map<Integer[],Object[]> data = new LinkedHashMap<>();
	private static Map<Integer,Integer[]> gamesOutData = new TreeMap<>();
	
	public static void analyze( int[] drawPositionNumbes ) {
		clearMap();		
		populateMap();
		performAnalysis(drawPositionNumbes);
		printOutStats();
	}
	
	private static void printOutStats(){
	    
	    data.forEach( (key, value) -> {
	        
			int gamesOut = (int) value[1];
			if(gamesOutData.containsKey(gamesOut)){
				Integer[] vals = gamesOutData.get( gamesOut );
				System.out.println("\nKey: " + Arrays.toString(key) + "\tValue: " + Arrays.toString(value) +
						"\tGroup Games Out: " + gamesOut + "\tGames Out Hits: " + vals[0] + "\tHits Games Out: " + vals[1] );

			}else{
				System.out.println("\nKey: " + Arrays.toString(key) + "\tValue: " + Arrays.toString(value) +
						"\tNever Hit at lottogames out");
			}
	    });
	}

	// define private methods down here
	private static void populateMap(){
		
		data.put(new Integer[]{0,9}, new Object[]{0,0});
		data.put(new Integer[]{1,8}, new Object[]{0,0});
		data.put(new Integer[]{2,7}, new Object[]{0,0});
		data.put(new Integer[]{3,6}, new Object[]{0,0});
		data.put(new Integer[]{4,5}, new Object[]{0,0});		
		
	}
	
	private static void clearMap(){
		gamesOutData.clear();
		data.clear();
	}
	
	private static void performAnalysis( int[] numbers ) {
		
		// Get the most recent winning number 
		int recentWinningNumber = numbers[ numbers.length - 1 ];
		System.out.println("Current Winning Number: " + recentWinningNumber );
		// Loop throuh the list and get all appropriate indexes
		List<Integer> indexes = new ArrayList<>();
		
		for(int i = 0; i < numbers.length; i++){
			
			if( numbers[i] == recentWinningNumber ) {
				
				indexes.add( i+1 );
			}
		}
		
		// Now that we have all the appropriate indexes find out the which number hit after the current winning digit
		List<Integer> numbersAsList = Arrays.asList( Arrays.stream(numbers).boxed().toArray(Integer[]::new) );
		for( int i = 0; i < indexes.size() - 1; i++ ){
			
			int nextWinningNumber = numbersAsList.get( indexes.get( i ) );
			
			for(Map.Entry<Integer[],Object[]> dataSet : data.entrySet()){
				
				List<Integer> arrayValues = Arrays.asList( dataSet.getKey() );
				if( arrayValues.contains( nextWinningNumber ) ) {
					
					Object[] dataValues = dataSet.getValue();
					dataValues[0] = (int) dataValues[0] + 1;
					
					if( !gamesOutData.containsKey( (int)dataValues[1] ) ){
						gamesOutData.put( (int)dataValues[1], new Integer[]{1,0} );
						incrementGamesOut(gamesOutData, (int)dataValues[1]);
					}else{
						
						Integer[] gameOutValues = gamesOutData.get( (int)dataValues[1] );
						gameOutValues[0]++;
						gameOutValues[1]=0;
						incrementGamesOut(gamesOutData, (int)dataValues[1]);
					}
					dataValues[1] = 0;
					incrementGamesOut(data, dataSet.getKey());
					break;
				}else{
				//	incrementGamesOut(data, null);
				}
			}
			
		}
	}
	
	private static void incrementGamesOut(Map<Integer,Integer[]> gamesOutInfo, Integer gamesOut){
		
		gamesOutInfo.forEach( (key,value) -> {
			
			if(key != gamesOut){
				value[1]++;
			}
		});
		
	}
	
	private static void incrementGamesOut(Map<Integer[],Object[]> hitInformation, Integer[] arrayValues ){
		
		if( arrayValues != null ){
			
			hitInformation.forEach( (key,value) -> {
				
				if(!Arrays.equals(key, arrayValues) ){
					
					value[1] = (int) value[1] + 1;
				}
				
			});
			
		}else{
			
			hitInformation.forEach( (key,value) -> {
				
				value[1] = (int) value[1] +1;
			});
		}
	}
	
}
