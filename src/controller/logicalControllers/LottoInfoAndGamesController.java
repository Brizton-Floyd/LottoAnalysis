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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by briztonfloyd on 8/19/17.
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

    public void controlAppearanceOfGamePanel(ActionEvent e) {

        if(e.getSource() instanceof Button){

            Button button = (Button)e.getSource();

            if(button.getId().equals("btn_game")){
                game_pane.setVisible(true);
                isGamePaneOpen = true;
            }
        }
    }

    @FXML
    private void initMenuItems(ActionEvent event) {

        // Get the even source and cast to appropriate object type
        MenuItem item = (MenuItem)event.getSource();

        boolean containsGame = (itemList.contains(item.getText()));
        if (containsGame) {

        }

    }

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
