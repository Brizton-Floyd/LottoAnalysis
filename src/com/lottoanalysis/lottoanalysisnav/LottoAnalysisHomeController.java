package com.lottoanalysis.lottoanalysisnav;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.screenloader.LottoScreenNavigator;
import com.lottoanalysis.screenloader.MainController;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.lottoinfoandgames.LottoInfoAndGamesController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javafx.event.*;
import javafx.scene.layout.StackPane;

/**
 * Class: This class is responsible for handling all events that are contained in the Lotto Analysis  Home View file. This
 * class will invoke all events that allow the overall application to work accordingly. (i.e. calling other controllers via
 * event handlers etc..
 */
public class LottoAnalysisHomeController  {


    private MainController mainController;

    @FXML
    private JFXButton btn_game, btn_lottoModalCloser, help_button, btn_search, btn_lottoDashboard, btn_charAnalysis, btn_compLottoAnalysis, btn_neutral, btn_lengthy;

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
    public void loadLotteryDashBoardScreen(ActionEvent event){

        LottoScreenNavigator.loadLottoScreen( LottoScreenNavigator.LOTTO_SCREEN_ONE, null );
    }

    @FXML
    public void makeGamePanelAppear(ActionEvent event){

        LottoInfoAndGamesController.makeGamePanelAppear( event );

    }

    @FXML
    public void loadLotteryChartAnalysisScreen(ActionEvent event){

        // Retrieve the current game that is currently being played
        LotteryGame game = LottoInfoAndGamesController.getCurrentLotteryGameBeingPlayed();

        if(game == null ){

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_TWO, null);
        }else{

            LottoScreenNavigator.loadLottoScreen(LottoScreenNavigator.LOTTO_SCREEN_TWO, game);
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
        btn_lengthy.setOnMouseExited(e -> {btn_lengthy.setStyle("-fx-font-size: 13px;");});
        btn_lengthy.setOnMouseEntered(e -> {btn_lengthy.setStyle("-fx-font-size: 13px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
    }
}
