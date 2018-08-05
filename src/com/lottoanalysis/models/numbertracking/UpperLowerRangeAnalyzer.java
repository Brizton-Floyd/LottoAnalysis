package com.lottoanalysis.models.numbertracking;

import com.lottoanalysis.models.lottogames.LottoGame;

import java.util.*;
import java.util.stream.Collectors;

import static sun.misc.Version.print;

public class UpperLowerRangeAnalyzer {

    private int drawIndex;
    private int[][] drawData;
    private LottoGame lottoGame;
    private int[] range;
    private int rangeHits;
    private int rangGamesOut;
    private int rangeHitAtGamesOut;
    private int gameOutLastAppearance;
    private List<Integer> gameOutHolder;

    private LottoNumberTracker lottoNumberTracker;
    private SingleDigitRangeTracker singleDigitRangeTracker;
    private UpperLowerRangeAnalyzer[] upperLowerRangeAnalyzers;
    private LottoNumberGameOutTracker lottoNumberGameOutTracker;
    private NumberMultipleAnalyzer numberMultipleAnalyzer;

    private UpperLowerRangeAnalyzer(){
        gameOutHolder = new ArrayList<>();
        singleDigitRangeTracker = new SingleDigitRangeTracker();
        lottoNumberTracker = new LottoNumberTracker();
    }
    public UpperLowerRangeAnalyzer(int[][] drawData, int drawIndex, LottoGame lottoGame){

        this.lottoGame = lottoGame;
        this.drawData = drawData;
        this.drawIndex = drawIndex;
        upperLowerRangeAnalyzers = new UpperLowerRangeAnalyzer[]{new UpperLowerRangeAnalyzer(),new UpperLowerRangeAnalyzer()};

        lottoNumberGameOutTracker = new LottoNumberGameOutTracker(drawData);

        determineHighAndLowRanges(lottoGame);
        determineRangeLottoNumbersHit();
        determineRangeHitsAtGamesOut();
        printData();
    }

