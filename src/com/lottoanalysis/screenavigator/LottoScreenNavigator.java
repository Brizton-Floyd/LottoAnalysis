package com.lottoanalysis.screenavigator;

import com.lottoanalysis.controllers.ChartAnalysisController;
import com.lottoanalysis.controllers.GroupChartController;
import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.controllers.CompanionNumberController;
import com.lottoanalysis.controllers.LottoAnalysisHomeController;
import com.lottoanalysis.lottogames.LottoGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class LottoScreenNavigator {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String MAIN    = "/com/lottoanalysis/view/LottoAnalysisHome.fxml";
    public static final String LOTTO_SCREEN_ONE   = "/com/lottoanalysis/view/LottoDashboard.fxml";
    public static final String LOTTO_SCREEN_TWO   = "/com/lottoanalysis/view/ChartView.fxml";
    public static final String LOTTO_SCREEN_THREE = "/com/lottoanalysis/view/LottoInfoAndGames.fxml";
    public static final String LOTTO_SCREEN_FOUR = "/com/lottoanalysis/view/GroupChartView.fxml";
    public static final String LOTTO_SCREEN_FIVE = "/com/lottoanalysis/view/CompanionNumber.fxml";

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
    public static void loadLottoScreen( String fxml, String controllerName, Object... domainObject ) {

        try {

            Pane pane = LottoScreenNavigator.getAppropriateView( fxml, controllerName, domainObject );
            mainController.setLottoScreen( pane );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static Pane getAppropriateView( String view, String controllerName, Object... domainObject ) throws IOException{

        FXMLLoader loader = new FXMLLoader(LottoScreenNavigator.class.getResource(view) );
        Pane pane;

        if(domainObject != null ){

            pane = loader.load( );
            if( domainObject[0] instanceof LottoGame){

                // use the values passed in by the domain object and provide contrller with
                // appropriate values
                if(controllerName.equalsIgnoreCase(LotteryGameConstants.CHART_ANALYSIS_CONTROLLER)) {
                    LottoGame game = (LottoGame) domainObject[0];

                    ChartAnalysisController controller = loader.getController();
                    controller.setDrawNumbers((int[][]) ((List<Object>) domainObject[1]).get(3));
                    controller.setLotteryGame(game);
                    controller.start();
                }
                else if( controllerName.equalsIgnoreCase(LotteryGameConstants.GROUP_CHART_ANALYSIS_CONTOLLER)){

                    LottoGame game = (LottoGame) domainObject[0];

                    GroupChartController controller = loader.getController();
                    controller.initFields(game, (int[][])((List<Object>)domainObject[1]).get(0));
                    controller.startSceneLayoutSequence();
                }
                else if(controllerName.equalsIgnoreCase(LotteryGameConstants.COMPANION_NUMBER_ANALYSIS_CONTOLLER)){

                    LottoGame game = (LottoGame) domainObject[0];
                    CompanionNumberController controller = loader.getController();

                    controller.setPostionalNumbers((int[][])((List<Object>)domainObject[1]).get(0));
                    controller.setGame(game);
                    controller.initComponents();
                }
            }

        }else{
            pane = loader.load();
        }

        return pane;
    }
}