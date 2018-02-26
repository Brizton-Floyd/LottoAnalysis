package com.lottoanalysis.companionnumbers;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.companionnumbers.companionnumberhelpers.CompanionNumberHelper;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.lottoinfoandgames.LottoGame;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;

@SuppressWarnings("unchecked")
public class CompanionNumberController {

    private int[][] postionalNumbers;
    private LottoGame game;
    private Map<Integer,String> positionStringNameHolder = new HashMap<>();
    private Map<String,List<Integer>> groupAndNumberHolder = new LinkedHashMap<>();
    private Object[] array = new Object[4];

    @FXML
    private ComboBox postionBox, positionNumbers, positionGroupNumbers, companionNumberGrpups;

    @FXML
    private Label companionPostionLbl, lblAnalyzedPosition,infoLbl, lbl2,lbl3;

    @FXML
    private Button analyzeBtn;

    @FXML
    private TableView companionTable, statTable, lastDigitDueTable;

    @FXML
    public void initialize(){

        //Start setting up the lottery game menu
        applyStyles();

        positionStringNameHolder.put(0,"Position One");
        positionStringNameHolder.put(1,"Position Two");
        positionStringNameHolder.put(2,"Position Three");
        positionStringNameHolder.put(3,"Position Four");
        positionStringNameHolder.put(4,"Position Five");
        positionStringNameHolder.put(5,"Position Six");

        companionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        statTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        lastDigitDueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void setPostionalNumbers(int[][] numbers)
    {
        this.postionalNumbers = numbers;
    }

    public void setGame(LottoGame game)
    {
        this.game = game;
    }

    public void initComponents()
    {
        setupGameNameLabel();
        plugPostionsIntoPostionComboBox();

        plugGroupRanges();

        adjustCompanionGroupSize();
        analyzePositions(true);

        analyzeBtn.setOnAction( event -> {

            analyzePositions(false);
            CompanionNumberHelper.analyze((int)array[0],(List<Integer>)array[1],(List<Integer>)array[2],(List<Integer>) array[3]);
            Map<String,Object[]> data = CompanionNumberHelper.getPatternHolder();
            setUpCompanionTable(data);
        });
    }

    private void setUpCompanionTable(Map<String, Object[]> data) {

        companionTable.refresh();
        companionTable.getItems().clear();
        companionTable.getColumns().clear();
        statTable.getItems().clear();
        statTable.getColumns().clear();
        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();
        // Create columns
        for(int i = 0; i < data.size(); i++){

            final int j = i;
            TableColumn col = new TableColumn(CompanionNumberHelper.getColumnHeaders().get(j));
            col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {

                @Override
                public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                    return new TableCell<ObservableList, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                this.setTextFill(Color.BEIGE);
                                // Get fancy and change color based on data
                                if (item.contains("X"))
                                    this.setTextFill(Color.valueOf("#EFA747"));
                                setText(item);
                            }
                        }
                    };
                }
            });

            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())



            );

            col.setSortable(false);
            companionTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        List<List<String>> points = CompanionNumberHelper.getListHolder();
        int size = points.get(0).size();

        for(int i = 0; i < size; i++){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            for(int j = 0; j < points.size(); j++){


                List<String> values = points.get(j);
                //Iterate Column
                row.add(values.get(i));
                // System.out.println("Row [1] added " + row);

            }

            dataItems.add(row);
        }

        companionTable.setItems(dataItems);
        companionTable.scrollTo(size - 1);

        setUpStatTable(CompanionNumberHelper.getPatternHolder());
    }

    private void setUpStatTable(Map<String, Object[]> patternHolder) {

        statTable.getItems().clear();
        statTable.getColumns().clear();
        ObservableList<ObservableList> getDataItemsTwo = FXCollections.observableArrayList();
        // Create columns
        String[] colNames = {"Pattern","Hits","Games Out","Out Hits","Last Seen"};
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
            statTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        //int size = statTable.size();
        for(Map.Entry<String,Object[]> data : patternHolder.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            String key = data.getKey();
            Object[] values = data.getValue();

            row.add(key);
            row.add(values[0]+"");
            row.add(values[1]+"");
            row.add(values[3]+"");
            row.add(values[4]+"");

            getDataItemsTwo.add(row);
        }

        statTable.setItems(getDataItemsTwo);

        setUpLastDigitTable();
    }

    private void setUpLastDigitTable() {

        lastDigitDueTable.refresh();
        lastDigitDueTable.getItems().clear();
        lastDigitDueTable.getColumns().clear();

        String[] headers = {"First Digit","Hits","Games Out"};

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        Map<Integer,Integer[]> data = CompanionNumberHelper.getLastDigitDueHolder();

        for(int i = 0; i < headers.length; i++){

            final int j = i;
            TableColumn col = new TableColumn(headers[i]);
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
                                if (observableList.get(2).toString().equalsIgnoreCase("0") ) {
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
            lastDigitDueTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        //int size = positionData.size();
        for(Map.Entry<Integer,Integer[]> dataa : data.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            String key = dataa.getKey() + "";
            Integer[] values = dataa.getValue();

            row.add(key);
            row.add(values[0]+"");
            row.add(values[1]+"");

            dataItems.add(row);
        }

        lastDigitDueTable.setItems(dataItems);
    }


    private void analyzePositions(boolean val) {

        Map<String,Integer> indexrefHolder = new HashMap<>();
        indexrefHolder.put("Position One",0);
        indexrefHolder.put("Position Two", 1);
        indexrefHolder.put("Position Three",2);
        indexrefHolder.put("Position Four",3);
        indexrefHolder.put("Position Five",4);
        indexrefHolder.put("Position Six",5);

        String currentPos = (String)postionBox.getValue();
        String companionPos = companionPostionLbl.getText();

        // numbers
        List<Integer> previousPostion = Arrays.asList( Arrays.stream(postionalNumbers[indexrefHolder.get(currentPos)]).boxed().toArray(Integer[]::new));
        List<Integer> companionNumbers = Arrays.asList( Arrays.stream(postionalNumbers[indexrefHolder.get(companionPos)]).boxed().toArray(Integer[]::new));

        // selected num
        int selectedNum = Integer.parseInt(positionNumbers.getValue().toString());

        // companion values
        List<Integer> companionValues;

        if(!game.getGameName().contains("Pick4") && !game.getGameName().contains("Pick3")){

           List<Integer> items = new ArrayList<>( groupAndNumberHolder.get(companionNumberGrpups.getValue()));


           items.removeIf(num -> num == selectedNum || num < selectedNum);

           companionValues = new ArrayList<>(items);

        }
        else{
            List<Integer> items = groupAndNumberHolder.get(companionNumberGrpups.getValue());
            companionValues = new ArrayList<>(items);
        }

        array[0] = selectedNum;
        array[1] = previousPostion;
        array[2] = companionNumbers;
        array[3] = companionValues;

        if(val) {
            CompanionNumberHelper.analyze(selectedNum, previousPostion, companionNumbers, companionValues);
            Map<String,Object[]> data = CompanionNumberHelper.getPatternHolder();
            setUpCompanionTable(data);
        }
        
    }

    private void plugGroupRanges() {



        int min = game.getMinNumber();
        int max = game.getMaxNumber();

        for(int i = min; i <= max; i++)
        {
            String numAsString = i + "";
            int val = (numAsString.length() > 1) ? Character.getNumericValue(numAsString.charAt(0)) : 0;

            String groupName = "";

            if(val == 0)
            {
                groupName = "Zero's";
            }
            else if( val == 1){
                groupName = "Tens";

            }
            else if( val == 2){
                groupName = "Twenties";

            }
            else if( val == 3){
                groupName = "Thirties";

            }
            else if( val == 4){
                groupName = "Fourties";

            }
            else if( val == 5){
                groupName = "Fifties";

            }
            else if( val == 6){
                groupName = "Sixties";
            }
            else if( val == 7){
                groupName = "Seventies";
            }
            else if( val == 8){
                groupName = "Eighties";
            }

            if(!groupAndNumberHolder.containsKey(groupName))
            {
                List<Integer> nums = new LinkedList<>();
                nums.add(i);
                groupAndNumberHolder.put(groupName, nums);
            }
            else {
                List<Integer> nums = groupAndNumberHolder.get(groupName);
                nums.add(i);
            }
        }

        groupAndNumberHolder.forEach( (k,v) -> {

            positionGroupNumbers.getItems().add(k);
        });

        positionGroupNumbers.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {

            positionNumbers.getItems().clear();

            List<Integer> numbers = groupAndNumberHolder.get(newValue);
            numbers.forEach( i -> {
                positionNumbers.getItems().add(i);
            });

            positionNumbers.getSelectionModel().selectFirst();
            adjustCompanionGroupSize();
        }));

        positionGroupNumbers.getSelectionModel().selectFirst();
        positionNumbers.getSelectionModel().selectFirst();
    }

    private void adjustCompanionGroupSize(){

        List<String> items = positionGroupNumbers.getItems();
        companionNumberGrpups.getItems().clear();

        if(game.getGameName().contains(LotteryGameConstants.PICK4_GAME_NAME) || game.getGameName().contains(LotteryGameConstants.PICK3_GAME_NAME)) {
            for (String n : items) {
                companionNumberGrpups.getItems().add(n);
            }
        }else{
            int index = items.indexOf(positionGroupNumbers.getValue());
            for(int i = index; i < items.size(); i++){
                companionNumberGrpups.getItems().add(items.get(i));
            }
        }

        companionNumberGrpups.getSelectionModel().selectFirst();
    }

    private void plugPostionsIntoPostionComboBox() {

        int len = postionalNumbers.length;
        for(int i = 0; i < len - 1; i++)
        {
            postionBox.getItems().add(positionStringNameHolder.get(i));
        }

        postionBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            String text = infoLbl.getText();
            //int index = text.lastIndexOf("One");
            text = postionBox.getValue() + " Group #'s for play:";
            //System.out.println(text);
            infoLbl.setText("");
            infoLbl.setText(text);

            lbl2.setText(postionBox.getValue() + " Numbers");

            switch ((String)newValue)
            {
                case "Position One":
                    companionPostionLbl.setText(positionStringNameHolder.get(1));
                    lbl3.setText("Companion " + companionPostionLbl.getText() + " Group #'s");
                    break;
                case "Position Two":
                    companionPostionLbl.setText(positionStringNameHolder.get(2));
                    lbl3.setText("Companion " + companionPostionLbl.getText() + " Group #'s");
                    break;
                case "Position Three":
                    companionPostionLbl.setText(positionStringNameHolder.get(3));
                    lbl3.setText("Companion " + companionPostionLbl.getText() + " Group #'s");
                    break;
                case "Position Four":
                    companionPostionLbl.setText(positionStringNameHolder.get(4));
                    lbl3.setText("Companion " + companionPostionLbl.getText() + " Group #'s");
                    break;
                case "Position Five":
                    companionPostionLbl.setText(positionStringNameHolder.get(5));
                    lbl3.setText("Companion " + companionPostionLbl.getText() + " Group #'s");
                    break;
            }
            //System.out.println(newValue);
        });

        postionBox.getSelectionModel().selectFirst();

    }

    private void setupGameNameLabel() {
        String text = lblAnalyzedPosition.getText();
        int index = text.lastIndexOf(":");

        text = text.substring(0,index+1) + " " +game.getGameName();

        lblAnalyzedPosition.setText(text);
    }

    private void applyStyles() {
        postionBox.setStyle("-fx-focus-color: transparent;");
        positionNumbers.setStyle("-fx-focus-color: transparent;");
        positionGroupNumbers.setStyle("-fx-focus-color: transparent;");
        companionNumberGrpups.setStyle("-fx-focus-color: transparent;");

    }
}

