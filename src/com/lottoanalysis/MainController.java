package com.lottoanalysis;


import com.lottoanalysis.chartanalysis.ChartAnalysisController;
import com.lottoanalysis.lottoanalysisnav.LottoAnalysisHomeController;
import com.lottoanalysis.lottoinfoandgames.LottoInfoAndGamesController;
import com.lottoanalysis.lottodashboard.LottoDashboardController;
import javafx.fxml.FXML;

/**
 * This class will allow all controllers in the application to communicate with one another. Whenever a new controller
 * is added to the application be sure to register the controller here and initialize the controller by passing the main
 * controller in as reference to the controller class that has a main controller instance inside. Remember that each
 * controller is responsible for its own view so remember to register that view via include in the Main application view
 * fxml file with an id this will be needed for this class to work appropriately.
 */
public class MainController {

    public LottoAnalysisHomeController lottoAnalysisHomeController;
    public LottoInfoAndGamesController lottoInfoAndGamesController;
    public LottoDashboardController lottoDashboardController;
    public ChartAnalysisController chartAnalysisController;

    @FXML
    public void initialize() {
        lottoAnalysisHomeController.init(this);
        lottoInfoAndGamesController.init(this);
        lottoDashboardController.init(this);
    }
}
