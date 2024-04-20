package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket;

        try {
            // Create a server socket
            serverSocket = new ServerSocket(3169);

            // Create an instance of AcceptClients
            AcceptClients acceptClients = new AcceptClients(serverSocket);

            // Start a thread for accepting clients
            Thread t = new Thread(acceptClients);
            t.start();

            // Print server readiness message
            System.out.println("Server.Server is ready!");

            // Print server address
            System.out.println("Server.Server address is : " + InetAddress.getLocalHost());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
