//package gui;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
///**
// * Created by Avi on 8/7/2017.
// */
//public class testGui {
//    private JPanel mainPanel;
//    private JLabel label;
//    private JButton getTimeButton;
//    private JButton recordButton;
//
//    public testGui(){
//
//
//        getTimeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "Current time is...");
//            }
//        });
//        recordButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "Now recording...");
//            }
//        });
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("App");
//        frame.setContentPane(new testGui().mainPanel);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setSize(new Dimension(400, 400));
//        frame.setLocation(850, 500);
//        frame.pack();
//        frame.setVisible(true);
//    }
//}
