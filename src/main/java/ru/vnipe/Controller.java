package ru.vnipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class Controller implements Initializable{
    private static DatabaseHandler databaseHandler;
    // при закрытии окна должен срабатывать
    private javafx.event.EventHandler<WindowEvent> closeEventHandler = new javafx.event.EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            //ТУТ НЕОБХОДИМАЯ ЛОГИКА
            databaseHandler.disconnect();
        }
    };

    public javafx.event.EventHandler<WindowEvent> getCloseEventHandler(){
        return closeEventHandler;
    }
    @FXML
    DatePicker beginDate, endDate;

    @FXML
    TextField  nameDataTo;//nameDataFrom,

    @FXML
    ComboBox<String> nameDataFrom;

    @FXML
     ListView<CheckBox> paeses;
    //  CheckBox paes1, paes2, paes3, paes4;

    public void clickCopyButton() {
        databaseHandler.setOldDatabase(nameDataFrom.getValue().trim());
        databaseHandler.setNewDatabase(nameDataTo.getText().trim());
        databaseHandler.setBeginDate(Date.valueOf(beginDate.getValue()).getTime());
        databaseHandler.setEndDate(Date.valueOf(endDate.getValue()).getTime());
        //DatabaseHandler dh = new DatabaseHandler(nameDataFrom.getValue().trim(), nameDataTo.getText().trim(), Date.valueOf(beginDate.getValue()).getTime(), Date.valueOf(endDate.getValue()).getTime());
          ArrayList<CheckBox> paes = new ArrayList<>();

        for (CheckBox c: paeses.getItems()) {
            if (c.isSelected()) paes.add(c);
        }
          if(paes.isEmpty()){
              Alert alert = new Alert(Alert.AlertType.WARNING,"Не выбрана ни одна таблица", ButtonType.OK);
              alert.showAndWait();
          }
          else{
              databaseHandler.setPaes(paes);
              databaseHandler.copyTables();
          }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            Configuration conf = null;

      try{
            // Загрузка файла YAML из папки
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
          //   File file = new File(classLoader.getResource("properties.yaml").getFile());
            File file = new File("properties.yaml");
            // Создание нового ObjectMapper как YAMLFactory
            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            if (file.exists()) {
                conf = om.readValue(file, Configuration.class);
            }
            else {
                conf = new Configuration();
                conf.setDbName("storage");
                conf.setPassword("1111");
                conf.setServer("localhost");
                conf.setPaesQNT(4);
                conf.setPort(3306);
                conf.setUser("root");
                om.writeValue(file, conf);
            }
        }
        catch(IOException e) {
            e.printStackTrace();

        }

        databaseHandler = new DatabaseHandler();
        databaseHandler.setServer(conf.getServer());
        databaseHandler.setHost(conf.getPort());
        databaseHandler.setUserName(conf.getUser());
        databaseHandler.setPassword(conf.getPassword());
        databaseHandler.connect();
        nameDataFrom.setItems(FXCollections.observableArrayList(databaseHandler.readDatabases()));
        nameDataFrom.setValue(conf.getDbName());
        databaseHandler.setOldDatabase(conf.getDbName());
        List<CheckBox> c = databaseHandler.readTables().stream().map(CheckBox::new).collect(Collectors.toList());
        paeses.setItems(FXCollections.observableArrayList(c));
        int i = 0;
        String str = conf.getDbName();
        for (String s: nameDataFrom.getItems()) {
            if (s.startsWith(str)) {
                if (!s.equals(str + i)) {
                    str = str + i;
                }
                else {
                    i++;
                }
            }
        }
        if (str != null) {
            nameDataTo.appendText(str);
        }

    }

    public void dataAction() {
        databaseHandler.setOldDatabase(nameDataFrom.getValue().trim());
        List<CheckBox> c = databaseHandler.readTables().stream().map(CheckBox::new).collect(Collectors.toList());
        paeses.setItems(FXCollections.observableArrayList(c));
        int i = 0;
        String str = databaseHandler.getOldDatabase();
        for (String s: nameDataFrom.getItems()) {
            if (s.startsWith(str)) {
                if (!s.equals(str + i)) {
                    str = str + i;
                }
                else {
                    i++;
                }
            }
        }
        if (str != null) {
            nameDataTo.clear();
            nameDataTo.appendText(str);
        }
    }

}
