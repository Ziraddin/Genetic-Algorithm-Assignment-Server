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
                    // Check if all preferred destinations exceed the maximum student limit
                    boolean flag = isPreferredDestinationsExceedLimit(preferences);
                    if (flag) {
                        out.println("Preferred destinations exceed the maximum limit.");
                        out.flush();
                        System.out.println("Preferred destinations exceed the maximum limit.");
                    } else {
                        System.out.println("Preferred destinations does not exceed the maximum limit.");
                        if (student == null) {
                            student = new Student(clientId + "", preferences);
                            students.add(student);
                        } else {
                            for (Student existingStudent : students) {
                                if (existingStudent.getName().equals(clientId + "")) {
                                    existingStudent.updatePreferredDestinations(preferences);
                                    geneticAlgorithm.updateStudents(students);
                                    break;
                                }
                            }
                        }
                        calculateBestAssignment();
                        System.out.println("Number of students stated their preferences: " + students.size());
                    }
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
                clientWriters.remove(clientId + "");
                System.out.println("Client with id : " + clientId + " is disconnected.");

                nbrClients--;
                System.out.println("Number of Active Clients :  " + nbrClients);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDisconnect() {
        try {
            // Send disconnection acknowledgment to the client
            out.println("disconnect_ack");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateBestAssignment() {
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

    private boolean isPreferredDestinationsExceedLimit(List<String> preferences) {
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
