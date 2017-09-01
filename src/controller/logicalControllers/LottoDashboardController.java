package controller.logicalControllers;


import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Drawing;
import model.LotteryGame;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LottoDashboardController implements Initializable {

    private MainController mainController;

    @FXML
    private AnchorPane pane, infoPane, predictedNumbersPane;

    @FXML
    private JFXButton learnMoreButton, btn_close;

    @FXML
    private Label lottoDashboard, predictedNumbersLabel;

    @FXML
    private TableView<Drawing> drawNumberTable;

    public void init(MainController mainController) {

        this.mainController = mainController;

        setTextStyleForAllLabels();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        learnMoreButton.setOnAction(event -> {

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

    public void setGameLabels(String gameName) {

        if (!gameName.equalsIgnoreCase("update database")) {

            if (gameName.equals("Super Lotto Plus")) {

                this.lottoDashboard.setText(gameName + " Dashboard");

            } else {

                this.lottoDashboard.setText(gameName + " Lotto Dashboard");

            }

            this.predictedNumbersLabel.setText("Historical Draw Table For: " + gameName.split(":")[1]);
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

    // This method will set up the table view with model data
    public void setUpTableView(LotteryGame lotteryGame) {

        drawNumberTable.getItems().clear();
        drawNumberTable.getColumns().clear();

        drawNumberTable.setVisible(true);
        predictedNumbersLabel.setVisible(true);

        ObservableList<Drawing> data = lotteryGame.getDrawingData();
        List<String> tableColumnName = new LinkedList<>();
        tableColumnName.add("Draw #");
        tableColumnName.add("Draw Date");

        TableColumn[] tableColumns = null;
        Map<SimpleStringProperty, String> newData = null;


        tableColumns = new TableColumn[4 + Drawing.drawSize];

        for (int i = 0; i < Drawing.drawSize; i++) {

            tableColumnName.add("Pos " + (i + 1));
        }

        tableColumnName.add("Sum");
        tableColumnName.add("E / O");

        double width = predictedNumbersPane.getWidth();
        double columnWidths = width / tableColumnName.size();

        for (int i = 0; i < tableColumnName.size(); i++) {
            tableColumns[i] = new TableColumn<>(tableColumnName.get(i));
            tableColumns[i].setSortable(false);
//            tableColumns[i].setMinWidth(columnWidths);
//            tableColumns[i].setMaxWidth(columnWidths);
            if(i == tableColumnName.size() - 1){
                double size = tableColumns[i].getWidth();
                tableColumns[i].setMaxWidth(size - 12);
                tableColumns[i].setMinWidth(size - 12);

            }

            drawNumberTable.getColumns().add(tableColumns[i]);
        }


        setUpCellValueFactories(tableColumns, Drawing.drawSize);

        drawNumberTable.scrollTo(lotteryGame.getDrawingData().size() - 1);
        drawNumberTable.setItems(lotteryGame.getDrawingData());
    }

    private void setUpCellValueFactories(TableColumn[] tableColumns, int drawSize) {

        tableColumns[0].setCellValueFactory(new PropertyValueFactory("drawNumber"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory("drawDate"));

        switch (drawSize){
            case 3:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;
            case 4:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("posFour"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[7].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;
            case 5:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("posFour"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("posFive"));
                tableColumns[7].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[8].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;

            case 6:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("posFour"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("posFive"));
                tableColumns[7].setCellValueFactory(new PropertyValueFactory("bonusNumber"));
                tableColumns[8].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[9].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;

        }
    }
}

