package com.lottoanalysis.screenloader;

import com.lottoanalysis.chartanalysis.ChartAnalysisController;
import com.lottoanalysis.lottoanalysisnav.LottoAnalysisHomeController;
import com.lottoanalysis.lottoinfoandgames.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LottoScreenNavigator {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String MAIN    = "/com/lottoanalysis/view/LottoAnalysisHome.fxml";
    public static final String LOTTO_SCREEN_ONE   = "/com/lottoanalysis/view/LottoDashboard.fxml";
    public static final String LOTTO_SCREEN_TWO   = "/com/lottoanalysis/view/ChartAnalysis.fxml";
    public static final String LOTTO_SCREEN_THREE = "/com/lottoanalysis/view/LottoInfoAndGames.fxml";

    /** The main application layout controller. */
    private static LottoAnalysisHomeController mainController;


    public static LottoAnalysisHomeController getMainController() {
        return mainController;
    }

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(LottoAnalysisHomeController mainController) {
        LottoScreenNavigator.mainController = mainController;
    }

    /**
     * Loads the vista specified by the fxml file into the
     * vistaHolder pane of the main application layout.
     *
     * Previously loaded vista for the same fxml file are not cached.
     * The fxml is loaded anew and a new vista node hierarchy generated
     * every time this method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     *   cache FXMLLoaders
     *   cache loaded vista nodes, so they can be recalled or reused
     *   allow a user to specify vista node reuse or new creation
     *   allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadLottoScreen( String fxml, Object domainObject ) {

        try {

            Pane pane = getAppropriateView( fxml, domainObject );
            mainController.setLottoScreen( pane );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Pane getAppropriateView( String view, Object domainObject ) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        Pane pane;

        if(domainObject != null ){

            pane = loader.load( LottoScreenNavigator.class.getResource(view) );
            if( domainObject instanceof LotteryGame ){

                // use the values passed in by the domain object and provide contrller with
                // appropriate values
                LotteryGame game = (LotteryGame) domainObject;

                ChartAnalysisController controller = loader.getController();
            }

        }else{
            pane = loader.load( LottoScreenNavigator.class.getResource(view) );
        }

        return pane;
    }
}