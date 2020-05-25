package sample;

import java.io.*;
import java.net.ServerSocket;
import java.sql.SQLException;

public class Server {
    private static final int PORT = 7070;

    private static boolean running = false;
    private static ServerSocket serverSocket = null;

    public static void launch() throws IOException, ClassNotFoundException, SQLException {
        DbConnection.getInstance().launch();
        System.out.println("Starting the server...");
        serverSocket = new ServerSocket(PORT);
        System.out.println("Started the server successfully...");
        running = true;
        mainLoop();
    }

    public static void close() throws IOException, SQLException {
        running = false;
        if (serverSocket != null)
            serverSocket.close();
        DbConnection.getInstance().close();
    }

    private static void mainLoop() {
        System.out.println("Waiting for connections...");
        while (running) {
            try {
                new Client(serverSocket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
