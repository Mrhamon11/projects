package main;

import client.Client;
import server.Server;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Please supply a .json file that contains the courses.");
            return;
        }

        Thread serverThread = new Thread(() -> {
            new Server(args[0]).startServer();
        });
        serverThread.start();


        Thread clientThread = new Thread(() -> {
           new Client().startClient();
        });
        clientThread.start();
    }
}
