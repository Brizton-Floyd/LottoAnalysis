package controller.logicalControllers;


import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

public class LottoDashboardController {

    private MainController mainController;

    @FXML
    private AnchorPane parentPane, childPane;

    public void init(MainController mainController) {

        this.mainController = mainController;

        this.parentPane.setBackground(Background.EMPTY);
    }

}