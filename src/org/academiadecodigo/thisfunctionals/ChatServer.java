package org.academiadecodigo.thisfunctionals;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int PORT = 8080;

    private final List<ClientConnection> CLIENTLIST = Collections.synchronizedList(new LinkedList<>());
    private ExecutorService cachedPoolThread = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;
    public ChatServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Keep my server open for the client, accept the connection, submit thread and add to the list

    public void listen() {

        while (true) {
            try {

                Socket clientSocket = serverSocket.accept();
                System.out.println("There is a new client connected!");

                ClientConnection clientConnection = new ClientConnection(clientSocket, this);
                cachedPoolThread.submit(clientConnection);
                CLIENTLIST.add(clientConnection);


            } catch (IOException e) {
                e.printStackTrace();

            }
        }



    }

    //Receive the message from the ClientConnection
    public void broadcast(String message, ClientConnection sender) throws IOException {
        for (ClientConnection clientConnection : CLIENTLIST) {
            if (!(clientConnection == sender)){
                if (message.equals("/quit")){
                    message = "left";
                    //everyone receives the message
                    clientConnection.getOut().println(sender.getUserName() + " " + message);

                }else{
                    clientConnection.getOut().println(sender.getUserName() + " says: " + message);
                }

            }
        }
    }
    //ejects the user from my LinkedList

    public void remove(ClientConnection client) {
        for (ClientConnection clientConnection : CLIENTLIST) {
            if (clientConnection == client){
                CLIENTLIST.remove(client);
            }
        }
    }

    public String getUserList(ClientConnection sender) {
        StringBuilder userList = new StringBuilder();
        for (ClientConnection clientConnection: CLIENTLIST) {

            userList.append(clientConnection.getUserName() + "\r\n");

        }
        return sender.getIn(userList.toString());
    }
}

;
