package com.lottoanalysis.ui.drawhistoryview;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPositions;

public interface DrawHistoryListener {

    void onDrawPositionChange(DrawPositions drawPosition);

    void onAnalysisMethodChange(AnalyzeMethod analyzeMethod);

    void onGameSpanChange(int span);

    void onTableCellSelectionChange(String value);

    void onRadioButtonChange(DayOfWeek dayOfWeek);

    void onPageLoad();

}
