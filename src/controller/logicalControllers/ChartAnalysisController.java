package controller.logicalControllers;

import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import line_chart_helper.NumberAnalyzer;
import model.DataFiles.LotteryGameConstants;
import model.LotteryGame;
import model.chartImplementations.LineChartWithHover;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ChartAnalysisController implements Initializable {

    private MainController mainController;
    private LotteryGame game;
    private int[][] numbers;
    private int[] firstElement, secondElement;
    private ObservableList<Integer> positionNumbers;
    private LineChartWithHover lineChartWithHover;
    private int valTwo = 0;

    @FXML
    private AnchorPane chartAnalysisMainPane;

    @FXML
    private RadioButton last20;

    @FXML
    private LineChart lineChart;

    @FXML
    private TextFlow aboveText;
    @FXML
    private TextFlow belowText;

    @FXML
    private HBox buttonContainerBox, chartHbox;

    @FXML
    private Label gameAvgLbl, hitsAboveAvgLbl, aboveGamesOutHitsLbl, hitsBelowAvgLbl, belowGamesOutHitsLbl,elementOneLbl,
            elementTwoLbl, elementOneHits,elementOneAboveGamesOut, belowTotalHitsEleOne, belowGamesOutEleOne,elementTwoAboveHits,
            elementTwoAboveGamesOut, elementTwoBelowHits, elementTwoBelowGamesOut, remainder0lbl,remainder1lbl,remainder2lbl;

    @FXML
    private Label remainder0Hits, remainder0GamesOut, remainder1Hits, remainder1GamesOut, remainder2Hits, remainder2GamesOut;

    @FXML
    private Label digit0Hitslbl, digit0HitsGamesOutLbl,digit3Hitslbl, digit3HitsGamesOutLbl,digit6Hitslbl,digit6HitsGamesOutLbl,
                  digit9Hitslbl,digit9HitsGamesOutLbl, digit1Hitslbl, digit1HitsGamesOutLbl, digit4Hitslbl, digit4HitsGamesOutLbl,
                  digit7Hitslbl, digit7HitsGamesOutLbl,digit2Hitslbl,digit2HitsGamesOutLbl, digit5Hitslbl, digit5HitsGamesOutLbl,
                  digit8Hitslbl,digit8HitsGamesOutLbl;

    private void createDynamicButtons() {

        for (int i = 0; i < numbers.length; i++) {
            JFXButton button = new JFXButton("Pos " + (i + 1));
            final int val = i;

            button.setFont(Font.font(10));
            button.setFocusTraversable(false);
            HBox.setMargin(button, new Insets(15, 0, 0, 0));
            button.setOnAction(e -> {

                last20.getToggleGroup().selectToggle(last20);
                valTwo = val;
                loadPastOneHundredDrawingsForPosition(80);


            });
            // button.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
            button.textFillProperty().setValue(Paint.valueOf("#dac6ac"));
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-font-size: 10px;" +
                        "-fx-background-color: #dac6ac;" +
                        "-fx-text-fill: #10000C;");
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-text-fill: #dac6ac;");

            });
            buttonContainerBox.getChildren().add(button);
        }
    }

    public void init(MainController mainController) {

        this.mainController = mainController;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup toggleGroup = last20.getToggleGroup();
        ObservableList<Toggle> toggles = toggleGroup.getToggles();

        for (Toggle t : toggles) {

            if (t instanceof RadioButton) {
                RadioButton button = (RadioButton) t;
                switch (button.getText()) {
                    case "Last 20 Draws":
                        button.setOnAction(e -> {
                            loadPastOneHundredDrawingsForPosition(80);
                        });
                        break;
                    case "Last 40 Draws":
                        button.setOnAction(e -> {
                            loadPastOneHundredDrawingsForPosition(60);
                        });
                        break;
                    case "Last 60 Draws":
                        button.setOnAction(e -> {
                            loadPastOneHundredDrawingsForPosition(40);
                        });
                        break;
                    case "Last 80 Draws":
                        button.setOnAction(e -> {
                            loadPastOneHundredDrawingsForPosition(20);

                        });
                        break;
                    case "Last 100 Draws":
                        button.setOnAction(e -> {
                            loadPastOneHundredDrawingsForPosition(0);
                        });
                        break;
                }
            }
        }

    }

    private void loadPastOneHundredDrawingsForPosition(int i) {
        int[] data = numbers[valTwo];
        loadFirstAndSecondElementValuesIntoList(data);
        loadPastOneHundredDrawingsForPosition(data, i);
        loadDataIntoPerspectivePanes();
    }

    private void loadFirstAndSecondElementValuesIntoList(int[] number) {

        firstElement = new int[number.length];
        secondElement = new int[number.length];

        for(int val = 0; val < number.length; val++){

            String numAsString = Integer.toString(number[val]);
            boolean isTwoCharacters = (numAsString.length() == 2);
            if(isTwoCharacters){
                String[] numsSplit = numAsString.split("");
                firstElement[val] = Integer.parseInt(numsSplit[0]);
                secondElement[val] = Integer.parseInt(numsSplit[1]);
            }else{

                firstElement[val] = 0;
                secondElement[val] = Integer.parseInt(numAsString);
            }
        }
    }

    private void loadPastOneHundredDrawingsForPosition(int[] number, int range) {

        Integer[] val = new Integer[100];
        int index = 0;

        for(int i = number.length - 100; i < number.length; i++){

            val[index++] = number[i];
        }

        if( positionNumbers != null )
            positionNumbers.clear();

        positionNumbers = FXCollections.observableArrayList(val);
        if(range == 0) {
            setUpChart();
        }
        else {
            if(range == 20)
                positionNumbers.remove(0,20);
            else if(range == 40)
                positionNumbers.remove(0,40);
            else if(range == 60)
                positionNumbers.remove(0,60);
            else if(range == 80)
                positionNumbers.remove(0,80);

            setUpChart();
        }
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
        createDynamicButtons();
    }

    public void showPane() {
        chartAnalysisMainPane.setVisible(true);
    }

    public void hidePane() {
        chartAnalysisMainPane.setVisible(false);
    }

    public void clearButtonBox() {
        if (buttonContainerBox != null && buttonContainerBox.getChildren().size() > 1) {

            buttonContainerBox.getChildren().remove(1, buttonContainerBox.getChildren().size());

        }

    }

    public void setGame(LotteryGame game) {
        this.game = game;
        loadPastOneHundredDrawingsForPosition(80);
    }

    public void setUpChart() {

        lineChartWithHover = new LineChartWithHover(game.getGameName() + " Pos " + (valTwo + 1) +" Analysis", positionNumbers);

        chartHbox.getChildren().remove(0);
        chartHbox.getChildren().add(0, lineChartWithHover.getLineChart());

    }

    public void loadDataIntoPerspectivePanes() {

        Map<Integer,Map<String,Object[]>> data =     null;
        Map<Integer,Map<String,Object[]>> firstEle = null;
        Map<Integer,Map<String,Object[]>> secondEle =null;
        if(!game.getGameName().equals(LotteryGameConstants.DAILY_PICK_4) && !game.getGameName().equals(LotteryGameConstants.DAILY_PICK_3)){
           data =      NumberAnalyzer.findAverageAndGamesOut(numbers[valTwo], LotteryGameConstants.inflateAverage);
           firstEle =  NumberAnalyzer.findAverageAndGamesOut(firstElement, LotteryGameConstants.doNotInflateAverage);
           secondEle = NumberAnalyzer.findAverageAndGamesOut(secondElement, LotteryGameConstants.doNotInflateAverage);
        }else{
            data =      NumberAnalyzer.findAverageAndGamesOut(numbers[valTwo], LotteryGameConstants.doNotInflateAverage);
            firstEle =  NumberAnalyzer.findAverageAndGamesOut(firstElement, LotteryGameConstants.doNotInflateAverage);
            secondEle = NumberAnalyzer.findAverageAndGamesOut(secondElement, LotteryGameConstants.doNotInflateAverage);
        }
        populateHeadersForBestLastDigit(new int[]{0,1,2,3,4,5,6,7,8,9});

        Map<Integer,Integer[]> hitsAndGamesOutatRemainder = NumberAnalyzer.findHitsAndGamesOutForRemainder(secondElement);
        populateRemainderHitsAndGamesOut(hitsAndGamesOutatRemainder);

        Map<Integer, Map<Integer, Integer[]>> lastDigitsToHitInRanges = NumberAnalyzer.findLastDigitsThatLastHitInRanges(secondElement);
        populateLastDigitHitRanges(lastDigitsToHitInRanges);

        for(Map.Entry<Integer, Map<String, Object[]>> dataTwo : data.entrySet()){

            gameAvgLbl.setText(dataTwo.getKey().toString());

            Map<String, Object[]> dataThree = dataTwo.getValue();
            for(Map.Entry<String, Object[]> value : dataThree.entrySet()){

                if(value.getKey().equals("Above")){
                    Object[] valueThree = value.getValue();
                    hitsAboveAvgLbl.setText(valueThree[0].toString());
                    aboveGamesOutHitsLbl.setText(valueThree[1].toString());
                    setUpTextFlowInformation(valueThree, 0);
                }
                else if(value.getKey().equals("Below")){

                    Object[] valueThree = value.getValue();
                    hitsBelowAvgLbl.setText(valueThree[0].toString());
                    belowGamesOutHitsLbl.setText(valueThree[1].toString());
                    setUpTextFlowInformation(valueThree, 1);
                }
            }
        }

        for(Map.Entry<Integer, Map<String, Object[]>> dataTwo : firstEle.entrySet()){

            elementOneLbl.setText(dataTwo.getKey().toString());

            Map<String, Object[]> dataThree = dataTwo.getValue();
            for(Map.Entry<String, Object[]> value : dataThree.entrySet()){

                if(value.getKey().equals("Above")){
                    Object[] valueThree = value.getValue();
                    elementOneHits.setText(valueThree[0].toString());
                    elementOneAboveGamesOut.setText(valueThree[1].toString());
                }
                else if(value.getKey().equals("Below")){

                    Object[] valueThree = value.getValue();
                    belowTotalHitsEleOne.setText(valueThree[0].toString());
                    belowGamesOutEleOne.setText(valueThree[1].toString());
                }
            }
        }

        for(Map.Entry<Integer, Map<String, Object[]>> dataTwo : secondEle.entrySet()){

            elementTwoLbl.setText(dataTwo.getKey().toString());

            Map<String, Object[]> dataThree = dataTwo.getValue();
            for(Map.Entry<String, Object[]> value : dataThree.entrySet()){

                if(value.getKey().equals("Above")){
                    Object[] valueThree = value.getValue();
                    elementTwoAboveHits.setText(valueThree[0].toString());
                    elementTwoAboveGamesOut.setText(valueThree[1].toString());
                }
                else if(value.getKey().equals("Below")){

                    Object[] valueThree = value.getValue();
                    elementTwoBelowHits.setText(valueThree[0].toString());
                    elementTwoBelowGamesOut.setText(valueThree[1].toString());
                }
            }
        }


    }

    private void populateLastDigitHitRanges(Map<Integer, Map<Integer, Integer[]>> lastDigitsToHitInRanges) {
        Label[] labels = {digit0Hitslbl, digit0HitsGamesOutLbl,digit3Hitslbl, digit3HitsGamesOutLbl,digit6Hitslbl,digit6HitsGamesOutLbl,
                digit9Hitslbl,digit9HitsGamesOutLbl, digit1Hitslbl, digit1HitsGamesOutLbl, digit4Hitslbl, digit4HitsGamesOutLbl,
                digit7Hitslbl, digit7HitsGamesOutLbl,digit2Hitslbl,digit2HitsGamesOutLbl, digit5Hitslbl, digit5HitsGamesOutLbl,
                digit8Hitslbl,digit8HitsGamesOutLbl};

        int count = 0;

        for(Map.Entry<Integer, Map<Integer, Integer[]>> data : lastDigitsToHitInRanges.entrySet()){
            Map<Integer, Integer[]> dataTwo = data.getValue();
            for(Map.Entry<Integer, Integer[]> d : dataTwo.entrySet()){
                labels[count++].setText(d.getValue()[0].toString());
                labels[count++].setText(d.getValue()[1].toString());
            }
        }
    }

    private void populateRemainderHitsAndGamesOut(Map<Integer, Integer[]> hitsAndGamesOutatRemainder) {

        int count =0;
        Label[] labels = {remainder0Hits, remainder0GamesOut, remainder1Hits, remainder1GamesOut,remainder2Hits, remainder2GamesOut};

        for(Map.Entry<Integer, Integer[]> data : hitsAndGamesOutatRemainder.entrySet()){
            Integer[] dataTwo = data.getValue();
            labels[count++].setText(dataTwo[0].toString());
            labels[count++].setText(dataTwo[1].toString());
        }
    }

    private void populateHeadersForBestLastDigit(int[] ints) {
        Map<Integer,String> header = new TreeMap<>();


        for(int num : ints){
            int remainder = num % 3;

            if(!header.containsKey(remainder)){
                String res = "( " + num;
                header.put(remainder, res);
            }
            else{
                String res = header.get(remainder);
                res += ", " + num;
                header.put(remainder, res);
            }
        }

        int count = 0;
        Label[] labels = {remainder0lbl, remainder1lbl, remainder2lbl};
        for(Map.Entry<Integer, String> value : header.entrySet()){

            String res = header.get(value.getKey());
            res+= " )";
            header.put(value.getKey(),res);
            labels[count++].setText(header.get(value.getKey()));
        }
    }

    private void setUpTextFlowInformation(Object[] valueThree, int index) {

        TextFlow[] d = {aboveText, belowText};
        String direction = (index == 0) ? "Above" : "Below";
        String lesserorhigher = (index == 0) ? "greater" : "lesser";

        for (int i = 0; i < 1; i++) {

            if(d[index].getChildren().size() > 0)
                d[index].getChildren().clear();

            Text t = new Text(String.format("When the %1s average hit counter is ",direction));
            t.setFill(Color.valueOf("#dac6ac"));

            Text tt = new Text(valueThree[1].toString());
            tt.setFill(Color.BLACK);

            Text ttt = new Text(String.format(" games out. The next draw number was %1s than the position average ",lesserorhigher));
            ttt.setFill(Color.valueOf("#dac6ac"));

            Text tttt = new Text(valueThree[3].toString());
            tttt.setFill(Color.BLACK);

            Text ttttt = new Text(" times. Its been ");
            ttttt.setFill(Color.valueOf("#dac6ac"));

            Text tttttt = new Text(valueThree[4].toString());
            tttttt.setFill(Color.BLACK);

            Text ttttttt = new Text(" drawings since the current games out appeared in the next drawing. ");
            ttttttt.setFill(Color.valueOf("#dac6ac"));

            d[index].getChildren().addAll(t, tt, ttt, tttt, ttttt, tttttt, ttttttt);

        }

    }
}
