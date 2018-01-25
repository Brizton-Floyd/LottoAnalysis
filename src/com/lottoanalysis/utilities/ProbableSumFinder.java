package com.lottoanalysis.utilities;

import com.lottoanalysis.lottoinfoandgames.LotteryGame;

import java.util.*;


@SuppressWarnings("unchecked")
public class ProbableSumFinder{
	
	private static Map<Integer,Map<String,Object[]>> hitDirectionHolder = new TreeMap<>();
	private static List<Integer> numbersAsSums = new ArrayList<>();
	private static Map<Integer, Integer> gamesOutHolderForAllRanges = new TreeMap<>();
	private static Map<String,Map<Integer[],Object[]>> groupAndNumberHitHolder = new LinkedHashMap<>();
	private static Map<String,Map<Integer,Integer[]>> aboveAndBelowGroupGameOutTracker = new LinkedHashMap<>();
	private static int winningSum; 

	
    private static void clear(){
		hitDirectionHolder.clear();
		numbersAsSums.clear();
		gamesOutHolderForAllRanges.clear();
		groupAndNumberHitHolder.clear();
		aboveAndBelowGroupGameOutTracker.clear();
		
	}

	public static void analyze( int[] winningNumbers, LotteryGame game){
		
		clear();
		loadHitDirectionHolder();
		convertWinningNumbersToSums(winningNumbers);
		populateHitDirectionHolder();
		
		// implement a way to determine the max sum of a lotterygame by taking in 
		// it min and max numbers. This will help when determining how to bunch up
		// groups depending on direction 
		
		//you will need to pass the game object here in order use the min and max value for the game but for now we will hardcode some values in the function
		formGroupsBasedAroundCurrentWinningNumber(winningNumbers,game);
		
		print();
	}
	
	private static void print(){
	    
	    System.out.println("\n\t\t\t\t************** Current Winning Number: " + winningSum + " ************** \n");
	    
	    System.out.println("------------------------------");	
	    System.out.println("Direction Analysis information");
	    System.out.println("------------------------------\n");	
	    
	    List<Integer> currentGamesOutForEachRange = new LinkedList<>();
	    
	    hitDirectionHolder.forEach( (key,value) -> {
	        
	        value.forEach( (keyTwo, valueTwo) -> {

	            System.out.println("Direction: " + String.format("%5s", keyTwo) + "\tHits: "+ String.format("%-5d", valueTwo[0]) + "\tGames Out: "+String.format("%-4d", valueTwo[1] ) + "\t\tHits At Games Out: " + valueTwo[2] + "\t\tGames Ago: " + valueTwo[4]);
	            
                currentGamesOutForEachRange.add((int)valueTwo[1]);	            
	        });
	    });
	    
	    System.out.println("\n--------------------------------------");	
	    System.out.println("Game Out Total Hits Amongst All Groups");
	    System.out.println("--------------------------------------\n");
	    
	    for(int i = 0; i < currentGamesOutForEachRange.size(); i++ ){
	        
	        int val = currentGamesOutForEachRange.get(i);
	        if(gamesOutHolderForAllRanges.get(val) != null){
	            
	            System.out.println("Out #: " + val + "\tHits: " + gamesOutHolderForAllRanges.get(val));
	        }
	        else
	        {
	            System.out.println("Out #: " + val + "\tHas never hit before");	            
	        }
	    }
	    
	    System.out.println("\n---------------------");	
	    System.out.println("Group Hit Information");
	    System.out.println("---------------------");
	    
	    String[] names = {"Below","Above"};
	    List<Integer> indexHolder = new LinkedList<>();
	    
	    for(int j = 0; j < names.length; j++){
	        
	        Map<Integer[],Object[]> lottoData = groupAndNumberHitHolder.get( names[j] );
	        if(lottoData != null){
	            
	            System.out.println( "\n<------------ " + names[j] + " Hit Information ------------> \n");
	            lottoData.forEach( (k,v) -> {
	                
	                int count = 0;
	                for(int n : (List<Integer>)v[3])
	                {
	                    if( n == (int)v[1]){
	                        count++;
	                    }
	                }
	                
	                indexHolder.add( (int)v[1] );
	                int gamesAgo = Math.abs( ((List<Integer>)v[3]).lastIndexOf((int)v[1]) - ((List<Integer>)v[3]).size());
	              	System.out.println("LottoNumber Group: " + String.format("%20s",Arrays.toString(k)) + "\t\tHits: " + String.format("%3d",v[0]) +
							"\t\tGames Out: " + v[1] + "\t\t Hits At Games Out: " + count + "\t\tGames Ago: " + gamesAgo +"\n");
					System.out.println(String.format("%50s","Number Performance Within Group") + "\n");

					((Map<Integer,Integer[]>)v[2]).forEach((kk,vv) -> {
						System.out.println(String.format("%30s", "Lotto #: " + String.format("%2d",kk)) + "\t\tHits: " + String.format("%4d",vv[0]) + "\t\tGames Out: " +
								String.format("%4d",vv[1]));

					});
					System.out.println();
	            });
	            
	        }
	        
	        Map<Integer,Integer[]> info = aboveAndBelowGroupGameOutTracker.get( names[j] );
	        if(info != null){
	        System.out.println("\n" + names[j] + " Game Out Statistics For All " + names[j] + " Groups");	            
	            for(int k = 0; k < indexHolder.size(); k++){
	                Integer[] stats = info.get( indexHolder.get(k) );
	                if(stats != null)
	                     System.out.println("Out #: " + indexHolder.get(k) + "\tHits: " + String.format("%03d", stats[0]) + "\tGames Ago: " + stats[1]);
	                else
	                     System.out.println("Out #: " + indexHolder.get(k) + "\tHas Never Hit Before" );	  
	            }
	        }
	        indexHolder.clear();
	    }
	    
	}
	
