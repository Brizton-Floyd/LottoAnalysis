package controller.logicalControllers;


import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class LottoDashboardController {

    private MainController mainController;

    @FXML
    private AnchorPane childPane;

    public void init(MainController mainController) {

        this.mainController = mainController;

    }

}