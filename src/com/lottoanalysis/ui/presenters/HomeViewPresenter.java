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
import com.lottoanalysis.ui.homeview.EventSource;
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
    public void handleViewEvent(EventSource eventSource) {

        switch (eventSource){
            case BET_SLIP_ANALYSIS:
                executeBetSlipAnalysis();
                break;
            case GAME_PANEL:
                presentLottoGameSelectionView();
                break;
            case LOTTO_DASHBOARD:
                loadGameDashBoard();
                break;
        }
    }

    private void executeBetSlipAnalysis() {

        DrawHistoryModel drawHistoryModel = new DrawHistoryModel(
                lottoGame,
                new TotalWinningNumberTracker(),
                new LottoNumberGameOutTracker(),
                new SumGroupAnalyzer()
        );

        DrawHistoryPresenter drawHistoryPresenter = new DrawHistoryPresenter(drawHistoryModel, new DrawHistoryViewImpl());

        homeViewImpl.injectView( drawHistoryPresenter.presentViewForDisplay() );
    }

    private void loadGameDashBoard(int id) {


        LottoDashBoardHomeService lottoDashBoardHomeService = new LottoDashBoardHomeServiceImpl(new HomeServiceRepositoryImpl());
        lottoGame = lottoDashBoardHomeService.loadById( id );

        loadGameDashBoard(lottoGame);

    }

    private void presentLottoGameSelectionView() {

        if(gameCache == null){

            GameSelectionPresenter gameSelectionPresenter = new GameSelectionPresenter(new GameSelectionViewImpl(), lottoGame);
            gameSelectionPresenter.showView();
            gameSelectionPresenter.setHomeViewListener(this);

            gameCache = new GameSelectionObject(lottoGame, lottoGame.getLottoId(), 0);


            CacheManager.putCache( gameCache );
        }
        else{

            Cacheable cacheable = CacheManager.getCache( gameCache );
            lottoGame = (LottoGame) cacheable.getObject();

            GameSelectionPresenter gameSelectionPresenter = new GameSelectionPresenter(new GameSelectionViewImpl(), lottoGame);
            gameSelectionPresenter.showView();
            gameSelectionPresenter.setHomeViewListener(this);

        }
    }

    private void loadGameDashBoard(LottoGame lottoGame) {

        LottoDashBoardPresenter presenter = new LottoDashBoardPresenter(new LottoDashBoardViewImpl(), lottoGame);
        presenter.setListener(this);

        homeViewImpl.injectView( presenter.presentView() );
    }

    private void loadGameDashBoard() {

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

    public AnchorPane onGameLoad() {
        return homeViewImpl.getView();
    }

    void copy(LottoGame lottoGame) {
        this.lottoGame = lottoGame;
    }

    void enable() {

        homeViewImpl.enableButtonAndDisableDashboardButton();

    }

    public void reloadDashBoard() {
        loadGameDashBoard();
    }
}
