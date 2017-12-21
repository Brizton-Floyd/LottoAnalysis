package com.lottoanalysis.chartanalysis;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.utilities.ChartHelper;
import com.lottoanalysis.utilities.ChartHelperTwo;
import com.lottoanalysis.utilities.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class GroupChartController {

    private LotteryGame lotteryGame;
    private int[][] drawPositionalNumbers;
    private Map<Integer, String> data;

    private static int globalDrawPosition,rowIndex;

    @FXML
    private VBox groupRadioButtonVbox;

    @FXML
    private HBox drawPositionHbox, radioBtnAndChartHbox, headerHbox;

    @FXML
    private MenuButton groupSizeMenuButton;

    @FXML
    private Label lblGame, lblAnalyzedPosition,groupHitOutlookLabel;

    @FXML
    private GridPane groupGridPane;

    @FXML
    public void initialize() {
        clearUnDynamicContent();
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
        Node node = radioBtnAndChartHbox.getChildren().get(0);
        radioBtnAndChartHbox.getChildren().setAll(node);
    }

    private void clearVbox() {
        ((VBox) radioBtnAndChartHbox.getChildren().get(0)).getChildren().clear();
    }

    public void initFields(LotteryGame game, int[][] drawPositionalNumbers) {

        setLotteryGame(game);
        setDrawPositionalNumbers(drawPositionalNumbers);

    }

    public void startSceneLayoutSequence() {

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
            menuItem.setOnAction(e -> retrieveDataForChartViewing(globalDrawPosition, Integer.parseInt(menuItem.getText())));
        });
    }

    private void insertPositionRadioButtonsIntoHbox() {

        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < drawPositionalNumbers.length; i++) {

            RadioButton button = new RadioButton("Position " + data.get(i));
            final int position = i;

            button.setOnAction(e -> {
                setGlobalDrawPosition(position);
                setPositionBeingAnalyzedLabel();
                retrieveDataForChartViewing(position, 10);
            });
            button.setStyle("-fx-text-fill: #dac6ac;");

            if (i == 0)
                button.setSelected(true);

            drawPositionHbox.getChildren().add(button);
            group.getToggles().add(button);
        }

        setGlobalDrawPosition(0);
        setPositionBeingAnalyzedLabel();
        retrieveDataForChartViewing(0, 10);
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

        clearVbox();
        lblGame.setText("Group Chart Analysis: " + lotteryGame.getGameName());
        groupHitOutlookLabel.setText("Group Hit Outlook Position " + data.get(globalDrawPosition));

        int[] drawingPos = drawPositionalNumbers[drawPosition];

        ChartHelperTwo.clearGroupHitInformation();
        ChartHelperTwo.processIncomingData(lotteryGame, drawingPos, drawSize);

        Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();

        setupChartForViewing(positionData);
    }

    private void setupChartForViewing(Map<String, Object[]> positionData) {

        ToggleGroup group = new ToggleGroup();
        int count = 0;
        for (Iterator<String> iterator = positionData.keySet().iterator(); iterator.hasNext(); ) {

            RadioButton button = new RadioButton(iterator.next());
            button.setOnAction(event -> {
                injectChartWithData(positionData,button.getText());
                setUpGroupHitGridPane(positionData);
                CompanionNumberFinder.analyzeIncomingInformation(
                      ChartHelperTwo.extractAppropriatePosition( positionData, button.getText()) 
                );
            });

            if(count == 0) {
                button.setSelected(true);
                count++;
            }

            button.setStyle("-fx-text-fill: #dac6ac;");
            VBox box = (VBox) radioBtnAndChartHbox.getChildren().get(0);
            box.setSpacing(2);
            box.getChildren().add(button);

            group.getToggles().add(button);
        }

        injectChartWithData(positionData,((RadioButton)group.getToggles().get(0)).getText());
        setUpGroupHitGridPane(positionData);
        CompanionNumberFinder.analyzeIncomingInformation(ChartHelperTwo.extractAppropriatePosition( positionData, "1"));
    }

    private void setUpGroupHitGridPane(Map<String, Object[]> positionData) {

        List<Node> nodesToRemove = new ArrayList<>();
        groupGridPane.setVgap(2);
        for(int i = 9; i < groupGridPane.getChildren().size(); i++){

            nodesToRemove.add( groupGridPane.getChildren().get(i));

        }

        for(Node node : nodesToRemove){
            groupGridPane.getChildren().remove(node);
        }

        Node[][] gameData = new Node[positionData.size()][9];

        positionData.forEach( (key,value) -> {
            int rowIndex= getRowIndex();
            int colIndex=0;

            Node[] d = gameData[rowIndex];
            d[colIndex] = new Label(key);
            d[++colIndex] = new Label(value[1]+"");
            d[++colIndex] = new Label(value[2]+"");
            d[++colIndex] = new Label(value[3]+"");
            d[++colIndex] = new Label(value[5]+"");
            d[++colIndex] = new Label(value[6]+"");
            d[++colIndex] = new Label(value[7]+"");
            d[++colIndex] = new Label(value[8]+"");
            d[++colIndex] = new Label(value[9]+"");

            setRowIndex( rowIndex + 1 );

        });

        for(int i = 0; i < gameData.length; i++){

            for(int j = 0; j < gameData[i].length; j++){

                gameData[i][j].setStyle("-fx-text-fill: #dac6ac;");
                groupGridPane.add(gameData[i][j],j,(i+1));
            }
        }

        setRowIndex(0);
    }

    @SuppressWarnings("unchecked")
    private void injectChartWithData(Map<String, Object[]> positionData, String text) {

        if(radioBtnAndChartHbox.getChildren().size() > 1)
            radioBtnAndChartHbox.getChildren().remove(1);

        String[] colors = {"#FF0000", "#FF0000"};

        List<Integer> numList = (List<Integer>)positionData.get(text)[0];
        List<Integer> numListTwo = new ArrayList<>(numList);
        Collections.sort(numListTwo);

        List<List<Integer>> dataPoints = new ArrayList<>();
        dataPoints.add((numList.size() > 100) ? numList.subList(numList.size()-100,numList.size()) : numList);

        List<Integer> pointTwo = (numList.size() > 0) ? ChartHelper.getListOfNumbersBasedOnCurrentWinningNumber(numList) : new ArrayList<>();

        if(pointTwo.size() > 0)
            dataPoints.add((pointTwo.size() > 100) ? pointTwo.subList(pointTwo.size() - 30, pointTwo.size()) : pointTwo);

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
                maxValue, unique.toString(), text,1130,293);

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
