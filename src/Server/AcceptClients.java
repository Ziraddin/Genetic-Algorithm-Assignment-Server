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
    // Random object for generating random numbers
    static Random rand = new Random();

    // ServerSocket for accepting client connections
    private ServerSocket serverSocket;

    // Socket for individual client connections
    private Socket socket;

    // Number of currently connected clients
    static int nbrClients = 0;

    // Counter for assigning client numbers
    private static int clientNumber = 0;

    // Limit for maximum number of clients
    private static final int CLIENT_LIMIT = 40;

    // Map to store client writers for sending messages
    static Map<String, PrintWriter> clientWriters = new HashMap<>();

    // List of available destinations
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

    // List of students
    static List<Student> students = new ArrayList<>();

    // Population size for genetic algorithm
    static int populationSize = 100;

    // Mutation rate for genetic algorithm
    static double mutationRate = 0.3;

    // Constructor for AcceptClients class
    public AcceptClients(ServerSocket s) {
        serverSocket = s;
        students = new ArrayList<>();
    }

    // Run method for handling client connections
    @Override
    public void run() {
        try {
            // Print max students for each destination
            for (int i = 0; i < destinations.size(); i++) {
                System.out.println(destinations.get(i).getName() + " : " + destinations.get(i).getMaxStudents());
            }

            // Accept client connections
            while (nbrClients < CLIENT_LIMIT) {
                socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                nbrClients++;
                clientNumber++;

                // Add client writer to map
                clientWriters.put((clientNumber % 40) + "", out);

                System.out.println("Number of clients connected : " + nbrClients);
                out.println("Tests.Client number : " + clientNumber + " is connected !");
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
