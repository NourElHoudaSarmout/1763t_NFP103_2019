/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread is responsible for reading user's input and send it to the
 * server. It runs in an infinite loop until the user types 'bye' to quit.
 *
 * @author user
 */
public class WriteThread extends Thread {

    //  private PrintWriter writer;
    private Socket socket;
    private Client client;

    PrintWriter reply;

    private static PrintWriter getOutput(Socket p) throws IOException {
        return new PrintWriter(new OutputStreamWriter(p.getOutputStream()), true);
    }

    public WriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {

            reply = getOutput(socket);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        String username = null;
        BufferedReader bufferRead = null;

        System.out.print("Enter Username : ");

        try {
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            username = bufferRead.readLine();

            client.setUserName(username);
            reply.println(username);
            reply.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = null;

        do {
            try {
                System.out.print("[" + username + "]");

                text = bufferRead.readLine();

                reply.println(text);
                reply.flush();
            } catch (IOException ex) {
                Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
