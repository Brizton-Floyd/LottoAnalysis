package com.lottoanalysis.controllers;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.Main;
import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.models.pastresults.DrawHistoryAnalyzer;
import com.lottoanalysis.models.pastresults.LottoNumberGameOutTracker;
import com.lottoanalysis.models.pastresults.SumGroupAnalyzer;
import com.lottoanalysis.models.pastresults.TotalWinningNumberTracker;
import com.lottoanalysis.presenters.DrawHistoryPresenter;
import com.lottoanalysis.screenavigator.LottoScreenNavigator;
import com.lottoanalysis.views.DrawHistoryView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class: This class is responsible for handling all events that are contained in the Lotto Analysis  Home View file. This
 * class will invoke all events that allow the overall application to work accordingly. (i.e. calling other controllers via
 * event handlers etc..
 */
public class LottoAnalysisHomeController {


    private MainController mainController;
    private List<JFXButton> buttons = new ArrayList<>();

    @FXML
    private JFXButton btn_game, btn_lottoModalCloser, help_button, btn_search, btn_gapSpacing,btn_lottoDashboard, btn_charAnalysis, btn_compLottoAnalysis, btn_neutral,
            btn_lengthy, gameOutBtn,betSlipBtn,gamePanelBtn;

    @FXML
    private Label lbl_game_hover, lbl_game_help, lbl_search;

    @FXML
    private AnchorPane mainPane;

    /**
     * Holder of a switchable lotto screens.
     */
    @FXML
    private StackPane screenHolder;

    /**
     * Replaces the screen displayed in the lottoScreen screenHolder with a new screen.
     *
     * @param node the vista node to be swapped in.
     */
    public void setLottoScreen(Node node) {

        screenHolder.getChildren().setAll(node);
    }

    @FXML
    public void initialize() {
        stylingAndVisibilityEvents();
    }

    public LottoAnalysisHomeController() {

    }

    @FXML
    public void loadCompanionNumberScreen(ActionEvent event) {

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);
        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FIVE, LotteryGameConstants.COMPANION_NUMBER_ANALYSIS_CONTOLLER, allData);
        } else {
            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FIVE, LotteryGameConstants.COMPANION_NUMBER_ANALYSIS_CONTOLLER, allData);

        }
    }

    private void enableOtherButtons() {

        for (Iterator<JFXButton> jfxButtonIterator = buttons.iterator(); jfxButtonIterator.hasNext(); ) {

            JFXButton button = jfxButtonIterator.next();
            button.setDisable(false);
            jfxButtonIterator.remove();
        }
    }

    @FXML
    public void loadGroupChartScreen(ActionEvent event) {

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FOUR, LotteryGameConstants.GROUP_CHART_ANALYSIS_CONTOLLER, allData);
        } else {

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FOUR, LotteryGameConstants.GROUP_CHART_ANALYSIS_CONTOLLER, allData);
        }

    }

    @FXML
    public void loadLotteryDashBoardScreen(ActionEvent event) {

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        if (LottoInfoAndGamesController.getStaticPane() == null)
            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_ONE, null, null);
        else
            LottoScreenNavigator.getMainController().setLottoScreen(LottoInfoAndGamesController.getStaticPane());
    }

    public void loadLotteryDashBoardScreen() {

        LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_ONE, null, null);
    }

    @FXML
    public void makeGamePanelAppear(ActionEvent event) {

        //JFXButton jfxButton = buttons.iterator().next();
        //jfxButton.setDisable(false);
        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        LottoInfoAndGamesController.makeGamePanelAppear(event);

    }

    @FXML
    public void loadGameOutView(ActionEvent event) {

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};
        }

        try {
            btn_lengthy.setDisable(true);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(LottoScreenNavigator.LOTTO_SCREEN_SIX));
            AnchorPane pane = loader.load();

            pane.setManaged(false);
            pane.managedProperty().bind(pane.visibleProperty());
            NumberMultipleController numberMultipleController = loader.getController();
            numberMultipleController.init(allData);

            setLottoScreen(pane);

