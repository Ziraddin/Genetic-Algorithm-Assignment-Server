package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(3169);
            AcceptClients acceptClients = new AcceptClients(serverSocket);
            Thread t = new Thread(acceptClients);
            t.start();
            System.out.println("Server.Server is ready!");
            System.out.println("Server.Server address is : " + InetAddress.getLocalHost());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
