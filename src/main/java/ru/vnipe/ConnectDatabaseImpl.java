package ru.vnipe;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectDatabaseImpl implements ConnectDatabaseProvider{
    private static ConnectDatabaseImpl instance;

    private ConnectDatabaseImpl() {}

    public static ConnectDatabaseImpl getInstance() {
        if (instance == null) instance = new ConnectDatabaseImpl();
        return instance;
    }
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void fetchFromTable(String database, String table, Long dateFrom, Long dateTo) {
        try {
            if (!table.startsWith("paes")){
                statement.executeUpdate("create table if not exists " + database + "." + table +
                        " select * from " + table + ";");
            }
            else{
                statement.executeUpdate("create table if not exists " + database + "." + table +
                        " select * from " + table + " where time32 >= " + dateFrom/1000 + " AND time32 <= " + dateTo/1000 + ";");
            }

        }
        catch (SQLException s){
            s.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка копирования таблицы: " + table);
            alert.showAndWait();
        }
    }

    public List<String> readDatabases(){
        List<String> listDatabase = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("show databases;");
            while (resultSet.next()) {
                String nameDatabase = resultSet.getString(1);
                listDatabase.add(nameDatabase);
            }
        }
        catch (SQLException s){
            s.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Список баз данных не прочитан!");
            alert.showAndWait();
        }
        return listDatabase;
    }

    public List<String> readTables(){
        List<String> list = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("show tables;");
            while (resultSet.next()) {
                String nameDatabase = resultSet.getString(1);
                list.add(nameDatabase);
            }
        }
        catch (SQLException s){
            s.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Список таблиц не прочитан!");
            alert.showAndWait();
        }
        return list;
    }

    @Override
    public void useDatabase(String name) {
        try {
            resultSet = statement.executeQuery("use " + name + ";");
        } catch (SQLException s) {
            s.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Команда use не выполнена", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @Override
    public void createNewDatabase(String name) {
        try {
            statement.executeUpdate("create database if not exists " + name + ";");
        } catch (SQLException s) {
            s.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "База данных существует. Попробуйте другое название!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @Override
    public void connect(String server, int host, String userName, String password, String database) {
            String url = "jdbc:mysql://" + server + ":" + host + "/" + database;
            try {
                connection = DriverManager.getConnection(url, userName, password);
                statement = connection.createStatement();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно подключиться к БД", ButtonType.CANCEL);
                alert.showAndWait();
                throw new RuntimeException("Невозможно подключиться к БД");
            }
    }

    public void connect(String server, int host, String userName, String password) {
        String url = "jdbc:mysql://" + server + ":" + host + "/" ;
        try {
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно подключиться к БД", ButtonType.CANCEL);
            alert.showAndWait();
            throw new RuntimeException("Невозможно подключиться к БД");
        }
    }

    @Override
    public void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        catch (SQLException s) {
            s.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (SQLException s) {
            s.printStackTrace();
        }

        try {
            if (connection.isClosed()) {
                System.out.println("Отключились от базы данных!");
            }
        }
        catch (NullPointerException | SQLException s) {
            s.printStackTrace();
        }
    }
}