//            Scene scene = new Scene(pane, 1500, 750);
//            Stage stage = new Stage();
//
//            stage.setOnHiding(event1 -> {
//                btn_lengthy.setDisable(false);
//            });
//
//            stage.setScene(scene);
//            stage.setResizable(false);
//            stage.setTitle("Number Multiple Analyzer Chart " + game.getGameName());
//            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void loadGameOutChart(ActionEvent event) {

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};
        }

        try {
            gameOutBtn.setDisable(true);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(LottoScreenNavigator.LOTTO_SCREEN_SEVEN));
            AnchorPane pane = loader.load();

            GameOutController gameOutController = loader.getController();
            gameOutController.init( allData );

            pane.setManaged(false);
            pane.managedProperty().bind(pane.visibleProperty());
            setLottoScreen(pane);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadGapSpacingChart( ActionEvent event ){

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};
        }

        try {
            btn_gapSpacing.setDisable(true);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(LottoScreenNavigator.LOTTO_SCREEN_EIGHT));
            AnchorPane pane = loader.load();

            GapSpacingController gameOutController = loader.getController();
            gameOutController.init( allData );

            pane.setManaged(false);
            pane.managedProperty().bind(pane.visibleProperty());

            setLottoScreen(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadLotteryChartAnalysisScreen(ActionEvent event) {

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};
        }

        try {
            btn_charAnalysis.setDisable(true);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(LottoScreenNavigator.LOTTO_SCREEN_TWO));
            AnchorPane pane = loader.load();
            pane.setManaged(false);
            pane.managedProperty().bind(pane.visibleProperty());

            ChartAnalysisController gameOutController = loader.getController();
            gameOutController.init( allData );
            gameOutController.start();

            setLottoScreen(pane);
//            Scene scene = new Scene(pane, 1500, 750);
//            Stage stage = new Stage();
//
//            stage.setOnHiding(event1 -> {
//                btn_charAnalysis.setDisable(false);
//            });

//            stage.setScene(scene);
//            stage.setResizable(false);
//            stage.setTitle("Three Chart Analysis Screen " + game.getGameName());
//            //stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void loadBetSlipAnalysisView(ActionEvent event){

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add(button);
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if (game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};
        }


        DrawHistoryAnalyzer drawHistoryAnalyzer = new DrawHistoryAnalyzer( allData, new TotalWinningNumberTracker(),
                                                                           new LottoNumberGameOutTracker(), new SumGroupAnalyzer());
        DrawHistoryView drawHistoryView = new DrawHistoryView(drawHistoryAnalyzer);
        DrawHistoryPresenter drawHistoryPresenter = new DrawHistoryPresenter(drawHistoryAnalyzer, drawHistoryView);

        setLottoScreen( drawHistoryPresenter.presentViewForDisplay() );


    }
    /**
     * Events to handle styling and visibility of UI Elements
     */
    private void stylingAndVisibilityEvents() {

        btn_lottoDashboard.setOnMouseExited(e -> {
            btn_lottoDashboard.setStyle("-fx-font-size: 13px;");
        });
        btn_lottoDashboard.setOnMouseEntered(e -> {
            btn_lottoDashboard.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });
        btn_charAnalysis.setOnMouseExited(e -> {
            btn_charAnalysis.setStyle("-fx-font-size: 13px;");
        });
        btn_charAnalysis.setOnMouseEntered(e -> {
            btn_charAnalysis.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });
        btn_compLottoAnalysis.setOnMouseExited(e -> {
            btn_compLottoAnalysis.setStyle("-fx-font-size: 13px;");
        });
        btn_compLottoAnalysis.setOnMouseEntered(e -> {
            btn_compLottoAnalysis.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });
        btn_neutral.setOnMouseExited(e -> {
            btn_neutral.setStyle("-fx-font-size: 13px;");
        });
        btn_neutral.setOnMouseEntered(e -> {
            btn_neutral.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });
        btn_lengthy.setOnMouseExited(e -> {
            btn_lengthy.setStyle("-fx-font-size: 13px;");
        });
        btn_lengthy.setOnMouseEntered(e -> {
            btn_lengthy.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });

        gameOutBtn.setOnMouseExited(e -> {
            gameOutBtn.setStyle("-fx-font-size: 13px;");
        });
        gameOutBtn.setOnMouseEntered(e -> {
            gameOutBtn.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });
        btn_gapSpacing.setOnMouseExited(e -> {
            btn_gapSpacing.setStyle("-fx-font-size: 13px;");
        });
        btn_gapSpacing.setOnMouseEntered(e -> {
            btn_gapSpacing.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });
        betSlipBtn.setOnMouseExited(e -> {
            betSlipBtn.setStyle("-fx-font-size: 13px;");
        });
        betSlipBtn.setOnMouseEntered(e -> {
            betSlipBtn.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });
        gamePanelBtn.setOnMouseExited(e -> {
            gamePanelBtn.setStyle("-fx-font-size: 13px;");
        });
        gamePanelBtn.setOnMouseEntered(e -> {
            gamePanelBtn.setStyle("-fx-font-size: 13px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;");
        });

    }
}
