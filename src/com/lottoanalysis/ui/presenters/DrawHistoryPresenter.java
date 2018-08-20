package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.drawhistory.*;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryListener;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryViewImpl;

import java.util.List;
import java.util.Set;

public class DrawHistoryPresenter implements DrawHistoryListener, DrawHistoryModelChanged{

    // This is form of aggregation
    private DrawHistoryModel drawHistoryModel;
    private DrawHistoryViewImpl drawHistoryViewImpl;

    public DrawHistoryPresenter(DrawHistoryModel drawHistoryModel, DrawHistoryViewImpl drawHistoryViewImpl) {

        this.drawHistoryModel = drawHistoryModel;
        drawHistoryModel.addListener( (this) );

        this.drawHistoryViewImpl = drawHistoryViewImpl;

        drawHistoryViewImpl.setDrawDays( extractDaysOfWeekFromResults() );
        drawHistoryViewImpl.setDayOfWeekPopulationNeeded( drawHistoryModel.isDayOfWeekPopulationNeeded() );
        drawHistoryViewImpl.setGameName( drawHistoryModel.getGameName() );
        drawHistoryViewImpl.setNumberOfPositions( drawHistoryModel.getDrawResultSize() );
        drawHistoryViewImpl.addListener(this);

    }

    @Override
    public void handleOnModelChanged(String property) {

        switch (property){
            case"gameSpan":
                populateViewPostGameSpanChange();
                break;
            case"drawPosition":
                populateViewPostDrawPositionChange();
                break;
            case"analyzeMethod":
                populateViewPostAnalysisMethodChange();
                break;
        }
    }

    @Override
    public void onGameSpanChange(int span) {

        drawHistoryModel.setGameSpan( span );
    }


    @Override
    public void onDrawPositionChange(DrawPositions drawPosition) {

        drawHistoryModel.setDrawPositions(drawPosition);

    }

    @Override
    public void onAnalysisMethodChange(AnalyzeMethod analyzeMethod) {

        drawHistoryModel.setAnalyzeMethod( analyzeMethod );

    }

    @Override
    public void onTableCellSelectionChange(String value) {

        List<Integer> values = drawHistoryModel.getLottoNumbersInSumRangeHolder( value );
        drawHistoryViewImpl.injectLottoNumberValues(values);
    }

    @Override
    public void onRadioButtonChange(DayOfWeek dayOfWeek) {

        drawHistoryModel.setDayOfWeek(dayOfWeek);
        drawHistoryModel.setDayOfWeekPopulationNeeded(false);
        drawHistoryModel.analyzeDrawData();

        drawHistoryViewImpl.setDayOfWeekPopulationNeeded( drawHistoryModel.isDayOfWeekPopulationNeeded() );

        setUpAllUIElements();

    }

    @Override
    public void onPageLoad() {

        defaultValuesSetUp();

    }

    private void populateViewPostAnalysisMethodChange() {
        drawHistoryModel.analyzeDrawData();
        drawHistoryViewImpl.setAbbreviationLabel( drawHistoryModel.getAnalyzeMethod().getAbbr() );

        setUpAllUIElements();
    }

    private void populateViewPostGameSpanChange() {
        drawHistoryModel.analyzeDrawData();

        drawHistoryViewImpl.setAverageAndSpan( drawHistoryModel.getGameSpan(), TotalWinningNumberTracker.getAverage());

        drawHistoryViewImpl.injectTotalWinningNumbers( drawHistoryModel.getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap());
        drawHistoryViewImpl.injectFirstDigitNumbers( drawHistoryModel.getFirstDigitValueHolderMap() );
        drawHistoryViewImpl.injectLottoNumberHits( drawHistoryModel.getLottoNumberGameOutTrackerMap() );
        drawHistoryViewImpl.injectSumGroupHits( drawHistoryModel.getSumGroupAnalyzer().getGroupAnalyzerMap() );
        drawHistoryViewImpl.injectLottoAndGameOutValues(drawHistoryModel.extractDefaultResults(),
                drawHistoryModel.getSumGroupAnalyzer().getGameOutHitHolder());
    }

