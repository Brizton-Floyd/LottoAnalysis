package com.lottoanalysis.services.dashboardservices;

import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.models.dashboard.ModifiedDrawModel;
import com.lottoanalysis.models.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.models.factories.enums.Databases;
import com.lottoanalysis.models.factories.enums.Factory;
import com.lottoanalysis.models.factories.factoryproducer.FactoryProducer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LottoDashboardRepositoryImpl implements LottoDashboardRepository {

    @Override
    public boolean update(ModifiedDrawModel modifiedDrawModel) {

        AbstractFactory dbFactory = FactoryProducer.getFactory(Factory.DataBaseFactory);
        Database database = dbFactory.getDataBase(Databases.MySql);

        try {

            final Map.Entry<List<Integer>, String> entry = buildSqlString(modifiedDrawModel);

            try (Connection connection = database.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(entry.getValue())) {

                for(int i = 0; i < modifiedDrawModel.getDrawPositions().size(); i++){

                    preparedStatement.setString(i+1, modifiedDrawModel.getDrawPositions().get(i).get());

                }

                preparedStatement.setInt(entry.getKey().get( entry.getKey().size() -1), modifiedDrawModel.getId());
                preparedStatement.executeUpdate();

                return true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Map.Entry<List<Integer>, String> buildSqlString(ModifiedDrawModel modifiedDrawModel) {

        StringBuilder builder = new StringBuilder();
        Map<List<Integer>, String> data = new LinkedHashMap<>();
        List<Integer> placeHolderValues = new ArrayList<>();

        Map<Integer, String> vals = colName.entrySet().stream()
                .filter(integer -> integer.getKey() <= modifiedDrawModel.getDrawPositions().size())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));

        builder.append("UPDATE DRAWING SET ");

        for (Map.Entry<Integer, String> entry : vals.entrySet()) {

            placeHolderValues.add(entry.getKey());
            builder.append(entry.getValue()).append(" = ?,").append(" ");
        }

        placeHolderValues.add(placeHolderValues.get(placeHolderValues.size() - 1) + 1);
        builder.setCharAt(builder.lastIndexOf(","), ' ');
        builder.append("WHERE ID = ?");

        data.put(placeHolderValues, builder.toString());

        return data.entrySet().iterator().next();
    }


    private Map<Integer, String> colName;

    {
        colName = new LinkedHashMap<>();
        colName.put(1, "DRAW_POS_ONE");
        colName.put(2, "DRAW_POS_TWO");
        colName.put(3, "DRAW_POS_THREE");
        colName.put(4, "DRAW_POS_FOUR");
        colName.put(5, "DRAW_POS_FIVE");
        colName.put(6, "BONUS_NUMBER");
    }

}
