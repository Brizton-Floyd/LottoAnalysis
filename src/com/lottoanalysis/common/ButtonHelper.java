package com.lottoanalysis.common;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Pos;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ButtonHelper {

    public static List<JFXButton> buildButtonList( Map<String,String> buttonNames, final double width ){

        List<JFXButton> jfxButtons = new ArrayList<>();

        for(Map.Entry<String,String> entry : buttonNames.entrySet()){

            JFXButton jfxButton = new JFXButton(entry.getKey());
            jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
            jfxButton.setAlignment(Pos.BASELINE_LEFT);
            jfxButton.setPrefWidth( width );
            jfxButton.setPrefHeight(33);
            jfxButton.setFocusTraversable(false);
            jfxButton.setStyle("-fx-text-fill:#dac6ac;");

            MaterialDesignIconView materialDesignIconView = new MaterialDesignIconView();
            materialDesignIconView.setGlyphName(entry.getValue());
            materialDesignIconView.setStyle("-fx-text-fill:#dac6ac;");
            materialDesignIconView.fillProperty().setValue(Paint.valueOf("#dac6ac"));

            jfxButton.setGraphic( materialDesignIconView );

            jfxButton.setOnMouseExited(e -> {
                jfxButton.setStyle("-fx-font-size: 13px; -fx-text-fill:#dac6ac;");
            });
            jfxButton.setOnMouseEntered(e -> {
                jfxButton.setStyle("-fx-font-size: 13px;" +
                        "-fx-background-color: #515B51;" +
                        "-fx-text-fill: #EFA747;");
            });

            jfxButtons.add( jfxButton );
        }

        return jfxButtons;
    }
}
