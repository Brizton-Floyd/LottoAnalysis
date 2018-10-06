package com.lottoanalysis.ui.homeview;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.common.ButtonHelper;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.HomeViewPresenter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.*;

public class HomeViewImpl extends BaseView<HomeViewPresenter> implements HomeView{

    private List<JFXButton> gameOutBtns;
    private List<JFXButton> buttonList = new ArrayList<>();

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
        HBox box = new HBox();
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

    @Override
    public void setUpUi() {

    }

    @Override
    public AnchorPane display() {
        return this;
    }

    private void buildButtons() {

        VBox buttonBox = new VBox();
        List<String> buttonNames = new ArrayList<>();
        buttonNames.add("Lotto Dashboard");

        Map<String,String> buttonAndGlyphNames = new LinkedHashMap<>();
        buttonAndGlyphNames.put("Lotto Dashboard","VIEW_DASHBOARD");
        buttonAndGlyphNames.put("Bet Slip Analysis","TRENDING_UP");
        buttonAndGlyphNames.put("Game Out Chart","TRENDING_UP");
        buttonAndGlyphNames.put("Lottery Number Gaps","CHART_LINE");
        buttonAndGlyphNames.put("Companion Numbers","TRENDING_NEUTRAL");
        buttonAndGlyphNames.put("Position Sequence Chart","RAY_START_END");
        buttonAndGlyphNames.put("Multiple Chart","TRENDING_UP");
        buttonAndGlyphNames.put("Gap Spacing Chart","TRENDING_UP");
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

                buttonList.stream().filter( button -> button != jfxButton).forEach( b -> b.setDisable(false) );

                EventSource eventSource = Arrays.stream(EventSource.values()).filter(ee -> ee.getText().equals( jfxButton.getText() ))
                                                                             .findFirst().orElse(null);
                getPresenter().handleViewEvent(eventSource);

            });

        }
    }

    private void injectScrren() {

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color:red");

        screenStackPane.getChildren().setAll(anchorPane);
    }

    @Override
    public void enableButtonAndDisableDashboardButton() {

        buttonList.forEach(b -> {

            if(b.isDisabled()){
                b.setDisable(false);
            }

            if(b.getText().equals("Lotto Dashboard")){
                b.setDisable(true);
            }
        });


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
