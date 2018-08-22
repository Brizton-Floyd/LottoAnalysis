package com.lottoanalysis.ui.gamesoutview;

import com.lottoanalysis.models.drawhistory.DrawPositions;

public interface GameOutView {

    void setGamePositionRange( int range );

    void setGameName( String gameName );

    void setGameMaxValue( int maxValue );

    void setOnGamePositionChange(DrawPositions drawPositions);
}
