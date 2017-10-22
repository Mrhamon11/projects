package gui.main_window;

import gui.Window;
import gui.record_window.RecordWindow;
import gui.view_files_window.ViewFilesWindow;
import main.Main;
import player.PlayAudio;
import time.TimeFileLoader;
import time.TimeRetriever;
import time_file_ds.TimeFile;

public class Controller {

    public void getTime(){
        TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false), MainWindow.getDb());
        TimeFile timeFile = tfl.getTimeFile();

        new PlayAudio(timeFile).play();
    }

    public void record(){
        try {
            Window rw = new RecordWindow();
            rw.start(rw.getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewFiles(){
        try {
            Window vfw = new ViewFilesWindow();
            vfw.start(vfw.getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
