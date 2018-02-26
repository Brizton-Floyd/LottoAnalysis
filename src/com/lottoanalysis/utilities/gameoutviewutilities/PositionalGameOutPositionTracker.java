package com.lottoanalysis.utilities.gameoutviewutilities;

import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.lottoanalysis.utilities.gameoutviewutilities.GamesOutViewDepicter.incrementGamesOut;

@SuppressWarnings("unchecked")
public class PositionalGameOutPositionTracker {

    private static List<StringBuilder> drawStringHolder = new ArrayList<>();
    private static Map<Integer, Integer[]> lottoNumberGameOutAndPositionHolder = new TreeMap<>();
    private static Map<Integer, Object[]> positonGameOutAndPositionHoleder = new TreeMap<>();


    public static void analyze(LotteryGame game, int[][] drawPositions) {

        clear();
        populateLottoNumberHolder(game);
        formDrawString(drawPositions);
        processDrawStringData(drawPositions);
        loadPositionAndHitInnerMaps();
      }

    private static void loadPositionAndHitInnerMaps() {

        positonGameOutAndPositionHoleder.forEach((k,v) -> {

            ((List<Integer>)v[1]).forEach( number -> {

                Map<Integer,Integer[]> positionRetracementMap = (Map<Integer,Integer[]>)v[2];
                if(!positionRetracementMap.containsKey(number)){

                    positionRetracementMap.put(number,new Integer[]{1,0});
                    NumberAnalyzer.incrementGamesOut(positionRetracementMap,number);
                }
                else{

                    Integer[] data = positionRetracementMap.get(number);
                    data[0]++;
                    data[1] = 0;
                    NumberAnalyzer.incrementGamesOut(positionRetracementMap,number);
                }

            });

            // This is where to iterate over game out list
            ((List<Integer>)v[0]).forEach( number -> {

                Map<Integer,Object[]> gameOutMap = (Map<Integer,Object[]>)v[3];
                int firstDigit = ((number+"").length() > 1) ? Character.getNumericValue((number+"").charAt(0)) : 0;

                if(!gameOutMap.containsKey(firstDigit)){

                    gameOutMap.put(firstDigit,new Object[]{1,0,new TreeMap<Integer,Integer[]>()});
                    incrementGamesOut(gameOutMap,firstDigit);

                    Map<Integer,Integer[]> innerData = (Map<Integer,Integer[]>)gameOutMap.get(firstDigit)[2];
                    if(!innerData.containsKey(number)){

                        innerData.put(number,new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(innerData,number);

                    }
                    else{
                        Integer[] dd = innerData.get(number);
                        dd[0]++;
                        dd[1] = 0;
                        NumberAnalyzer.incrementGamesOut(innerData,number);
                    }
                }
                else{

                    Object[] data = gameOutMap.get(firstDigit);
                    data[0] = (int) data[0] + 1;
                    data[1] = 0;
                    incrementGamesOut(gameOutMap,firstDigit);

                    Map<Integer,Integer[]> innerData = (Map<Integer,Integer[]>)gameOutMap.get(firstDigit)[2];
                    if(!innerData.containsKey(number)){

                        innerData.put(number,new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(innerData,number);

                    }
                    else{
                        Integer[] dd = innerData.get(number);
                        dd[0]++;
                        dd[1] = 0;
                        NumberAnalyzer.incrementGamesOut(innerData,number);
                    }

                }

            });
        });
    }

    private static void processDrawStringData(int[][] drawPostions) {

        for (int i = 0; i < drawPostions.length; i++) {
            // arraylist will hold which lottogames out has occurred in each position and the other arraylist will hold position number last hit in
            positonGameOutAndPositionHoleder.put((i + 1), new Object[]{new ArrayList<Integer>(), new ArrayList<Integer>(),
            new TreeMap<Integer,Integer[]>(),new TreeMap<Integer,Object[]>()});
        }

        drawStringHolder.forEach(drawing -> {

            String[] numbers = drawing.toString().split("-");
            setPositionHit(numbers, false);

            for(int i = 0; i < numbers.length; i++){

                Integer[] data = lottoNumberGameOutAndPositionHolder.get(Integer.parseInt(numbers[i].trim()));

                int num = (data[2] == 0) ? i+1 : data[2];

                Object[] positionData = positonGameOutAndPositionHoleder.get(i+1);
                ((List<Integer>)positionData[0]).add(data[0]);
                ((List<Integer>)positionData[1]).add(num);
            }

            setPositionHit(numbers, true);
        });
    }

    private static void setPositionHit(String[] numbers, boolean include) {

        if(!include) {
            for (int i = 0; i < numbers.length; i++) {

                int num = Integer.parseInt(numbers[i].trim());

                Integer[] data = lottoNumberGameOutAndPositionHolder.get(num);
                data[1] = i + 1;
            }
        }
        else{

            List<Integer> safeNumbers = new ArrayList<>();

            for(String num : numbers) {

                safeNumbers.add(Integer.parseInt(num.trim()));
            }

            lottoNumberGameOutAndPositionHolder.forEach( (k,v) -> {

                if(safeNumbers.contains((Integer)k))
                {
                    v[0] = 0;
                    v[2] = v[1];
                }
                else{
                    v[0]++;
                }
            });
        }
    }

    /**
     *
     */
    private static void formDrawString(int[][] drawPositions) {

        for (int i = 0; i < drawPositions[0].length; i++) {
            StringBuilder builder = new StringBuilder();
            drawStringHolder.add(builder);
        }

        for (int j = 0; j < drawPositions.length; j++) {

            for (int i = 0; i < drawPositions[j].length; i++) {

                StringBuilder builder = drawStringHolder.get(i);
                builder.append(drawPositions[j][i] + "-");
            }
        }

        drawStringHolder.forEach(stringBuilder -> stringBuilder.setCharAt(stringBuilder.toString().trim().lastIndexOf('-'), ' '));
    }

    /**
     * @param game
     */
    private static void populateLottoNumberHolder(LotteryGame game) {

        int minNumber = game.getMinNumber();
        int maxNumber = game.getMaxNumber();

        for (int i = minNumber; i <= maxNumber; i++) {
            if (!lottoNumberGameOutAndPositionHolder.containsKey(i)) {

                lottoNumberGameOutAndPositionHolder.put(i, new Integer[]{0, 0, 0});
            }
        }

    }

    /**
     *
     */
    private static void clear() {
        drawStringHolder.clear();
        positonGameOutAndPositionHoleder.clear();
        lottoNumberGameOutAndPositionHolder.clear();
    }
}
