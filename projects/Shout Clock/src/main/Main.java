package main;

import database.Database;
import gui.Window;
import gui.main_window.MainWindow;
import javafx.stage.Stage;

import java.util.Stack;

public class Main {
    private static Database db = new Database();
    private static Stack<Window> windows = new Stack<>();
//    private static Stage window = new Stage();

    public static Database getDb() {
        return db;
    }

    public static Stack<Window> getWindows() {
        return windows;
    }

    public static void pushWindow(Window window){
        windows.push(window);
    }

    public static void popWindow(){
        windows.pop();
    }

    public static void main(String[] args) {
        try {
//            MainWindow mw = new MainWindow(args);
//            windows.push(mw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