    private void populateViewPostDrawPositionChange() {
        if(DrawPositions.BONUS != drawHistoryModel.getDrawPositions()) {

            drawHistoryModel.analyzeDrawData();

            drawHistoryViewImpl.setHeaderInformation(Integer.toString(drawHistoryModel.getDrawPositions().getIndex()));
            drawHistoryViewImpl.injectFirstDigitNumbers(drawHistoryModel.getFirstDigitValueHolderMap());
            drawHistoryViewImpl.injectLottoNumberHits(drawHistoryModel.getLottoNumberGameOutTrackerMap());
            drawHistoryViewImpl.injectSumGroupHits(drawHistoryModel.getSumGroupAnalyzer().getGroupAnalyzerMap());
            drawHistoryViewImpl.injectLottoAndGameOutValues(drawHistoryModel.extractDefaultResults(),
                    drawHistoryModel.getSumGroupAnalyzer().getGameOutHitHolder());
        }
    }

    private Set<String> extractDaysOfWeekFromResults() {
        return drawHistoryModel.getLottoGame().extractDaysOfWeekFromResults( drawHistoryModel.getLottoGame().getDrawingData());
    }

    private void setUpAllUIElements() {

        drawHistoryViewImpl.setAverageAndSpan(drawHistoryModel.getGameSpan(), TotalWinningNumberTracker.getAverage());
        drawHistoryViewImpl.injectTotalWinningNumbers( drawHistoryModel.getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap());
        drawHistoryViewImpl.injectFirstDigitNumbers( drawHistoryModel.getFirstDigitValueHolderMap() );
        drawHistoryViewImpl.injectLottoNumberHits( drawHistoryModel.getLottoNumberGameOutTrackerMap() );
        drawHistoryViewImpl.injectSumGroupHits( drawHistoryModel.getSumGroupAnalyzer().getGroupAnalyzerMap() );
        drawHistoryViewImpl.setAnalyzeLabel( drawHistoryModel.getAnalyzeMethod().getTitle() );
        drawHistoryViewImpl.injectLottoAndGameOutValues(drawHistoryModel.extractDefaultResults(),
                drawHistoryModel.getSumGroupAnalyzer().getGameOutHitHolder());
    }

    private void defaultValuesSetUp() {

        drawHistoryModel.analyzeDrawData();

        drawHistoryViewImpl.setAbbreviationLabel( drawHistoryModel.getAnalyzeMethod().getAbbr() );
        drawHistoryViewImpl.setHeaderInformation(drawHistoryModel.getDrawPositions().getIndex()+"");
        drawHistoryViewImpl.setAnalyzeLabel( drawHistoryModel.getAnalyzeMethod().getTitle() );
        drawHistoryViewImpl.setAverageAndSpan(drawHistoryModel.getGameSpan(), TotalWinningNumberTracker.getAverage());
        drawHistoryViewImpl.injectTotalWinningNumbers( drawHistoryModel.getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap());
        drawHistoryViewImpl.injectFirstDigitNumbers( drawHistoryModel.getFirstDigitValueHolderMap() );
        drawHistoryViewImpl.injectLottoNumberHits( drawHistoryModel.getLottoNumberGameOutTrackerMap() );
        drawHistoryViewImpl.injectSumGroupHits( drawHistoryModel.getSumGroupAnalyzer().getGroupAnalyzerMap() );
        drawHistoryViewImpl.injectLottoAndGameOutValues(drawHistoryModel.extractDefaultResults(),
                drawHistoryModel.getSumGroupAnalyzer().getGameOutHitHolder());

    }

    public DrawHistoryViewImpl presentViewForDisplay() {
        return drawHistoryViewImpl;
    }

}
