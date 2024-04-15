package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String clientNumber;

    @Override
    public void run() {

        try {
            socket = new Socket(InetAddress.getLocalHost(), 3169);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            String serverMessage = in.readLine();
            System.out.println(serverMessage);
            clientNumber = in.readLine();
            System.out.println("clientNumber : " + clientNumber);

            socket.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
