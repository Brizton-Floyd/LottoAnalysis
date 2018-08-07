package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.controllers.LottoDashboardController;
import com.lottoanalysis.controllers.LottoInfoAndGamesController;
import com.lottoanalysis.models.gameselection.GameSelectionModelImpl;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.drawhistory.DrawHistoryModel;
import com.lottoanalysis.models.drawhistory.LottoNumberGameOutTracker;
import com.lottoanalysis.models.drawhistory.SumGroupAnalyzer;
import com.lottoanalysis.models.drawhistory.TotalWinningNumberTracker;
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

        LottoDashBoardHomeService lottoDashBoardHomeService = new LottoDashBoardHomeServiceImpl(new HomeServiceRepositoryImpl());
        lottoGame = lottoDashBoardHomeService.getDefaultGame();

        LottoDashBoardPresenter presenter = new LottoDashBoardPresenter(new LottoDashBoardViewImpl(), lottoGame);
        presenter.setListener(this);

        homeViewImpl.injectView( presenter.presentView() );

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

        GameSelectionPresenter gameSelectionPresenter = new GameSelectionPresenter(new GameSelectionViewImpl(),new GameSelectionModelImpl());
        gameSelectionPresenter.showView();

        gameSelectionPresenter.setHomeViewListener(this);
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
