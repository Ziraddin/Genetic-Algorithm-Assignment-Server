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
import java.util.List;
import java.util.Objects;

import static Server.AcceptClients.*;

class ClientHandler implements Runnable {

    private Socket socket;
    private int clientId;
    private BufferedReader in;
    private PrintWriter out;
    private Student student;

    private GeneticAlgorithm geneticAlgorithm;

    // Constructor for ClientHandler class
    public ClientHandler(Socket socket, int clientId, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.clientId = clientId;
        this.in = in;
        this.out = out;
        geneticAlgorithm = new GeneticAlgorithm(populationSize, mutationRate, students, destinations);
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

                    // Process preferences
                    handlePreferences(preferences);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the socket upon client disconnection
                socket.close();
                // Remove client preferences upon disconnection
                if (student != null) {
                    students.remove(student);
                    geneticAlgorithm.updateStudents(students);
                }
                clientWriters.remove(clientId + "");
                System.out.println("Client with id : " + clientId + " is disconnected.");

                nbrClients--;
                System.out.println("Number of Active Clients :  " + nbrClients);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void handleDisconnect() {
        try {
            // Send disconnection acknowledgment to the client
            out.println("disconnect_ack");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void handlePreferences(List<String> preferences) {
        if (student == null) {
            student = new Student(clientId + "", preferences);
            students.add(student);
        } else {
            student.updatePreferredDestinations(preferences);
            geneticAlgorithm.updateStudents(students);
        }

        // Calculate best assignment
        calculateBestAssignment();
        System.out.println("Number of students stated their preferences: " + students.size());
    }

    private synchronized void calculateBestAssignment() {
        Assignment bestAssignment = geneticAlgorithm.optimize();

        // Send assignment results to the clients
        for (Student student : bestAssignment.getAssignmentMap().keySet()) {
            String studentName = student.getName();
            Destination AssignedDestination = bestAssignment.getAssignedDestination(student);

            // Find the PrintWriter associated with this student
            PrintWriter writer = clientWriters.get(studentName);
            if (writer != null) {
                // Send the assignment result to the client
                writer.flush();
                writer.println("You are assigned to " + AssignedDestination.getName());
                writer.flush();
                System.out.println("Student with id : " + studentName + " is assigned to : " + AssignedDestination.getName());
            } else {
                System.out.println("No writer found for student name: " + studentName);
            }
        }
    }

    private synchronized boolean isPreferredDestinationsExceedLimit(List<String> preferences) {
        for (String preferredDestinationName : preferences) {
            int count = 0;
            for (Student student : students) {
                if (student.getPreferredDestinations().contains(preferredDestinationName)) {
                    count++;
                }
            }
            if (preferences.indexOf(preferredDestinationName) == preferences.size() - 1)
                for (Destination destination : destinations) {
                    if (Objects.equals(preferredDestinationName, destination.getName()) && count >= destination.getMaxStudents()) {
                        return true;
                    }
                }
        }
        return false;
    }

}

