package controller.logicalControllers;

import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LottoAnalysisHomeController {


    private MainController mainController;

    @FXML
    private JFXButton btn_game, btn_lottoModalCloser, help_button, btn_search, btn_lottoDashboard, btn_charAnalysis,
            btn_compLottoAnalysis, btn_neutral, btn_lengthy;

    @FXML
    private Label lbl_game_hover, lbl_game_help, lbl_search;


    public void init(MainController mainController) {

        this.mainController = mainController;

        lbl_game_help.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
        lbl_game_hover.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
        lbl_search.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");


        HandleEventsAndActions();
    }

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
            mainController.lottoInfoAndGamesController.controlAppearanceOfGamePanel(e);
            btn_game.setDisable(true);
        });


        help_button.setOnAction(e -> {

            // Still needs to be implemented
        });
        btn_search.setOnAction(event -> {

        });


        btn_lottoDashboard.setOnMouseExited(event);
        btn_lottoDashboard.setOnMouseEntered(event);
        btn_charAnalysis.setOnMouseEntered(event);
        btn_charAnalysis.setOnMouseExited(event);
        btn_compLottoAnalysis.setOnMouseExited(event);
        btn_compLottoAnalysis.setOnMouseEntered(event);
        btn_neutral.setOnMouseEntered(event);
        btn_neutral.setOnMouseExited(event);
        btn_lengthy.setOnMouseEntered(event);
        btn_lengthy.setOnMouseExited(event);

        btn_search.setOnMouseEntered(e -> lbl_search.setVisible(true));
        btn_search.setOnMouseExited(e -> lbl_search.setVisible(false));
        btn_game.setOnMouseEntered(e -> lbl_game_hover.setVisible(true));
        btn_game.setOnMouseExited(e -> lbl_game_hover.setVisible(false));
        help_button.setOnMouseEntered(e -> lbl_game_help.setVisible(true));
        help_button.setOnMouseExited(e -> lbl_game_help.setVisible(false));

    }

    /**
     * Event handler to handle all mouse events pertaining to the main GUI dashboard
     * and not the side pane
     */
    EventHandler<MouseEvent> event = e -> {

        Button b = (Button) e.getSource();

        String type = e.getEventType().toString();
        if (type.equals("MOUSE_ENTERED")) {
            b.setOnMouseEntered(event1 -> b.setStyle("-fx-font-size: 15px;" +
                    "-fx-background-color: #515B51;" +
                    "-fx-text-fill: #EFA747;"));

        } else {
            b.setOnMouseExited(event1 -> b.setStyle("-fx-font-size: 13px;"));
        }
    };
}
