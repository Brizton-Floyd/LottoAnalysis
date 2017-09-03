package controller.logicalControllers;

import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Drawing;
import model.FiveDigitLotteryGame;
import model.LotteryGame;
import utils.NumberPatternAnalyzer;

import java.net.URL;
import java.util.*;

public class LottoDashboardController implements Initializable {

    private MainController mainController;
    private LotteryGame lotteryGame;

    @FXML
    private AnchorPane pane, infoPane, infoPane1, predictedNumbersPane;

    @FXML
    private JFXButton btn_close;

    @FXML
    private Label lottoDashboard, predictedNumbersLabel;

    @FXML
    private TableView<Drawing> drawNumberTable;

    @FXML
    private BarChart barChart;

    @FXML
    private ChoiceBox choiceBox;

    public void init(MainController mainController) {

        this.mainController = mainController;

        setTextStyleForAllLabels();
        loadDefaultGameForView();
        loadChoicesIntoChoiceBox();

    }

    private void loadChoicesIntoChoiceBox() {

        // Retrive how many positions are currently in the lotto game and then add choices to choicebox
        int count = 0;
        List<String> choiceBoxItems = new LinkedList<>();


        List<TableColumn> columns = new ArrayList<>(drawNumberTable.getColumns());
        for (TableColumn column : columns) {
            String columnName = column.getText();
            if (columnName.contains("Pos") || columnName.contains("Bonus")) {
                if (count < 5)
                    choiceBoxItems.add("Position " + (++count));
                else
                    choiceBoxItems.add("Bonus Position");
            }
        }

        // Create two dimensional array and load all numbers into correct positions
        int[][] positionalNumbers = new int[count][drawNumberTable.getItems().size()];
        loadUpPositionalNumbers(positionalNumbers, lotteryGame.getDrawingData());

        int[][] deltaNumberForLastDraw = NumberPatternAnalyzer.findDeltaNumbers(positionalNumbers);

        choiceBox.setItems(FXCollections.observableArrayList(choiceBoxItems));
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            int number = (int) newValue;
            switch (number) {
                case 0:
                    NumberPatternAnalyzer.findDeltaNumberGroupLikelyToHit(deltaNumberForLastDraw[0]);
                    break;
                case 1:
                    NumberPatternAnalyzer.findDeltaNumberGroupLikelyToHit(deltaNumberForLastDraw[1]);
                    break;
                case 2:
                    NumberPatternAnalyzer.findDeltaNumberGroupLikelyToHit(deltaNumberForLastDraw[2]);
                    break;
                case 3:
                    NumberPatternAnalyzer.findDeltaNumberGroupLikelyToHit(deltaNumberForLastDraw[3]);
                    break;
                case 4:
                    NumberPatternAnalyzer.findDeltaNumberGroupLikelyToHit(deltaNumberForLastDraw[4]);
                    break;
                case 5:
                    System.out.print("Bonus Number");
                    break;
            }
        });

    }


    private void loadUpPositionalNumbers(int[][] positionalNumbers, ObservableList<Drawing> drawingData) {

        for (int i = 0; i < drawingData.size(); i++) {

            if (positionalNumbers.length == 3) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
            } else if (positionalNumbers.length == 4) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
            } else if (positionalNumbers.length == 5) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
                positionalNumbers[4][i] = Integer.parseInt(drawingData.get(i).posFiveProperty().get());
            } else if (positionalNumbers.length == 6) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
                positionalNumbers[4][i] = Integer.parseInt(drawingData.get(i).posFiveProperty().get());
                positionalNumbers[5][i] = Integer.parseInt(drawingData.get(i).bonusNumberProperty().get());

            }


        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btn_close.setOnAction(event -> {

            predictedNumbersPane.setVisible(true);
            infoPane.setVisible(false);
            btn_close.setVisible(false);

        });

        // this is a hack to make the tableview stop scrolling horizontally
        drawNumberTable.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
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

    public void setUpTableView(LotteryGame lotteryGame) {

        choiceBox.getItems().clear();
        drawNumberTable.getItems().clear();
        drawNumberTable.getColumns().clear();
        drawNumberTable.setVisible(true);
        predictedNumbersLabel.setVisible(true);

        this.lotteryGame = lotteryGame;

        ObservableList<Drawing> data = lotteryGame.getDrawingData();
        List<String> tableColumnName = new LinkedList<>();
        tableColumnName.add("Draw #");
        tableColumnName.add("Draw Date");

        TableColumn[] tableColumns = null;
        Map<SimpleStringProperty, String> newData = null;


        tableColumns = new TableColumn[4 + Drawing.drawSize];

        for (int i = 0; i < Drawing.drawSize; i++) {

            if (Drawing.drawSize > 5 && i == Drawing.drawSize - 1)
                tableColumnName.add("Bonus ");

            else
                tableColumnName.add("Pos " + (i + 1));

        }

        tableColumnName.add("Sum");
        tableColumnName.add("E/O");

        drawNumberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (int i = 0; i < tableColumnName.size(); i++) {
            tableColumns[i] = new TableColumn<>(tableColumnName.get(i));
            if (i == 1)
                tableColumns[i].setMaxWidth(1f * Integer.MAX_VALUE * 70);
            else
                tableColumns[i].setMaxWidth(1f * Integer.MAX_VALUE * 30);

            if (tableColumnName.size() > 9 && i == 7) {
                tableColumns[i].setCellFactory(new Callback<TableColumn<Drawing, String>, TableCell<Drawing, String>>() {

                    @Override
                    public TableCell<Drawing, String> call(TableColumn<Drawing, String> param) {
                        return new TableCell<Drawing, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!isEmpty()) {
                                    this.setTextFill(Color.valueOf("#EFA747"));
                                    // Get fancy and change color based on data
                                    if (item.contains("@"))
                                        this.setTextFill(Color.BLUEVIOLET);
                                    setText(item);
                                }
                            }
                        };
                    }
                });
            } else {
                tableColumns[i].setCellFactory(new Callback<TableColumn<Drawing, String>, TableCell<Drawing, String>>() {

                    @Override
                    public TableCell<Drawing, String> call(TableColumn<Drawing, String> param) {
                        return new TableCell<Drawing, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!isEmpty()) {
                                    this.setTextFill(Color.BEIGE);
                                    // Get fancy and change color based on data
                                    if (item.contains("@"))
                                        this.setTextFill(Color.BLUEVIOLET);
                                    setText(item);
                                }
                            }
                        };
                    }
                });
            }
            tableColumns[i].setSortable(false);
            drawNumberTable.getColumns().add(tableColumns[i]);
        }

        setUpCellValueFactories(tableColumns, Drawing.drawSize);
        drawNumberTable.scrollTo(lotteryGame.getDrawingData().size() - 1);
        drawNumberTable.setItems(lotteryGame.getDrawingData());
        loadChoicesIntoChoiceBox();
    }

    private void setUpCellValueFactories(TableColumn[] tableColumns, int drawSize) {

        tableColumns[0].setCellValueFactory(new PropertyValueFactory("drawNumber"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory("drawDate"));

        switch (drawSize) {
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
    private void setTextStyleForAllLabels() {

        List<Node> children = pane.getChildren();
        for (Node node : children) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
            }
        }
    }
    private void loadDefaultGameForView() {

        choiceBox.getItems().clear();

        String gameName = mainController.lottoInfoAndGamesController.getDefaultGameName();
        this.lotteryGame = new FiveDigitLotteryGame(gameName);
        predictedNumbersLabel.setText("Historical draw table for: " + lotteryGame.loadGameData().getGameName());
        lottoDashboard.setText(gameName + "Lotto Dashboard");
        setUpTableView(lotteryGame.loadGameData());
    }



}

