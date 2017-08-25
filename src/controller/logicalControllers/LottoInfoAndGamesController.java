package controller.logicalControllers;

import controller.MainController;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.DataDownLoader;
import model.LotteryUrlPaths;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: The LottoInfoGamesController is responsible for handling input accordingly that is entered via the lotto info
 * and games view. This class will process information for a lottery game in a given state. It will aid in
 * retrieving all relevant information for the game the user wants to play. This controller will interact with the lotto
 * game update launcher controller that will ensure all data in the application is up to date.
 */
public class LottoInfoAndGamesController {


    private List<String> itemList = new ArrayList<String>();
    private MainController mainController;
    private  boolean isGamePaneOpen = false;

    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane game_pane;
    @FXML
    private Label lotteryUpdateLabel;

    @FXML
    private ProgressBar updateProgressBar;
    @FXML
    private VBox progressAndLabelVbox;

    public void init(MainController mainController) {

        this.mainController = mainController;

        loadMenuItems();
    }

    public void makeGamePanelAppear(ActionEvent e) {


        if(e.getSource() instanceof Button){

            Button button = (Button)e.getSource();

            if(button.getId().equals("btn_game")){
                game_pane.setVisible(true);
                isGamePaneOpen = true;
            }
        }
    }

    /**
     * Once a game is selected from the drop down menu this event will trigger the retrieval of correct lotto information
     * for the selected game
     * @param event
     */
    @FXML
    private void getAppropriateGameData(ActionEvent event) {

        // Get the event source and cast to appropriate object type
        MenuItem item = (MenuItem)event.getSource();
        mainController.lottoDashboardController.setGameLabels(item.getText());

        boolean containsGame = (itemList.contains(item.getText()));
        if (containsGame) {

            if(item.getText().equalsIgnoreCase("update database")){
                 downloadFilesFromInternet();
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
        ObservableList<Menu> menus = menuBar.getMenus();
        for(Menu menu : menus){
            for(MenuItem item : menu.getItems()){
                itemList.add(item.getText());
            }
        }
    }


    /**
     * This method will return a boolean indicating the game info panel is open
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
}
