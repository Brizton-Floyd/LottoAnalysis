package com.lottoanalysis.utilities;

// Games Out Of View Algorithm
import java.util.*;

@SuppressWarnings("unchecked")
public class GamesOutViewDepicter{


    public GamesOutViewDepicter( int[][] numbers ){

        Map<String,Map<Integer, Object[]>> data = analyzeNumberDistrobution( numbers );
        determineTrueGamesOutAndComputeGamesOutAverageForPosition( data );
    }

    private void determineTrueGamesOutAndComputeGamesOutAverageForPosition( Map<String, Map<Integer, Object[]>> data ){

        Map<Integer, Map<Integer, Object[]>> mappingData = null;
        List<Map<Integer,Map<Integer,Object[]>>> mappingDataTwo = new ArrayList<>();
        int count = 1;
        int countTwo = 0;

        for(Map.Entry<String,Map<Integer,Object[]>> d : data.entrySet()){
            mappingData = new TreeMap<>();
            mappingData.put( count, d.getValue() );
            mappingDataTwo.add( mappingData );
            count++;
        }

        for(int i = 0; i < mappingDataTwo.size(); i++){

           // if(i < mappingDataTwo.size() - 1){

                for(int g = 0; g < mappingDataTwo.size(); g++){

                    if(i == g)
                        continue;

                    Map<Integer,Map<Integer,Object[]>> firsData = mappingDataTwo.get( i  );
                    Map<Integer,Map<Integer,Object[]>> secondData = mappingDataTwo.get( g );

                    Map<Integer, Object[]> firsDataTwo = firsData.get( i + 1 );
                    Map<Integer, Object[]> secondDataTwo = secondData.get( g + 1 );

                    for(Map.Entry<Integer,Object[]> dataContents : firsDataTwo.entrySet()){

                        int val = dataContents.getKey();
                        if( secondDataTwo.keySet().contains( val ) ){

                            // Second data contains the key you are looking for see if the games out for that digit is
                            // less than the games out in the prior draw index


                            Object[] internalDataSecondPosition = secondDataTwo.get( val );
                            Object[] internalDataPriorPosition = firsDataTwo.get( val );


                            int secondPositionGamesOut = (int) internalDataSecondPosition[1];
                            int priorPositionGamesOut = (int) internalDataPriorPosition[1];

                            if( secondPositionGamesOut < priorPositionGamesOut ){

                                if( ((String)internalDataPriorPosition[0]).contains("Games ago:") && ((String)internalDataSecondPosition[0]).contains("Games ago:")){

                                    // Empty if block to keep unecessary text from appearing in array
                                }
                                else{
                                    //internalDataPriorPosition[0] = internalDataSecondPosition[0]; //
                                    internalDataPriorPosition[0] =  (internalDataSecondPosition[0] + " Games ago: " + internalDataSecondPosition[1]);
                                }
                            }
                            else if( secondPositionGamesOut > priorPositionGamesOut){
                                if( ((String)internalDataPriorPosition[0]).contains("Games ago:") && ((String)internalDataSecondPosition[0]).contains("Games ago:")){

                                    // Empty if block to keep unecessary text from appearing in array

                                }
                                else {
                                    internalDataSecondPosition[0] = internalDataPriorPosition[0] + " Games ago: " + internalDataPriorPosition[1];
                                }
                            }
                        }
                    }
                }
           // }
        }



        for(int i = 0; i < mappingDataTwo.size(); i++){

            Map<Integer,Map<Integer, Object[]>> r = mappingDataTwo.get( i );
            Map<Integer,Object[]> rr = r.get( i + 1);
            displayData( rr , ( i + 1) );
            System.out.println( "\n");

        }
    }

    public static void displayData( Map<Integer, Object[]> data , int position){

        System.out.println("Draw Position " + position);
        for(Map.Entry<Integer, Object[]> d : data.entrySet()){

            //Map<Integer, Object[]> dd = d.getValue();


            System.out.println(String.format("Position Num: %1s\t Last Position: %2s", d.getKey(), Arrays.toString( d.getValue() )));


        }
    }

    /**
     * Method analyzes each draw position and marks the position the winning lottery number occurred in along with
     * how many games out the number is.The games out data is then put into a list to be analyzed further, a map is then
     * returned for further analyzation.
     * @param numbers
     */
    private Map<String,Map<Integer,Object[]>> analyzeNumberDistrobution( int[][] numbers ){

        Map<Integer, Object[]> numberTraceData = null;
        Map<String, Map<Integer,Object[]>> fullData = new TreeMap<>();

        String drawPosition = "";

        for(int i = 0; i < numbers.length; i++){

            List<Integer> numericValues = new ArrayList<>();
            for(int k = 0; k < numbers[i].length; k++){

                numericValues.add( numbers[i][k] );

            }
            // Reverse numbers in list so they are in correct draw sequence
           // Collections.reverse( numericValues );
            numberTraceData = new TreeMap<>();

            for(int count = 0; count < numericValues.size(); count++){


                int number = numericValues.get( count );

                if( !numberTraceData.containsKey( number ) ){


                    drawPosition = "Position: " + ( i + 1 );

                    numberTraceData.put( number, new Object[]{drawPosition, 0, new LinkedList<Integer>()} );
                    incrementGamesOut( numberTraceData, number );

                }
                else {

                    Object[] values = numberTraceData.get( number );

                    List<Integer> gameOutValues = (LinkedList<Integer>)values[2];
                    gameOutValues.add( (int)values[1] );
                    Collections.reverse( gameOutValues );
                    values[1] = 0;
                    values[0] = "Position: " + ( i + 1);
                    incrementGamesOut( numberTraceData, number );
                }
            }

            //Assign all data for current position to full data
            drawPosition = "Position: " + ( i + 1);
            fullData.put( drawPosition, numberTraceData );

        }


        //displayData( fullData );

        return fullData;
    }

    public static void incrementGamesOut( Map<Integer,Object[]> data, int number ){

        for( Map.Entry<Integer, Object[]> d : data.entrySet() ){

            if( d.getKey() != number){

                Object[] values = d.getValue();
                values[1] = (int) values[1] + 1;
            }
        }
    }

}
