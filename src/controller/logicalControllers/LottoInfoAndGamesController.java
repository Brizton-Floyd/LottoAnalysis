package controller.logicalControllers;

import controller.MainController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

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
     * for the selected gane
     * @param event
     */
    @FXML
    private void getAppropriateGameData(ActionEvent event) {

        // Get the event source and cast to appropriate object type
        MenuItem item = (MenuItem)event.getSource();

        boolean containsGame = (itemList.contains(item.getText()));
        if (containsGame) {

        }

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
}
