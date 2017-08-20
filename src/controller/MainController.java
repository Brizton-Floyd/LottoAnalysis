package controller;


import controller.logicalControllers.LottoAnalysisHomeController;
import controller.logicalControllers.LottoInfoAndGamesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Created by briztonfloyd on 8/18/17.
 */
public class MainController {

    @FXML
    public LottoAnalysisHomeController lottoAnalysisHomeController;

    @FXML
    public LottoInfoAndGamesController lottoInfoAndGamesController;



    @FXML public void initialize(){
        lottoAnalysisHomeController.init(this);
        lottoInfoAndGamesController.init(this);
    }

}
