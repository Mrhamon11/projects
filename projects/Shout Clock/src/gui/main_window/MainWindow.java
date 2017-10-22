package gui.main_window;

import database.Database;
import gui.Window;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by Avi on 8/5/2017.
 */
public class MainWindow extends Window {


    public static void main(String[] args) {
        new MainWindow();
        launch(args);
    }
//
    public MainWindow(){
        super("MainWindow.fxml");
    }

}
