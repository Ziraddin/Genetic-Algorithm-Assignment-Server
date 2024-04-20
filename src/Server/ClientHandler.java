package Server;

import GenAlgorithm.Algorithm.GeneticAlgorithm;
import GenAlgorithm.Structures.Assignment;
import GenAlgorithm.Structures.Destination;
import GenAlgorithm.Structures.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static Server.AcceptClients.*;

class ClientHandler implements Runnable {

    private Socket socket;
    private int clientId;
    private BufferedReader in;
    private PrintWriter out;
    private Student student;


    // Constructor for ClientHandler class
    public ClientHandler(Socket socket, int clientId, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.clientId = clientId;
        this.in = in;
        this.out = out;
    }

    // Run method for handling client requests
    @Override
    public void run() {
        try {
            // Send client ID to the client
            out.println(clientId);
            out.flush();
            String inputLine;

            // Listen for client requests
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("disconnect")) {
                    // Handle client disconnection
                    handleDisconnect();
                    break;
                } else if (inputLine.equals("submit")) {
                    // Handle client submission of preferences
                    List<String> preferences = new ArrayList<>();
                    while (!(inputLine = in.readLine()).equals("end")) {
                        preferences.add(inputLine);
                    }
                    // Update student preferences
                    if (student == null) {
                        student = new Student(clientId + "", preferences);
                        students.add(student);
                    } else {
                        students.remove(student);
                        student.updatePreferredDestinations(preferences);
                        students.add(student);
                    }
                    System.out.println("Number of students stated their preferences: " + students.size());
                    calculateBestAssignment();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the socket upon client disconnection
                socket.close();
                // Remove client preferences upon disconnection
                for (int i = 0; i < students.size(); i++) {
                    if (Objects.equals(students.get(i).getName(), student.getName())) {
                        students.remove(i);
                    }
                }
                // Remove client writer
                clientWriters.remove(clientId + "");
                System.out.println("Client with id : " + clientId + " is disconnected.");
                // Decrease the number of active clients
                nbrClients--;
                System.out.println("Number of Active Clients :  " + nbrClients);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to handle client disconnection
    private void handleDisconnect() {
        try {
            // Send disconnection acknowledgment to the client
            out.println("disconnect_ack");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to calculate the best assignment based on preferences
    private void calculateBestAssignment() {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(populationSize, mutationRate, students, destinations);
        Assignment bestAssignment = geneticAlgorithm.optimize();

        // Send assignment results to the clients
        for (Student student : bestAssignment.getAssignmentMap().keySet()) {
            String studentName = student.getName();
            Destination AssignedDestination = bestAssignment.getAssignedDestination(student);

            // Find the PrintWriter associated with this student
            PrintWriter writer = clientWriters.get(studentName);
            if (writer != null) {
                // Send the assignment result to the client
                writer.println("You are assigned to " + AssignedDestination.getName());
                System.out.println("Student with id : " + studentName + " is assigned to : " + AssignedDestination.getName());
                writer.flush();
            } else {
                System.out.println("No writer found for student name: " + studentName);
            }
        }
    }
}
