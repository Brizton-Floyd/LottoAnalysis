package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.controllers.LottoDashboardController;
import com.lottoanalysis.controllers.LottoInfoAndGamesController;
import com.lottoanalysis.interfaces.Cacheable;
import com.lottoanalysis.models.GameSelectionObject;
import com.lottoanalysis.models.gameselection.GameSelectionModel;
import com.lottoanalysis.models.gameselection.GameSelectionModelImpl;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.drawhistory.DrawHistoryModel;
import com.lottoanalysis.models.drawhistory.LottoNumberGameOutTracker;
import com.lottoanalysis.models.drawhistory.SumGroupAnalyzer;
import com.lottoanalysis.models.drawhistory.TotalWinningNumberTracker;
import com.lottoanalysis.models.managers.CacheManager;
import com.lottoanalysis.services.homeviewservices.HomeServiceRepositoryImpl;
import com.lottoanalysis.services.homeviewservices.LottoDashBoardHomeService;
import com.lottoanalysis.services.homeviewservices.LottoDashBoardHomeServiceImpl;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardViewImpl;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryViewImpl;
import com.lottoanalysis.ui.gameselection.GameSelectionViewImpl;
import com.lottoanalysis.ui.homeview.HomeView;
import com.lottoanalysis.ui.homeview.HomeViewListener;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class HomeViewPresenter implements HomeViewListener {

    private HomeView homeViewImpl;
    private LottoGame lottoGame;
    private Cacheable gameCache;

    public HomeViewPresenter(HomeView homeView){

        this.homeViewImpl = homeView;
        homeViewImpl.setHomeViewListener( this );

        loadGameDashBoard();
    }

    @Override
    public void executeBetSlipAnalysis() {

        DrawHistoryModel drawHistoryModel = new DrawHistoryModel(
                lottoGame,
                new TotalWinningNumberTracker(),
                new LottoNumberGameOutTracker(),
                new SumGroupAnalyzer()
        );
        DrawHistoryPresenter drawHistoryPresenter = new DrawHistoryPresenter(drawHistoryModel, new DrawHistoryViewImpl());

        homeViewImpl.injectView( drawHistoryPresenter.presentViewForDisplay() );
    }

    @Override
    public void loadGameDashBoard(int id) {

        LottoDashBoardHomeService lottoDashBoardHomeService = new LottoDashBoardHomeServiceImpl(new HomeServiceRepositoryImpl());
        lottoGame = lottoDashBoardHomeService.loadById( id );

        loadGameDashBoard(lottoGame);
    }

    @Override
    public void loadGameDashBoard() {

        if(lottoGame == null) {

            LottoDashBoardHomeService lottoDashBoardHomeService = new LottoDashBoardHomeServiceImpl(new HomeServiceRepositoryImpl());
            lottoGame = lottoDashBoardHomeService.getDefaultGame();

            LottoDashBoardPresenter presenter = new LottoDashBoardPresenter(new LottoDashBoardViewImpl(), lottoGame);
            presenter.setListener(this);

            homeViewImpl.injectView(presenter.presentView());
        }
        else{

            loadGameDashBoard( lottoGame.getLottoId() );
        }

    }

    @Override
    public void invokeDashBoard() {

        loadGameDashBoard(lottoGame);
    }

    @Override
    public void reloadViewsBasedOnId(Integer val, Stage stage, boolean closeStage) {

        loadGameDashBoard(val);

        if(closeStage) {
            stage.close();
        }

        homeViewImpl.enableButtonAndDisableDashboardButton();
    }

    @Override
    public void presentLottoGameSelectionView() {

        GameSelectionModel model;
        if(gameCache == null){

            model = new GameSelectionModelImpl();
            GameSelectionPresenter gameSelectionPresenter = new GameSelectionPresenter(new GameSelectionViewImpl(), model);
            gameSelectionPresenter.showView();
            gameSelectionPresenter.setHomeViewListener(this);

            gameCache = new GameSelectionObject(model, model.getGameNameAndIds().get( model.getDefaultName() ), 0);


            CacheManager.putCache( gameCache );
        }
        else{

            Cacheable cacheable = CacheManager.getCache( gameCache );
            GameSelectionModel gameSelectionModel = (GameSelectionModel)cacheable.getObject();

            GameSelectionPresenter gameSelectionPresenter = new GameSelectionPresenter(new GameSelectionViewImpl(), gameSelectionModel);
            gameSelectionPresenter.showView();
            gameSelectionPresenter.setHomeViewListener(this);

            System.out.println(gameSelectionModel.getDefaultName());

        }
    }

    @Override
    public void loadGameDashBoard(LottoGame lottoGame) {

        LottoDashBoardPresenter presenter = new LottoDashBoardPresenter(new LottoDashBoardViewImpl(), lottoGame);
        presenter.setListener(this);

        homeViewImpl.injectView( presenter.presentView() );
    }

    @Override
    public AnchorPane onGameLoad() {
        return homeViewImpl.getView();
    }
}
