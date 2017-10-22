package gui.util;

import gui.Window;
import gui.main_window.MainWindow;

public class WindowControlls {
    public static void returnToMainWindow(){
        try {
            Window mn = new MainWindow();
            mn.start(mn.getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
