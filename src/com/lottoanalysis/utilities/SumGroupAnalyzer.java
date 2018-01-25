package com.lottoanalysis.utilities;

import com.lottoanalysis.lottoinfoandgames.LotteryGame;

import java.util.*;

@SuppressWarnings("unchecked")
public class SumGroupAnalyzer {

    private static Map<Integer[], Object[]> sumGroupHolder = new LinkedHashMap<>();
    private static Map<Integer, List<Integer>> sumLottoNumberHolder = new TreeMap<>();

    public static void analyze(int[] positionalNumbers, LotteryGame game) {

        clear();
        injectMapsWithValues(game);

        analyzeNumbers(positionalNumbers);
    }

    private static void injectMapsWithValues(LotteryGame game) {

        populateSumAndNumberHolder(game);
        sumGroupHolder.put(new Integer[]{0, 1, 2, 3}, new Object[]{0, 0, new TreeMap<Integer,Object[]>()});
        sumGroupHolder.put(new Integer[]{4, 5, 6, 7}, new Object[]{0, 0, new TreeMap<Integer,Object[]>()});
        sumGroupHolder.put(new Integer[]{8, 9, 10, 11}, new Object[]{0, 0, new TreeMap<Integer,Object[]>()});
        sumGroupHolder.put(new Integer[]{12, 13, 14, 15}, new Object[]{0, 0, new TreeMap<Integer,Object[]>()});
        sumGroupHolder.put(new Integer[]{16, 17, 18, 19}, new Object[]{0, 0, new TreeMap<Integer,Object[]>()});

        sumGroupHolder.forEach( (key,value) -> {

            List<Integer> numbers = Arrays.asList( key );
            Map<Integer,Object[]> data = (TreeMap<Integer,Object[]>)value[2];

            for(int i = 0; i < numbers.size(); i++){

                if(!data.containsKey(numbers.get(i))){

                    Map<Integer, Integer[]> dataTwo = new TreeMap<>();
                    List<Integer> nums = sumLottoNumberHolder.get(numbers.get(i));

                    if(nums != null) {

                        for (int j = 0; j < nums.size(); j++) {
                            dataTwo.put(nums.get(j), new Integer[]{0, 0});
                        }

                        data.put(numbers.get(i), new Object[]{0,0, dataTwo});
                    }


                }
            }


        });
    }

    private static void populateSumAndNumberHolder(LotteryGame game) {

        int maxNumber = game.getMaxNumber();

        for (int i = game.getMinNumber(); i <= maxNumber; i++) {

            String numAsString = i + "";

            if (numAsString.length() > 1)
                numAsString = Character.getNumericValue(numAsString.charAt(0)) + Character.getNumericValue(numAsString.charAt(1)) + "";

            if (!sumLottoNumberHolder.containsKey(Integer.parseInt(numAsString))) {
                List<Integer> numbers = new ArrayList<>();
                numbers.add(i);

                sumLottoNumberHolder.put(Integer.parseInt(numAsString), numbers);
            } else {
                List<Integer> data = sumLottoNumberHolder.get(Integer.parseInt(numAsString));
                data.add(i);
            }
        }


    }

    private static void clear() {
        sumGroupHolder.clear();
        sumLottoNumberHolder.clear();
    }

    private static void analyzeNumbers(int[] positionalNumbers) {

        for(int i = 0; i < positionalNumbers.length; i++){

            String number = positionalNumbers[i] + "";

            if(number.length() > 1)
                number = Character.getNumericValue(number.charAt(0)) + Character.getNumericValue(number.charAt(1)) + "";

            for(Map.Entry<Integer[], Object[]> data : sumGroupHolder.entrySet()){

                List<Integer> keys = Arrays.asList(data.getKey());
                if(keys.contains(Integer.parseInt(number) )){

                    // Group hits
                    Object[] values = data.getValue();
                    values[0] = (int) values[0] + 1;
                    values[1] = 0;

                    NumberAnalyzer.incrementGamesOut(sumGroupHolder, data.getKey());

                    Map<Integer,Object[]> lottoNumberData = (TreeMap<Integer,Object[]>) values[2];
                    if(lottoNumberData.containsKey(Integer.parseInt(number))){

                        // Sum hits
                        Object[] hitData = lottoNumberData.get( Integer.parseInt(number));
                        hitData[0] = (int) hitData[0] + 1;
                        hitData[1] = 0;

                        incrementGamesOut(lottoNumberData, Integer.parseInt(number));

                        // Lotto Number Info
                        Map<Integer, Integer[]> numberData = (TreeMap<Integer, Integer[]>) hitData[2];
                        if(numberData.containsKey( positionalNumbers[i])){

                            Integer[] hitInfo = numberData.get(positionalNumbers[i]);
                            hitInfo[0]++;
                            hitInfo[1] = 0;
                            NumberAnalyzer.incrementGamesOut(numberData, positionalNumbers[i]);
                        }

                    }

                }
            }


        }

    }

    private static void incrementGamesOut( Map<Integer, Object[]> data, int number ){

        data.forEach( (key,value) -> {

            if( key != number ){
                value[1] = (int) value[1] + 1;
            }
        });
    }

}
