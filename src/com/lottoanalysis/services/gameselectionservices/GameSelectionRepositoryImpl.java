package com.lottoanalysis.services.gameselectionservices;

import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.models.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.models.factories.enums.Databases;
import com.lottoanalysis.models.factories.enums.Factory;
import com.lottoanalysis.models.factories.factoryproducer.FactoryProducer;
import com.lottoanalysis.models.gameselection.GameSelectionModel;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.services.gameselectionservices.texttoobjecttask.DrawTextToObjectTask;
import com.lottoanalysis.ui.gameselection.GameSelectionViewListener;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class GameSelectionRepositoryImpl extends Task<Void> implements GameSelectionRepository {

    private GameSelectionViewListener gameSelectionViewListener;

    public GameSelectionRepositoryImpl() {
    }


    public GameSelectionRepositoryImpl(GameSelectionViewListener gameSelectionViewListener) {

        this.gameSelectionViewListener = gameSelectionViewListener;

    }

    @Override
    public void executeGameUpdates() throws ExecutionException, InterruptedException {

        gameSelectionViewListener.unbindDataFromProgressAndMessage();
        gameSelectionViewListener.bindToMessageAndProgress(messageProperty(),progressProperty());

        System.out.println(Thread.currentThread().getName());
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    protected Void call() throws Exception {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        DrawTextToObjectTask drawTextToObjectTask = new DrawTextToObjectTask();
        Future<Map<Integer, List<Drawing>>> futureAllGameDrawMap = executorService.submit(drawTextToObjectTask);

        //updateMessage("Converting file data to draw objects");
        gameSelectionViewListener.showMessageLabel();
        while (!futureAllGameDrawMap.isDone()) {

            updateMessage("Converting file data to draw objects");

        }

        // This will pause all running task until thread completes running
        updateMessage("Conversion task completed!");
        Map<Integer, List<Drawing>> results = futureAllGameDrawMap.get();

        executorService.shutdown();

        updateMessage("Analyzing database for needed updates!!");

        extractIrrelevantDrawingFromList(results);

        AbstractFactory abstractFactory = FactoryProducer.getFactory(Factory.DataBaseFactory);
        Database database = abstractFactory.getDataBase(Databases.MySql);

        int count = 0;
        for(List<Drawing> drawingList : results.values()){
            count += drawingList.size();
        }

        int outerCount = 0;

        gameSelectionViewListener.showProgess();
        for(Map.Entry<Integer,List<Drawing>> entry : results.entrySet()){

            final Map.Entry<String,String> headerAndValueHolder = getInsertDynamicInsertString(entry.getKey());

            String SQL_INSERT = String.format("INSERT INTO DRAWING %s VALUES %s",
                    headerAndValueHolder.getKey(),headerAndValueHolder.getValue());

            try(Connection connection = database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)){

                List<Drawing> drawingList = entry.getValue();
                for(int i = 0; i < drawingList.size(); i++) {

                    Drawing drawing = drawingList.get(i);

                    int indexer = 3;
                    preparedStatement.setInt(1, entry.getKey());
                    preparedStatement.setString(2, drawing.drawDateProperty().get());
                    for(int j = 0; j < drawing.getDrawNumbers().size(); j++){

                        preparedStatement.setString(indexer++, drawing.getDrawNumbers().get(j).get());
                    }

                    updateProgress(outerCount++, count);
                    preparedStatement.setInt(indexer, Integer.parseInt(drawing.drawNumberProperty().get()) );

                    preparedStatement.executeUpdate();
                }

            }


        }

        updateMessage("All files updated successfully!!!");


        return null;
    }

    private void extractIrrelevantDrawingFromList(Map<Integer, List<Drawing>> results) throws SQLException, ClassNotFoundException {

        AbstractFactory abstractFactory = FactoryProducer.getFactory(Factory.DataBaseFactory);
        Database database = abstractFactory.getDataBase(Databases.MySql);

        try(Connection connection = database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DRAW_NUMBER_SQL)){

            for(Map.Entry<Integer,List<Drawing>> entry : results.entrySet()){

                preparedStatement.setInt(1, entry.getKey());

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){

                    final int drawNum = resultSet.getInt("NUMBER");
                    entry.getValue().removeIf(drawing -> drawing.drawNumberProperty().get().equals( Integer.toString( drawNum)));

                }

            }

            connection.close();
        }


    }

    @Override
    protected void succeeded() {

        gameSelectionViewListener.unbindDataFromProgressAndMessage();

        try {
            Thread.sleep(2000);

            gameSelectionViewListener.reloadViewPostUpdate(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populateGameSelectionModel(GameSelectionModel gameSelectionModel) throws Exception {

        Map<String, Integer> gameNameAndIds = new LinkedHashMap<>();

        AbstractFactory databaseFactory = FactoryProducer.getFactory(Factory.DataBaseFactory);
        Database mySqlDb = databaseFactory.getDataBase(Databases.MySql);

        try (Connection connection = mySqlDb.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ALL_GAMES_SQL)) {

            final ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                gameNameAndIds.put(rs.getString("NAME"), rs.getInt("ID"));
            }

        }

        gameSelectionModel.setGameNameAndIds(gameNameAndIds);
    }

    private Map.Entry<String,String> getInsertDynamicInsertString( final int id){
        Map<List<Integer>,Map<String,String>> insertStringHolder = new HashMap<>();

        Map<String,String> v1 = new HashMap<>();
        v1.put("(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, DRAW_POS_FOUR, DRAW_POS_FIVE, BONUS_NUMBER, DRAW_NUMBER)",
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insertStringHolder.put(new ArrayList<>(Arrays.asList(2,3,4)), v1);

        Map<String,String> v2 = new HashMap<>();
        v2.put("(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, DRAW_POS_FOUR, DRAW_POS_FIVE, DRAW_NUMBER)",
                "(?, ?, ?, ?, ?, ?, ?, ?)");
        insertStringHolder.put(new ArrayList<>(Arrays.asList(1)), v2);

        Map<String,String> v3 = new HashMap<>();
        v3.put("(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, DRAW_POS_FOUR, DRAW_NUMBER)",
                "(?, ?, ?, ?, ?, ?, ?)");
        insertStringHolder.put(new ArrayList<>(Arrays.asList(5)), v3);

        Map<String,String> v4 = new HashMap<>();
        v4.put("(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, DRAW_NUMBER)",
                "(?, ?, ?, ?, ?, ?)");
        insertStringHolder.put(new ArrayList<>(Arrays.asList(6)), v4);

        for(Map.Entry<List<Integer>, Map<String,String>> entry : insertStringHolder.entrySet()){
            if(entry.getKey().contains( id )){
                return entry.getValue().entrySet().iterator().next();
            }
        }

        return null;
    }

    private static final String ALL_GAMES_SQL = "SELECT LG.ID AS ID, LG.GAME_NAME AS NAME " +
            " FROM LOTTERY_GAME LG;";

    private static final String DRAW_NUMBER_SQL = "SELECT D.DRAW_NUMBER AS NUMBER FROM DRAWING D WHERE D.LOTTERY_GAME_ID = ?";
}
