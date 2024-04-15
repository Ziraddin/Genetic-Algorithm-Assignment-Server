package Server;

import GenAlgorithm.Structures.Destination;
import GenAlgorithm.Structures.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Random;

class AcceptClients implements Runnable {
    static Random rand = new Random();
    private ServerSocket serverSocket;
    private Socket socket;
    private static int nbrClients = 0;
    private static int clientNumber = 0;
    private static final int CLIENT_LIMIT = 40;
    static Map<String, PrintWriter> clientWriters = new HashMap<>();
    static List<Destination> destinations = Arrays.asList(
            new Destination("Paris", rand.nextInt(5) + 1),
            new Destination("London", rand.nextInt(5) + 1),
            new Destination("Dubai", rand.nextInt(5) + 1),
            new Destination("Tokyo", rand.nextInt(5) + 1),
            new Destination("Istanbul", rand.nextInt(5) + 1),
            new Destination("Rome", rand.nextInt(5) + 1),
            new Destination("Singapore", rand.nextInt(5) + 1),
            new Destination("Seoul", rand.nextInt(5) + 1),
            new Destination("New York City", rand.nextInt(5) + 1),
            new Destination("Baku", rand.nextInt(5) + 1)
    );
    static List<Student> students = new ArrayList<>();
    static int populationSize = 100;
    static double mutationRate = 0.3;

    public AcceptClients(ServerSocket s) {
        serverSocket = s;
        students = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            while (nbrClients < CLIENT_LIMIT) {
                socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                nbrClients++;
                clientNumber++;

                clientWriters.put((clientNumber % 40) + "", out);

                System.out.println("Number of clients connected : " + nbrClients);
                out.println("Server.Client number : " + clientNumber + " is connected !");
                out.flush();
                out.println(clientNumber % 40);
                out.flush();

                // Start client handler thread
                Thread clientThread = new Thread(new ClientHandler(socket, clientNumber % 40, in, out));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
