package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.models.gameselection.GameSelectionModel;
import com.lottoanalysis.models.gameselection.GameSelectionModelImpl;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.data.LotteryGameDaoImpl;
import com.lottoanalysis.services.gameselectionservices.GameSelectionRepositoryImpl;
import com.lottoanalysis.services.gameselectionservices.GameSelectionService;
import com.lottoanalysis.services.gameselectionservices.GameSelectionServiceImpl;
import com.lottoanalysis.ui.gameselection.GameSelectionView;
import com.lottoanalysis.ui.gameselection.GameSelectionViewListener;
import com.lottoanalysis.ui.homeview.EventSource;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.lottoanalysis.ui.homeview.EventSource.LOTTO_DASHBOARD;

public class GameSelectionPresenter implements GameSelectionViewListener  {

    private GameSelectionModel gameSelectionModel;
    private LottoGame lottoGame;
    private GameSelectionView gameSelectionView;
    private HomeViewPresenter homeViewListener;
    private Stage stage = new Stage(StageStyle.DECORATED);

    GameSelectionPresenter(GameSelectionView gameSelectionView, LottoGame lottoGame) {

        this.gameSelectionView = gameSelectionView;
        this.lottoGame = lottoGame;
        this.gameSelectionModel = new GameSelectionModelImpl();

        gameSelectionView.initializeListener(this);
        invokeServiceCall();
        gameSelectionView.initializeView();

    }

    @Override
    public void injectMenuItemValues() {

        gameSelectionView.setMenuBarItems(new ArrayList<>(gameSelectionModel.getGameNameAndIds().keySet()));
    }

    @Override
    public void notifyMainViewOfValueChange(String text, boolean flag) {

        gameSelectionModel.setGameName(text);
        lottoGame.setLottoId( gameSelectionModel.getGameNameAndIds().get(text) );
        homeViewListener.copy( lottoGame );
        homeViewListener.handleViewEvent(LOTTO_DASHBOARD);

        if(flag) {
            stage.close();
            homeViewListener.enable();
        }

    }

    @Override
    public void showMessageLabel() {
        gameSelectionView.showMessage();
    }

    @Override
    public void showProgess() {
        gameSelectionView.showProgress();
    }

    @Override
    public void unbindDataFromProgressAndMessage() {
        gameSelectionView.unbind();
    }

    @Override
    public void bindToMessageAndProgress(ReadOnlyStringProperty messageProperty, ReadOnlyDoubleProperty progressProperty) {
        gameSelectionView.bindToProgressAndMessage(progressProperty, messageProperty);
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
        gameSelectionView.bindToMessage(s);
    }

    @Override
    public void invokeDbService() {

        Task repository = new LotteryGameDaoImpl(this);
        gameSelectionView.bindToMessage( repository.messageProperty() );

        Thread thread = new Thread(repository);
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void reloadViewPostUpdate(boolean flag) {

        String gameName = "";

        for (Map.Entry<String, Integer> entry : gameSelectionModel.getGameNameAndIds().entrySet()) {

            if (entry.getValue() == lottoGame.getLottoId()){
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

        Scene scene = new Scene(gameSelectionView.view());
        stage.setScene(scene);
        stage.setTitle(gameSelectionModel.getTitle());

        stage.show();

        stage.setOnHiding( event -> homeViewListener.enable());

    }
}
