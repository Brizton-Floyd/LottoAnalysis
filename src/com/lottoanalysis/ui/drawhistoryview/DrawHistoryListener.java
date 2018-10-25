package com.lottoanalysis.ui.drawhistoryview;

import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;

public interface DrawHistoryListener {

    void onDrawPositionChange(DrawPosition drawPosition);

    void onAnalysisMethodChange(AnalyzeMethod analyzeMethod);

    void onGameSpanChange(int span);

    void onTableCellSelectionChange(String value, int gamesOut);

    void onRadioButtonChange(DayOfWeek dayOfWeek);

    void onPageLoad();

}
