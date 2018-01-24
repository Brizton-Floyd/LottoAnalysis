package com.lottoanalysis.utilities;

import java.util.*;

@SuppressWarnings("unchecked")
class ProbableSumFinder{
	
	private static Map<Integer,Map<String,Object[]>> hitDirectionHolder = new TreeMap<>();
	private static List<Integer> numbersAsSums = new ArrayList<>();
	private static int winningSum; 
	
	public static void analyze( int[] winningNumbers, LotteryGame game ){
		
		clear();
		loadHitDirectionHolder();
		convertWinningNumbersToSums(winningNumbers);
		populateHitDirectionHolder();
		
		// implement a way to determine the max sum of a lotterygame by taking in 
		// it min and max numbers. This will help when determining how to bunch up
		// groups depending on direction 
		
		
		print();
	}
	
	private static void print(){
	    
	    System.out.println("\n\t\t\t\t************** Current Winning Sum: " + winningSum + " ************** \n");
	    
	    System.out.println("------------------------------");	
	    System.out.println("Direction Analysis information");
	    System.out.println("------------------------------\n");	   
	    
	    hitDirectionHolder.forEach( (key,value) -> {
	        
	        value.forEach( (keyTwo, valueTwo) -> {

	            System.out.println("Direction: " + String.format("%5s", keyTwo) + "\tHits: "+ String.format("%-5d", valueTwo[0]) + "\tGames Out: "+String.format("%-4d", valueTwo[1] ) + "\t\tHits At Games Out: " + valueTwo[2] + "\t\tGames Ago: " + valueTwo[4]);
	        });
	    });
	}
	
	private static void setSum(int value){
	    
	    winningSum = value;
	    
	}
	
	private static void clear(){
		hitDirectionHolder.clear();
		numbersAsSums.clear();
	}
	
	private static void populateHitDirectionHolder(){
		
		int currentWinningLottoNumberAsSum = numbersAsSums.get( numbersAsSums.size() - 1 );
		setSum(currentWinningLottoNumberAsSum);
		
		for(int i = 0; i < numbersAsSums.size() - 1; i++){
			
			if(numbersAsSums.get( i ) == currentWinningLottoNumberAsSum){
				
				int winningSumAfterCurrentSum = numbersAsSums.get( i + 1 );
				Map<String, Object[]> data;
				
				if(winningSumAfterCurrentSum > currentWinningLottoNumberAsSum){
				
					data = hitDirectionHolder.get(1);
					Object[] dataTwo = data.get("Up");
					dataTwo[0] = (int)dataTwo[0] + 1;
					((List<Integer>) dataTwo[3]).add( (int)dataTwo[1] );
					dataTwo[1] = 0;
					incrementGamesOut(hitDirectionHolder,1);
				}
				else if(winningSumAfterCurrentSum < currentWinningLottoNumberAsSum){
					
					data = hitDirectionHolder.get(2);
					Object[] dataTwo = data.get("Down");					
					dataTwo[0] = (int) dataTwo[0] + 1;
					((List<Integer>) dataTwo[3]).add( (int)dataTwo[1] );					
					dataTwo[1] = 0;
					incrementGamesOut(hitDirectionHolder,2);					
				}
				else{
					
					data = hitDirectionHolder.get(3);
					Object[] dataTwo = data.get("Equal");					
					dataTwo[0] = (int) dataTwo[0] + 1;
					((List<Integer>) dataTwo[3]).add( (int)dataTwo[1] );					
					dataTwo[1] = 0;
					incrementGamesOut(hitDirectionHolder,3);					
				}
			}
		}
		
		// Hit occurence for current games out into the third index
		hitDirectionHolder.forEach( (key,value) -> {
			
			value.forEach( (keyTwo, valueTwo) -> {
				
				int count = 0;
				int currentGamesOut = (int) valueTwo[1];
				
				List<Integer> nums = (ArrayList<Integer>)valueTwo[3];
				for(int i = 0; i < nums.size(); i++){
					
					if(nums.get(i) == currentGamesOut)
						count++;
				}
				
				valueTwo[2] = count;
				valueTwo[4] = Math.abs( nums.lastIndexOf(currentGamesOut) - nums.size() );
			});
		});
	}
	
	private static void incrementGamesOut(Map<Integer,Map<String,Object[]>> numberInformation, int number){
		
		numberInformation.forEach( (key,value) -> {
			
			if(key != number){
				
				value.forEach( (keyTwo, valueTwo) -> {
					
					valueTwo[1] = (int) valueTwo[1] + 1;
					
				});
			}
		});
	}
	
	private static void convertWinningNumbersToSums(int[] winningNumbers){
		
		for(int i = 0; i < winningNumbers.length; i++){
			
			String numberAsString = winningNumbers[i] + "";
			
			if(numberAsString.length() > 1)
				numberAsString = Character.getNumericValue( numberAsString.charAt(0) ) + Character.getNumericValue( numberAsString.charAt(1) ) + "";
			
			numbersAsSums.add( Integer.parseInt( numberAsString ) );
		
		}
	}
	
	/**
     * Loads the direction holder with appropriate data 
     *
     * @param context -passed as parameter.
     */
	private static void loadHitDirectionHolder(){
		
		Map<String, Object[]> upData = new TreeMap<>();
		upData.put("Up", new Object[]{0,0,0,new ArrayList<Integer>(),0});
		hitDirectionHolder.put(1,upData);
		
		Map<String, Object[]> downData = new TreeMap<>();
		downData.put("Down", new Object[]{0,0,0,new ArrayList<Integer>(),0});
		hitDirectionHolder.put(2,downData);

		Map<String ,Object[]> equalData = new TreeMap<>();
		equalData.put("Equal", new Object[]{0,0,0,new ArrayList<Integer>(),0});
		hitDirectionHolder.put(3,equalData);		
		
	}
}
