package com.lottoanalysis.controllers;

import com.lottoanalysis.Main;
import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.factories.factoryproducer.FactoryProducer;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.screenavigator.LottoScreenNavigator;
import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.constants.LotteryGameDaoConstants;
import com.lottoanalysis.tasks.DataDownLoaderTask;
import com.lottoanalysis.utilities.fileutilities.OnlineFileUtility;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Class: The LottoInfoGamesController is responsible for handling input accordingly that is entered via the lotto info
 * and lottogames view. This class will process information for a lottery game in a given state. It will aid in
 * retrieving all relevant information for the game the user wants to play. This controller will interact with the lotto
 * game update launcher controller that will ensure all data in the application is up to date.
 */
public class LottoInfoAndGamesController {


    private List<String> itemList = new LinkedList<>();
    private boolean isGamePaneOpen = false;
    private static List<Object> values = new ArrayList<>();
    private static Pane staticPane;
    private static LottoGame lotteryGame;


    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane game_pane;
    @FXML
    public Label lotteryUpdateLabel;

    @FXML
    private ProgressBar updateProgressBar;
    @FXML
    private VBox progressAndLabelVbox, progressBox;

    @FXML
    public void initialize(){

        //Start setting up the lottery game menu
        loadMenuItems();

    }

    public static Pane getStaticPane() {
        return staticPane;
    }

    public static void setStaticPane(Pane staticPane) {
        LottoInfoAndGamesController.staticPane = staticPane;
    }

    public static void makeGamePanelAppear(ActionEvent e) {

        if (e.getSource() instanceof Button) {

            Button button = (Button) e.getSource();

            if (button.getId().equals("btn_game")) {
//                game_pane.setVisible(true);
//                isGamePaneOpen = true;
                // load view
                // loadMenuItems();
                loadview();
            }
        }
    }

