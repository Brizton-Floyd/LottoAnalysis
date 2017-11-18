package controller.logicalControllers;

import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import line_chart_helper.LineRetracementAnalyzer;
import line_chart_helper.NumberAnalyzer;
import model.DataFiles.LotteryGameConstants;
import model.LotteryGame;
import model.LottoBetSlipDefinitions;
import model.PickFourLotteryGame;
import model.PickThreeLotteryGame;
import model.chartImplementations.LineChartWithHover;

import java.net.URL;
import java.util.*;

public class ChartAnalysisController implements Initializable {

    private MainController mainController;
    private LotteryGame game;
    private int[][] numbers;
    private int[] firstElement, secondElement;
    private ObservableList<Integer> positionNumbers;
    private LineChartWithHover lineChartWithHover;
    private int valTwo, globalInt;

    @FXML
    private AnchorPane chartAnalysisMainPane;

    @FXML
    private RadioButton last20, remainder0Radio;

    @FXML
    private VBox remainder0Vbox, remainder1Vbox, remainder2Vbox;

    @FXML
    private TextFlow aboveText, betSlipTextFlow;
    @FXML
    private TextFlow belowText;

    @FXML
    private HBox buttonContainerBox, chartHbox, gridPaneHbox, columnHeaderBox,elementButtonBox;

    @FXML
    private Label gameAvgLbl, hitsAboveAvgLbl, aboveGamesOutHitsLbl, hitsBelowAvgLbl, belowGamesOutHitsLbl, elementOneLbl,
            elementTwoLbl, elementOneHits, elementOneAboveGamesOut, belowTotalHitsEleOne, belowGamesOutEleOne, elementTwoAboveHits,
            elementTwoAboveGamesOut, elementTwoBelowHits, elementTwoBelowGamesOut, remainder0lbl, remainder1lbl, remainder2lbl;

    @FXML
    private Label remainder0Hits, remainder0GamesOut, remainder0PrevGamesOut, remainder1Hits, remainder1GamesOut, remainder1PrevGamesOut,
            remainder2Hits, remainder2GamesOut, remainder2PrevGamesOut, retracementLbl, abovePrevLbl, belowPrevLbl, equalPrevLbl,
            lineAboveLbl, lineBelowLbl;

    @FXML
    private Label digit0Hitslbl, digit0HitsGamesOutLbl, digit3Hitslbl, digit3HitsGamesOutLbl, digit6Hitslbl, digit6HitsGamesOutLbl,
            digit9Hitslbl, digit9HitsGamesOutLbl, digit1Hitslbl, digit1HitsGamesOutLbl, digit4Hitslbl, digit4HitsGamesOutLbl,
            digit7Hitslbl, digit7HitsGamesOutLbl, digit2Hitslbl, digit2HitsGamesOutLbl, digit5Hitslbl, digit5HitsGamesOutLbl,
            digit8Hitslbl, digit8HitsGamesOutLbl;

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
                clearContents();
                enableRadioButotns();
                loadPastOneHundredDrawingsForPosition(80);

                ToggleGroup toggle = remainder0Radio.getToggleGroup();
                for (Toggle node : toggle.getToggles()) {
                    if (node instanceof RadioButton) {
                        if ((node.isSelected())) {

                            node.setSelected(false);
                        }
                    }
                }

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

