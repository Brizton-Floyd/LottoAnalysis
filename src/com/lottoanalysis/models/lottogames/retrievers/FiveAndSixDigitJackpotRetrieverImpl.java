package com.lottoanalysis.models.lottogames.retrievers;

import com.lottoanalysis.interfaces.JackpotRetriever;
import com.lottoanalysis.models.tasks.NotifyingThread;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FiveAndSixDigitJackpotRetrieverImpl extends NotifyingThread implements JackpotRetriever{

    private String amount;
    private static Map<String,String> urlPaths = new HashMap<>();
    private String url;

    static{
        urlPaths.put("CA: Fantasy Five FantasyFive","http://www.calottery.com/play/draw-games/fantasy-5");
        urlPaths.put("CA: Powerball","http://www.calottery.com/play/draw-games/powerball");
        urlPaths.put("CA: Super Lotto Plus SuperLottoPlus","http://www.calottery.com/play/draw-games/superlotto-plus");
        urlPaths.put("CA: Mega Millions MegaMillions","http://www.calottery.com/play/draw-games/mega-millions");
    }

    public FiveAndSixDigitJackpotRetrieverImpl(String game){

        for(Map.Entry<String,String> data : urlPaths.entrySet()){

            if(data.getKey().contains(game)){
                url = data.getValue();
                break;
            }
        }
    }

    @Override
    public String getEstimatedJackpot(String gameName) {
        return amount;
    }

    @Override
    public void doRun() {

        try {

            Document document = Jsoup.connect(url).get();
            Element element = document.selectFirst("#heroImage1 > div.heroContentBox.drawGameHero > h2");
            amount = element.text();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
