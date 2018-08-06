package com.lottoanalysis.services.gameselectionservices;

import com.lottoanalysis.models.gameselection.GameSelectionModel;

import java.util.List;
import java.util.Map;

public interface GameSelectionRepository {

   void populateGameSelectionModel(GameSelectionModel gameSelectionModel) throws Exception;
}