    public void printData( UpperLowerRangeAnalyzer analyzerr, int index){

        String[] directions = {"Lower","Upper"};
        int[] count = {0};

        UpperLowerRangeAnalyzer analyzer = analyzerr;

        System.out.printf("\n%s Half Numbers: %s\n",directions[index],Arrays.toString(analyzer.getRange()));
        System.out.printf("\n%25s %5s %15s %5s %25s %5s %30s %3s\n","Hits:",analyzer.getRangeHits(),"Games Out:",analyzer.getRangGamesOut(),
                "Hits At Games Out:", analyzer.getRangeHitAtGamesOut(),"Game Out Last Appearance:",analyzer.getGameOutLastAppearance());

        Map<String,SingleDigitRangeTracker> data = analyzer.singleDigitRangeTracker.getData();
        data.forEach( (k,v) -> {

            Map<String,SingleDigitRangeTracker> d = v.getTracker();

            d.forEach((key,value) -> {

                long hits = value.getGameOutHolder().stream().filter( gOut -> gOut == value.getGamesOut()).count();
                int lastSeen = Math.abs(value.getGameOutHolder().size() - value.getGameOutHolder().lastIndexOf(value.getGamesOut()));

                System.out.printf("\nLAST DIGIT PERFORMANCE WITHIN GROUP %s %s\n",directions[count[0]++].toUpperCase(),key);
                System.out.printf("\n %30s %3s %15s %3s %20s %3s %15s %3s\n","Hits:",value.getHits(),"Games Out:",value.getGamesOut(),
                        "Hits @ Games Out", hits, "Last Seen:",lastSeen);

                List<Map<Integer,Integer[]>> lottoNumberMap = value.getLottoNumberHolder();
                Map<Integer,Integer[]> ss = new TreeMap<>();
                for(Map<Integer,Integer[]> dd : lottoNumberMap){

                    dd.forEach((kkk,vvv) -> {
                        ss.put(kkk,vvv);
                    });
                }

                List<Map.Entry<Integer,Integer[]>> entries = new ArrayList<>(ss.entrySet());
                entries.forEach(map -> {
                    System.out.printf("\n%15s %4s %20s %4s %30s %4s %25s %4s %25s %3s\n","Lotto #",map.getKey(),"Position Hits:",map.getValue()[0],
                            "Games Out In Position:",map.getValue()[1],"Actual Games Out:",map.getValue()[2],"Last Hit Position:",map.getValue()[3]);
                });

                System.out.println("\nRemainder Groups Due");
                Map<Integer,Object[]> remainderData = value.getRemainderTracker().getRemainderHolder();
                remainderData.forEach( (keyTwo,valueTwo)  -> {

                    System.out.printf("%15s %4s %15s %4s %15s %4s %15s %4s\n","Remainder:",keyTwo,"Hits:",valueTwo[0],"Games Out:",valueTwo[1],
                            "Numbers:",Arrays.toString( ((Set<Integer>)valueTwo[2]).toArray()));
                });

            });
//            analyzer.numberMultipleAnalyzer.computeHitsAtGamesOutAndLastAppearance();
//            analyzer.numberMultipleAnalyzer.print();
            count[0] = 0;
        });
    }
    public void printData() {

        String[] directions = {"Lower","Upper"};
        int[] count = {0};

        for( int i = 0; i < upperLowerRangeAnalyzers.length; i++){

            UpperLowerRangeAnalyzer analyzer = upperLowerRangeAnalyzers[i];

            System.out.printf("\n%s Half Numbers: %s\n",directions[i],Arrays.toString(analyzer.getRange()));
            System.out.printf("\n%25s %5s %15s %5s %25s %5s %30s %3s\n","Hits:",analyzer.getRangeHits(),"Games Out:",analyzer.getRangGamesOut(),
                    "Hits At Games Out:", analyzer.getRangeHitAtGamesOut(),"Game Out Last Appearance:",analyzer.getGameOutLastAppearance());

            Map<String,SingleDigitRangeTracker> data = analyzer.singleDigitRangeTracker.getData();
            data.forEach( (k,v) -> {

                Map<String,SingleDigitRangeTracker> d = v.getTracker();

                d.forEach((key,value) -> {

                    long hits = value.getGameOutHolder().stream().filter( gOut -> gOut == value.getGamesOut()).count();
                    int lastSeen = Math.abs(value.getGameOutHolder().size() - value.getGameOutHolder().lastIndexOf(value.getGamesOut()));

                    System.out.printf("\nLAST DIGIT PERFORMANCE WITHIN GROUP %s %s\n",directions[count[0]++].toUpperCase(),key);
                    System.out.printf("\n %30s %3s %15s %3s %20s %3s %15s %3s\n","Hits:",value.getHits(),"Games Out:",value.getGamesOut(),
                            "Hits @ Games Out", hits, "Last Seen:",lastSeen);

                    List<Map<Integer,Integer[]>> lottoNumberMap = value.getLottoNumberHolder();
                    Map<Integer,Integer[]> ss = new TreeMap<>();
                    for(Map<Integer,Integer[]> dd : lottoNumberMap){

                        dd.forEach((kkk,vvv) -> {
                            ss.put(kkk,vvv);
                        });
                    }

                    List<Map.Entry<Integer,Integer[]>> entries = new ArrayList<>(ss.entrySet());
                    entries.forEach(map -> {
                        System.out.printf("\n%15s %4s %20s %4s %30s %4s %25s %4s %25s %3s\n","Lotto #",map.getKey(),"Position Hits:",map.getValue()[0],
                                "Games Out In Position:",map.getValue()[1],"Actual Games Out:",map.getValue()[2],"Last Hit Position:",map.getValue()[3]);
                    });

                    System.out.println("\nRemainder Groups Due");
                    Map<Integer,Object[]> remainderData = value.getRemainderTracker().getRemainderHolder();
                    remainderData.forEach( (keyTwo,valueTwo)  -> {

                        System.out.printf("%15s %4s %15s %4s %15s %4s %15s %4s\n","Remainder:",keyTwo,"Hits:",valueTwo[0],"Games Out:",valueTwo[1],
                                "Numbers:",Arrays.toString( ((Set<Integer>)valueTwo[2]).toArray()));
                    });

                });
                analyzer.numberMultipleAnalyzer.computeHitsAtGamesOutAndLastAppearance();
                analyzer.numberMultipleAnalyzer.print();
                count[0] = 0;
            });

        }
    }

    // getters and setters

    public List<Integer> getGameOutHolder() {
        return gameOutHolder;
    }
    public int[] getRange() {
        return range;
    }
    public void setRange(int[] range) {
        this.range = range;
    }
    public int getRangeHits() {
        return rangeHits;
    }
    public void setRangeHits(int rangeHits) {
        this.rangeHits = rangeHits;
    }
    public int getRangGamesOut() {
        return rangGamesOut;
    }
    public void setRangGamesOut(int rangGamesOut) {
        this.rangGamesOut = rangGamesOut;
    }
    public int getRangeHitAtGamesOut() {
        return rangeHitAtGamesOut;
    }
    public void setRangeHitAtGamesOut(int rangeHitAtGamesOut) {
        this.rangeHitAtGamesOut = rangeHitAtGamesOut;
    }
    public int getGameOutLastAppearance() {
        return gameOutLastAppearance;
    }
    public void setGameOutLastAppearance(int gameOutLastAppearance) {
        this.gameOutLastAppearance = gameOutLastAppearance;
    }

