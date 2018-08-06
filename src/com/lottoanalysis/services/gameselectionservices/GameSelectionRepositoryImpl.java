package com.lottoanalysis.services.gameselectionservices;

import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.models.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.models.factories.enums.Databases;
import com.lottoanalysis.models.factories.enums.Factory;
import com.lottoanalysis.models.factories.factoryproducer.FactoryProducer;
import com.lottoanalysis.models.gameselection.GameSelectionModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameSelectionRepositoryImpl implements GameSelectionRepository{

    @Override
    public void populateGameSelectionModel(GameSelectionModel gameSelectionModel) throws Exception {

        Map<String,Integer> gameNameAndIds = new LinkedHashMap<>();

        AbstractFactory databaseFactory = FactoryProducer.getFactory(Factory.DataBaseFactory);
        Database mySqlDb = databaseFactory.getDataBase(Databases.MySql);

        try(Connection connection = mySqlDb.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ALL_GAMES_SQL)) {

            final ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                gameNameAndIds.put(rs.getString("NAME"), rs.getInt("ID"));
            }

        }

        gameSelectionModel.setGameName( gameNameAndIds.keySet().iterator().next() );
        gameSelectionModel.setGameNameAndIds( gameNameAndIds );
    }

    private static final String ALL_GAMES_SQL = "SELECT LG.ID AS ID, LG.GAME_NAME AS NAME " +
                                                " FROM LOTTERY_GAME LG;";
}
