# Server

## Introduction
This server application is designed to handle client connections, receive their preferences, perform assignments using a genetic algorithm, and return the results to the clients.

## Features
- **Client-Server Communication:** Allows multiple clients to connect to the server simultaneously.
- **Preferences Submission:** Clients can submit their preferences for destinations.
- **Genetic Algorithm:** Uses a genetic algorithm to optimize the assignment of destinations to clients based on their preferences.
- **Dynamic Destination Selection:** Clients can select up to 5 destinations from a list.
- **Real-Time Results:** Provides clients with real-time updates on their assignments.
- **Error Handling:** Handles client disconnections and errors gracefully.

## Usage
1. Run the server application.
2. Clients can connect to the server and submit their preferences.
3. The server performs the assignment using the genetic algorithm.
4. Clients receive their assignments in real-time.

## Technologies Used
- Java
- Socket Programming
- Genetic Algorithm

## Dependencies
- None

## Installation
1. Clone the repository.
2. Compile the Java source files.
3. Run the `Server` class to start the server.

## Contributing
Contributions are welcome! If you find any bugs or have suggestions for improvements, please open an issue or submit a pull request.

## License
This project is licensed under the [MIT License](LICENSE).
