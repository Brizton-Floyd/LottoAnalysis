package com.lottoanalysis.com.lottoanalysis.screenloader;


import com.lottoanalysis.chartanalysis.ChartAnalysisController;
import com.lottoanalysis.lottoanalysisnav.LottoAnalysisHomeController;
import com.lottoanalysis.lottoinfoandgames.LottoInfoAndGamesController;
import com.lottoanalysis.lottodashboard.LottoDashboardController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class will allow all controllers in the application to communicate with one another. Whenever a new controller
 * is added to the application be sure to register the controller here and initialize the controller by passing the main
 * controller in as reference to the controller class that has a main controller instance inside. Remember that each
 * controller is responsible for its own view so remember to register that view via include in the Main application view
 * fxml file with an id this will be needed for this class to work appropriately.
 */
public class MainController {

    /** Holder of a switchable lotto screens. */
    @FXML
    private StackPane screenHolder;

    /**
     * Replaces the screen displayed in the lottoScreen holder with a new screen.
     *
     * @param node the vista node to be swapped in.
     */
    public void setLottoScreen(Node node) {
        screenHolder.getChildren().setAll(node);
    }

}