	private static void formGroupsBasedAroundCurrentWinningNumber(int[] winningNumbers, LotteryGame game){
	
		int min = game.getMinNumber();
		int max = game.getMaxNumber();
		int dif = 0;
		int groupSize = 0;
		//Map<String,Map<Integer[],Map<Integer,Integer[]>>> groupAndNumberHitHolder
		
		int currentWinningDigit = getCurrentWinningNumber();
		
		if( currentWinningDigit > min )
		{

			Map<Integer[], Object[]> data = new LinkedHashMap<>();
			
			dif = (min == 0) ? Math.abs( currentWinningDigit - min ) - 1 : Math.abs( currentWinningDigit - min );
			List<Integer> belowNumberList = new ArrayList<>();
			
			for(int i = min; i <= dif; i++){

				if(groupSize < 5)
				{
					belowNumberList.add( i );
					groupSize++;
					
				}
	            else{
	                
	                Integer[] dd = belowNumberList.toArray(new Integer[belowNumberList.size()]);
    				data.put(dd, new Object[]{0,0,new TreeMap<Integer,Integer[]>(),new ArrayList<Integer>(),0});
    				groupSize = 0;
    				groupAndNumberHitHolder.put("Below",data);
    				belowNumberList.clear();
    				i--;
	            }
				
			}

			if(belowNumberList.size() > 0){
	            Integer[] dd = belowNumberList.toArray(new Integer[belowNumberList.size()]);
				data.put(dd, new Object[]{0,0,new TreeMap<Integer,Integer[]>(),new ArrayList<Integer>(),0});
			    groupAndNumberHitHolder.put("Below",data);
			}
			
		}
		
		groupSize = 0;
		
		if( currentWinningDigit < max ) 
		{
			Map<Integer[], Object[]> data = new LinkedHashMap<>();
			
			dif = Math.abs( currentWinningDigit - max );
			List<Integer> aboveNumberList = new ArrayList<>();
			
			for(int i = max; i > currentWinningDigit; i--){

				if(groupSize < 5)
				{
					aboveNumberList.add( i );
					groupSize++;
					
				}
	            else{
	                
	                Integer[] dd = aboveNumberList.toArray(new Integer[aboveNumberList.size()]);
					data.put(dd, new Object[]{0,0,new TreeMap<Integer,Integer[]>(),new ArrayList<Integer>(),0});
    				groupSize = 0;
    				groupAndNumberHitHolder.put("Above",data);
    				aboveNumberList.clear();
    				i++;
	            }
				
			}

			if(aboveNumberList.size() > 0){
	            Integer[] dd = aboveNumberList.toArray(new Integer[aboveNumberList.size()]);
				data.put(dd, new Object[]{0,0,new TreeMap<Integer,Integer[]>(),new ArrayList<Integer>(),0});
			    groupAndNumberHitHolder.put("Above",data);
			}
				
		}
		
		// now plug in numbers 
		plugNumbersIntoMap(winningNumbers, groupAndNumberHitHolder);
	}
	
