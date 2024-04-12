import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class AcceptClients implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    public static int nbrCLients = 0;
    public static int clientNumber = 0;
    private static final int CLIENT_LIMIT = 40;


    public AcceptClients(ServerSocket s) {
        serverSocket = s;
    }

    @Override
    public void run() {
        try {
            while (nbrCLients < CLIENT_LIMIT) {
                socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                nbrCLients++;
                clientNumber++;

                System.out.println("Number of clients connected : " + nbrCLients);
                out.println("Client number : " + clientNumber + " is connected !");
                out.flush();
                out.println(clientNumber % 40);
                out.flush();

                socket.close();
                nbrCLients--;
                System.out.println("Number of clients connected : " + nbrCLients);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
