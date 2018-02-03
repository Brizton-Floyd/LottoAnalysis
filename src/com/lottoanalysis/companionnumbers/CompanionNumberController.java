package com.lottoanalysis.companionnumbers;

import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.*;

@SuppressWarnings("unchecked")
public class CompanionNumberController {

    private int[][] postionalNumbers;
    private LotteryGame game;
    private Map<Integer,String> positionStringNameHolder = new HashMap<>();
    private Map<String,List<Integer>> groupAndNumberHolder = new LinkedHashMap<>();
    @FXML
    private ComboBox postionBox, positionNumbers, positionGroupNumbers, companionNumberGrpups;

    @FXML
    private Label companionPostionLbl, lblAnalyzedPosition,infoLbl, lbl2,lbl3;

    @FXML
    private Button analyzeBtn;

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
    }

    public void setPostionalNumbers(int[][] numbers)
    {
        this.postionalNumbers = numbers;
    }

    public void setGame(LotteryGame game)
    {
        this.game = game;
    }

    public void initComponents()
    {
        setupGameNameLabel();
        plugPostionsIntoPostionComboBox();

        plugGroupRanges();

        List<String> items = positionGroupNumbers.getItems();

        for(String n : items)
            companionNumberGrpups.getItems().add(n);
        companionNumberGrpups.getSelectionModel().selectFirst();

        analyzePositions();
    }

    private void analyzePositions() {

        Map<String,Integer> indexrefHolder = new HashMap<>();
        indexrefHolder.put("Position One",0);
        indexrefHolder.put("Position Two", 1);
        indexrefHolder.put("Position Three",2);
        indexrefHolder.put("Position Four",3);
        indexrefHolder.put("Position Five",4);
        indexrefHolder.put("Position Six",5);

        String currentPos = (String)postionBox.getValue();
        String companionPos = companionPostionLbl.getText();

        
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
        }));

        positionGroupNumbers.getSelectionModel().selectFirst();
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

