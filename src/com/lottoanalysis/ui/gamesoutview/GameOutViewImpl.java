package com.lottoanalysis.ui.gamesoutview;

import com.lottoanalysis.common.MenuBarHelper;
import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DrawPositions;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.GameOutPresenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameOutViewImpl extends BaseView<GameOutPresenter> implements GameOutView {

    private MenuBar menuBar;
    private Label positionLabel;

    private int gameRange;
    private String gameName;
    private int gameMaxValue;

    public GameOutViewImpl(){
        super.setPrefWidth(1270);
        super.setPrefHeight(770);
        super.setStyle("-fx-background-color:#515B51;");

        this.menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color:#dac6ac;");

        this.positionLabel = new Label();

    }

    public Label getPositionLabel() {
        return positionLabel;
    }

    @Override
    public void setUpUi() {
        getPresenter().handleViewEvent("load");

        buildMenuBar();
        buildHeader();
        buildScene();
    }

    private void buildScene() {

        HBox hBox = new HBox();
        VBox infoVBox = new VBox();

        AnchorPane.setBottomAnchor(hBox,5.0);
        AnchorPane.setLeftAnchor(hBox,5.0);
        AnchorPane.setRightAnchor(hBox,5.0);
        AnchorPane.setTopAnchor(hBox,65.0);

        hBox.setStyle("-fx-background-color: black;");

        for(int i = 0; i < 3; i++){
            StackPane pane = new StackPane();
            pane.setStyle("-fx-background-color: white");
            pane.setPrefWidth(600);
            pane.setPrefHeight(300);
            infoVBox.getChildren().add( pane );
        }

        infoVBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: red;");
        HBox.setHgrow(vBox, Priority.ALWAYS);
        HBox.setMargin(infoVBox,new Insets(0,0,.5,0));

        setUpChartPanes( vBox );

        hBox.getChildren().addAll( infoVBox, vBox );
        hBox.setSpacing(10);
        getChildren().add( hBox);
    }

    private void setUpChartPanes(VBox vBox) {

        vBox.setSpacing(10);
        for(int i = 0; i < 3; i++){
            StackPane stackPane = new StackPane();
            stackPane.setStyle("-fx-background-color: white;");

            switch (i){
                case 0:
                    stackPane.setPrefWidth( vBox.getPrefWidth());
                    stackPane.setPrefHeight(300);
                    vBox.getChildren().add( stackPane );
                    break;
                case 1:
                    stackPane.setPrefWidth( vBox.getPrefWidth() );
                    stackPane.setPrefHeight(200);
                    vBox.getChildren().add( stackPane );
                    break;
                case 2:
                    HBox hBox = new HBox();
                    createSideBySidePanes(hBox, vBox.getPrefWidth());
                    vBox.getChildren().add(hBox);
                    break;
            }
        }
    }

    private void createSideBySidePanes(HBox hBox, final double width) {

        hBox.setPrefWidth( width );
        hBox.setStyle("-fx-background-color: green;");
        hBox.setPrefHeight(200);
        hBox.setSpacing(10);
        for(int i = 0; i < 2; i++){
            StackPane stackPane = new StackPane();
            stackPane.setStyle("-fx-background-color: white");
            stackPane.setPrefWidth(400);
            stackPane.setPrefHeight(200);
            hBox.getChildren().add(stackPane);
        }


    }

    @Override
    public AnchorPane display() {
        return this;
    }

    private void setOnGamePositionChange(DrawPositions drawPosition) {

        getPresenter().setDrawPosition( drawPosition );
    }

    public void setGamePositionRange(int range) {
        this.gameRange = range;
    }

    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void setGameMaxValue(int maxValue) {
        this.gameMaxValue = maxValue;
    }

    private void buildMenuBar() {

        final Menu drawPositionMenu = buildPositionMenu();
        final Menu analyzeMethodMenu = buildAnalyzeMethodMenu();
        final Menu groupRangeMenu = buildGroupRangeMenu();

        menuBar.getMenus().addAll( drawPositionMenu, groupRangeMenu, analyzeMethodMenu );

        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        getChildren().add(menuBar);
    }

    private Menu buildGroupRangeMenu(){

        final List<Integer> ranges = MenuBarHelper.buildRangeSizeList( gameMaxValue, gameName );
        Menu menu = new Menu( "Group Range" );

        for(Integer integer : ranges){
            MenuItem menuItem = new MenuItem( integer.toString() );
            menuItem.setOnAction(event -> {

                getPresenter().setGroupRange( menuItem.getText() );
            });

            menu.getItems().add( menuItem );
        }

        return menu;
    }

    private Menu buildAnalyzeMethodMenu() {

        Menu menu = new Menu("Analysis Methods");
        List<AnalyzeMethod> analyzeMethods = Arrays.stream( AnalyzeMethod.values() ).collect(Collectors.toList());
        if (gameName.equals(LotteryGameConstants.PICK4_GAME_NAME_TWO) ||
                gameName.equals(LotteryGameConstants.PICK3_GAME_NAME_TWO)) {
            analyzeMethods.remove(AnalyzeMethod.DELTA_NUMBERS);
        }

        for(AnalyzeMethod analyzeMethod : analyzeMethods){

            MenuItem menuItem = new MenuItem( analyzeMethod.getTitle() );
            menuItem.setOnAction( event -> getPresenter().setAnalyzeMethod( analyzeMethod ));
            menu.getItems().add( menuItem );
        }

        return menu;
    }

    private Menu buildPositionMenu() {

        Menu menu = new Menu("Draw Positions");
        for(DrawPositions drawPosition : DrawPositions.values()){

            if( (drawPosition.getIndex() + 1) <= gameRange){

                MenuItem menuItem = new MenuItem( drawPosition.getText() );
                menuItem.setOnAction( event -> {

                    DrawPositions position = Arrays.stream( DrawPositions.values() )
                            .filter( val -> val.getText().equals( menuItem.getText() )).findAny().orElse(null);

                    setOnGamePositionChange(position);

                });
                menu.getItems().add( menuItem );
            }
        }

        return menu;
    }


    private void buildHeader() {

        HBox headerHbox = new HBox();

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        Label pageAndGameTitle = new Label();
        pageAndGameTitle.textProperty().bind( new SimpleStringProperty(

                String.format("Game Out View Chart: %s", gameName)
        ));
        pageAndGameTitle.setFont(Font.font(20.0));
        pageAndGameTitle.setStyle("-fx-text-fill:#dac6ac;");

        positionLabel.setFont(Font.font(20.0));
        positionLabel.setStyle("-fx-text-fill:#dac6ac;");

        headerHbox.getChildren().addAll(pageAndGameTitle, region, positionLabel);

        AnchorPane.setTopAnchor(headerHbox, 25.0);
        AnchorPane.setLeftAnchor(headerHbox, 5.0);
        AnchorPane.setRightAnchor(headerHbox,5.0);

        Pane dividerPane = new Pane();
        dividerPane.setPrefHeight(3.0);
        dividerPane.setStyle("-fx-background-color:#EFA747;");

        AnchorPane.setTopAnchor(dividerPane, 54.0);
        AnchorPane.setLeftAnchor(dividerPane, 5.0);
        AnchorPane.setRightAnchor(dividerPane,5.0);

        getChildren().addAll(headerHbox, dividerPane);
    }
}
