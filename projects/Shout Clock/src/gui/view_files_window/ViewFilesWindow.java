package gui.view_files_window;

import gui.Window;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewFilesWindow extends Window {
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("ViewFilesWindow.fxml"));
//        primaryStage.setTitle("Shout Clock || Record");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setResizable(false);
//        primaryStage.show();
//    }

    public ViewFilesWindow(){
        super("ViewFilesWindow.fxml");
    }
}