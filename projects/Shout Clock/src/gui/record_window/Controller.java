package gui.record_window;

import gui.main_window.MainWindow;
import gui.util.WindowControlls;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import player.PlayAudio;
import record.Recorder;
import time_file_ds.TimeFile;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private TextField folderName;
    @FXML
    private ComboBox<String> hour;
    @FXML
    private ComboBox<String> minute;
    @FXML
    private ImageView back;

    public void recordNewFile(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String fn = folderName.getText();
        String hn = hour.getValue();
        String mn = minute.getValue();

        if(fn.isEmpty() || fn == null){
            System.out.println("You must input a folder name");
            //todo make popup
        }
        else if(hn == null){
            System.out.println("You must choose an hour from the menu");
            //todo make popup
        }
        else if(mn == null){
            System.out.println("You must choose a minute from the menu");
            //todo make popup
        }
        else{
            TimeFile newTimeFile = new Recorder(folderName.getText(), hour.getValue(), minute.getValue()).record();

//        System.out.println(">>> Playing back recording...");

            PlayAudio playAudio = new PlayAudio(newTimeFile);
            playAudio.play();
        }
    }

    public void back(){
        WindowControlls.returnToMainWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.hour.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        this.minute.getItems().addAll(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59");
    }
}
