package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.controllers.LottoDashboardController;
import com.lottoanalysis.controllers.LottoInfoAndGamesController;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.models.drawhistory.DrawHistoryModel;
import com.lottoanalysis.models.drawhistory.LottoNumberGameOutTracker;
import com.lottoanalysis.models.drawhistory.SumGroupAnalyzer;
import com.lottoanalysis.models.drawhistory.TotalWinningNumberTracker;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryViewImpl;
import com.lottoanalysis.ui.homeview.HomeView;
import com.lottoanalysis.ui.homeview.HomeViewListener;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class HomeViewPresenter implements HomeViewListener {

    private HomeView homeViewImpl;

    public HomeViewPresenter(HomeView homeView){

        this.homeViewImpl = homeView;
        homeViewImpl.setHomeViewListener( this );

    }

    @Override
    public void executeBetSlipAnalysis() {

        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};
        }
        DrawHistoryModel drawHistoryModel = new DrawHistoryModel(
                allData,
                new TotalWinningNumberTracker(),
                new LottoNumberGameOutTracker(),
                new SumGroupAnalyzer()
        );
        DrawHistoryViewImpl drawHistoryViewImpl = new DrawHistoryViewImpl();
        DrawHistoryPresenter drawHistoryPresenter = new DrawHistoryPresenter(drawHistoryModel, drawHistoryViewImpl);

        homeViewImpl.injectView( drawHistoryPresenter.presentViewForDisplay() );
    }

    @Override
    public AnchorPane onGameLoad() {
        return homeViewImpl.getView();
    }
}
