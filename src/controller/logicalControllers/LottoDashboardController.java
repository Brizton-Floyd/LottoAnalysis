package controller.logicalControllers;

import com.jfoenix.controls.JFXButton;
import controller.MainController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import model.Drawing;
import model.FiveDigitLotteryGame;
import model.GameInstructions;
import model.LotteryGame;
import model.chartImplementations.BarChartExt;
import utils.LottoBetSlipAnalyzer;
import utils.NumberPatternAnalyzer;

import java.net.URL;
import java.util.*;

public class LottoDashboardController implements Initializable {

    private MainController mainController;
    private LotteryGame lotteryGame;
    private static int universalCount = 0;
    int[][] positionalNumbers = null;

    @FXML
    private AnchorPane pane, infoPane, infoPane1, predictedNumbersPane;

    @FXML
    private JFXButton btn_close;

    @FXML
    private Label lottoDashboard, predictedNumbersLabel, dashBoardHelpLabel;

    @FXML
    private TableView<Drawing> drawNumberTable;

    @FXML
    private BarChart barChart;

    @FXML
    private HBox hBox;
    @FXML
    private VBox vBox;

    @FXML
    FontAwesomeIconView helpIconLottoDashboard;

    private ChoiceBox choiceBox;
    private BarChartExt<Number, Number> bc;

    public void init(MainController mainController) {

        choiceBox = new ChoiceBox();
        choiceBox.setStyle("-fx-focus-color: transparent;");
        bc = new BarChartExt<>(new CategoryAxis(), new NumberAxis());
        hBox.getChildren().addAll(bc, choiceBox);
        vBox.setPadding(new Insets(20, 0,0,0));
        hBox.setPadding(new Insets(0,0,-18,0));
        this.mainController = mainController;

        styleChart();
        setTextStyleForAllLabels();
        loadDefaultGameForView();
        loadChoicesIntoChoiceBox();


    }

    public static int getUniversalCount() {
        return universalCount;
    }

