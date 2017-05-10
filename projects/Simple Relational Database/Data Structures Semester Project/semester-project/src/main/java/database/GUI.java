package database;
import dataStructures.Table;

import javax.swing.*;
import java.awt.*;
/**
 * GUI that runs the database.
 * Created by mordechaichern on 1/30/16.
 */
public class GUI extends JFrame {
    String userWord = "";
    JTextField userInput = new JTextField(50);
    JButton submit = new JButton("Submit");
    JTextArea myOutput;
    CanExecute executor;
    public GUI(CanExecute executor) {
        super("Database");
        this.executor = executor;
        this.userInput.setPreferredSize(new Dimension(20, 50));
        this.userInput.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
// This center the window on the screen
        submit.addActionListener((e) -> {
            submitAction();
        });
        centerPanel.add(userInput);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        southPanel.add(submit);
        myOutput = new JTextArea(50,70);
        myOutput.setVisible(true);
        southPanel.add(myOutput);
        Box theBox = Box.createVerticalBox();
        theBox.add(Box.createVerticalStrut(10));
        theBox.add(centerPanel);
        theBox.add(Box.createVerticalStrut(10));
        theBox.add(southPanel);
        add(theBox);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
    }
    private void submitAction() {
        userWord = userInput.getText();
        try{
            Table table = executor.execute(userWord);
            myOutput.setText(table.returnTable());
        }
        catch (Exception e){
            myOutput.setText("Query Failed");
        }
    }
    public static void main(String[] args) {
        GUI gui = new GUI(new Database());
        gui.setVisible(true);
    }
}