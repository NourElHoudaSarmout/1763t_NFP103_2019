/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 * @author user
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class UserThread extends Thread {

    private Socket socket;
    private Server server;

    BufferedReader ir;
    PrintWriter reply;

    private static BufferedReader getInput(Socket p) throws IOException {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static PrintWriter getOutput(Socket p) throws IOException {
        return new PrintWriter(new OutputStreamWriter(p.getOutputStream()), true);
    }

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {

            ir = getInput(socket);
            reply = getOutput(socket);

            printUsers();

            String userName = ir.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = ir.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            reply.println("Connected users: " + server.getUserNames());
            reply.flush();
        } else {
            reply.println("No other users connected");
            reply.flush();
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        reply.println(message);
        reply.flush();
    }
}
