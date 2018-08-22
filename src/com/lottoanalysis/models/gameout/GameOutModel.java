package com.lottoanalysis.models.gameout;

import com.lottoanalysis.models.drawhistory.DrawModel;
import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.drawhistory.DrawPositions;

import javax.swing.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class GameOutModel extends DrawModel {

    public GameOutModel(LottoGame lottoGame) {
        super(lottoGame,(null),(null),(null));
    }

    public int getGameMaxValue(){
        return 39;
    }
}
