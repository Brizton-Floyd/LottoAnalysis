package com.lottoanalysis.models.companionnumber;

import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.gameout.GroupRange;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.models.lottogames.LottoGame;

import java.util.ArrayList;
import java.util.List;

public class LotteryNumberRange extends Range {

    private List<LotteryNumberRange> lotteryNumberRangeList;
    private int rangeInstances, rangeWinningNumber, currentWinningLottoNumber;
    private int[][] historicalDrawData;
    private DrawPosition drawPosition;

    private LotteryNumberRange(){}
    LotteryNumberRange(LottoGame lottoGame, int rangeInstances){
        super(10,lottoGame.getMinNumber(),lottoGame.getMaxNumber());
        this.rangeInstances = rangeInstances;
        lotteryNumberRangeList = new ArrayList<>();

        computeRangeUpperLowerBound();
    }

    @Override
    protected void computeRangeUpperLowerBound() {

        for(int j = 0; j < rangeInstances; j++) {

            LotteryNumberRange lotteryNumberRange = new LotteryNumberRange();

            for (int i = getMinNumber(); i <= getMaxNumber(); i++) {

                LotteryNumberRange groupRange = new LotteryNumberRange();
                groupRange.setLowerBound(i);
                groupRange.setUpperBound((i + getRange()) - 1);
                groupRange.setIndex();
                lotteryNumberRange.getRanges().add(groupRange);

                setMinNumber(groupRange.getUpperBound());
                i = getMinNumber();
            }

            validateUpperBoundsForOverflow(lotteryNumberRange.getRanges());
            super.resetLowerUpperBound();

            lotteryNumberRangeList.add( lotteryNumberRange );
        }
    }

    @Override
    public void analyze() {

        final int index = drawPosition.getIndex();
        final int currentwinnngNumberAtMainIndex = currentWinningLottoNumber;

        for(int i = 0; i < historicalDrawData[index].length; i++){

            int mainPositionLotteryNumber = historicalDrawData[index][i];
            if(mainPositionLotteryNumber >= 0) {
                // Increment range hit where main number falls into to
                incrementHitsForAppropriateRange(lotteryNumberRangeList.get(0).getRanges(), mainPositionLotteryNumber);
                if (mainPositionLotteryNumber == currentwinnngNumberAtMainIndex) {
                    int companionPositionNumber = historicalDrawData[index + 1][i];
                    if ( (companionPositionNumber >= 0) ) {
                        incrementHitsForAppropriateRange(lotteryNumberRangeList.get(1).getRanges(), companionPositionNumber);
                    }
                }
            }
        }
        computeHitsAtGamesOut(getLotteryNumberRangeList());
        findLastOccurenceOfGameOut(getLotteryNumberRangeList());
        findRangeWinningNumber( getLotteryNumberRangeList().get(0).getRanges());
    }

    private void findRangeWinningNumber(List<Range> ranges) {
        for(Range range : ranges){
            if(range.getGamesOut() == 0){
                rangeWinningNumber = range.getRangeWinningNumberHolder().get(range.getRangeWinningNumberHolder().size() -1);
                break;
            }
        }
    }

    public List<LotteryNumberRange> getLotteryNumberRangeList() {
        return lotteryNumberRangeList;
    }

    public int getRangeWinningNumber() {
        return rangeWinningNumber;
    }

    public void setDrawPosition(DrawPosition drawPosition) {
        this.drawPosition = drawPosition;
    }

    public void setRangeWinningNumber(int rangeWinningNumber) {
        this.rangeWinningNumber = rangeWinningNumber;
    }

    void setHistoricalData(int[][] historicalDrawData) {
        this.historicalDrawData = historicalDrawData;
    }

    void resetRangeHitData() {
        getLotteryNumberRangeList().stream().forEach( rangeInstance -> {
            rangeInstance.getRanges().stream().forEach( range -> {
                range.setHits(0);
                range.setGameOutLastSeen(0);
                range.setGamesOut(0);
                range.setHitsAtGamesOut(0);
            });
        });
    }

    void redetermineCompanionHits(int rangeWinningNumber) {
        resetCompanionListValues();

        for(int i = 0; i < historicalDrawData[drawPosition.getIndex()].length; i++){

            int mainPositionLotteryNumber = historicalDrawData[drawPosition.getIndex()][i];
            if(mainPositionLotteryNumber >= 0) {
                if (mainPositionLotteryNumber == rangeWinningNumber) {
                    int companionPositionNumber = historicalDrawData[drawPosition.getIndex() + 1][i];
                    if ((companionPositionNumber >= 0)) {
                        incrementHitsForAppropriateRange(lotteryNumberRangeList.get(1).getRanges(), companionPositionNumber);
                    }
                }
            }
        }
        //computeHitsAtGamesOut(getLotteryNumberRangeList());
        //findLastOccurenceOfGameOut(getLotteryNumberRangeList());
    }

    private void resetCompanionListValues() {
        getLotteryNumberRangeList().get(1).getRanges().stream().forEach(range -> {
            range.setHits(0);
            range.setGameOutLastSeen(0);
            range.setGamesOut(0);
            range.setHitsAtGamesOut(0);
        });
    }

    public void setCurrentWinningLottoNumber(int currentWinningLottoNumber) {
        this.currentWinningLottoNumber = currentWinningLottoNumber;
    }
}
