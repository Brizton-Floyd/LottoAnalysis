package com.lottoanalysis.controllers;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.Main;
import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.enums.LotteryGame;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.screenavigator.LottoScreenNavigator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class: This class is responsible for handling all events that are contained in the Lotto Analysis  Home View file. This
 * class will invoke all events that allow the overall application to work accordingly. (i.e. calling other controllers via
 * event handlers etc..
 */
public class LottoAnalysisHomeController  {


    private MainController mainController;
    private List<JFXButton> buttons = new ArrayList<>();

    @FXML
    private JFXButton btn_game, btn_lottoModalCloser, help_button, btn_search, btn_lottoDashboard, btn_charAnalysis, btn_compLottoAnalysis, btn_neutral, btn_gameOutView;

    @FXML
    private Label lbl_game_hover, lbl_game_help, lbl_search;

    @FXML
    private AnchorPane mainPane;

    /** Holder of a switchable lotto screens. */
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
    public void initialize(){
        stylingAndVisibilityEvents();
    }

    public LottoAnalysisHomeController(){

    }

    @FXML
    public void loadCompanionNumberScreen(ActionEvent event)
    {

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add( button );
        button.setDisable(true);
        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if(game == null || drawData == null) {

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FIVE, LotteryGameConstants.COMPANION_NUMBER_ANALYSIS_CONTOLLER, allData);
        }
        else{
            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FIVE, LotteryGameConstants.COMPANION_NUMBER_ANALYSIS_CONTOLLER, allData);

        }
    }

    private void enableOtherButtons() {

        for(Iterator<JFXButton> jfxButtonIterator = buttons.iterator(); jfxButtonIterator.hasNext();){

            JFXButton button = jfxButtonIterator.next();
            button.setDisable(false);
            jfxButtonIterator.remove();
        }
    }

    @FXML
    public void loadGroupChartScreen(ActionEvent event){

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add( button );
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if(game == null || drawData == null){

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FOUR, LotteryGameConstants.GROUP_CHART_ANALYSIS_CONTOLLER, allData);
        }else{

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_FOUR, LotteryGameConstants.GROUP_CHART_ANALYSIS_CONTOLLER,allData);
        }

    }
    @FXML
    public void loadLotteryDashBoardScreen(ActionEvent event){

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add( button );
        button.setDisable(true);

        if(LottoInfoAndGamesController.getStaticPane() == null)
            LottoScreenNavigator.loadLottoScreen( LottoScreenNavigator.LOTTO_SCREEN_ONE, null ,null);
        else
            LottoScreenNavigator.getMainController().setLottoScreen( LottoInfoAndGamesController.getStaticPane());
    }

    public void loadLotteryDashBoardScreen(){

            LottoScreenNavigator.loadLottoScreen( LottoScreenNavigator.LOTTO_SCREEN_ONE, null ,null);
    }

    @FXML
    public void makeGamePanelAppear(ActionEvent event){

        LottoInfoAndGamesController.makeGamePanelAppear( event );

    }

    @FXML
    public void loadGameOutView( ActionEvent event){

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add( button );
        button.setDisable(true);

        try {
            btn_gameOutView.setDisable(true);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(LottoScreenNavigator.LOTTO_SCREEN_SIX));
            AnchorPane pane = loader.load();

            LottoGame lotteryGame = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
            if(lotteryGame == null){
                lotteryGame = LottoDashboardController.getClassLevelLotteryGame();
            }

            GameOutViewController gameOutViewController = loader.getController();
            gameOutViewController.init( lotteryGame );

            Scene scene = new Scene(pane,1500,750);
            Stage stage = new Stage();

            stage.setOnHiding(event1 -> {
                btn_gameOutView.setDisable(false);
            });

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Game Out View");
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void loadLotteryChartAnalysisScreen(ActionEvent event){

        enableOtherButtons();
        JFXButton button = (JFXButton) event.getSource();
        buttons.add( button );
        button.setDisable(true);

        // Retrieve the current game that is currently being played
        LottoGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();
        List<Object> drawData = LottoInfoAndGamesController.getValues();

        Object[] allData = {game, drawData};

        if(game == null || drawData == null){

            game = LottoDashboardController.getClassLevelLotteryGame();
            drawData = LottoDashboardController.getNumbersForChartDisplay();
            allData = new Object[]{game, drawData};

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_TWO, LotteryGameConstants.CHART_ANALYSIS_CONTROLLER, allData);
        }else{

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_TWO, LotteryGameConstants.CHART_ANALYSIS_CONTROLLER,allData);
        }

    }

    /**
     * Events to handle styling and visibility of UI Elements
     */
    private void stylingAndVisibilityEvents() {

        btn_lottoDashboard.setOnMouseExited(e -> {btn_lottoDashboard.setStyle("-fx-font-size: 13px;");});
        btn_lottoDashboard.setOnMouseEntered(e -> {btn_lottoDashboard.setStyle("-fx-font-size: 13px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_charAnalysis.setOnMouseExited(e -> {btn_charAnalysis.setStyle("-fx-font-size: 13px;");});
        btn_charAnalysis.setOnMouseEntered(e -> {btn_charAnalysis.setStyle("-fx-font-size: 13px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_compLottoAnalysis.setOnMouseExited(e -> {btn_compLottoAnalysis.setStyle("-fx-font-size: 13px;");});
        btn_compLottoAnalysis.setOnMouseEntered(e -> {btn_compLottoAnalysis.setStyle("-fx-font-size: 13px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_neutral.setOnMouseExited(e -> {btn_neutral.setStyle("-fx-font-size: 13px;");});
        btn_neutral.setOnMouseEntered(e -> {btn_neutral.setStyle("-fx-font-size: 13px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_gameOutView.setOnMouseExited(e -> {btn_gameOutView.setStyle("-fx-font-size: 13px;");});
        btn_gameOutView.setOnMouseEntered(e -> {btn_gameOutView.setStyle("-fx-font-size: 13px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
    }
}
