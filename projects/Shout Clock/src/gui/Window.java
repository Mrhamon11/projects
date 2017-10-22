package gui;

import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Window extends Application{
    private static Database db = new Database();
    private static Stage window;

    private String fxmlFileName;

    public Window(String fxmlFileName){
        this.fxmlFileName = fxmlFileName;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        if(fxmlFileName.equals("MainWindow.fxml")){
            this.window = primaryStage;
        }
        Parent root = FXMLLoader.load(getClass().getResource(this.fxmlFileName));
        primaryStage.setTitle("Shout Clock");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static Stage getWindow() {
        return window;
    }


    public static Database getDb() {
        return db;
    }
}