    private static void setCount(int val){

        universalCount = val;
    }
    private void loadChoicesIntoChoiceBox() {

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

        setCount(count);
        positionalNumbers = new int[count][drawNumberTable.getItems().size()];
        loadUpPositionalNumbers(positionalNumbers, lotteryGame.getDrawingData());

        int[][] deltaNumberForLastDraw = NumberPatternAnalyzer.findDeltaNumbers(positionalNumbers);

        choiceBox.setItems(FXCollections.observableArrayList(choiceBoxItems));
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            final Object[] data;
            int number = (int) newValue;
            Map<Integer[], Object[]> chartData = null;
            Map<Integer, Integer> chartDataTwo = null;
            List<Object> rowInformation = null;

            switch (number) {
                case 0:
                    LottoBetSlipAnalyzer analyzerPosOne = new LottoBetSlipAnalyzer(lotteryGame, 2);
                    rowInformation = analyzerPosOne.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);
                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[0]);
                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[0], positionalNumbers.length,
                            (int) data[1], (int) data[2], lotteryGame);

                    break;
                case 1:
                    LottoBetSlipAnalyzer analyzerPosTwo = new LottoBetSlipAnalyzer(lotteryGame, 3);
                    rowInformation = analyzerPosTwo.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);
                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[1]);
                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[1], positionalNumbers.length,
                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 2:
                    LottoBetSlipAnalyzer analyzerPosThree = new LottoBetSlipAnalyzer(lotteryGame, 4);
                    rowInformation = analyzerPosThree.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);
                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[2]);
                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[2], positionalNumbers.length,
                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 3:
                    LottoBetSlipAnalyzer analyzerPosFour = new LottoBetSlipAnalyzer(lotteryGame, 5);
                    rowInformation = analyzerPosFour.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);
                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[3]);
                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[3], positionalNumbers.length,
                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 4:
                    LottoBetSlipAnalyzer analyzerPosFive = new LottoBetSlipAnalyzer(lotteryGame, 6);
                    rowInformation = analyzerPosFive.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);
                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[4]);
                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[4], positionalNumbers.length,
                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 5:
                    LottoBetSlipAnalyzer analyzerBonus = new LottoBetSlipAnalyzer(lotteryGame, 7);
                    System.out.print("Bonus Number");
                    break;
            }

            // Plot data on bar chart
            if (number < 5) {
                int size = vBox.getChildren().size();
                if(size > 1)
                    vBox.getChildren().remove(1);
                setUpChart(number + 1, chartDataTwo);
            }

        });

    }

    private Map<Integer, Integer> extractInformationForBarChart(List<Object> rowInformation) {

        List<Integer> columnHitCounter = new LinkedList<>();

        Map<Integer, Object[]> chartData = (Map<Integer, Object[]>) rowInformation.get(1);

        for (Map.Entry<Integer, Object[]> dd : chartData.entrySet()) {

            Object[] value = dd.getValue();
            Map<Integer, Integer[]> numberAndHitCounter = (Map<Integer, Integer[]>) value[0];
            for (Map.Entry<Integer, Integer[]> ddd : numberAndHitCounter.entrySet()) {

                Integer[] data = ddd.getValue();
                columnHitCounter.add(ddd.getKey());
                columnHitCounter.add(data[0]);
            }

        }

        Map<Integer, Integer> sortedNums = new TreeMap<>();

        for (int num = 0; num < columnHitCounter.size(); num++) {

            if (num % 2 == 0 && num < columnHitCounter.size() - 1)
                sortedNums.put(columnHitCounter.get(num), columnHitCounter.get(num + 1));

        }

        return sortedNums;
    }


    /**
     * Method will be used to set up the bar chart with the appropriate data
     *
     * @param position
     */
    private void setUpChart(int position, Map<Integer, Integer> chartData) {

        bc.getData().clear();

        bc.setTitle(String.format("Best Group For Position %1s", position));
        //bc.getXAxis().setLabel("Predicted Numbers");
        bc.getYAxis().setLabel("Hits in Pos " + position);
        bc.legendVisibleProperty().setValue(false);
        bc.setAnimated(false);
        Label label = new Label();

        if (chartData != null) {

            XYChart.Series[] series = new XYChart.Series[1];
            StringBuilder builder = new StringBuilder();
            //builder.append("\t\t\t   ");
            series[0] = new XYChart.Series();

            int count = 0;

            for (Map.Entry<Integer, Integer> data : chartData.entrySet()) {

                series[0].getData().add(new XYChart.Data<>(data.getKey()  + "", data.getValue()));

                if(count == 0)
                    builder.append(String.format("%1$28s",data.getKey()));
                else {
                    if(chartData.entrySet().size() < 10) {
                        if (Integer.toString(data.getKey()).length() > 1)
                            builder.append(String.format("%1$10s", data.getKey()));
                        else
                            builder.append(String.format("%1$10s", data.getKey()));
                    }else{
                        if (Integer.toString(data.getKey()).length() > 1)
                            builder.append(String.format("%1$9s", data.getKey()));
                        else
                            builder.append(String.format("%1$10s", data.getKey()));
                    }
                }
                count++;


            }

            //int index = builder.lastIndexOf(",");
            //builder.setCharAt(index, ']');
            //series[0].setName(builder.toString());
            bc.getData().add(series[0]);

            label.setText(builder.toString());
            vBox.getChildren().add(label);


        }
    }

    private void styleChart() {

        bc.getStylesheets().addAll("/styles/bar_chart.css");

    }

    public static void loadPositionalNumbers(int[][] positions, ObservableList<Drawing> drawings){
        loadUpPositionalNumbers(positions, drawings);
    }
    /**
     * Loader that injects values into perspective draw positions from a Lotterygame object.
     *
     * @param positionalNumbers
     * @param drawingData
     */
    private static void loadUpPositionalNumbers(int[][] positionalNumbers, ObservableList<Drawing> drawingData) {

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

        helpIconLottoDashboard.setOnMouseEntered(e -> {

            dashBoardHelpLabel.setText(GameInstructions.LOTTO_DASHBOARD_INSTRUCTION_FIRST_PANE);
            dashBoardHelpLabel.setVisible(true);
            helpIconLottoDashboard.fillProperty().setValue(Paint.valueOf("#EFA747"));
            hBox.setVisible(false);

        });

        helpIconLottoDashboard.setOnMouseExited(e -> {

            dashBoardHelpLabel.setVisible(false);
            helpIconLottoDashboard.fillProperty().setValue(Paint.valueOf("#DAC6AC"));
            hBox.setVisible(true);
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

    /**
     * Method will take in any lottery game and and set up the number draw tableview. Method also calls the necessary methods
     * to aid in populating the choicebox in the lotto dashboard section.
     *
     * @param lotteryGame
     */
    public void setUpTableView(LotteryGame lotteryGame) {

        positionalNumbers = null;
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
        String name = lotteryGame.loadGameData().getGameName();
        String newName = "";
        for (int i = 0; i < name.length(); i++) {

            if (Character.isUpperCase(name.charAt(i)) && i == 0)

                newName = Character.toString(name.charAt(i));

            else if (Character.isUpperCase(name.charAt(i)) && i > 0) {
                newName += " " + Character.toString(name.charAt(i));
            } else {
                newName += Character.toString(name.charAt(i));
            }

        }

        predictedNumbersLabel.setText("Historical Draw Table For: " + newName);
        lottoDashboard.setText(gameName + "Lotto Dashboard");
        setUpTableView(lotteryGame.loadGameData());
    }

    public LotteryGame getLotteryGame() {
        return lotteryGame;
    }
}

