package org.academiadecodigo.thisfunctionals;

public class Main {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
        //to connect to the Port: nc 127.0.01 8080
    }
}