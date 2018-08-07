package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.interfaces.ThreadCompleteListener;
import com.lottoanalysis.models.gameselection.GameSelectionModel;
import com.lottoanalysis.models.lottogames.data.LotteryGameDaoImpl;
import com.lottoanalysis.models.tasks.DataDownLoaderTask;
import com.lottoanalysis.models.tasks.NotifyingThread;
import com.lottoanalysis.services.gameselectionservices.GameSelectionRepositoryImpl;
import com.lottoanalysis.services.gameselectionservices.GameSelectionService;
import com.lottoanalysis.services.gameselectionservices.GameSelectionServiceImpl;
import com.lottoanalysis.services.gameselectionservices.webscrapper.WebScrapperImpl;
import com.lottoanalysis.ui.gameselection.GameSelectionView;
import com.lottoanalysis.ui.gameselection.GameSelectionViewListener;
import com.lottoanalysis.ui.homeview.HomeViewListener;
import com.lottoanalysis.utilities.fileutilities.OnlineFileUtility;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class GameSelectionPresenter implements GameSelectionViewListener  {

    private GameSelectionModel gameSelectionModel;
    private GameSelectionView gameSelectionView;
    private HomeViewListener homeViewListener;
    private Stage stage = new Stage(StageStyle.DECORATED);

    GameSelectionPresenter(GameSelectionView gameSelectionView, GameSelectionModel gameSelectionModel) {

        this.gameSelectionView = gameSelectionView;
        this.gameSelectionModel = gameSelectionModel;

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
        homeViewListener.reloadViewsBasedOnId(gameSelectionModel.getGameNameAndIds().get(text), stage, flag);

    }

    @Override
    public void executeGameUpdates() {

        Task<Void> fileDownloadTask = new WebScrapperImpl(OnlineFileUtility.getUrlPaths(), this);
        gameSelectionView.bindToProgressAndMessage( fileDownloadTask.progressProperty(), fileDownloadTask.messageProperty() );

        Thread thread = new Thread(fileDownloadTask, "File Downloader");
        thread.setDaemon(true);
        thread.start();
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

        notifyMainViewOfValueChange( gameSelectionModel.getDefaultName(), flag );
    }

    @Override
    public void setHomeViewListener(HomeViewListener homeViewListener) {
        this.homeViewListener = homeViewListener;
    }

    private void invokeServiceCall() {

        GameSelectionService gameSelectionService = new GameSelectionServiceImpl(new GameSelectionRepositoryImpl(), gameSelectionModel);
        gameSelectionService.populateModel();

    }

    public void showView() {

        Scene scene = new Scene(gameSelectionView.view());
        stage.setScene(scene);
        stage.setTitle(gameSelectionModel.getTitle());

        stage.show();

    }
}
