package gui_test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by Avi on 8/5/2017.
 */
public class MainWindow {//extends Application {
    private Stage window;

//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("testGui.fxml"));
//        primaryStage.setTitle("Shout Clock");
//        primaryStage.setScene(new Scene(root, 600, 450));
//        primaryStage.setResizable(false);
//        primaryStage.show();
//    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        this.window = primaryStage;
//        this.window.setTitle("Shout Clock");
//
//
//        Button getTimebutton = new Button("Get Time");
//        getTimebutton.setOnAction( e -> System.out.println("Shouting current time..."));
//        Button recordButton = new Button("Record");
//
//        HBox layout = new HBox();
//        layout.getChildren().addAll(getTimebutton, recordButton);
//        layout.setAlignment(Pos.CENTER);
//
//        BorderPane boderLayout = new BorderPane();
//        boderLayout.setCenter(layout);
//
//        Scene scene = new Scene(boderLayout, 600, 450);
//        this.window.setScene(scene);
//        this.window.show();
//    }

    //    private JFrame frame;
//    private Database db;
//
//    public MainWindow(){
//        makeFrame();
//        this.db = new Database();
//    }
//
//    private void makeFrame(){
//        this.frame = new JFrame("Shout Clock");
//        Container pane = this.frame.getContentPane();
//        JLabel label = new JLabel("Shout Clock");
//        pane.add(label);
//        makeButtons(pane);
//        this.frame.pack();
//        this.frame.setVisible(true);
//    }
//
//    private void makeButtons(Container pane){
//        JButton getTimeButton = new JButton("Current Time");
//        getTimeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                getCurrentTime();
//            }
//        });
//
//        JButton recordButton = new JButton("Record");
//        recordButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new RecordWindow();
//            }
//        });
//
//        pane.add(getTimeButton);
//        pane.add(recordButton);
//    }
//
//    private void getCurrentTime(){
//        TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false), this.db);
//        TimeFile timeFile = tfl.getTimeFile();
//
//        new PlayAudio(timeFile).play();
//    }
//
//    private void record(){
//
//    }
//
//    public static void main(String[] args) {
//        new MainWindow();
//    }
}
