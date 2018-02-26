package com.lottoanalysis.utilities.numbergrouputilites;

import java.util.*;

public class NextProbableGroupFinder{
	
	private static Map<Integer,Object[]> groupHitCollector = new LinkedHashMap<>();
	private static Map<String,Map<String,Integer[]>> companionHitTracker = new LinkedHashMap<>();
	
	public static void analyze(int[][] drawNumbers){
		
		// We will use the game to determine which game range to retrieve 
		clear();
		analyzeAllPositions(drawNumbers);
		plugRangesIntoCompanionHitTracker();
		print();
	}
	private static void print(){
	    
	    companionHitTracker.forEach( (key,value) -> {
	        System.out.println("\nCurrent Winning Pattern: " + key);
	        
	        value.forEach( (keyTwo,valueTwo) -> {
	            
	            System.out.println("Companion Pattern: " + keyTwo + "\t\tHit and Games Out: " + Arrays.toString(valueTwo) );
	        });
	    });
	}
	private static void plugRangesIntoCompanionHitTracker(){
		
		List<String> patternHolder = new LinkedList<>();
		
		for(Map.Entry<Integer,Object[]> data : groupHitCollector.entrySet()){
			
			Integer[] values = (Integer[]) data.getValue()[1];
	    	String sequence = String.format("%1$s/%2$s", values[0], values[1]);
			patternHolder.add( sequence );
		}

		// get the most recent winning patternHolder
		String pattern = patternHolder.get( patternHolder.size() - 1 );
		companionHitTracker.put(pattern, new LinkedHashMap<>());
		
		for(int i = 0; i < patternHolder.size() - 1; i++){
			
			if( patternHolder.get(i).equals(pattern) ){
				
				String nextPattern = patternHolder.get( i + 1 );
				Map<String,Integer[]> innerData = companionHitTracker.get( pattern );
				
				if(!innerData.containsKey(nextPattern)){
					innerData.put(nextPattern, new Integer[]{1,0});
					// increment lottogames out
					incrementGamesOut( innerData, nextPattern );
				}
				else{
					Integer[] valueData = innerData.get( nextPattern );
					valueData[0]++;
					valueData[1] = 0;
					incrementGamesOut( innerData, nextPattern );
				}
			}
		}

	}
	private static void incrementGamesOut(Map<String,Integer[]> data, String pattern){
		
		data.forEach( (key,value) -> {
			
			if( !key.equals(pattern) ){
				value[1]++;
			}
		});
	}
	private static void clear(){
		groupHitCollector.clear();
		companionHitTracker.clear(); 
	}
	private static void analyzeAllPositions(int[][] positionalNumbers){
		
		for(int i = 0; i < positionalNumbers.length; i++){
			
			for(int j = 0; j < positionalNumbers[i].length; j++){
				
				if( !groupHitCollector.containsKey(j) ){
					Integer[] rangeHolder = {20,21};
					groupHitCollector.put(j, new Object[]{rangeHolder, new Integer[]{0,0}});
				}
				
				// determine what range the number hit int
				int number = positionalNumbers[i][j];
				
				Object[] holder = groupHitCollector.get( j );
				Integer[] rangeHolder = (Integer[]) holder[0];
				Integer[] numberSide = (Integer[]) holder[1];

				if(number <= rangeHolder[0]){
					// increment lower half of number range
					numberSide[0]++;
				}
				else if( number >= rangeHolder[1]){
					// increment upper half of number range
					numberSide[1]++;
				}
			}
		}
	}
}
