package com.lottoanalysis.ui.drawhistoryview;

import com.lottoanalysis.models.drawhistory.*;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DrawHistoryView {

    void notifyListenerOfDrawPositionChange(DrawPositions drawPosition);

    void notifyListenerOfAnalysisChange(AnalyzeMethod analyzeMethod );

    void notifyListenerOfGameSpanChange( int span );

    void notifyListenerOfTableCellSelectionChange( String value );

    void addListener( DrawHistoryListener listener);

    void setHeaderInformation(String position, String gameName);

    void setAnalyzeLabel( String analyzeLabelValue );

    void setAbbreviationLabel( String value );

    void setAverageAndSpan( int span, Double avg );

    void setDrawDays(Set<String> days);

    void injectTotalWinningNumbers(Map<String, TotalWinningNumberTracker> totalWinningNumberTrackerMap);

    void injectFirstDigitNumbers( Map<Integer, Integer[]> firstDigitNumbers);

    void injectLottoNumberHits(Map<Integer, LottoNumberGameOutTracker> lottoNumberGameOutTrackerMap);

    void injectSumGroupHits(Map<Integer[], SumGroupAnalyzer> sumGroupAnalyzerMap);

    void injectLottoAndGameOutValues( List<Integer> lottoNumbers, List<Integer> gameOutValues );

    void injectLottoNumberValues( List<Integer> lottoNumbers );

}
