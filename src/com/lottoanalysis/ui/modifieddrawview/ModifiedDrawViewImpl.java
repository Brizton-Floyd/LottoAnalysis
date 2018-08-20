package com.lottoanalysis.ui.modifieddrawview;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.common.NodeUtils;
import com.lottoanalysis.services.dashboardservices.enums.CrudOperation;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.ModifiedDrawPresenter;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.awt.event.MouseEvent;
import java.util.Map;

public class ModifiedDrawViewImpl extends BaseView<ModifiedDrawPresenter> implements ModifiedDrawView, EventHandler<ActionEvent>{

    private GridPane pane = new GridPane();
    private Button update, delete;

    public ModifiedDrawViewImpl(){
        setPrefHeight(300);
        setStyle("-fx-background-color:#515B51;");
        setPrefWidth(400);
    }

    @Override
    public void setUpUi() {

    }

    @Override
    public AnchorPane display() {
        return this;
    }

    @Override
    public void initializeView(String gamNumber, String drawDate, ObservableList<StringProperty> drawPositions) {

        pane.setHgap(10);
        pane.setVgap(10);

        setUpLabelAndTextBoxes(gamNumber, drawDate, drawPositions);
        setUpButtons();

        AnchorPane.setTopAnchor(pane,10.0);
        AnchorPane.setRightAnchor(pane,10.0);
        AnchorPane.setLeftAnchor(pane,10.0);
        AnchorPane.setBottomAnchor(pane,10.0);
    }

    private void setUpButtons() {

        update = new Button("Update");
        update.setOnAction(this);

        update.setPrefWidth(100.0);
        pane.add(update,2,0);

        delete = new Button("Delete");
        delete.setOnAction(this);
        delete.setPrefWidth(100.0);

        pane.add(delete,2,1);

        getChildren().add(pane);
    }

    @Override
    public void handle(ActionEvent event) {

        if(event.getSource() == update){

            Map<Integer,String> valAndKeys = NodeUtils.textValues(pane);

            getPresenter().updateModelList( valAndKeys );
            getPresenter().updateCrudeOpertaion(CrudOperation.UPDATE);
        }
        else if( event.getSource() == delete){

            getPresenter().updateCrudeOpertaion(CrudOperation.DELETE);
        }
    }

    private void setUpLabelAndTextBoxes(String gamNumber, String drawDate, ObservableList<StringProperty> drawPositions) {

        Label drawLabel = new Label("Draw Number");
        drawLabel.setStyle("-fx-text-fill: beige;");

        Label drawDatelbl = new Label("Draw Date");
        drawDatelbl.setStyle("-fx-text-fill: beige;");

        TextField drawNumbersField = new TextField(gamNumber);
        drawNumbersField.setDisable(true);

        TextField drawDateTextFld = new TextField(drawDate);
        drawDateTextFld.setDisable(true);

        pane.add(drawLabel,0,0);
        pane.add(drawNumbersField,1,0);

        pane.add(drawDatelbl,0,1);
        pane.add(drawDateTextFld,1,1);

        int indexer = 2;
        for(int i = 0; i < drawPositions. size(); i++){

            String text = (i < 5 ) ? String.format("Draw Position %s",(i+1) ) : "Bonus";

            Label label = new Label(text);
            label.setStyle("-fx-text-fill: beige;");

            TextField numTextField = new TextField(drawPositions.get(i).get());
            addListeners( numTextField );
            numTextField.setPrefWidth(15.0);

            pane.add(label,0,indexer);
            pane.add(numTextField,1,indexer++);

        }
    }

    private void addListeners(TextField numTextField) {

        numTextField.textProperty().addListener( (observable, oldValue, newValue) -> {

            if(newValue.length() > 2){

                numTextField.setText( newValue.substring(0,2) );

            }
        });
    }

    @Override
    public AnchorPane getView() {
        return this;
    }
}
