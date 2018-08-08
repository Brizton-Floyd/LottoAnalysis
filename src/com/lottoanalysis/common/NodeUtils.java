package com.lottoanalysis.common;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class NodeUtils {

    public static <T extends Pane> Map<Integer, String> textValues( T parent ){

        return formValues(parent, new HashMap<>());
    }

    private static <T extends Pane> Map<Integer,String> formValues(T parent, HashMap<Integer, String> map) {

        // Do not count any elements that are disabled and or a label in the iteration for the map
        parent.getChildren().removeIf( element -> element instanceof Label || element.isDisabled());

        for(int i = 0; i < parent.getChildren().size(); i++){

            if(parent.getChildren().get(i) instanceof TextField){

                map.put(i, ((TextField)parent.getChildren().get(i)).getText());
            }
        }

        return map;
    }
}
