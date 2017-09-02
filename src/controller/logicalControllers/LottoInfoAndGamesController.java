package controller.logicalControllers;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.*;
import model.DataFiles.LotteryRepository;

import java.sql.Connection;
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
    private LotteryRepository repository;
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

    private void addGamesToDatabase() {

        repository = new LotteryRepository(mainController);
        if (repository.isDbConnected()) {

            ObservableList<Menu> menus = menuBar.getMenus();

            // Establish Connection
            Connection conn = repository.getConnection();

            for (int i = 0; i < menus.size(); i++) {

                for (MenuItem item : menus.get(i).getItems()) {
                    if (item instanceof SeparatorMenuItem || item.getText().equalsIgnoreCase("update database")) {
                        continue;
                    }
                    repository.update(item, item.getText());
                    //repository.insert(item);
                }
            }

        }

        repository.closeConnection();

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

            if (item.getText().equalsIgnoreCase("update database")) {
                downloadFilesFromInternet();
            }
            else if(gameName.contains("Fantasy")){
                game = new FiveDigitLotteryGame(gameName);
                this.lotteryGame = game.loadGameData();
                mainController.lottoDashboardController.setUpTableView(lotteryGame);
            }
            else if(gameName.contains("Powerball")){

                game = new SixDigitLotteryGame(gameName);
                this.lotteryGame = game.loadGameData();
                mainController.lottoDashboardController.setUpTableView(lotteryGame);
            }
            else if(gameName.contains("Mega")){
                game = new SixDigitLotteryGame(gameName);
                this.lotteryGame = game.loadGameData();
                mainController.lottoDashboardController.setUpTableView(lotteryGame);
            }
            else if(gameName.contains("Pick 3")){
                game = new PickThreeLotteryGame(gameName);
                this.lotteryGame = game.loadGameData();
                mainController.lottoDashboardController.setUpTableView(lotteryGame);
            }
            else if(gameName.contains("Pick 4")){
                game = new PickFourLotteryGame(gameName);
                this.lotteryGame = game.loadGameData();
                mainController.lottoDashboardController.setUpTableView(lotteryGame);
            }
            else if(gameName.contains("Super")){
                game = new SixDigitLotteryGame(gameName);
                this.lotteryGame = game.loadGameData();
                mainController.lottoDashboardController.setUpTableView(lotteryGame);
            }
        }
    }

    /**
     * Method will be responsible for downloading historical data from the internet for lottery games on seperate
     * thread
     */
    private void downloadFilesFromInternet() {

        LotteryUrlPaths paths = new LotteryUrlPaths();

        Task<Void> task = new DataDownLoader(paths.getPathFiles(), mainController);
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

        repository = new LotteryRepository(mainController);

        List<String> menus = repository.selectAllGames();
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

                if (!(item instanceof SeparatorMenuItem) && !item.getText().equalsIgnoreCase("update database")) {
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
