package com.lottoanalysis.services.gameselectionservices;

import com.lottoanalysis.models.gameselection.GameSelectionModel;

public class GameSelectionServiceImpl implements GameSelectionService {

    private GameSelectionRepository gameSelectionRepository;
    private GameSelectionModel gameSelectionModel;

    public GameSelectionServiceImpl( GameSelectionRepository gameSelectionRepository, GameSelectionModel gameSelectionModel){
        this.gameSelectionRepository = gameSelectionRepository;
        this.gameSelectionModel = gameSelectionModel;
    }

    @Override
    public void populateModel() {
        try {
            this.gameSelectionRepository.populateGameSelectionModel( gameSelectionModel );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
