package controller.logicalControllers;


import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class LottoDashboardController {

    private MainController mainController;

    @FXML
    private AnchorPane pane, infoPane;

    @FXML
    private JFXButton learnMoreButton,btn_close;

    public void init(MainController mainController) {

        setTextStyleForAllLabels();
        learnMoreButton.setOnAction( event ->  {

            infoPane.setVisible(true);
            btn_close.visibleProperty().setValue(true);
        });

        btn_close.setOnAction(event -> {
            infoPane.setVisible(false);
            btn_close.setVisible(false);
        });
    }

    public void showPane() {
        pane.setVisible(true);
    }
    public void hidePane() {
        pane.setVisible(false);
    }
    private void setTextStyleForAllLabels() {

        List<Node> children = pane.getChildren();
        for (Node node : children) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
            }
        }
    }

}

