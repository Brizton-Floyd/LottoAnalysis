package com.lottoanalysis.ui.dashboardview;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.ui.dashboardview.cells.DrawHistoryCell;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.LottoDashBoardPresenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


public class LottoDashBoardViewImpl extends BaseView<LottoDashBoardPresenter> implements LottoDashBoardView {

    private VBox lottoDashBoardHolder = new VBox();
    private HBox headerHolder = new HBox();
    private TableView<Drawing> drawingTableView = new TableView<>();

    private String gameName;
    private String jackPot;

    public LottoDashBoardViewImpl(){
        setPrefWidth(1270);
        setPrefHeight(770);
        setStyle("-fx-background-color:#515B51;");
        getStylesheets().add("./src/com/lottoanalysis/styles/table_view.css");

    }

    @Override
    public void setUpUi() {

    }

    @Override
    public AnchorPane display() {
        return this;
    }

    @Override
    public void initialize() {

        setUpDashboardHeader();
        setUpDividerPane();
        getPresenter().handleViewEvent("inject",null);
    }

    @Override
    public void setGameNameToLabel(String name) {

        this.gameName = name;
    }

    @Override
    public void setJackPotLabel(String jackpot) {
        this.jackPot = jackpot;
    }

    @Override
    public void tableViewRenabled() {
        drawingTableView.setDisable(false);
    }

    @Override
    public void injectDataIntoTable(ObservableList<Drawing> lottoDrawData) {

        drawingTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Drawing,String> dateCol = new TableColumn<>("Draw Date");
        dateCol.setSortable(false);
        dateCol.setCellValueFactory(param -> param.getValue().drawDateProperty());
        dateCol.setCellFactory(param -> new DrawHistoryCell(getPresenter(), drawingTableView));

        TableColumn<Drawing,String> drawCol = new TableColumn<>("Draw Number");
        drawCol.setSortable(false);
        drawCol.setCellValueFactory(param -> param.getValue().drawNumberProperty());
        drawCol.setCellFactory(param -> new DrawHistoryCell(getPresenter(), drawingTableView));

        drawingTableView.getColumns().addAll(drawCol, dateCol );

        for(Drawing drawing : lottoDrawData){

            for(int i = 0; i < drawing.getDrawNumbers().size(); i++){

                final int index = i;
                String label = (i < 5) ? ("Position " + (i+1)) : "Bonus";
                TableColumn<Drawing,String> posCol = new TableColumn<>(label);
                posCol.setCellValueFactory( param -> param.getValue().getDrawNumbers().get(index));
                posCol.setCellFactory( param -> new DrawHistoryCell(getPresenter(), drawingTableView));
                drawingTableView.getColumns().add( posCol );
            }

            break;
        }

        TableColumn<Drawing,String> sumCol = new TableColumn<>("Draw Sum");
        sumCol.setSortable(false);
        sumCol.setCellValueFactory(param -> param.getValue().drawSumProperty());
        sumCol.setCellFactory(param -> new DrawHistoryCell(getPresenter(), drawingTableView));

        TableColumn<Drawing,String> oeCol = new TableColumn<>("Even/Odd");
        oeCol.setSortable(false);
        oeCol.setCellValueFactory(param -> param.getValue().oddEvenRatioProperty());
        oeCol.setCellFactory(param -> new DrawHistoryCell(getPresenter(), drawingTableView));

        drawingTableView.getColumns().addAll(sumCol,oeCol);

        drawingTableView.setItems( lottoDrawData );
        drawingTableView.scrollTo( lottoDrawData.size() -1);
        drawingTableView.getSelectionModel().select( drawingTableView.getItems().size() - 1);
        lottoDashBoardHolder.getChildren().addAll( drawingTableView );


        System.out.println();
    }

    @Override
    public AnchorPane getView() {
        return this;
    }


    private void setUpDividerPane() {
        Pane dividerPane = new Pane();
        dividerPane.setPrefHeight(3.0);
        dividerPane.setStyle("-fx-background-color:#EFA747;");

        AnchorPane.setTopAnchor(dividerPane, 40.0);
        AnchorPane.setLeftAnchor(dividerPane, 5.0);
        AnchorPane.setRightAnchor(dividerPane, 5.0);

        getChildren().add( dividerPane );
    }

    private void setUpDashboardHeader() {
        Label gameLabel = new Label();
        gameLabel.textProperty().bind( new SimpleStringProperty(String.format("Historical Draw Table For: %s", gameName)));
        gameLabel.setFont(Font.font(25.0));
        gameLabel.setStyle("-fx-text-fill:#dac6ac;");

        Label jackpotLabel = new Label();
        jackpotLabel.textProperty().bind( new SimpleStringProperty(String.format("Current Jackpot: %s", jackPot)));
        jackpotLabel.setFont(Font.font(25.0));
        jackpotLabel.setStyle("-fx-text-fill:#dac6ac;");

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        headerHolder.getChildren().setAll( gameLabel, region, jackpotLabel );
        lottoDashBoardHolder.getChildren().add( headerHolder );
        lottoDashBoardHolder.setSpacing(10);
        lottoDashBoardHolder.setFocusTraversable(false);

        AnchorPane.setRightAnchor(lottoDashBoardHolder,5.0);
        AnchorPane.setBottomAnchor(lottoDashBoardHolder,5.0);
        AnchorPane.setLeftAnchor(lottoDashBoardHolder,5.0);
        AnchorPane.setTopAnchor(lottoDashBoardHolder,5.0);

        getChildren().add( lottoDashBoardHolder );
    }
}
