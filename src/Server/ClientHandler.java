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

import static Server.AcceptClients.*;

class ClientHandler implements Runnable {

    private Socket socket;
    private int clientId;
    private BufferedReader in;
    private PrintWriter out;
    private Student student;


    public ClientHandler(Socket socket, int clientId, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.clientId = clientId;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            // Send client ID
            out.println(clientId);
            out.flush();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("disconnect")) {
                    // Handle client disconnection
                    handleDisconnect();
                    break;
                } else if (inputLine.equals("submit")) {
                    // Handle client submission
                    List<String> preferences = new ArrayList<>();
                    while (!(inputLine = in.readLine()).equals("end")) {
                        preferences.add(inputLine);
                    }
                    student = new Student(clientId + "", preferences);
                    students.add(student);
                    // Calculate and send best assignment
                    calculateBestAssignment();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                // Remove client preferences upon disconnection
                students.remove(clientId);
                clientWriters.remove(clientId + "");
                System.out.println("Client " + clientId + " disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDisconnect() {
        try {
            out.println("disconnect_ack");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateBestAssignment() {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(populationSize, mutationRate, students, destinations);
        Assignment bestAssignment = geneticAlgorithm.optimize();

        for (Student student : bestAssignment.getAssignmentMap().keySet()) {
            String studentName = student.getName();
            Destination AssignedDestination = bestAssignment.getAssignedDestination(student);

            // Find the PrintWriter associated with this student
            PrintWriter writer = clientWriters.get(studentName);
            if (writer != null) {
                // Send the assignment result to the client
                writer.println("Your assignment is: " + AssignedDestination.getName());
                writer.flush();
            } else {
                System.out.println("No writer found for student name: " + studentName);
            }
        }
    }
}
