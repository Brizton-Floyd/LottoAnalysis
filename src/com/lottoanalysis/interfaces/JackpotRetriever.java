package com.lottoanalysis.interfaces;

import com.lottoanalysis.lottogames.drawing.Drawing;
import javafx.collections.ObservableList;

public interface JackpotRetriever {

    String getEstimatedJackpot(String gameName);

}
