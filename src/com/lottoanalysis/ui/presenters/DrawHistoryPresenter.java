package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.drawhistory.*;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryListener;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryViewImpl;
import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.Set;

public class DrawHistoryPresenter extends BasePresenter<DrawHistoryViewImpl, DrawModel> implements DrawHistoryListener{

    public DrawHistoryPresenter(DrawModel drawHistoryModel, DrawHistoryViewImpl drawHistoryViewImpl) {
        super(drawHistoryViewImpl, drawHistoryModel);

        getModel().addListener( (this) );
        getView().setPresenter( this );
        createUi();
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
            case"dayOfWeek":
                populateViewPostDayOfWeekChange();
                break;
        }
    }

    @Override
    public void onGameSpanChange(int span) { getModel().setGameSpan( span ); }

    @Override
    public void onDrawPositionChange(DrawPosition drawPosition) { getModel().setDrawPosition(drawPosition); }

    @Override
    public void onAnalysisMethodChange(AnalyzeMethod analyzeMethod) { getModel().setAnalyzeMethod( analyzeMethod ); }

    @Override
    public void onTableCellSelectionChange(String value) {

        List<Integer> values = getModel().getLottoNumbersInSumRangeHolder( value );
        getView().injectLottoNumberValues(values);
    }

    @Override
    public void onRadioButtonChange(DayOfWeek dayOfWeek) { getModel().setDayOfWeek(dayOfWeek); }

    private void populateViewPostDayOfWeekChange() {
        getModel().setDayOfWeekPopulationNeeded(false);
        getModel().analyzeDrawData();

        getView().setDayOfWeekPopulationNeeded( getModel().isDayOfWeekPopulationNeeded() );

        setUpAllUIElements();
    }

    @Override
    public void onPageLoad() {

        defaultValuesSetUp();

    }

    private void populateViewPostAnalysisMethodChange() {
        getModel().analyzeDrawData();
        getView().setAbbreviationLabel( getModel().getAnalyzeMethod().getAbbr() );

        setUpAllUIElements();
    }

    private void populateViewPostGameSpanChange() {
        getModel().analyzeDrawData();

        getView().setAverageAndSpan( getModel().getGameSpan(), TotalWinningNumberTracker.getAverage());

        getView().injectTotalWinningNumbers( getModel().getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap());
        getView().injectFirstDigitNumbers( getModel().getFirstDigitValueHolderMap() );
        getView().injectLottoNumberHits( getModel().getLottoNumberGameOutTrackerMap() );
        getView().injectSumGroupHits( getModel().getSumGroupAnalyzer().getGroupAnalyzerMap() );
        getView().injectLottoAndGameOutValues(getModel().extractDefaultResults(),
                getModel().getSumGroupAnalyzer().getGameOutHitHolder());
    }

    private void populateViewPostDrawPositionChange() {
        if(DrawPosition.BONUS != getModel().getDrawPosition()) {

            getModel().analyzeDrawData();

            getView().setHeaderInformation(Integer.toString(getModel().getDrawPosition().getIndex()));
            getView().injectFirstDigitNumbers(getModel().getFirstDigitValueHolderMap());
            getView().injectLottoNumberHits(getModel().getLottoNumberGameOutTrackerMap());
            getView().injectSumGroupHits(getModel().getSumGroupAnalyzer().getGroupAnalyzerMap());
            getView().injectLottoAndGameOutValues(getModel().extractDefaultResults(),
                    getModel().getSumGroupAnalyzer().getGameOutHitHolder());
        }
    }

    private Set<String> extractDaysOfWeekFromResults() {
        return getModel().getLottoGame().extractDaysOfWeekFromResults( getModel().getLottoGame().getDrawingData());
    }

    private void setUpAllUIElements() {

        getView().setAverageAndSpan(getModel().getGameSpan(), TotalWinningNumberTracker.getAverage());
        getView().injectTotalWinningNumbers( getModel().getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap());
        getView().injectFirstDigitNumbers( getModel().getFirstDigitValueHolderMap() );
        getView().injectLottoNumberHits( getModel().getLottoNumberGameOutTrackerMap() );
        getView().injectSumGroupHits( getModel().getSumGroupAnalyzer().getGroupAnalyzerMap() );
        getView().setAnalyzeLabel( getModel().getAnalyzeMethod().getTitle() );
        getView().injectLottoAndGameOutValues(getModel().extractDefaultResults(),
                getModel().getSumGroupAnalyzer().getGameOutHitHolder());
    }

    private void defaultValuesSetUp() {

        getModel().analyzeDrawData();

        getView().setAbbreviationLabel( getModel().getAnalyzeMethod().getAbbr() );
        getView().setHeaderInformation(getModel().getDrawPosition().getIndex()+"");
        getView().setAnalyzeLabel( getModel().getAnalyzeMethod().getTitle() );
        getView().setAverageAndSpan(getModel().getGameSpan(), TotalWinningNumberTracker.getAverage());
        getView().injectTotalWinningNumbers( getModel().getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap());
        getView().injectFirstDigitNumbers( getModel().getFirstDigitValueHolderMap() );
        getView().injectLottoNumberHits( getModel().getLottoNumberGameOutTrackerMap() );
        getView().injectSumGroupHits( getModel().getSumGroupAnalyzer().getGroupAnalyzerMap() );
        getView().injectLottoAndGameOutValues(getModel().extractDefaultResults(),
                getModel().getSumGroupAnalyzer().getGameOutHitHolder());

    }

    private void createUi() {
        getView().setDrawDays( extractDaysOfWeekFromResults() );
        getView().setDayOfWeekPopulationNeeded( getModel().isDayOfWeekPopulationNeeded() );
        getView().setGameName( getModel().getGameName() );
        getView().setNumberOfPositions( getModel().getDrawResultSize() );
        getView().setUpUi();
    }


    public AnchorPane presentViewForDisplay() {
        return getView().display();
    }

}