    public UpperLowerRangeAnalyzer[] getUpperLowerRangeAnalyzers() {
        return upperLowerRangeAnalyzers;
    }

    // Methods

    private void determineRangeHitsAtGamesOut() {

        for(UpperLowerRangeAnalyzer analyzer : upperLowerRangeAnalyzers){

            int gamesOut = analyzer.getRangGamesOut();
            long count = analyzer.getGameOutHolder().stream().filter(out -> out  == gamesOut).count();

            int index = analyzer.getGameOutHolder().lastIndexOf( gamesOut );
            int gamesAgo = Math.abs( analyzer.getGameOutHolder().size() - index );

            analyzer.setRangeHitAtGamesOut((int)count);
            analyzer.setGameOutLastAppearance( gamesAgo );

        }
    }

    private void determineHighAndLowRanges(LottoGame game)
    {
        int maxNumber = game.getMaxNumber();

        int half = (game.getMaxNumber() == 9) ? (maxNumber+1)/2 : maxNumber / 2;
        List<List<Integer>> upperLowerNumbers = new LinkedList<>();
        upperLowerNumbers.add(new ArrayList<>());
        upperLowerNumbers.add(new ArrayList<>());

        for(int i = game.getMinNumber(); i <= maxNumber; i++)
        {
            if(i < half)
            {
                List<Integer> lower = upperLowerNumbers.get(0);
                lower.add(i);
            }
            else
            {
                List<Integer> upper = upperLowerNumbers.get(1);
                upper.add(i);
            }
        }

        // plug data into map
        Integer[] lower = upperLowerNumbers.get(0).toArray(new Integer[upperLowerNumbers.get(0).size()]);
        Integer[] upper = upperLowerNumbers.get(1).toArray(new Integer[upperLowerNumbers.get(1).size()]);

        upperLowerRangeAnalyzers[0].setRange( Arrays.stream( lower ).mapToInt(i -> i).toArray() );
        upperLowerRangeAnalyzers[1].setRange( Arrays.stream( upper ).mapToInt(i -> i).toArray() );
    }

    private void determineRangeLottoNumbersHit(){

        String direction = "";

        for(int i = 0; i < drawData[drawIndex].length; i++){

            String lottNumber = drawData[drawIndex][i] + "";

            for (int k = 0; k < upperLowerRangeAnalyzers.length; k++) {

                List<Integer> rangeNumbers = Arrays.stream(upperLowerRangeAnalyzers[k].getRange()).boxed()
                                                                                        .collect(Collectors.toList());

                if (rangeNumbers.contains(Integer.parseInt(lottNumber))) {

                    direction = (k == 0)? "Lower" : "Upper";

                    UpperLowerRangeAnalyzer analyzer = upperLowerRangeAnalyzers[k];

                    int hits = upperLowerRangeAnalyzers[k].getRangeHits();

                    List<Integer> gameOutHolder = upperLowerRangeAnalyzers[k].getGameOutHolder();

                    gameOutHolder.add(upperLowerRangeAnalyzers[k].getRangGamesOut());
                    analyzer.setRangeHits(++hits);
                    analyzer.setRangGamesOut(0);

                    if(analyzer.numberMultipleAnalyzer == null)
                        analyzer.numberMultipleAnalyzer = new NumberMultipleAnalyzer(lottoGame);

                    analyzer.numberMultipleAnalyzer.analyzeLottoNumber(Integer.parseInt(lottNumber));
                    analyzer.singleDigitRangeTracker.populateDataMap(direction,lottNumber);
                    analyzer.lottoNumberTracker.insertNumberAndIncrementHits(Integer.parseInt(lottNumber));
                    analyzer.lottoNumberTracker.insertHitsAndGamesOutForLottoNumbers( analyzer.singleDigitRangeTracker.getData());
                    lottoNumberGameOutTracker.insertLastHitPositionAndActualGamesOut(analyzer.singleDigitRangeTracker.getData());

                } else {

                    int gamesOut = upperLowerRangeAnalyzers[k].getRangGamesOut();
                    upperLowerRangeAnalyzers[k].setRangGamesOut(++gamesOut);

                }
            }

        }

    }
}
