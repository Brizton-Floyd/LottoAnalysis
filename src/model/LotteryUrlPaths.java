package model;

import java.util.HashMap;
import java.util.Map;

/**
 * class will hold all the constants that contain the url to file on the internet
 */
public class LotteryUrlPaths {

    private final String FANTASY_FIVE_PATH = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=fantasy-5&Order=Yes";
    private final String POWERBALL = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=powerball&Order=Yes";
    private final String MEGA_MILLIONS = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=mega-millions&Order=Yes";
    private final String PICK_4 = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=daily-4&Order=Yes";
    private final String PICK_3 = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=daily-3&Order=Yes";

    private Map<String, String> lottoFilesPaths = new HashMap<>();

    public LotteryUrlPaths() {
        setPaths();
    }

    private void setPaths() {
        lottoFilesPaths.put("Fantasy Five", FANTASY_FIVE_PATH);
        lottoFilesPaths.put("PowerBall", POWERBALL );
        lottoFilesPaths.put("Mega Millions",MEGA_MILLIONS);
        lottoFilesPaths.put("Daily Pick4", PICK_4);
        lottoFilesPaths.put("Daily Pick3", PICK_3);
    }

    public Map<String, String> getPathFiles() {

        return lottoFilesPaths;
    }
}
