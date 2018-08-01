package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.lottogames.drawing.Drawing;
import com.lottoanalysis.models.drawhistory.*;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryListener;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryView;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryViewImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DrawHistoryPresenter implements DrawHistoryListener{

    // This is form of aggregation
    private DrawHistoryModel drawHistoryModel;
    private DrawHistoryViewImpl drawHistoryViewImpl;

    public DrawHistoryPresenter(DrawHistoryModel drawHistoryModel, DrawHistoryViewImpl drawHistoryViewImpl) {

        this.drawHistoryModel = drawHistoryModel;
        this.drawHistoryViewImpl = drawHistoryViewImpl;

        drawHistoryViewImpl.setDrawDays( extractDaysOfWeekFromResults() );
        drawHistoryViewImpl.addListener(this);

        defaultValuesSetUp();

    }

    @Override
    public void onGameSpanChange(int span) {

        drawHistoryModel.setGameSpan( span );
        drawHistoryModel.analyzeDrawData();

        drawHistoryViewImpl.setAverageAndSpan( drawHistoryModel.getGameSpan(), TotalWinningNumberTracker.getAverage());

        drawHistoryViewImpl.injectTotalWinningNumbers( drawHistoryModel.getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap());
        drawHistoryViewImpl.injectFirstDigitNumbers( drawHistoryModel.getFirstDigitValueHolderMap() );
        drawHistoryViewImpl.injectLottoNumberHits( drawHistoryModel.getLottoNumberGameOutTrackerMap() );
        drawHistoryViewImpl.injectSumGroupHits( drawHistoryModel.getSumGroupAnalyzer().getGroupAnalyzerMap() );

    }

    @Override
    public void onDrawPositionChange(DrawPositions drawPosition) {

        drawHistoryModel.setDrawPositions(drawPosition);
        drawHistoryModel.analyzeDrawData();

        final String name = Drawing.splitGameName(drawHistoryModel.getGameName());

        drawHistoryViewImpl.setHeaderInformation( Integer.toString(drawHistoryModel.getDrawPositions().getIndex()), name);
        drawHistoryViewImpl.injectFirstDigitNumbers( drawHistoryModel.getFirstDigitValueHolderMap() );
        drawHistoryViewImpl.injectLottoNumberHits( drawHistoryModel.getLottoNumberGameOutTrackerMap() );
        drawHistoryViewImpl.injectSumGroupHits( drawHistoryModel.getSumGroupAnalyzer().getGroupAnalyzerMap() );
        drawHistoryViewImpl.injectLottoAndGameOutValues(drawHistoryModel.extractDefaultResults(),
                drawHistoryModel.getSumGroupAnalyzer().getGameOutHitHolder());

    }

    @Override
    public void onAnalysisMethodChange(AnalyzeMethod analyzeMethod) {

        drawHistoryModel.setAnalyzeMethod( analyzeMethod );
        drawHistoryModel.analyzeDrawData();
        drawHistoryViewImpl.setAbbreviationLabel( drawHistoryModel.getAnalyzeMethod().getAbbr() );

        setUpAllUIElements();

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

        setUpAllUIElements();

    }

    @Override
    public String getGameName() {
        return drawHistoryModel.getLottoGame().getGameName();
    }

    @Override
    public int getNumberOfPositions() {
        return drawHistoryModel.getDrawResultSize();
    }

    @Override
    public boolean dayOfWeekPopulationNeeded() {
        return drawHistoryModel.dayOfWeekPopulationNeeded;
    }

    @Override
    public String getDay() {
        return drawHistoryModel.getDayOfWeek().getDay();
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

        final String name = Drawing.splitGameName(drawHistoryModel.getGameName());

        drawHistoryViewImpl.setAbbreviationLabel( drawHistoryModel.getAnalyzeMethod().getAbbr() );
        drawHistoryViewImpl.setHeaderInformation(drawHistoryModel.getDrawPositions().getIndex()+"", name);
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
