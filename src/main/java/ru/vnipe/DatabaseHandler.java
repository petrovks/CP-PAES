package ru.vnipe;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private ConnectDatabaseImpl con;
    private String server;
    private int host;
    private String userName;
    private String password;

    public void setServer(String server) {
        this.server = server;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldDatabase() {
        return oldDatabase;
    }

    public void setOldDatabase(String oldDatabase) {
        this.oldDatabase = oldDatabase;
    }

    public String getNewDatabase() {
        return newDatabase;
    }

    public void setNewDatabase(String newDatabase) {
        this.newDatabase = newDatabase;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    private String oldDatabase, newDatabase;
    private Long   beginDate, endDate;
    private ArrayList<CheckBox> paes = new ArrayList<CheckBox>();

    public ArrayList<CheckBox> getPaes() {
        return paes;
    }

    public DatabaseHandler() {

    }

    public void connect(){
        con = ConnectDatabaseImpl.getInstance();
        con.connect(server, host, userName, password);
    }

    public void setPaes(ArrayList<CheckBox> paes) {
        this.paes = paes;
    }

    public void copyTables() {
        con.createNewDatabase(this.newDatabase);
        con.useDatabase(this.oldDatabase);
        for (CheckBox c: paes) {
            if (c.isSelected()) {
                con.fetchFromTable(this.newDatabase, c.getText(), this.beginDate, this.endDate);
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Копирование завершенно", ButtonType.OK);
        alert.showAndWait();
    }

    public List<String> readDatabases() {
        List<String> list = con.readDatabases();
        return list;
    }

    public List<String> readTables() {
        con.useDatabase(oldDatabase);
        //con.connect(server, host, oldDatabase, userName, password);
        List<String> list = con.readTables();
        return list;
    }

    public void disconnect(){
        con.disconnect();
    }
}
