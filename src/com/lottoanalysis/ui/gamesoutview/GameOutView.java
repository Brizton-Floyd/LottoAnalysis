package com.lottoanalysis.ui.gamesoutview;

import com.lottoanalysis.models.drawhistory.DrawPositions;

public interface GameOutView {

    void initializeViewElements();

    void setListener( GameOutListener gameOutListener );

    void setGamePositionRange( int range );

    void setGameName( String gameName );

    void setGameMaxValue( int maxValue );

    void setDrawposition( int drawposition );

    void setOnGamePositionChange(DrawPositions drawPositions);
}
