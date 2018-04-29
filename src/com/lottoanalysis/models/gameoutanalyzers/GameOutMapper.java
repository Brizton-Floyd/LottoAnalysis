package com.lottoanalysis.models.gameoutanalyzers;

import com.lottoanalysis.lottogames.LottoGame;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameOutMapper {

    private Map<Integer, Map<Integer, LottoNumber>> lottoNumberTrackerMap = new TreeMap<>();
    private Map<Integer, Integer[]> lottoNumberTrackerMapTwo = new TreeMap<>();
    private GameOutHitGrouper[] gameOutHitGrouper;

    public GameOutMapper(LottoGame game, int[][] drawData ){

        gameOutHitGrouper = new GameOutHitGrouper[drawData.length];

        populateMap( drawData.length, game);

        analyzeData( drawData );

        for(GameOutHitGrouper gameOutHitGrouperOne: gameOutHitGrouper)
            gameOutHitGrouperOne.getGameOutGroupHolderMap();
    }

    public GameOutHitGrouper getGameOutHitGrouper(int index) {
        return gameOutHitGrouper[index];
    }

    public List<List<String>> processDataRequest(String value, int index){

        List<List<String>> valuesToReturn = new LinkedList<>();

        StringBuilder builder = new StringBuilder(value);
        builder.setCharAt(0,' ');
        builder.setCharAt(value.length() -1, ' ');

        String[] rangeValues = builder.toString().split(",");

        Map<Integer,LottoNumber> values = lottoNumberTrackerMap.get(index);
        //System.out.println(values.get(3).gameOutPatternHolder.get(values.get(3).gameOutPatternHolder.size() - 1));

        final int minVal = Integer.parseInt( rangeValues[0].trim() );
        final int maxVal = Integer.parseInt( rangeValues[1].trim() );

        values.forEach( (k,v) -> {

            if(k >= minVal && k <= maxVal){

                valuesToReturn.add( v.getGameOutPatternHolder() );
            }
        });

        return valuesToReturn;
    }
    private void analyzeData(int[][] drawData) {

        for(int i = 0; i < drawData[0].length; i++){

            int[] recentWinningNumbers = new int[drawData.length];
            for(int j = 0; j < drawData.length; j++){

                recentWinningNumbers[j] = drawData[j][i];
            }

            List<Integer> winningNumberList = Arrays.stream( recentWinningNumbers ).boxed().collect(Collectors.toList());
            Map<Integer,Integer> numberHitIndex = new LinkedHashMap<>();
            IntStream.range(0,winningNumberList.size()).forEach( num -> {

                numberHitIndex.put(winningNumberList.get(num), num+1);
            });

            lottoNumberTrackerMapTwo.forEach( (k,v) -> {

                if(winningNumberList.contains(k)){

                    v[0] = 0;
                    v[1] = numberHitIndex.get( k );
                    lottoNumberTrackerMapTwo.put(k,v);
                }
                else{
                    v[0]++;
                    lottoNumberTrackerMapTwo.put(k,v);
                }
            });

            IntStream.range(0, winningNumberList.size()).forEach( num -> {


                Map<Integer,LottoNumber> positionData = lottoNumberTrackerMap.get( num );
                LottoNumber lottoNumber = positionData.get( winningNumberList.get(num) );

                if(gameOutHitGrouper[num] == null)
                    gameOutHitGrouper[num] = new GameOutHitGrouper();

                gameOutHitGrouper[num].checkValueAgainstMap( lottoNumber.getLottoNumberGamesOut() );

                lottoNumber.setLottoNumberGamesOut( lottoNumberTrackerMapTwo.get( winningNumberList.get(num) )[0]);
                int lottoNumberHits = lottoNumber.getLottoNumberHits();
                lottoNumber.setLottoNumberHits( ++lottoNumberHits );

                lottoNumber.setGameOutPatternHolder("##");

                incrementGamesOut(positionData, winningNumberList.get(num));

            });
        }
    }

    private void incrementGamesOut(Map<Integer,LottoNumber> data, int winningNumber){

        data.forEach( (k,v) -> {

            if(k != winningNumber ){

                int gamesOut = v.getLottoNumberGamesOut();
                v.setLottoNumberGamesOut(lottoNumberTrackerMapTwo.get( k )[0] );
                v.setGameOutPatternHolder( (Integer.toString(v.getLottoNumberGamesOut()).equals( "0") ) ?
                                                                 "P" + lottoNumberTrackerMapTwo.get(k)[1] : Integer.toString(v.getLottoNumberGamesOut()) );

            }
        });
    }
    private void populateMap(int length, LottoGame game) {

        Map<Integer,LottoNumber> data;

        for(int i = 0; i < length; i ++){

            data = new TreeMap<>();
            for(int j = 0; j<= game.getMaxNumber(); j++) {

                data.put(j, new LottoNumber());

                if(i == 0)
                    lottoNumberTrackerMapTwo.put(j, new Integer[]{0,0});
            }

            lottoNumberTrackerMap.put(i, data);
        }
    }

    private class LottoNumber {

        private int lottoNumberHits;
        private int lottoNumberGamesOut;

        List<String> gameOutPatternHolder = new ArrayList<>();

        public int getLottoNumberHits() {
            return lottoNumberHits;
        }

        public void setLottoNumberHits(int lottoNumberHits) {
            this.lottoNumberHits = lottoNumberHits;
        }

        public int getLottoNumberGamesOut() {
            return lottoNumberGamesOut;
        }

        public void setLottoNumberGamesOut(int lottoNumberGamesOut) {
            this.lottoNumberGamesOut = lottoNumberGamesOut;
        }

        public List<String> getGameOutPatternHolder() {
            return gameOutPatternHolder;
        }

        public void setGameOutPatternHolder(String gameOut ) {

            gameOutPatternHolder.add( gameOut );
        }
    }
}
