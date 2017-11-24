package com.lottoanalysis.common;

/**
 * Created by briztonfloyd on 9/30/17.
 */
public class LotteryGameConstants {

    /**
     * Private constructor to prevent instansiation
     */
    private LotteryGameConstants(){}

    // Game names
    public static final String DAILY_PICK_4 = "DailyPick4";
    public static final String DAILY_PICK_3 = "DailyPick3";
    public static final String FANTASY_FIVE = "FantasyFive";
    public static final String POWERBALL = "Powerball";
    public static final String MEGA_MILLIONS = "MegaMillions";
    public static final String SUPPER_LOTTO_PLUS = "SuperLottoPlus";

    // Positions in game
    public static final int FIVE_POSITIONS = 5;
    public static final int SIX_POSITIONS = 6;
    public static final int THREE_POSITIONS = 3;
    public static final int FOUR_POSITIONS = 4;

    // Lottery game names
    public static final String FANTASY_FIVE_GAME_NAME = "Fantasy";
    public static final String POWERBALL_GAME_NAME = "Powerball";
    public static final String MEGA_MILLIONS_GAME_NAME = "Mega";
    public static final String PICK4_GAME_NAME = "Pick 4";
    public static final String PICK3_GAME_NAME = "Pick 3";
    public static final String SUPER_LOTTO_PLUS_GAME_NAME = "Super";

    // Lottery database names
    public static final String FANTASY_FIVE_DB_NAME= "fantasy_five_results";
    public static final String POWERBALL_DB_NAME= "powerball_results";
    public static final String MEGA_MILLIONS_DB_NAME= "mega_million_results";
    public static final String PICK3_DB_NAME= "pick3_results";
    public static final String PICK4_DB_NAME= "pick4_results";
    public static final String SUPER_LOTTO_DB_NAME= "super_lotto_results";


    // Average inflate constants
    public static final boolean inflateAverage = true;
    public static final boolean doNotInflateAverage = false;

    // Instructions
    public static final String LOTTO_DASHBOARD_INSTRUCTION_FIRST_PANE = "This section provides you with a set of four numbers " +
            "listed at the bottom of the chart. The first number represents the first bar in the chart, the second represents the second bar and so forth. " +
            "The numbers provided here are a good set of numbers to consider for the next" +
            " drawing since these group of numbers contain the most hits following the current winning number in current position "+
            "being analyzed";

    // URL Paths
    public static final String FANTASY_FIVE_URL = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=fantasy-5&Order=Yes";
    public static final String POWERBALL_URL = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=powerball&Order=Yes";
    public static final String MEGA_MILLIONS_URL = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=mega-millions&Order=Yes";
    public static final String PICK_4_URL = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=daily-4&Order=Yes";
    public static final String PICK_3_URL = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=daily-3&Order=Yes";
    public static final String SUPER_LOTTO_PLUS_URL = "http://www.calottery.com/sitecore/content/Miscellaneous/download-numbers/?GameName=superlotto-plus&Order=Yes";


}
