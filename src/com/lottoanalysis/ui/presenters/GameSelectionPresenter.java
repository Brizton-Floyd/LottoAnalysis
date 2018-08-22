package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.gameselection.GameSelectionModelImpl;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.data.LotteryGameDaoImpl;
import com.lottoanalysis.services.gameselectionservices.GameSelectionRepositoryImpl;
import com.lottoanalysis.services.gameselectionservices.GameSelectionService;
import com.lottoanalysis.services.gameselectionservices.GameSelectionServiceImpl;
import com.lottoanalysis.ui.gameselection.GameSelectionViewImpl;
import com.lottoanalysis.ui.gameselection.GameSelectionViewListener;
import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Map;

public class GameSelectionPresenter extends BasePresenter<GameSelectionViewImpl, LottoGame> implements GameSelectionViewListener  {

    private HomeViewPresenter homeViewListener;
    private GameSelectionModelImpl gameSelectionModel;
    private Stage stage = new Stage(StageStyle.DECORATED);

    GameSelectionPresenter(GameSelectionViewImpl gameSelectionView, LottoGame lottoGame) {

        super(gameSelectionView, lottoGame);

        gameSelectionModel = new GameSelectionModelImpl();
        gameSelectionModel.addListener((this));

        getView().setPresenter( (this) );

        invokeServiceCall();
        gameSelectionView.initializeView();

    }

    @Override
    public void handleOnModelChanged(String property) {

        switch (property){
            case "name":
                reinitializeModel();
                getModel().onModelChange("name");
                break;
        }
    }

    @Override
    public void injectMenuItemValues() {

        getView().setMenuBarItems(new ArrayList<>(gameSelectionModel.getGameNameAndIds().keySet()));
    }

    @Override
    public void notifyMainViewOfValueChange(String text, boolean flag) {

        gameSelectionModel.setStageCloseFlag( flag );
        gameSelectionModel.setGameName(text);
    }

    private void reinitializeModel() {

        getModel().setLottoId( gameSelectionModel.getGameNameAndIds().get( gameSelectionModel.getDefaultName() ) );
        homeViewListener.copy( getModel() );

        if(gameSelectionModel.isStageClosed()) {
            stage.close();
            homeViewListener.enable();
        }
    }

    @Override
    public void showMessageLabel() {
        getView().showMessage();
    }

    @Override
    public void showProgess() {
        getView().showProgress();
    }

    @Override
    public void unbindDataFromProgressAndMessage() {
        getView().unbind();
    }

    @Override
    public void bindToMessageAndProgress(ReadOnlyStringProperty messageProperty, ReadOnlyDoubleProperty progressProperty) {
        getView().bindToProgressAndMessage(progressProperty, messageProperty);
    }

    @Override
    public void executeGameUpdates() {

        GameSelectionService gameSelectionService = new GameSelectionServiceImpl(
                                                    new GameSelectionRepositoryImpl(this),
                                                    gameSelectionModel);
        gameSelectionService.executeGameUpdate();

   }

    @Override
    public void bindToMessageProperty(ReadOnlyStringProperty s) {
        getView().bindToMessage(s);
    }

    @Override
    public void invokeDbService() {

        Task repository = new LotteryGameDaoImpl(this);
        getView().bindToMessage( repository.messageProperty() );

        Thread thread = new Thread(repository);
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void reloadViewPostUpdate(boolean flag) {

        String gameName = "";

        for (Map.Entry<String, Integer> entry : gameSelectionModel.getGameNameAndIds().entrySet()) {

            if (entry.getValue() == getModel().getLottoId()){
                gameName = entry.getKey();
                break;
            }
        }

        notifyMainViewOfValueChange( gameName, flag );
    }

    @Override
    public void setHomeViewListener(HomeViewPresenter homeViewListener) {
        this.homeViewListener = homeViewListener;
    }

    private void invokeServiceCall() {

        GameSelectionService gameSelectionService = new GameSelectionServiceImpl(new GameSelectionRepositoryImpl(), gameSelectionModel);
        gameSelectionService.populateModel();

    }

    void showView() {

        Scene scene = new Scene( getView().display() );
        stage.setScene(scene);
        stage.setTitle(gameSelectionModel.getTitle());

        stage.show();

        stage.setOnHiding( event -> homeViewListener.enable());

    }
}
