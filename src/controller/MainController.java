package controller;


import controller.logicalControllers.LottoAnalysisHomeController;
import controller.logicalControllers.LottoDashboardController;
import controller.logicalControllers.LottoInfoAndGamesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * This class will allow all controllers in the application to communicate with one another. Whenever a new controller
 * is added to the application be sure to register the controller here and initialize the controller by passing the main
 * controller in as reference to the controller class that has a main controller instance inside. Remember that each
 * controller is responsible for its own view so remember to register that view via include in the Main application view
 * fxml file with an id this will be needed for this class to work appropriately.
 */
public class MainController {

    @FXML
    public LottoAnalysisHomeController lottoAnalysisHomeController;

    @FXML
    public LottoInfoAndGamesController lottoInfoAndGamesController;

    @FXML
    public LottoDashboardController lottoDashboardController;



    @FXML public void initialize(){
        lottoAnalysisHomeController.init(this);
        lottoInfoAndGamesController.init(this);
        lottoDashboardController.init(this);
    }

}
