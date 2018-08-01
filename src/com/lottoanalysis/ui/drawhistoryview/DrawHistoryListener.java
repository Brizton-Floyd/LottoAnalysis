package com.lottoanalysis.ui.drawhistoryview;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPositions;

import java.util.Set;

public interface DrawHistoryListener {

    void onDrawPositionChange(DrawPositions drawPosition);
    void onAnalysisMethodChange(AnalyzeMethod analyzeMethod);
    void onGameSpanChange( int span );
    void onTableCellSelectionChange( String value );
    void onRadioButtonChange(DayOfWeek dayOfWeek);
    String getGameName();
    int getNumberOfPositions();
    boolean dayOfWeekPopulationNeeded();
    String getDay();
}