        if( !(game instanceof PickFourLotteryGame) && !(game instanceof PickThreeLotteryGame)){

            // Create Javafx button to represent each element
            JFXButton elementOneButton = new JFXButton("Ele: 1");
            JFXButton elementTwoButton = new JFXButton("Ele: 2");
            JFXButton fullNumber = new JFXButton("Draw Num");

            JFXButton[] buttons = {elementOneButton, elementTwoButton, fullNumber};
            int count = 0;
            // Loop through and style each button
            for(JFXButton button : buttons){

                button.textFillProperty().setValue(Paint.valueOf("#dac6ac"));
                button.setOnMouseEntered(e -> {
                    button.setStyle("-fx-font-size: 10px;" +
                            "-fx-background-color: #dac6ac;" +
                            "-fx-text-fill: #10000C;");
                });
                button.setOnMouseExited(e -> {
                    button.setStyle("-fx-text-fill: #dac6ac;");

                });

                int finalIntVal = count;
                button.setOnAction( action -> {

                    //Load proper index values into list
                    loadProperIndexValuesIntoList(finalIntVal, valTwo);

                });

                count++;

                elementButtonBox.getChildren().add(button);
            }

            // add the button to the hbox

        }
    }

    private void loadProperIndexValuesIntoList(int count, int valTwo) {

        // Disable radio buttons
        disableRadioButton();
        // Get all the winning numbers for current position
        int[] drawNumbers = numbers[valTwo];

        int[][] splitAndCombinedNumbers = splitAndCombineNumbersForList( drawNumbers );

        if( count == 2)
            globalInt = count;

        loadPastOneHundredDrawingsForPosition(splitAndCombinedNumbers[count], 0, true);

    }

    private void enableRadioButotns(){

        ToggleGroup toggleGroup = last20.getToggleGroup();
        ObservableList<Toggle> toggles = toggleGroup.getToggles();

        for( Toggle toggle : toggles){

            if( toggle instanceof RadioButton){

                RadioButton radioButton = (RadioButton)toggle;
                if(radioButton.isDisable()){
                    radioButton.setDisable( false );
                }
            }
        }


    }
    private void disableRadioButton() {

        ToggleGroup toggleGroup = last20.getToggleGroup();
        ObservableList<Toggle> toggles = toggleGroup.getToggles();

        for( Toggle toggle : toggles){

            if( toggle instanceof RadioButton){

                RadioButton radioButton = (RadioButton)toggle;
                if(!radioButton.isDisable()){
                    radioButton.setDisable( true );
                }
            }
        }

    }

    private int[][] splitAndCombineNumbersForList(int[] drawNumbers) {

        int[][] drawPosVals = new int[3][drawNumbers.length];

        for(int i = 0; i < drawNumbers.length; i++){

            String numAsString = "" + drawNumbers[i];

            if(numAsString.length() > 1){

                drawPosVals[0][i] = Character.getNumericValue( numAsString.charAt(0));
                drawPosVals[1][i] = Character.getNumericValue( numAsString.charAt(1));

                String combinedNumber = drawPosVals[0][i] +""+ drawPosVals[1][i];
                drawPosVals[2][i] = Integer.parseInt( combinedNumber );

            }
            else{

                drawPosVals[0][i] = 0;
                drawPosVals[1][i] = Character.getNumericValue( numAsString.charAt(0));

                String combinedNumber = drawPosVals[0][i] +""+ drawPosVals[1][i];
                drawPosVals[2][i] = Integer.parseInt( combinedNumber );

            }
        }

        return drawPosVals;
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
                button.setStyle("-fx-focus-traversable: false;");

                switch (button.getText()) {
                    case "Last 20 Draws":
                        button.setOnAction(e -> {
                            clearContents();
                            loadPastOneHundredDrawingsForPosition(80);
                        });
                        break;
                    case "Last 40 Draws":
                        button.setOnAction(e -> {
                            clearContents();
                            loadPastOneHundredDrawingsForPosition(60);
                        });
                        break;
                    case "Last 60 Draws":
                        button.setOnAction(e -> {
                            clearContents();
                            loadPastOneHundredDrawingsForPosition(40);
                        });
                        break;
                    case "Last 80 Draws":
                        button.setOnAction(e -> {
                            clearContents();
                            loadPastOneHundredDrawingsForPosition(20);

                        });
                        break;
                    case "Last 100 Draws":
                        button.setOnAction(e -> {
                            clearContents();
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
        loadPastOneHundredDrawingsForPosition(data, i, false);
        loadDataIntoPerspectivePanes();
    }

    private void loadFirstAndSecondElementValuesIntoList(int[] number) {

        firstElement = new int[number.length];
        secondElement = new int[number.length];

        for (int val = 0; val < number.length; val++) {

            String numAsString = Integer.toString(number[val]);
            boolean isTwoCharacters = (numAsString.length() == 2);
            if (isTwoCharacters) {
                String[] numsSplit = numAsString.split("");
                firstElement[val] = Integer.parseInt(numsSplit[0]);
                secondElement[val] = Integer.parseInt(numsSplit[1]);
            } else {

                firstElement[val] = 0;
                secondElement[val] = Integer.parseInt(numAsString);
            }
        }
    }

    private ArrayList<Integer> getPrecidingHitsFromLastDigit( int[] number, boolean isCallFromElementButtonClick ){

        int lastDigit = number[number.length - 1];

        ArrayList<Integer> percedingHitsForLastDigit = new ArrayList<>();

        if( !(isCallFromElementButtonClick) && Integer.toString( lastDigit ).length() > 1)
            lastDigit = Character.getNumericValue( (""+lastDigit).trim().charAt(1) );

        for (int i = 0; i < number.length; i++) {

            if (i < number.length - 1) {

                int numm = ( ( ""+number[i] ).length() > 1) ? Character.getNumericValue((""+number[i]).trim().charAt(1)): number[i];

                if (numm == lastDigit) {
                    int nextNum = number[i + 1];

                    if( !(isCallFromElementButtonClick) && Integer.toString( nextNum ).length() > 1)
                        nextNum = Character.getNumericValue( (""+nextNum).trim().charAt(1) );

                    percedingHitsForLastDigit.add(numm);
                    percedingHitsForLastDigit.add(nextNum);
                }
            }
        }

        percedingHitsForLastDigit.add(lastDigit);

        return percedingHitsForLastDigit;
    }

    private List<Integer> getPrecidingHitsForWholeNumber(int[] number) {

        int lastDigit = number[number.length - 1];
        ArrayList<Integer> percedingHitsForLastDigit = new ArrayList<>();


        for (int i = 0; i < number.length; i++) {

            if (i < number.length - 1) {

                if (number[i] == lastDigit) {

                    int nextNum = number[i + 1];

                    percedingHitsForLastDigit.add(lastDigit);
                    percedingHitsForLastDigit.add(nextNum);
                }
            }
        }

        percedingHitsForLastDigit.add( lastDigit );

        return percedingHitsForLastDigit;

    }

    private void loadPastOneHundredDrawingsForPosition(int[] number, int range, boolean isComingFrom) {

        Integer[] val = null;
        int index = 0;


        List<Integer> percedingHitsForLastDigit = null;

        if( globalInt < 2 )
          percedingHitsForLastDigit = getPrecidingHitsFromLastDigit( number, isComingFrom);
        else
          percedingHitsForLastDigit = getPrecidingHitsForWholeNumber( number );

        if (percedingHitsForLastDigit.size() > 100) {

            val = new Integer[100];
            for (int i = percedingHitsForLastDigit.size() - 100; i < percedingHitsForLastDigit.size(); i++) {

                val[index++] = percedingHitsForLastDigit.get(i);
            }

        } else {

            val = new Integer[100];
            for (int i = number.length - 100; i < number.length; i++) {

                val[index++] = number[i];
            }

        }

        if (positionNumbers != null)
            positionNumbers.clear();

        positionNumbers = FXCollections.observableArrayList(val);
        if (range == 0) {
            setUpChart();
        } else {
            if (range == 20)
                positionNumbers.remove(0, 20);
            else if (range == 40)
                positionNumbers.remove(0, 40);
            else if (range == 60)
                positionNumbers.remove(0, 60);
            else if (range == 80)
                positionNumbers.remove(0, 80);

            setUpChart();
        }

    }



    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;

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
        createDynamicButtons();
        loadPastOneHundredDrawingsForPosition(80);
    }

    public void setUpChart() {

        lineChartWithHover = new LineChartWithHover(game.getGameName() + " Pos " + (valTwo + 1) + " Analysis", positionNumbers);

        chartHbox.getChildren().remove(0);
        chartHbox.getChildren().add(0, lineChartWithHover.getLineChart());

    }

    public void loadDataIntoPerspectivePanes() {

        Map<Integer, Map<String, Object[]>> data;
        Map<Integer, Map<String, Object[]>> firstEle;
        Map<Integer, Map<String, Object[]>> secondEle;

        if (!game.getGameName().equals(LotteryGameConstants.DAILY_PICK_4) && !game.getGameName().equals(LotteryGameConstants.DAILY_PICK_3)) {
            data = NumberAnalyzer.findAverageAndGamesOut(numbers[valTwo], LotteryGameConstants.inflateAverage);
            firstEle = NumberAnalyzer.findAverageAndGamesOut(firstElement, LotteryGameConstants.doNotInflateAverage);
            secondEle = NumberAnalyzer.findAverageAndGamesOut(secondElement, LotteryGameConstants.doNotInflateAverage);
        } else {
            data = NumberAnalyzer.findAverageAndGamesOut(numbers[valTwo], LotteryGameConstants.doNotInflateAverage);
            firstEle = NumberAnalyzer.findAverageAndGamesOut(firstElement, LotteryGameConstants.doNotInflateAverage);
            secondEle = NumberAnalyzer.findAverageAndGamesOut(secondElement, LotteryGameConstants.doNotInflateAverage);
        }

        populateHeadersForBestLastDigit();

        Map<Integer, Integer[]> hitsAndGamesOutatRemainder = NumberAnalyzer.findHitsAndGamesOutForRemainder(secondElement);
        populateRemainderHitsAndGamesOut(hitsAndGamesOutatRemainder);

        Map<Integer, Map<Integer, Integer[]>> lastDigitsToHitInRanges = NumberAnalyzer.findLastDigitsThatLastHitInRanges(secondElement);
        populateLastDigitHitRanges(lastDigitsToHitInRanges);

        for (Map.Entry<Integer, Map<String, Object[]>> dataTwo : data.entrySet()) {

            gameAvgLbl.setText(dataTwo.getKey().toString());

            Map<String, Object[]> dataThree = dataTwo.getValue();
            for (Map.Entry<String, Object[]> value : dataThree.entrySet()) {

                if (value.getKey().equals("Above")) {
                    Object[] valueThree = value.getValue();
                    hitsAboveAvgLbl.setText(valueThree[0].toString());
                    aboveGamesOutHitsLbl.setText(valueThree[1].toString());
                    setUpTextFlowInformation(valueThree, 0);
                } else if (value.getKey().equals("Below")) {

                    Object[] valueThree = value.getValue();
                    hitsBelowAvgLbl.setText(valueThree[0].toString());
                    belowGamesOutHitsLbl.setText(valueThree[1].toString());
                    setUpTextFlowInformation(valueThree, 1);
                }
            }
        }

        for (Map.Entry<Integer, Map<String, Object[]>> dataTwo : firstEle.entrySet()) {

            elementOneLbl.setText(dataTwo.getKey().toString());

            Map<String, Object[]> dataThree = dataTwo.getValue();
            for (Map.Entry<String, Object[]> value : dataThree.entrySet()) {

                if (value.getKey().equals("Above")) {
                    Object[] valueThree = value.getValue();
                    elementOneHits.setText(valueThree[0].toString());
                    elementOneAboveGamesOut.setText(valueThree[1].toString());
                } else if (value.getKey().equals("Below")) {

                    Object[] valueThree = value.getValue();
                    belowTotalHitsEleOne.setText(valueThree[0].toString());
                    belowGamesOutEleOne.setText(valueThree[1].toString());
                }
            }
        }

        for (Map.Entry<Integer, Map<String, Object[]>> dataTwo : secondEle.entrySet()) {

            elementTwoLbl.setText(dataTwo.getKey().toString());

            Map<String, Object[]> dataThree = dataTwo.getValue();
            for (Map.Entry<String, Object[]> value : dataThree.entrySet()) {

                if (value.getKey().equals("Above")) {
                    Object[] valueThree = value.getValue();
                    elementTwoAboveHits.setText(valueThree[0].toString());
                    elementTwoAboveGamesOut.setText(valueThree[1].toString());
                } else if (value.getKey().equals("Below")) {

                    Object[] valueThree = value.getValue();
                    elementTwoBelowHits.setText(valueThree[0].toString());
                    elementTwoBelowGamesOut.setText(valueThree[1].toString());
                }
            }
        }

        // Set up the Betslip panel
        setUpPanelTwo();
    }

    private void clearContents() {
        gridPaneHbox.getChildren().clear();
        columnHeaderBox.getChildren().clear();
    }

    private void setUpPanelTwo() {

        LottoBetSlipDefinitions betSlipDefinitions = new LottoBetSlipDefinitions();
        Map<String, Object[]> data = betSlipDefinitions.getPayslipOrientation(game.getGameName());
        betSlipDefinitions.lodMatrixIntoMap(game.getGameName());

        GridPane betSlipPanel = new GridPane();

        determineColumnsNumbersHitIn(numbers[valTwo], betSlipDefinitions.getGamePaySlip(game.getGameName()));

        Text[][] textValues = null;
        String[] columnHeaders = null;

        for (Map.Entry<String, Object[]> dat : data.entrySet()) {

            Object[] va = dat.getValue();
            textValues = new Text[(int) va[1]][];
            break;
        }

        int count = 0;
        int countTwo = 0;
        columnHeaders = new String[textValues.length];

        for (Map.Entry<String, Object[]> d : data.entrySet()) {

            Object[] values = d.getValue();
            Text[] textArray = new Text[((int[]) values[0]).length];
            int[] array = (int[]) values[0];
            for (int i = 0; i < array.length; i++) {

                Text text = new Text(Integer.toString(array[i]));
                text.setFill(Color.valueOf("#dac6ac"));
                textArray[count++] = text;
            }
            columnHeaders[countTwo] = d.getKey();
            textValues[countTwo++] = textArray;
            count = 0;
        }

        if (game.getGameName().equals(LotteryGameConstants.DAILY_PICK_4) || game.getGameName().equals(LotteryGameConstants.DAILY_PICK_3))
            columnHeaderBox.setSpacing(30);
        else if (game.getGameName().equals(LotteryGameConstants.POWERBALL))
            columnHeaderBox.setSpacing(6);
        else
            columnHeaderBox.setSpacing(40);

        columnHeaderBox.setPadding(new Insets(20, 0, 0, 0));
        for (int row = 0; row < textValues.length; row++) {

            Label label = new Label();
            label.setText(columnHeaders[row]);
            label.setTextFill(Paint.valueOf("#ff9e0e"));

            for (int col = 0; col < textValues[row].length; col++) {
                betSlipPanel.add(textValues[row][col], row, col);
            }

            columnHeaderBox.getChildren().add(row, label);
        }

        if (game.getGameName().equals(LotteryGameConstants.POWERBALL))
            betSlipPanel.setHgap(7);
        else
            betSlipPanel.setHgap(40);

        betSlipPanel.setVgap(10);
        gridPaneHbox.getChildren().add(0, betSlipPanel);

        initializeRemainderRadioButtons(betSlipPanel);
        setUpStatViewForColumnHitsAndRemainderDue(betSlipPanel, numbers[valTwo], countTwo);
        findLineRetracementProbability(numbers[valTwo]);
    }

    private void findLineRetracementProbability(int[] number) {

        Map<String, Integer[]> data = LineRetracementAnalyzer.findAboveAndBelowHitsForPrecedingDigit(number);
        Map<Integer,Integer[]> dataTwo = LineRetracementAnalyzer.calculateLineSpacings();

        populateRetracementHeader(data, LineRetracementAnalyzer.getCurrentWinningNumber(), LineRetracementAnalyzer.getNextWinningNumber());
        Object[] directiondata = LineRetracementAnalyzer.populateAbovePreviousRanges(LineRetracementAnalyzer.getNextWinningNumber(), game);

    }

    private void populateRetracementHeader(Map<String, Integer[]> data, int currentWinningNumber, int prevWinningNumber) {

        // In the entire history when Lotto #7 retraced to Lotto # 10 the below data occured

        String display = "In the entire history when Lotto #" + currentWinningNumber + " retraced to Lotto #" + prevWinningNumber +
                " the below data occurred";
        retracementLbl.setText(display);

        lineAboveLbl.setText("Last Ranges retraced to when greater than " + prevWinningNumber);
        lineBelowLbl.setText("Last Ranges retraced to when lower than " + prevWinningNumber);

        Label[] labels = {abovePrevLbl, belowPrevLbl, equalPrevLbl};
        String[] direction = {"higher than", "lower than", "equal to"};

        String displayHitDirection = "";
        int count = 0;
        for (Map.Entry<String, Integer[]> numData : data.entrySet()) {
            Integer[] hitData = numData.getValue();
            displayHitDirection = hitData[0] + " times the next draw number was " + direction[count] + " lotto #" + prevWinningNumber +
                    " It's currently been " + hitData[1] + " drawings since this happened";
            labels[count++].setText(displayHitDirection);
        }

    }

    private void determineColumnsNumbersHitIn(int[] numbers, int[][] gameMatrix) {

        int[][] positionalNumbers = new int[1][];
        positionalNumbers[0] = numbers;

        // get the matrix for the current game
        NumberAnalyzer.determineColumnsNumbersHitIn(positionalNumbers, gameMatrix);
    }

    private void setUpStatViewForColumnHitsAndRemainderDue(GridPane betSlipPanel, int[] number, int num) {

        VBox vBox = new VBox();
        if (game.getGameName().equals(LotteryGameConstants.DAILY_PICK_4))
            vBox.setPadding(new Insets(0, 0, 0, 167));

        else if (game.getGameName().equals(LotteryGameConstants.DAILY_PICK_3))
            vBox.setPadding(new Insets(0, 0, 0, 220));

        else if (game.getGameName().equals(LotteryGameConstants.POWERBALL))
            vBox.setPadding(new Insets(0, 0, 0, 105));

        else
            vBox.setPadding(new Insets(0, 0, 0, 135));

        gridPaneHbox.getChildren().add(vBox);

        Map<String, Object[]> results = findhitsInEachGameColumn(betSlipPanel, number);
        if (results.size() < num)
            addMoreKeysToMap(results, num);

        for (int i = 0; i < num; i++) {
            HBox box = new HBox();
            Label label = new Label("C" + (i + 1));
            label.setPrefWidth(57);
            label.textFillProperty().setValue(Color.valueOf("#dac6ac"));
            box.getChildren().add(label);

            Object[] data = results.get(label.getText());

            label = new Label(data[0].toString());
            label.setPrefWidth(65);
            label.setTextFill(Color.WHITE);
            box.getChildren().add(label);

            label = new Label(data[1].toString());
            label.setPrefWidth(100);
            label.setTextFill(Color.valueOf("#ff9e0e"));
            box.getChildren().add(label);

            label = new Label(data[2].toString());
            label.setTextFill(Color.valueOf("#10000C"));
            box.getChildren().add(label);

            vBox.getChildren().add(box);
        }

        Separator separator = new Separator();
        separator.paddingProperty().setValue(new Insets(10, 0, 0, 0));
        separator.setPrefWidth(columnHeaderBox.getPrefWidth() - 30);
        vBox.getChildren().add(separator);

        Map<Integer, Integer[]> remainderData = NumberAnalyzer.findRemainderDueToHitInPosition(numbers[valTwo]);

        HBox remainderAndColoumnbox = new HBox();
        VBox b = new VBox();


    }

    private void generateCombinations(int[][] numberForPlay) {


    }

    private void addMoreKeysToMap(Map<String, Object[]> results, int count) {

        Set<Integer> keys = new TreeSet<>();

        for (String res : results.keySet()) {
            String[] resTwo = res.split("C");
            keys.add(Integer.parseInt(resTwo[1]));
        }

        List<Integer> newKeys = new ArrayList<>(keys);
        Collections.sort(newKeys);

        int dif = Math.abs(count - newKeys.size());
        int diff = 0;
        for (int i = 0; i < newKeys.size() + dif; i++) {

            if (i == 0) {
                int val = newKeys.get(i);
                if (val > 1)
                    newKeys.add(0, 1);
            }
            if (i < newKeys.size() - 1) {
                diff = Math.abs(newKeys.get(i) - newKeys.get(i + 1));
            }

            if (diff > 1) {

                int min = newKeys.get(i);
                int max = newKeys.get(i + 1);

                for (int k = min; k < max - 1; k++) {
                    int index = newKeys.indexOf(newKeys.get(min - 1));
                    index++;
                    newKeys.add(index, ++k);
                }

                Collections.sort(newKeys);
            }
        }

        int additionalNumbers = Math.abs(newKeys.size() - count);
        if (additionalNumbers > 0) {
            for (int n = 0; n < additionalNumbers; n++) {
                int incrementLastIndex = newKeys.get(newKeys.size() - 1);
                incrementLastIndex++;
                newKeys.add(incrementLastIndex);
            }
        }

        for (int num : newKeys) {

            String key = "C" + num;

            if (!results.containsKey(key)) {
                results.put(key, new Object[]{0, 0, 0});
            }
        }
    }

    private Map<String, Object[]> findhitsInEachGameColumn(GridPane betSlipPanel, int[] numbers) {

        Map<String, Object[]> result = new LinkedHashMap<>();
        // int lastNumToHit = numbers[numbers.length - 1];

        for (int number = 0; number < numbers.length; number++) {

            // if (numbers[number] == lastNumToHit) {

            //if (number < numbers.length - 1) {

            int nextNum = numbers[number];

            for (Node node : betSlipPanel.getChildren()) {

                if (node instanceof Text) {

                    int num = Integer.parseInt(((Text) node).getText());

                    if (num == nextNum) {
                        int column = GridPane.getColumnIndex(node);
                        String columnString = "C" + (column + 1);

                        if (!result.containsKey(columnString)) {

                            result.put(columnString, new Object[]{1, 0, 0});
                            NumberAnalyzer.incrementGamesOut(result, columnString);
                        } else {
                            Object[] data = result.get(columnString);
                            data[0] = (int) data[0] + 1;
                            data[2] = data[1];
                            data[1] = 0;
                            NumberAnalyzer.incrementGamesOut(result, columnString);
                        }

                        break;
                    }
                }
            }
            // }
            // }
        }

        return result;
    }

    private void initializeRemainderRadioButtons(GridPane panel) {
        ToggleGroup group = remainder0Radio.getToggleGroup();
        ObservableList<Toggle> toggles = group.getToggles();

        for (Toggle toggle : toggles) {

            if (toggle instanceof RadioButton) {

                RadioButton button = (RadioButton) toggle;
                button.setStyle("-fx-focus-traversable: false;");

                switch (button.getText()) {
                    case "Remainder 0":
                        button.setOnAction(event -> {
                            if (button.isSelected())
                                searchPanelForRemainder(panel, 0);
                        });
                        break;
                    case "Remainder 1":
                        button.setOnAction(event -> {
                            if (button.isSelected())
                                searchPanelForRemainder(panel, 1);
                        });
                        break;
                    case "Remainder 2":
                        button.setOnAction(event -> {
                            if (button.isSelected())
                                searchPanelForRemainder(panel, 2);
                        });
                        break;
                    default:
                        break;

                }
            }
        }
    }

    private void searchPanelForRemainder(GridPane panel, int i) {

        ObservableList<Node> values = panel.getChildren();
        for (Node node : values) {
            if (node instanceof Text) {
                int val = Integer.parseInt(((Text) node).getText());
                int remainder = val % 3;
                if (remainder == i) {
                    ((Text) node).setFill(Color.valueOf("#ff9e0e"));
                } else {
                    ((Text) node).setFill(Paint.valueOf("#dac6ac"));
                }
            }
        }
    }

    private void populateLastDigitHitRanges(Map<Integer, Map<Integer, Integer[]>> lastDigitsToHitInRanges) {
        Label[] labels = {digit0Hitslbl, digit0HitsGamesOutLbl, digit3Hitslbl, digit3HitsGamesOutLbl, digit6Hitslbl, digit6HitsGamesOutLbl,
                digit9Hitslbl, digit9HitsGamesOutLbl, digit1Hitslbl, digit1HitsGamesOutLbl, digit4Hitslbl, digit4HitsGamesOutLbl,
                digit7Hitslbl, digit7HitsGamesOutLbl, digit2Hitslbl, digit2HitsGamesOutLbl, digit5Hitslbl, digit5HitsGamesOutLbl,
                digit8Hitslbl, digit8HitsGamesOutLbl};

        int count = 0;

        for (Map.Entry<Integer, Map<Integer, Integer[]>> data : lastDigitsToHitInRanges.entrySet()) {
            Map<Integer, Integer[]> dataTwo = data.getValue();
            for (Map.Entry<Integer, Integer[]> d : dataTwo.entrySet()) {
                labels[count++].setText(d.getValue()[0].toString());
                labels[count++].setText(d.getValue()[1].toString());
            }
        }
    }

    private void populateRemainderHitsAndGamesOut(Map<Integer, Integer[]> hitsAndGamesOutatRemainder) {

        int count = 0;
        Label[] labels = {remainder0Hits, remainder0GamesOut, remainder0PrevGamesOut,
                remainder1Hits, remainder1GamesOut, remainder1PrevGamesOut, remainder2Hits, remainder2GamesOut, remainder2PrevGamesOut};

        for (Map.Entry<Integer, Integer[]> data : hitsAndGamesOutatRemainder.entrySet()) {
            Integer[] dataTwo = data.getValue();
            labels[count++].setText(dataTwo[0].toString());
            labels[count++].setText(dataTwo[1].toString());
            labels[count++].setText(dataTwo[2].toString());
        }
    }

    private void populateHeadersForBestLastDigit() {

        Map<Integer, String> header = new TreeMap<>();

        int[] ints = new int[10];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = i;
        }

        for (int num : ints) {
            int remainder = num % 3;

            if (!header.containsKey(remainder)) {
                String res = "( " + num;
                header.put(remainder, res);
            } else {
                String res = header.get(remainder);
                res += ", " + num;
                header.put(remainder, res);
            }
        }

        int count = 0;
        Label[] labels = {remainder0lbl, remainder1lbl, remainder2lbl};
        for (Map.Entry<Integer, String> value : header.entrySet()) {

            String res = header.get(value.getKey());
            res += " )";
            header.put(value.getKey(), res);
            labels[count].setFont(Font.font("", FontWeight.BOLD, 12.0));
            labels[count++].setText(header.get(value.getKey()));

        }

    }

    private void setUpTextFlowInformation(Object[] valueThree, int index) {

        TextFlow[] d = {aboveText, belowText};
        String direction = (index == 0) ? "Above" : "Below";
        String lesserorhigher = (index == 0) ? "greater" : "lesser";

        for (int i = 0; i < 1; i++) {

            if (d[index].getChildren().size() > 0)
                d[index].getChildren().clear();

            Text t = new Text(String.format("When the %1s average hit counter is ", direction));
            t.setFill(Color.valueOf("#dac6ac"));

            Text tt = new Text(valueThree[1].toString());
            tt.setFill(Color.BLACK);

            Text ttt = new Text(String.format(" games out. The next draw number was %1s than the position average ", lesserorhigher));
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
