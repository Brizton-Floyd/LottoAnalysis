package com.lottoanalysis.services.gameselectionservices;

import com.lottoanalysis.models.gameselection.GameSelectionModel;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.beans.property.ReadOnlyDoubleProperty;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface GameSelectionRepository {

   void populateGameSelectionModel(GameSelectionModel gameSelectionModel) throws Exception;

   void executeGameUpdates() throws ExecutionException, InterruptedException;

}
