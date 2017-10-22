package gui_test;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Avi on 8/5/2017.
 */
public class RecordWindow {
    private JFrame frame;

    public RecordWindow(){
        makeFrame();
    }

    private void makeFrame(){
        this.frame = new JFrame("Shout Clock: Record New Time");
        Container pane = this.frame.getContentPane();
        JTextField folderName = new JTextField("Folder Name");
        JTextArea hour = new JTextArea("Hour");
        JCheckBox hourBox = new JCheckBox();
        JTextArea minute = new JTextArea("Minute");
        JCheckBox minuteBox = new JCheckBox();

        pane.add(folderName);
        pane.add(hour);
        pane.add(hourBox);
        pane.add(minute);
        pane.add(minuteBox);

        this.frame.pack();
        this.frame.setVisible(true);
    }
}
