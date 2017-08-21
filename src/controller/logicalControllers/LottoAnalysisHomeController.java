package controller.logicalControllers;

import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Class: This class is responsible for handling all events that are contained in the Lotto Analysis Home View file. This
 * class will invoke all events that allow the overall application to work accordingly. (i.e. calling other controllers via
 * event handlers etc..
 */
public class LottoAnalysisHomeController {


    private MainController mainController;

    @FXML
    private JFXButton btn_game, btn_lottoModalCloser, help_button, btn_search, btn_lottoDashboard, btn_charAnalysis,
            btn_compLottoAnalysis, btn_neutral, btn_lengthy;

    @FXML
    private Label lbl_game_hover, lbl_game_help, lbl_search;


    public void init(MainController mainController) {

        this.mainController = mainController;

        HandleEventsAndActions();
        stylingAndVisibilityEvents();
    }

    /**
     * Events to handle UI Actions
     */
    private void HandleEventsAndActions() {
        btn_lottoModalCloser.setOnAction(e -> {

            if (mainController.lottoInfoAndGamesController.isGamePanelOpen()) {
                mainController.lottoInfoAndGamesController.closePanel();
                btn_lottoModalCloser.setVisible(false);
                btn_game.setDisable(false);
            }

        });

        btn_game.setOnAction(e -> {

            btn_lottoModalCloser.setVisible(true);
            mainController.lottoInfoAndGamesController.makeGamePanelAppear(e);
            btn_game.setDisable(true);

        });


        help_button.setOnAction(e -> {

            // Still needs to be implemented
        });
        btn_search.setOnAction(event -> {
            // Still needs to be implemented
        });

    }

    /**
     * Events to handle styling and visibility of UI Elements
     */
    private void stylingAndVisibilityEvents() {

        lbl_game_help.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
        lbl_game_hover.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
        lbl_search.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");

        btn_lottoDashboard.setOnMouseExited(e -> {btn_lottoDashboard.setStyle("-fx-font-size: 13px;");});
        btn_lottoDashboard.setOnMouseEntered(e -> {btn_lottoDashboard.setStyle("-fx-font-size: 15px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_charAnalysis.setOnMouseExited(e -> {btn_charAnalysis.setStyle("-fx-font-size: 13px;");});
        btn_charAnalysis.setOnMouseEntered(e -> {btn_charAnalysis.setStyle("-fx-font-size: 15px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_compLottoAnalysis.setOnMouseExited(e -> {btn_compLottoAnalysis.setStyle("-fx-font-size: 13px;");});
        btn_compLottoAnalysis.setOnMouseEntered(e -> {btn_compLottoAnalysis.setStyle("-fx-font-size: 15px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_neutral.setOnMouseExited(e -> {btn_neutral.setStyle("-fx-font-size: 13px;");});
        btn_neutral.setOnMouseEntered(e -> {btn_neutral.setStyle("-fx-font-size: 15px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});
        btn_lengthy.setOnMouseExited(e -> {btn_lengthy.setStyle("-fx-font-size: 13px;");});
        btn_lengthy.setOnMouseEntered(e -> {btn_lengthy.setStyle("-fx-font-size: 15px;" +
                "-fx-background-color: #515B51;" +
                "-fx-text-fill: #EFA747;");});

        btn_search.setOnMouseEntered(e -> lbl_search.setVisible(true));
        btn_search.setOnMouseExited(e -> lbl_search.setVisible(false));
        btn_game.setOnMouseEntered(e -> lbl_game_hover.setVisible(true));
        btn_game.setOnMouseExited(e -> lbl_game_hover.setVisible(false));
        help_button.setOnMouseEntered(e -> lbl_game_help.setVisible(true));
        help_button.setOnMouseExited(e -> lbl_game_help.setVisible(false));
    }

}
