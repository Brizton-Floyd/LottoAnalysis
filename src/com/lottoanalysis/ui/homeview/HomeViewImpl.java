package com.lottoanalysis.ui.homeview;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.common.ButtonHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.*;

public class HomeViewImpl extends AnchorPane implements HomeView{

    private HomeViewListener homeViewListener;

    private List<JFXButton> gameOutBtns;
    private List<JFXButton> buttonList = new ArrayList<>();

    HBox box = new HBox();
    private AnchorPane buttonAnchorPane = new AnchorPane();
    private StackPane screenStackPane = new StackPane();

    public HomeViewImpl(){
        setPrefWidth(1432);
        setPrefHeight(753);

        buttonAnchorPane.setStyle("-fx-background-color:black");

        buttonAnchorPane.setPrefWidth(178);
        buttonAnchorPane.setPrefHeight(700);

        screenStackPane.setPrefWidth(1254);
        screenStackPane.setPrefHeight(700);
        screenStackPane.setStyle("-fx-background-color:blue");

        HBox.setHgrow(screenStackPane, Priority.ALWAYS);
        box.getChildren().addAll(buttonAnchorPane,screenStackPane);
        box.setStyle("-fx-background-color:purple");

        getChildren().addAll(box);

        AnchorPane.setLeftAnchor(box,0.0);
        AnchorPane.setTopAnchor(box,0.0);
        AnchorPane.setBottomAnchor(box,0.0);
        AnchorPane.setRightAnchor(box,0.0);

        buildButtons();
        injectScrren();
    }

    private void buildButtons() {

        VBox buttonBox = new VBox();
        List<String> buttonNames = new ArrayList<>();
        buttonNames.add("Lotto Dashboard");

        Map<String,String> buttonAndGlyphNames = new LinkedHashMap<>();
        buttonAndGlyphNames.put("Lotto Dashboard","VIEW_DASHBOARD");
        buttonAndGlyphNames.put("Top Chart Analysis","CHART_LINE");
        buttonAndGlyphNames.put("Group Hit Chart","RAY_START_END");
        buttonAndGlyphNames.put("Companion Numbers","TRENDING_NEUTRAL");
        buttonAndGlyphNames.put("Multiple Chart","TRENDING_UP");
        buttonAndGlyphNames.put("Game Out Chart","TRENDING_UP");
        buttonAndGlyphNames.put("Gap Spacing Chart","TRENDING_UP");
        buttonAndGlyphNames.put("Bet Slip Analysis","TRENDING_UP");
        buttonAndGlyphNames.put("Load Game Panel","TRENDING_UP");

        gameOutBtns = ButtonHelper.buildButtonList( buttonAndGlyphNames, buttonAnchorPane.getPrefWidth() );
        placeActionOnButton( gameOutBtns );

        buttonBox.getChildren().setAll( gameOutBtns );

        buttonAnchorPane.getChildren().setAll(buttonBox);
    }

    private void placeActionOnButton(List<JFXButton> gameOutBtns) {

        for(JFXButton jfxButton : gameOutBtns){

            buttonList.add(jfxButton);

            jfxButton.setOnAction( e -> {

                jfxButton.setDisable(true);

                buttonList.stream().filter( button -> button != jfxButton).forEach( b -> b.setDisable(false));

                switch (jfxButton.getText()){

                    case "Bet Slip Analysis":
                        loadBetSlipAnalysis();
                        break;
                }

            });

        }
    }

    private void injectScrren() {

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color:red");

        screenStackPane.getChildren().setAll(anchorPane);
    }

    @Override
    public void setHomeViewListener(HomeViewListener homeViewListener) {
        this.homeViewListener = homeViewListener;
    }

    @Override
    public void loadBetSlipAnalysis() {
        homeViewListener.executeBetSlipAnalysis();
    }

    @Override
    public void injectView(AnchorPane pane) {
        screenStackPane.getChildren().setAll( pane );
    }

    @Override
    public AnchorPane getView() {
        return this;
    }
}
