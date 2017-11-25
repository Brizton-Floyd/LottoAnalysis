package com.lottoanalysis.lottoinfoandgames;

import com.lottoanalysis.MainController;
import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.common.LotteryGameDaoConstants;
import com.lottoanalysis.lottoinfoandgames.data.DataDownLoader;
import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDao;
import com.lottoanalysis.utilities.FileTweaker;
import com.lottoanalysis.utilities.OnlineFileUtility;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDaoImpl;

import java.util.LinkedList;
import java.util.List;


/**
 * Class: The LottoInfoGamesController is responsible for handling input accordingly that is entered via the lotto info
 * and games view. This class will process information for a lottery game in a given state. It will aid in
 * retrieving all relevant information for the game the user wants to play. This controller will interact with the lotto
 * game update launcher controller that will ensure all data in the application is up to date.
 */
public class LottoInfoAndGamesController {


    private List<String> itemList = new LinkedList<>();
    private MainController mainController;
    private boolean isGamePaneOpen = false;
    private LotteryGameManager lotteryGameManager;
    private LotteryGame lotteryGame;


    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane game_pane;
    @FXML
    public Label lotteryUpdateLabel;

    @FXML
    private ProgressBar updateProgressBar;
    @FXML
    private VBox progressAndLabelVbox;

    public void init(MainController mainController) {

        this.mainController = mainController;

        //addGamesToDatabase();
        loadMenuItems();
    }


    public LotteryGame getLotteryGame() {
        return lotteryGame;
    }

    public void makeGamePanelAppear(ActionEvent e) {

        if (e.getSource() instanceof Button) {

            Button button = (Button) e.getSource();

            if (button.getId().equals("btn_game")) {
                game_pane.setVisible(true);
                isGamePaneOpen = true;
            }
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

        LotteryGame game = null;

        // Get the event source and cast to appropriate object type
        MenuItem item = (MenuItem) event.getSource();
        mainController.lottoDashboardController.setGameLabels(item.getText());

        String gameName = item.getText();
        boolean containsGame = (itemList.contains(gameName));
        if (containsGame) {

            // get lottery game manager instance
            lotteryGameManager = getLotteryGameManagerInstance();

            if (item.getText().equalsIgnoreCase(LotteryGameDaoConstants.UPDATE_DB)) {
                downloadFilesFromInternet();
            }
            else if(gameName.contains(LotteryGameConstants.FANTASY_FIVE_GAME_NAME)){

                game = lotteryGameManager.loadLotteryData( gameName, LotteryGameConstants.FANTASY_FIVE_DB_NAME,
                                                            LotteryGameConstants.FIVE_POSITIONS);

                mainController.lottoDashboardController.setUpTableView( game );
                mainController.lottoDashboardController.loadChoicesIntoChoiceBox();

            }
            else if(gameName.contains(LotteryGameConstants.POWERBALL_GAME_NAME)){

                game = lotteryGameManager.loadLotteryData( gameName,LotteryGameConstants.POWERBALL_DB_NAME,
                        LotteryGameConstants.SIX_POSITIONS);

                mainController.lottoDashboardController.setUpTableView( game );
                mainController.lottoDashboardController.loadChoicesIntoChoiceBox();

            }
            else if(gameName.contains(LotteryGameConstants.MEGA_MILLIONS_GAME_NAME)){

                game = lotteryGameManager.loadLotteryData( gameName, LotteryGameConstants.MEGA_MILLIONS_DB_NAME,
                        LotteryGameConstants.SIX_POSITIONS);

                mainController.lottoDashboardController.setUpTableView( game );
                mainController.lottoDashboardController.loadChoicesIntoChoiceBox();

            }
            else if(gameName.contains(LotteryGameConstants.PICK3_GAME_NAME)){

                game = lotteryGameManager.loadLotteryData( gameName, LotteryGameConstants.PICK3_DB_NAME,
                        LotteryGameConstants.THREE_POSITIONS);

                mainController.lottoDashboardController.setUpTableView( game );
                mainController.lottoDashboardController.loadChoicesIntoChoiceBox();

            }
            else if(gameName.contains(LotteryGameConstants.PICK4_GAME_NAME)){

                game = lotteryGameManager.loadLotteryData( gameName, LotteryGameConstants.PICK4_DB_NAME,
                        LotteryGameConstants.FOUR_POSITIONS);

                mainController.lottoDashboardController.setUpTableView( game );
                mainController.lottoDashboardController.loadChoicesIntoChoiceBox();

            }
            else if(gameName.contains(LotteryGameConstants.SUPER_LOTTO_PLUS_GAME_NAME)){

                game = lotteryGameManager.loadLotteryData( gameName, LotteryGameConstants.SUPER_LOTTO_DB_NAME,
                        LotteryGameConstants.SIX_POSITIONS);

                mainController.lottoDashboardController.setUpTableView( game );
                mainController.lottoDashboardController.loadChoicesIntoChoiceBox();

            }
        }
    }

    /**
     * Method will be responsible for downloading historical data from the internet for lottery games on seperate
     * thread
     */
    private void downloadFilesFromInternet() {

        Task<Void> task = new DataDownLoader(OnlineFileUtility.getUrlPaths(), mainController);
        updateProgressBar.progressProperty().bind(task.progressProperty());
        lotteryUpdateLabel.textProperty().bind(task.messageProperty());
        lotteryUpdateLabel.setVisible(true);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    /**
     * Method will be responsible for importing games into drop down list based on state user chooses to play in
     */
    private void loadMenuItems() {

        List<String> menus = getLotteryGameManagerInstance().getAllGames();

        for (String menu : menus) {
            itemList.add(menu);
        }

        itemList.add("Update Database");
        setMenuItemText(itemList);

    }

    /**
     * Return a lotterygamemanager instance
     * @return
     */
    private LotteryGameManager getLotteryGameManagerInstance(){

        return new LotteryGameManagerImpl();
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

        progressAndLabelVbox.setVisible(false);
        lotteryUpdateLabel.setVisible(false);

    }

    public void makeVboxVisible() {
        progressAndLabelVbox.setVisible(true);
        lotteryUpdateLabel.setVisible(true);
    }

    public String getDefaultGameName(){

        return "CA: Fantasy Five";
    }
}
