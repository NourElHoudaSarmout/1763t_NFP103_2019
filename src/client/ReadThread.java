/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 * This thread is responsible for reading server's input and printing it
 * to the console.
 * It runs in an infinite loop until the client disconnects from the server.
 * @author user
 */
import java.io.*;
import java.net.*;

public class ReadThread extends Thread {
    //  private BufferedReader reader;

    private Socket socket;
    private Client client;

    BufferedReader ir;

    private static BufferedReader getInput(Socket p) throws IOException {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    public ReadThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            // InputStream input = socket.getInputStream();
            // reader = new BufferedReader(new InputStreamReader(input));
            ir = getInput(socket);

        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = ir.readLine();
                System.out.println("\n" + response);

                // prints the username after displaying the server's message
                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
