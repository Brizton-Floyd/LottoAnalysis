package model.DataFiles;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by briztonfloyd on 8/26/17.
 */
public class DaoConstants {

    public static Map<Integer,String> lotto_query;
    public static List<String> deleteQueries;

    public static final String GAME_ID_QUERY = "SELECT g.game_id, gg.min_number, gg.max_number FROM game g " +
                                                    "INNER JOIN game_min_max gg ON gg.game_id = g.game_id " +
                                                "WHERE game_name = ?";

    public static final String INSERT_GAME_QUERY = "INSERT INTO game(game_name) VALUES(?)";

    public static final String UPDATE_GAME_GAME = "UPDATE game SET game_name = ? " +
                                                  "WHERE game_name = ?";

    public static final String SELECT_ALL_GAMES = "SELECT * From game";

    public static final String DELETE_FANTASY_FIVE = "DELETE From fantasy_five_results";
    public static final String DELETE_POWERBALL = "DELETE From powerball_results";
    public static final String DELETE_MEGA_MILLION = "DELETE From mega_million_results";
    public static final String DELETE_SUPER_LOTTO = "DELETE From super_lotto_results";
    public static final String DELETE_PICK4 = "DELETE From pick4_results";
    public static final String DELETE_PICK3 = "DELETE From pick3_results";

    public static final String FIND_CERTAIN_GAME_QUERY = "SELECT game_id FROM game Where game_id = ?";

    public static final String LOAD_DATA_FOR_GAME_QUERY = "SELECT * FROM ? where game_id = ?" +
            " ORDER BY draw_id desc";

    private static final String INSERT_HISTORICAL_POWERBALL =
            "INSERT OR IGNORE INTO powerball_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five,bonus_number, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

    private static final String INSERT_HISTORICAL_SUPER_LOTTO_PLUS =
            "INSERT OR IGNORE INTO super_lotto_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five,bonus_number, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

    private static final String INSERT_HISTORICAL_MEGA_MILLIONS =
            "INSERT OR IGNORE INTO mega_million_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five,bonus_number, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

    private static final String INSERT_HISTORICAL_FANTASY_FIVE =
            "INSERT OR IGNORE INTO fantasy_five_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_HISTORICAL_PICK_4 =
            "INSERT OR IGNORE INTO pick4_results (draw_number, draw_date, position_one, position_two, position_three, position_four, game_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_HISTORICAL_PICK_3 =
            "INSERT OR IGNORE INTO pick3_results (draw_number, draw_date, position_one, position_two, position_three, game_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";


    public static List<String> getDeleteQueries() {

        if(deleteQueries == null)
            deleteQueries = new ArrayList<>();

        deleteQueries.add(DELETE_FANTASY_FIVE);
        deleteQueries.add(DELETE_MEGA_MILLION);
        deleteQueries.add(DELETE_PICK3);
        deleteQueries.add(DELETE_POWERBALL);
        deleteQueries.add(DELETE_SUPER_LOTTO);
        deleteQueries.add(DELETE_PICK4);

        return deleteQueries;
    }

    public static Map<Integer, String> getLottoQueryString(){

        loadData();

        return lotto_query;
    }

    private static void loadData() {
        lotto_query = new LinkedHashMap<>();
        lotto_query.put(1,INSERT_HISTORICAL_FANTASY_FIVE);
        lotto_query.put(2,INSERT_HISTORICAL_POWERBALL);
        lotto_query.put(3,INSERT_HISTORICAL_MEGA_MILLIONS);
        lotto_query.put(4,INSERT_HISTORICAL_SUPER_LOTTO_PLUS);
        lotto_query.put(5,INSERT_HISTORICAL_PICK_4);
        lotto_query.put(6,INSERT_HISTORICAL_PICK_3);

    }
}
