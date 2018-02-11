package com.lottoanalysis.chartanalysis;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.companionnumbers.companionnumberhelpers.CompanionNumberHelper;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.utilities.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;

@SuppressWarnings("unchecked")
public class GroupChartController {

    private LotteryGame lotteryGame;
    private int[][] drawPositionalNumbers;
    private Map<Integer, String> data;
    private ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

    private static int globalDrawPosition,rowIndex;

    @FXML
    private VBox groupRadioButtonVbox;

    @FXML
    private HBox drawPositionHbox, radioBtnAndChartHbox, headerHbox;

    @FXML
    private MenuButton groupSizeMenuButton;

    @FXML
    private Label lblGame, lblAnalyzedPosition,groupHitOutlookLabel,patternOutlookLabel;

    @FXML
    private GridPane groupGridPane;

    @FXML
    private TableView groupInfoTable, patternTable;

    @FXML
    public void initialize() {
        groupInfoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        patternTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public GroupChartController() {
        data = new HashMap<>();
        data.put(0, "One");
        data.put(1, "Two");
        data.put(2, "Three");
        data.put(3, "Four");
        data.put(4, "Five");
    }

    /**
     * Clear all undynamic content
     */
    private void clearUnDynamicContent() {
        groupRadioButtonVbox.getChildren().clear();
        drawPositionHbox.getChildren().clear();
        drawPositionHboxSet();

        Node node = radioBtnAndChartHbox.getChildren().get(0);
        radioBtnAndChartHbox.getChildren().setAll(node);
    }

    private void clearVbox() {
        //((VBox) radioBtnAndChartHbox.getChildren().get(0)).getChildren().clear();

        radioBtnAndChartHbox.getChildren().removeIf(obj -> obj instanceof VBox);
    }

    public void initFields(LotteryGame game, int[][] drawPositionalNumbers) {

        setLotteryGame(game);
        setDrawPositionalNumbers(drawPositionalNumbers);
        PatternFinder.analyze(drawPositionalNumbers);

    }

    public void startSceneLayoutSequence() {

        clearUnDynamicContent();
        insertPositionRadioButtonsIntoHbox();
        placeActionsOnMenuButtonItems();
    }

    private void placeActionsOnMenuButtonItems() {

        ObservableList<MenuItem> items = groupSizeMenuButton.getItems();


        if (lotteryGame.getGameName().equalsIgnoreCase(LotteryGameConstants.POWERBALL) ||
                lotteryGame.getGameName().equalsIgnoreCase(LotteryGameConstants.MEGA_MILLIONS)) {
            items.remove(0);
        }

        items.forEach(menuItem -> {

            menuItem.setOnAction(e -> {

                //groupSizeMenuButton.setText(menuItem.getText());
                retrieveDataForChartViewing(globalDrawPosition, Integer.parseInt(menuItem.getText()));
            });
        });


    }

    private void insertPositionRadioButtonsIntoHbox() {
        setGlobalDrawPosition(0);
        setPositionBeingAnalyzedLabel();
        retrieveDataForChartViewing(0, 10);
    }

    private void drawPositionHboxSet() {
        // clearUnDynamicContent();
        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < drawPositionalNumbers.length; i++) {

            RadioButton button = new RadioButton("Position " + data.get(i));
            final int position = i;

            button.setOnAction(e -> {
                ObservableList<Toggle> toggles = group.getToggles();
                for(Toggle t : toggles){
                    RadioButton b = (RadioButton)t;
                    if(b.getText().equalsIgnoreCase(button.getText()))
                        b.setSelected(true);
                }

                patternTable.getItems().clear();
                patternTable.getColumns().clear();

                setGlobalDrawPosition(position);
                setPositionBeingAnalyzedLabel();
                retrieveDataForChartViewing(position, 10);
            });
            button.setStyle("-fx-text-fill: #dac6ac;");

            drawPositionHbox.getChildren().add(button);
            group.getToggles().add(button);
        }

        ((RadioButton)group.getToggles().get(0)).setSelected(true);
    }

