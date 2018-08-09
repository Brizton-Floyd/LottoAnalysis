package com.lottoanalysis.services.gameselectionservices.texttoobjecttask;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.utilities.fileutilities.OnlineFileUtility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Callable;

public class DrawTextToObjectTask implements Callable<Map<Integer,List<Drawing>>> {

    @Override
    public Map<Integer,List<Drawing>> call() throws Exception {

        Map<Integer,List<Drawing>> results = new LinkedHashMap<>();
        final Collection<String> urlPaths = OnlineFileUtility.getUrlPaths().values();

        int indexer = 1;
        for(String url : urlPaths){

            List<Drawing> drawingList = new ArrayList<>();

            final URLConnection urlConnection = new URL( url ).openConnection();

            String line;

            try(BufferedReader br = new BufferedReader(new InputStreamReader( urlConnection.getInputStream()))){

                while((line = br.readLine()) != null){

                    if(validateFirstCharacter(line)) {

                        final Drawing drawing;
                        if(indexer == 6){

                            drawing = convertToDrawing(line,true);
                        }
                        else {
                            drawing =convertToDrawing(line,false);
                        }

                        if(drawing != null)
                            drawingList.add(drawing);

                    }
                }

                results.put(indexer++, drawingList);
            }
        }

        return results;

    }

    private boolean validateFirstCharacter(String line) {

        if(line.length() == 0)
            return false;

        return Character.isDigit(line.charAt(0));
    }

    private Drawing convertToDrawing(String line, final boolean useModulus) {

        String[] results = line.split("[\\s]+");
        final String drawNumber = Arrays.asList(results).subList(0,1).get(0);

        if(useModulus && Integer.parseInt(drawNumber) % 2 == 0)
            return null;

        final String drawDate = String.join(" ", Arrays.asList(results).subList(1,5));

        final String[] drawNumbers = String.join(" ", Arrays.asList(results).subList(5, results.length)).split(" ");

        for(int i = 0; i < drawNumbers.length; i++){

            if(drawNumbers[i].length() == 1){
                drawNumbers[i] = "0" + drawNumbers[i];
            }
        }

        Drawing drawing = new Drawing();
        drawing.setDrawDate(drawDate);

        drawing.setDrawNumber(drawNumber);
        drawing.setDrawNumbers(drawNumbers);

        return drawing;
    }

}
