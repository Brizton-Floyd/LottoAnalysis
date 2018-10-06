package com.lottoanalysis.models.drawresults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DrawResultPosition {

    private int drawPositionIndex;
    private int drawPositionHits, drawPositionGamesOut, lastSeen, hitsAtGamesOut;
    private List<Integer> lotteryResultPositionList = new ArrayList<>();
    private List<Integer> gameOutHolderList = new ArrayList<>();
    private Map<Integer,DrawResultPosition> drawPositionColumnMap;

    private DrawResultPosition(){}
    DrawResultPosition(int drawPositionIndex){
        this.drawPositionIndex = drawPositionIndex;
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

        if(!drawPositionColumnMap.containsKey( winningColumn )){
            DrawResultPosition drawResultPosition = new DrawResultPosition();
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
}