    private void setPositionBeingAnalyzedLabel() {
        if( globalDrawPosition == 2)
            headerHbox.setSpacing(380);
        else
            headerHbox.setSpacing(402);

        lblAnalyzedPosition.setText("Analyzing Position " + data.get(globalDrawPosition));
    }

    /**
     * @param drawPosition
     */
    private void retrieveDataForChartViewing(int drawPosition, int drawSize) {

       //clearUnDynamicContent();

        patternTable.getItems().clear();
        patternTable.getColumns().clear();
        groupInfoTable.getItems().clear();
        groupInfoTable.getColumns().clear();

        clearVbox();
        lblGame.setText("Group Chart Analysis: " + lotteryGame.getGameName());
        groupHitOutlookLabel.setText("Group Hit Outlook Position " + data.get(globalDrawPosition));

        NextProbableGroupFinder.analyze(drawPositionalNumbers);
       // GameOutViewPatternFinder.analyze(drawPositionalNumbers);;

        int[] drawingPos = drawPositionalNumbers[drawPosition];

        //SumGroupAnalyzer.analyze(drawingPos, lotteryGame);
        //LineSpacingHelperTwo.analyze( Arrays.asList( Arrays.stream(drawingPos).boxed().toArray(Integer[]::new)), false);
        //ProbableSumFinder.analyze(drawingPos, lotteryGame, drawPositionalNumbers);

        //int[] dd = p.stream().mapToInt(i -> i).toArray();
        //UpperLowerRangeAnalyzer.analyze(drawingPos, lotteryGame);

        ChartHelperTwo.clearGroupHitInformation();
        ChartHelperTwo.processIncomingData(lotteryGame, drawingPos, drawSize);

        Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();

        setupChartForViewing(positionData);
    }

    private void setupChartForViewing(Map<String, Object[]> positionData) {
        //clearUnDynamicContent();
        radioBtnAndChartHbox.getChildren().add(0,new VBox());
        radioBtnAndChartHbox.getChildren().add(1,new VBox());

        ToggleGroup group = new ToggleGroup();
        int count = 0;
        int countTwo = 1;

        for (Iterator<String> iterator = positionData.keySet().iterator(); iterator.hasNext(); ) {

            RadioButton button = new RadioButton(iterator.next());
            button.setOnAction(event -> {
                patternTable.getItems().clear();
                patternTable.getColumns().clear();
                injectChartWithData(positionData,button.getText());
                List<Integer> specialList = ChartHelperTwo.getRepeatedNumberList((List<Integer>) positionData.get(button.getText())[0]);
                setUpGroupHitGridPane(positionData,button.getText(),(List<Integer>) positionData.get(button.getText())[0]);

                int[] num = ( ((List<Integer>) positionData.get(button.getText())[0]) ).stream().mapToInt(Integer::intValue).toArray();
                //TrendLineAnalyzer.analyzeData(num);

//               LineSpacingHelperTwo.analyze( ChartHelperTwo.extractAppropriatePosition(positionData, button.getText()));
//                CompanionNumberFinder.analyzeIncomingInformation(
//                      ChartHelperTwo.extractAppropriatePosition( positionData, button.getText())
//                );
                LineSpacingHelper.determineMostProbableLineSpacing(ChartHelperTwo.extractAppropriatePosition(positionData,button.getText()));
            });

            if(count == 0) {
                button.setSelected(true);
                count++;
            }

            VBox box2 = (VBox) radioBtnAndChartHbox.getChildren().get(1);
            button.setStyle("-fx-text-fill: #dac6ac;");
            VBox box = (VBox) radioBtnAndChartHbox.getChildren().get(0);
            box.setSpacing(2);


            if( box.getChildren().size() > 13){

                box2.setVisible(true);
                box2.setSpacing(2);

                box2.getChildren().add(button);
            }
            else {
                box.getChildren().add(button);
                box2.setVisible(false);
            }

            group.getToggles().add(button);
        }

        radioBtnAndChartHbox.setSpacing(20);
        injectChartWithData(positionData,((RadioButton)group.getToggles().get(0)).getText());
        List<Integer> specialList = ChartHelperTwo.getRepeatedNumberList(
                (List<Integer>)positionData.get(((RadioButton)group.getToggles().get(0)).getText())[0]);

        setUpGroupHitGridPane(positionData, ((RadioButton)group.getToggles().get(0)).getText(),
                (List<Integer>)positionData.get(((RadioButton)group.getToggles().get(0)).getText())[0]);

        int[] nums = ((List<Integer>)positionData.get(((RadioButton)group.getToggles().get(0)).getText())[0]).stream().mapToInt(i->i).toArray();
        //TrendLineAnalyzer.analyzeData(nums);
//        setUpPatternChart((List<Integer>)positionData.get(((RadioButton)group.getToggles().get(0)).getText())[0],
//                ((RadioButton)group.getToggles().get(0)).getText());
        //LineSpacingHelperTwo.analyze(ChartHelperTwo.extractAppropriatePosition(positionData,"1"));
        //CompanionNumberFinder.analyzeIncomingInformation(ChartHelperTwo.extractAppropriatePosition( positionData, "1"));
        LineSpacingHelper.determineMostProbableLineSpacing(ChartHelperTwo.extractAppropriatePosition(positionData,"1"));

    }

