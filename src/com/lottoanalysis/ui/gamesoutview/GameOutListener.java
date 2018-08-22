package com.lottoanalysis.ui.gamesoutview;

import com.lottoanalysis.models.drawhistory.DrawPositions;

public interface GameOutListener {

    void handleViewEvents(String action);

    void onDrawPositionChange(DrawPositions drawPositions);
}