    /*
     *Displays menu items of all the lottogames that currently exist for a given states lottery
     *
     */
    private static void loadview() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(LottoScreenNavigator.LOTTO_SCREEN_THREE));
            AnchorPane pane = loader.load();

            //LottoInfoAndGamesController lottoInfoAndGamesController = loader.getController();
            //lottoInfoAndGamesController.init(mainController);

            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Lotto Drawing Updater");
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Once a game is selected from the drop down menu this event will trigger the retrieval of correct lotto information
     * for the selected game
     *
     * @param event
     */
    @FXML
    private void getAppropriateGameData(ActionEvent event) {

        LottoGame game = null;

        // Get the event source and cast to appropriate object type
        MenuItem item = (MenuItem) event.getSource();

        AbstractFactory factory = FactoryProducer.getFactory("lotteryGameFactory");

        FXMLLoader loader = new FXMLLoader( getClass().getResource( LottoScreenNavigator.LOTTO_SCREEN_ONE));
        try {

            Pane pane = (Pane)loader.load();
            setStaticPane(pane);
            LottoDashboardController dashboardController = loader.getController();

            AbstractFactory lottoGameManagerFactory = FactoryProducer.getFactory("lotteryGameManagerFactory");
            LotteryGameManager lotteryGameManager = lottoGameManagerFactory.getLotteryGameManager();


            String gameName = item.getText();
            boolean containsGame = (itemList.contains(gameName));
            if (containsGame) {

                dashboardController.setGameLabels( gameName );

                if (item.getText().equalsIgnoreCase(LotteryGameDaoConstants.UPDATE_DB)) {
                    downloadFilesFromInternet();
                }else {

                    if (gameName.contains(LotteryGameConstants.FANTASY_FIVE_GAME_NAME)) {

                        LottoGame lotteryGame = factory.getLotteryGame("five");
                        lotteryGame.setGameName(gameName);
                        game = lotteryGameManager.loadLotteryData(lotteryGame);
                        //System.out.println(lotteryGame.getCurrentEstimatedJackpot());

                    } else if (gameName.contains(LotteryGameConstants.POWERBALL_GAME_NAME)) {

                        LottoGame lotteryGame = factory.getLotteryGame("six");
                        lotteryGame.setGameName(gameName);
                        game = lotteryGameManager.loadLotteryData(lotteryGame);

                    } else if (gameName.contains(LotteryGameConstants.MEGA_MILLIONS_GAME_NAME)) {

                        LottoGame lotteryGame = factory.getLotteryGame("six");
                        lotteryGame.setGameName(gameName);
                        game = lotteryGameManager.loadLotteryData(lotteryGame);

                    } else if (gameName.contains(LotteryGameConstants.PICK3_GAME_NAME)) {

                        LottoGame lotteryGame = factory.getLotteryGame("THREE");
                        lotteryGame.setGameName(gameName);
                        game = lotteryGameManager.loadLotteryData(lotteryGame);

                    } else if (gameName.contains(LotteryGameConstants.PICK4_GAME_NAME)) {

                        LottoGame lotteryGame = factory.getLotteryGame("four");
                        lotteryGame.setGameName(gameName);
                        game = lotteryGameManager.loadLotteryData(lotteryGame);

                    } else if (gameName.contains(LotteryGameConstants.SUPER_LOTTO_PLUS_GAME_NAME)) {

                        LottoGame lotteryGame = factory.getLotteryGame("six");
                        lotteryGame.setGameName(gameName);
                        game = lotteryGameManager.loadLotteryData(lotteryGame);
                    }

                    // Once we have the selected game from the manager send the data to the table view
                    dashboardController.setUpTableView(game);
                    dashboardController.loadChoicesIntoChoiceBox();

                    List<Object> data = new ArrayList<>();
                    data.add(dashboardController.getPositionalNumbers() );
                    data.add( dashboardController.getDeltaNumberForLastDraw());
                    data.add(dashboardController.getPositionalSums());
                    data.add(dashboardController.getLineSpacings());
                    data.add(dashboardController.getRemainder());

                    setValues( data );

                    // Set static lottery game for reference by other classes
                    setLotteryGame(game);

                    // Prepare view to be rendered in stackpane
                    LottoScreenNavigator.getMainController().setLottoScreen(pane);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Object> getValues() {
        return values;
    }

    public static void setValues(List values) {
        LottoInfoAndGamesController.values = values;
    }
    private static void setLotteryGame(LottoGame game){

        LottoInfoAndGamesController.lotteryGame = game;
    }
    /*
     * Returns the current Lottery game being played in the system really helpful when wanting
     * to render chart data
     */
    public static LottoGame getCurrentLotteryGameBeingPlayed(){
        return lotteryGame;
    }


    /**
     * Method will be responsible for downloading historical data from the internet for lottery lottogames on seperate
     * thread
     */
    private void downloadFilesFromInternet() {

        Task<Void> task = new DataDownLoaderTask(OnlineFileUtility.getUrlPaths(), this);
        updateProgressBar.progressProperty().bind(task.progressProperty());
        lotteryUpdateLabel.textProperty().bind(task.messageProperty());
        lotteryUpdateLabel.setVisible(true);

        Thread thread = new Thread(task,"Thread-FileDownload");
        thread.setDaemon(true);
        thread.start();

        //progressBox.getChildren().add(updateProgressBar);
    }

    /**
     * Method will be responsible for importing lottogames into drop down list based on state user chooses to play in
     */
    private void loadMenuItems() {

        AbstractFactory lottoGameManagerFactory = FactoryProducer.getFactory("lotteryGameManagerFactory");
        LotteryGameManager lotteryGameManager = lottoGameManagerFactory.getLotteryGameManager();

        List<String> menus = lotteryGameManager.getAllGames();

        for (String menu : menus) {
            itemList.add(menu);
        }

        itemList.add("Update Database");
        setMenuItemText(itemList);

    }

    private void setMenuItemText(List<String> itemList) {

        int count = 0;

        for (Menu menu : menuBar.getMenus()) {
            for (MenuItem item : menu.getItems()) {

                if (!(item instanceof SeparatorMenuItem) && !item.getText().equalsIgnoreCase(LotteryGameDaoConstants.UPDATE_DB)) {
                    item.setText(itemList.get(count));
                    count++;
                }
            }
        }

    }


    /**
     * This method will return a boolean indicating the game info panel is open
     *
     * @return
     */
    public boolean isGamePanelOpen() {
        return isGamePaneOpen;
    }

    public void closePanel() {
        game_pane.setVisible(false);
    }

    public void unbindData() {
        updateProgressBar.progressProperty().unbind();
        lotteryUpdateLabel.textProperty().unbind();
        lotteryUpdateLabel.setText("All files updated successfully");
    }

    public void hideProgressBarAndLabeVbox() {

        updateProgressBar.setVisible(false);
        progressAndLabelVbox.setVisible(false);
        lotteryUpdateLabel.setVisible(false);

    }

    public void makeVboxVisible() {
        updateProgressBar.setVisible(true);
        progressAndLabelVbox.setVisible(true);
        lotteryUpdateLabel.setVisible(true);
    }

    public String getDefaultGameName(){

        return "CA: Fantasy Five";
    }
}