package com.lottoanalysis.models.drawresults;

import java.util.*;

public class DrawResultPosition {

    private int drawPositionIndex;
    private int drawPositionHits, drawPositionGamesOut, lastSeen, hitsAtGamesOut;
    private List<Integer> lotteryResultPositionList = new ArrayList<>();
    private List<Integer> gameOutHolderList = new ArrayList<>();
    private Map<Integer,DrawResultPosition> drawPositionColumnMap;
    public static List<Integer> compainonWinningColumnList = new ArrayList<>();

    private DrawResultPosition(){}
    DrawResultPosition(int drawPositionIndex){
        this.drawPositionIndex = drawPositionIndex;
    }

    public List<Integer> getCompainonWinningColumnList() {
        return compainonWinningColumnList;
    }

    public int getDrawPositionIndex() {
        return drawPositionIndex;
    }

    public Map<Integer, DrawResultPosition> getDrawPositionColumnMap() {
        return drawPositionColumnMap;
    }

    public int getDrawPositionHits() {
        return drawPositionHits;
    }

    public int getDrawPositionGamesOut() {
        return drawPositionGamesOut;
    }

    public List<Integer> getGameOutHolderList() {
        return gameOutHolderList;
    }

    public List<Integer> getLotteryResultPositionList() {
        return lotteryResultPositionList;
    }

    void increasePositionColumnHit(int[] gameOutAndWinningColumnArray, final int lotteryNumber) {
        if(drawPositionColumnMap == null)
            drawPositionColumnMap = new TreeMap<>();

        int winningColumn = gameOutAndWinningColumnArray[1] + 1;
        int gamesOut = gameOutAndWinningColumnArray[0];
        compainonWinningColumnList.add( winningColumn );
        if(!drawPositionColumnMap.containsKey( winningColumn )){
            DrawResultPosition drawResultPosition = new DrawResultPosition();
            //drawResultPosition.getCompainonWinningColumnList().add( winningColumn );
            drawResultPosition.drawPositionIndex = winningColumn;
            drawResultPosition.drawPositionGamesOut = 0;
            drawResultPosition.drawPositionHits = 1;
            drawResultPosition.getLotteryResultPositionList().add( lotteryNumber );
            drawPositionColumnMap.put(winningColumn, drawResultPosition);
            incremenGamesOut(drawResultPosition);
        }
        else{
            DrawResultPosition drawResultPosition = drawPositionColumnMap.get( winningColumn );
            drawResultPosition.getLotteryResultPositionList().add( lotteryNumber );
            //drawResultPosition.getCompainonWinningColumnList().add( winningColumn );
            drawResultPosition.drawPositionGamesOut = 0;
            drawResultPosition.drawPositionHits++;
            drawResultPosition.gameOutHolderList.add(gamesOut);
            incremenGamesOut(drawResultPosition);
        }
    }

    private void incremenGamesOut(DrawResultPosition drawResultPosition) {
        drawPositionColumnMap.values().forEach(value -> {
            if(!value.equals(drawResultPosition)){
                value.drawPositionGamesOut++;
            }
        });
    }

    public void scanDrawResultsBasedOnSpan(int gameSpan) {
        List<Integer> data = new ArrayList<>( getLotteryResultPositionList().subList(getLotteryResultPositionList().size() - gameSpan, getLotteryResultPositionList().size()));
        System.out.println(Arrays.toString(data.toArray()));
    }
}
