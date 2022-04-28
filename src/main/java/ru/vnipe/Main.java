package ru.vnipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Копирование БД");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        Controller controller = loader.getController();
        primaryStage.setOnCloseRequest(controller.getCloseEventHandler());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
