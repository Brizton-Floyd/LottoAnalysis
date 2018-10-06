package com.lottoanalysis.ui.presenters;

import com.lottoanalysis.interfaces.Cacheable;
import com.lottoanalysis.models.GameSelectionObject;
import com.lottoanalysis.models.companionnumber.CompanionNumber;
import com.lottoanalysis.models.drawhistory.DrawModel;
import com.lottoanalysis.models.drawhistory.LottoNumberGameOutTracker;
import com.lottoanalysis.models.drawhistory.SumGroupAnalyzer;
import com.lottoanalysis.models.drawhistory.TotalWinningNumberTracker;
import com.lottoanalysis.models.gameout.GameOutModel;
import com.lottoanalysis.models.lottogames.FiveDigitLotteryGameImpl;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.managers.CacheManager;
import com.lottoanalysis.services.homeviewservices.HomeServiceRepositoryImpl;
import com.lottoanalysis.services.homeviewservices.LottoDashBoardHomeService;
import com.lottoanalysis.services.homeviewservices.LottoDashBoardHomeServiceImpl;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardViewImpl;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryViewImpl;
import com.lottoanalysis.ui.gameselection.GameSelectionViewImpl;
import com.lottoanalysis.ui.homeview.EventSource;
import com.lottoanalysis.ui.homeview.HomeViewImpl;
import com.lottoanalysis.ui.homeview.HomeViewListener;
import com.lottoanalysis.ui.presenters.base.BasePresenter;
import javafx.scene.layout.AnchorPane;

public class HomeViewPresenter extends BasePresenter<HomeViewImpl, LottoGame> implements HomeViewListener {

    private Cacheable gameCache;

    public HomeViewPresenter(HomeViewImpl homeView){
        super(homeView, new FiveDigitLotteryGameImpl());

        getModel().addListener( (this) );
        getView().setPresenter( (this) );

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
            case GAME_OUT_ANALYSIS:
                presentGameOutAnalysisView();
                break;
            case Lottery_Number_Gaps:
                presentLotteryNumberGapView();
                break;
            case COMPANION_NUMBER:
                presentCompanionNumberView();
                break;
            case POSITION_HIT_SEQUENCE:
                presentPositionHitSequenceChart();
                break;
        }
    }

    @Override
    public void handleOnModelChanged(String property) {

        switch (property){
            case "list":
            case "name":
                loadGameDashBoard();
                break;
        }

    }

    private void presentPositionHitSequenceChart(){
        PositionHitSequencePresenter positionHitSequencePresenter = new PositionHitSequencePresenter(getModel());
        getView().injectView(positionHitSequencePresenter.getView());
    }

    private void presentCompanionNumberView(){
        CompanionNumberPresenter companionNumberPresenter = new CompanionNumberPresenter(new CompanionNumber(getModel()));
        getView().injectView(companionNumberPresenter.getView());
    }

    private void presentLotteryNumberGapView() {
        LotteryNumberGapPresenter lotteryNumberGapPresenter = new LotteryNumberGapPresenter(getModel());
        getView().injectView(lotteryNumberGapPresenter.getView());
    }

    private void presentGameOutAnalysisView() {

        GameOutPresenter gameOutPresenter = new GameOutPresenter(new GameOutModel(getModel()));
        getView().injectView( gameOutPresenter.getViewForDisplay());
    }

    private void executeBetSlipAnalysis() {

        DrawModel drawHistoryModel = new DrawModel(
                getModel(),
                new TotalWinningNumberTracker(),
                new LottoNumberGameOutTracker(),
                new SumGroupAnalyzer()
        );

        DrawHistoryPresenter drawHistoryPresenter = new DrawHistoryPresenter(drawHistoryModel, new DrawHistoryViewImpl());

        getView().injectView( drawHistoryPresenter.presentViewForDisplay() );
    }

    private void loadGameDashBoard(int id) {


        LottoDashBoardHomeService lottoDashBoardHomeService = new LottoDashBoardHomeServiceImpl(new HomeServiceRepositoryImpl());
        setModel( lottoDashBoardHomeService.loadById( id ) );

        loadGameDashBoard(getModel());

    }

    private void presentLottoGameSelectionView() {

        if(gameCache == null){

            GameSelectionPresenter gameSelectionPresenter = new GameSelectionPresenter(new GameSelectionViewImpl(), getModel());
            gameSelectionPresenter.setHomeViewListener(this);
            gameSelectionPresenter.showView();

            gameCache = new GameSelectionObject(getModel(), getModel().getLottoId(), 0);


            CacheManager.putCache( gameCache );
        }
        else{

            Cacheable cacheable = CacheManager.getCache( gameCache );
            setModel( (LottoGame)cacheable.getObject() );

            GameSelectionPresenter gameSelectionPresenter = new GameSelectionPresenter(new GameSelectionViewImpl(), getModel());
            gameSelectionPresenter.showView();
            gameSelectionPresenter.setHomeViewListener(this);

        }
    }

    private void loadGameDashBoard(LottoGame lottoGame) {

        LottoDashBoardPresenter presenter = new LottoDashBoardPresenter(new LottoDashBoardViewImpl(), lottoGame);
        //presenter.setListener(this);

        getView().injectView( presenter.presentView() );
    }

    private void loadGameDashBoard() {

        if(getModel().getLottoId() <= 0) {

            LottoDashBoardHomeService lottoDashBoardHomeService = new LottoDashBoardHomeServiceImpl(new HomeServiceRepositoryImpl());
            setModel( lottoDashBoardHomeService.getDefaultGame() );
            getModel().addListener( (this) );

            LottoDashBoardPresenter presenter = new LottoDashBoardPresenter(new LottoDashBoardViewImpl(), getModel());

            getView().injectView(presenter.presentView());
        }
        else{

            loadGameDashBoard( getModel().getLottoId() );
            getModel().addListener(this);
        }

    }

    public AnchorPane onGameLoad() {
        return getView().display();
    }

    void copy(LottoGame lottoGame) {
        setModel( lottoGame );
    }

    void enable() {

        getView().enableButtonAndDisableDashboardButton();

    }

}
