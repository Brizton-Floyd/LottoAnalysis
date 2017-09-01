package controller.logicalControllers;


import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LottoDashboardController implements Initializable {

    private MainController mainController;

    @FXML
    private AnchorPane pane, infoPane,predictedNumbersPane;

    @FXML
    private JFXButton learnMoreButton,btn_close;

    @FXML
    private Label lottoDashboard, predictedNumbersLabel;

    public void init(MainController mainController) {

        this.mainController = mainController;

        setTextStyleForAllLabels();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        learnMoreButton.setOnAction( event ->  {

            predictedNumbersPane.setVisible(false);
            learnMoreButton.setDisable(true);
            infoPane.setVisible(true);
            btn_close.visibleProperty().setValue(true);

        });

        btn_close.setOnAction(event -> {


            predictedNumbersPane.setVisible(true);
            learnMoreButton.setDisable(false);
            infoPane.setVisible(false);
            btn_close.setVisible(false);

        });
    }

    public void setGameLabels(String gameName){

        if(!gameName.equalsIgnoreCase("update database")){

            if(gameName.equals("Super Lotto Plus")){

                this.lottoDashboard.setText(gameName + " Dashboard");

            }else{

                this.lottoDashboard.setText(gameName + " Lotto Dashboard");

            }

            this.predictedNumbersLabel.setText("Predicted Numbers For" + gameName.split(":")[1]);
        }

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