    private void setUpPatternChart(List<Integer> integers,String text) {

        patternTable.getItems().clear();
        patternTable.getColumns().clear();

        Map<String,Object[]> positionData = ChartHelperTwo.getPatternData(integers,text);
        //getDataItemsTwo.clear();

        ObservableList<ObservableList> getDataItemsTwo = FXCollections.observableArrayList();
        // Create columns
        String[] colNames = {"Pattern","Hits","Games Out","Game Out Hits","Last Seen"};
        for(int i = 0; i < colNames.length; i++){

            final int j = i;
            TableColumn col = new TableColumn(colNames[i]);
            col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {

                @Override
                public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                    return new TableCell<ObservableList, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {

                                setText(item);
                                this.setTextFill(Color.BEIGE);
                                // System.out.println(param.getText());

                                ObservableList observableList = getTableView().getItems().get(getIndex());
                                if (observableList.get(2).toString().equalsIgnoreCase("0") &&
                                        !observableList.get(1).toString().equalsIgnoreCase("0") ) {
                                    getTableView().getSelectionModel().select(getIndex());

                                    if (getTableView().getSelectionModel().getSelectedItems().contains(observableList)) {

                                        this.setTextFill(Color.valueOf("#76FF03"));
                                    }

                                    //System.out.println(getItem());
                                    // Get fancy and change color based on data
                                    //if (item.contains("X"))
                                    //this.setTextFill(Color.valueOf("#EFA747"));
                                }
                            }
                        }
                    };
                }
            });

            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())



            );

            col.setSortable(false);
            patternTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = positionData.size();
        for(Map.Entry<String,Object[]> data : positionData.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            String key = data.getKey();
            Object[] values = data.getValue();

            row.add(key);
            row.add(values[0]+"");
            row.add(values[1]+"");
            row.add(values[2]+"");
            row.add(values[3]+"");

            getDataItemsTwo.add(row);
        }

        patternTable.setItems(getDataItemsTwo);
    }


    private void setUpGroupHitGridPane(Map<String, Object[]> positionData, String text, List<Integer> points) {

        groupInfoTable.refresh();

        groupInfoTable.getItems().clear();
        groupInfoTable.getColumns().clear();

        // Create columns
        String[] colNames = {"Group","Hits","Games Out","Hits At", "Avg Skips", "Hits Above","Hits Below","Equal To","Last Seen"};
        for(int i = 0; i < colNames.length; i++){

            final int j = i;
            TableColumn col = new TableColumn(colNames[i]);
            col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {

                @Override
                public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                    return new TableCell<ObservableList, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {

                                setText(item);
                                this.setTextFill(Color.BEIGE);
                                // System.out.println(param.getText());

                                ObservableList observableList = getTableView().getItems().get(getIndex());
                                if (observableList.get(2).toString().equalsIgnoreCase("0")) {
                                    getTableView().getSelectionModel().select(getIndex());

                                    if (getTableView().getSelectionModel().getSelectedItems().contains(observableList)) {

                                        this.setTextFill(Color.valueOf("#76FF03"));
                                    }

                                    //System.out.println(getItem());
                                    // Get fancy and change color based on data
                                    //if (item.contains("X"))
                                    //this.setTextFill(Color.valueOf("#EFA747"));
                                }
                            }
                        }
                    };
                }
            });

            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())



            );

            col.setSortable(false);
            groupInfoTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = positionData.size();
        for(Map.Entry<String,Object[]> data : positionData.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            String key = data.getKey();
            Object[] values = data.getValue();

            row.add(key);
            row.add(values[1]+"");
            row.add(values[2]+"");
            row.add(values[3]+"");
            row.add(values[5]+"");
            row.add(values[6]+"");
            row.add(values[7]+"");
            row.add(values[8]+"");
            row.add(values[9]+"");

            dataItems.add(row);
        }

        groupInfoTable.setItems(dataItems);
      //  groupInfoTable.scrollTo(size - 1);

        setUpPatternChart(points,text);
    }

    @SuppressWarnings("unchecked")
    private void injectChartWithData(Map<String, Object[]> positionData, String text) {

        if(radioBtnAndChartHbox.getChildren().size() < 3) {
            radioBtnAndChartHbox.getChildren().remove(1);
        }
        else if(radioBtnAndChartHbox.getChildren().size() > 2){

            for(int i = 0; i < radioBtnAndChartHbox.getChildren().size(); i++){

                Object obj = radioBtnAndChartHbox.getChildren().get(i);

                if(obj instanceof LineChart){

                    radioBtnAndChartHbox.getChildren().remove(i);
                }
            }

        }
            //radioBtnAndChartHbox.getChildren().remove(2);

        String[] colors = {"#FF0000", "#FF0000"};

        List<Integer> numList = (List<Integer>)positionData.get(text)[0];
        List<Integer> numListTwo = new ArrayList<>(numList);
        Collections.sort(numListTwo);

        List<Integer> specialList = ChartHelperTwo.getRepeatedNumberList(numList);

        List<List<Integer>> dataPoints = new ArrayList<>();
        //dataPoints.add((numList.size() > 150) ? numList.subList(numList.size()-150,numList.size()) : numList);
        dataPoints.add( (numList.size() > 150) ? numList.subList(numList.size()-150,numList.size()) : numList);

//        List<Integer> pointTwo = (numList.size() > 0) ? ChartHelper.getListOfNumbersBasedOnCurrentWinningNumber(numList) : new ArrayList<>();
//
//        if(pointTwo.size() > 0)
//            dataPoints.add((pointTwo.size() > 100) ? pointTwo.subList(pointTwo.size() - 100, pointTwo.size()) : pointTwo);

        Set<Integer> unique = new HashSet<>(numListTwo);

        int minValue,maxValue;

        if(numListTwo.size() > 1)
        {
            minValue = numListTwo.get(0);
            maxValue = numListTwo.get(numList.size() - 1);
        }
        else{
            minValue =1;
            maxValue = 2;
        }
        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minValue,
                maxValue, unique.toString(), text,996,263);

        radioBtnAndChartHbox.getChildren().add(lc.getLineChart());

    }


    // getters and setters

    public static int getRowIndex() {
        return rowIndex;
    }

    public static void setRowIndex(int rowIndex) {
        GroupChartController.rowIndex = rowIndex;
    }

    public LotteryGame getLotteryGame() {
        return lotteryGame;
    }

    private void setLotteryGame(LotteryGame lotteryGame) {
        this.lotteryGame = lotteryGame;
    }

    public int[][] getDrawPositionalNumbers() {
        return drawPositionalNumbers;
    }

    private void setDrawPositionalNumbers(int[][] drawPositionalNumbers) {
        this.drawPositionalNumbers = drawPositionalNumbers;
    }

    public static void setGlobalDrawPosition(int globalDrawPosition) {
        GroupChartController.globalDrawPosition = globalDrawPosition;
    }
}