	private static void plugNumbersIntoMap(int[] wininingNumbers, Map<String,Map<Integer[],Object[]>> groupAndNumberHitHolder)
	{
		int currentWinningLottoNumbers = wininingNumbers[ wininingNumbers.length - 1];
		
		for(int i = 0; i < wininingNumbers.length; i++){
			
			final int num = wininingNumbers[i];
			List<Integer> n = Arrays.asList( Arrays.stream(wininingNumbers).boxed().toArray(Integer[]::new) );
			groupAndNumberHitHolder.forEach( (k,v) -> {
				
				v.forEach( (kTwo, vTwo) -> {
					
					List<Integer> data = Arrays.asList(kTwo);
					
					if(data.contains( num ) ) {

						vTwo[0] = (int) vTwo[0] + 1;
						String val = (currentWinningLottoNumbers < num) ? "Below" : "Above";
						Map<Integer, Integer[]> tracker = aboveAndBelowGroupGameOutTracker.get(val);

						((List<Integer>) vTwo[3]).add((int) vTwo[1]);
						if (!tracker.containsKey((int) vTwo[1])) {

							tracker.put((int) vTwo[1], new Integer[]{1, 0});

							// yourll need to increment games out here
							incrementGamesOutTwo(tracker, (int) vTwo[1]);
						} else {

							Integer[] gameOutData = tracker.get((int) vTwo[1]);
							gameOutData[0]++;
							gameOutData[1] = 0;

							// youll need to increment games out here
							incrementGamesOutTwo(tracker, (int) vTwo[1]);
						}

						vTwo[1] = 0;
						incrementGamesOut(groupAndNumberHitHolder, kTwo);
						Map<Integer, Integer[]> lottoNumberData = (TreeMap<Integer, Integer[]>) vTwo[2];

						if (!lottoNumberData.containsKey(num)) {

							lottoNumberData.put(num, new Integer[]{1, 0});
							incrementGamesOut(lottoNumberData, num,n);
						} else {
							Integer[] lottoData = lottoNumberData.get(num);
							lottoData[0]++;
							lottoData[1] = 0;
							incrementGamesOut(lottoNumberData, num, n);
						}

					}

				});
			});
		}
		
	}

	private static void incrementGamesOut(Map<Integer, Integer[]> tracker, int num, List<Integer> n)
	{
		tracker.forEach( (k,v) -> {

			if(k != num)
				v[1] = Math.abs( n.lastIndexOf(k) - n.size());
		});
	}
	
	private static void incrementGamesOutTwo(Map<Integer, Integer[]> tracker, int num)
	{
	    tracker.forEach( (k,v) -> {
	        
	        if(k != num)
	            v[1]++;
	    });
	}
	private static void incrementGamesOut( Map<String,Map<Integer[],Object[]>> data, Integer[] key )
	{
		
		data.forEach( (k,v) -> {

			v.forEach( (kk,vv) -> {
				if(!Arrays.equals( kk, key ) ){

					vv[1] = (int) vv[1] + 1;
				}
			});

		});
		
	}
	
	private static void setSum(int value){
	    
	    winningSum = value;
	    
	}
	
	private static int getCurrentWinningNumber(){
		
		return winningSum;
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
					loadGamesOutHolderForAllRanges( (int) dataTwo[1] );
					dataTwo[1] = 0;
					incrementGamesOut(hitDirectionHolder,1);
				}
				else if(winningSumAfterCurrentSum < currentWinningLottoNumberAsSum){
					
					data = hitDirectionHolder.get(2);
					Object[] dataTwo = data.get("Down");					
					dataTwo[0] = (int) dataTwo[0] + 1;
					((List<Integer>) dataTwo[3]).add( (int)dataTwo[1] );
					loadGamesOutHolderForAllRanges( (int) dataTwo[1] );
					dataTwo[1] = 0;
					incrementGamesOut(hitDirectionHolder,2);					
				}
				else{
					
					data = hitDirectionHolder.get(3);
					Object[] dataTwo = data.get("Equal");					
					dataTwo[0] = (int) dataTwo[0] + 1;
					((List<Integer>) dataTwo[3]).add( (int)dataTwo[1] );
					loadGamesOutHolderForAllRanges( (int) dataTwo[1] );
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
	
	private static void loadGamesOutHolderForAllRanges( int gameOutValue ){
	    
	    if(!gamesOutHolderForAllRanges.containsKey( gameOutValue ) ){
	        gamesOutHolderForAllRanges.put(gameOutValue, 1);
	    }
	    else{
	        int num = gamesOutHolderForAllRanges.get(gameOutValue);
	        num++;
	        gamesOutHolderForAllRanges.put(gameOutValue, num);
	    }
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

// 			if(numberAsString.length() > 1)
// 				numberAsString = Character.getNumericValue( numberAsString.charAt(0) ) + Character.getNumericValue( numberAsString.charAt(1) ) + "";
			
			numbersAsSums.add( Integer.parseInt( numberAsString ) );
		
		}
	}
	
	/**
     * Loads the direction holder with appropriate data 
     *
     *
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
		
	    aboveAndBelowGroupGameOutTracker.put("Below",new TreeMap<>());
		aboveAndBelowGroupGameOutTracker.put("Above",new TreeMap<>());
	}
}
