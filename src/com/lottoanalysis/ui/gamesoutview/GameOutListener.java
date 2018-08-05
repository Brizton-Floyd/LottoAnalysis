package com.lottoanalysis.ui.gamesoutview;

import com.lottoanalysis.models.drawhistory.DrawPositions;

public interface GameOutListener {

    void onPageLoad();

    void onDrawPositionChange(DrawPositions drawPositions);
}
