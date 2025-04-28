# Gwent-Online

 Group: Ali Mirzaei Feizabadei,  Erfan Bohloul , Parsa Mohammad Hasanzadeh

---

## Overview

Gwent-Online is a Java-based networked implementation of the popular card game **Gwent** from The Witcher universe. It features both a rich JavaFX graphical client and a robust multi-threaded server, allowing players to engage in strategic card battles locally or online.

## Features

- **JavaFX GUI**: Built with FXML views and custom CSS styling for an immersive user experience.
- **Client-Server Architecture**: A scalable, thread-pooled server handles matchmaking, game logic, and real-time communication.
- **Offline & Online Play**: Supports local matches or online battles against friends and random opponents.
- **User Management**: Registration, login, and profile persistence ensure secure, personalized gameplay.
- **Matchmaking & Friends**: Create private/public matches, invite friends, or join random lobbies.
- **In-Game Chat & Reactions**: Real-time chat with emoji reactions during live matches.
- **Game Persistence**: Save and load decks, player stats, and match history using JSON/XML storage.
- **Cheat Menu**: Developer console for testing special codes and debugging.
- **MVC Structure**: Clean separation of concerns across client, server, and data models.

## Architecture

```
Gwent-Online/
├─ src/main/java
│  ├─ client       # JavaFX client controllers, views, and networking
│  ├─ server       # Server socket, communication, game logic, and database handlers
│  ├─ model        # Shared models and message definitions
├─ src/main/resources
│  ├─ fxml         # FXML layouts for JavaFX
│  ├─ css          # Stylesheets for UI
│  ├─ images       # Card illustrations and assets
│  ├─ data         # Example JSON/XML files
├─ pom.xml         # Maven build configuration
├─ README.md       # Project documentation (this file)
├─ faaz-aval-uml.pdf            # UML class diagrams
├─ Untitled Diagram.drawio.xml  # Editable Draw.io diagram
```

## Prerequisites

- **Java 11+** (tested on Java 17)
- **Maven 3.6+**

## Building the Project

```bash
# Clone the repository
git clone https://github.com/Alimirzaei84/Gwent-online.git
cd Gwent-online

# Build with Maven
mvn clean install
```

## Running the Server

```bash
# Launch the server (default port 8080)
mvn exec:java -Dexec.mainClass="server.Main"
```

## Running the Client

```bash
# In a separate terminal, start the client
mvn exec:java -Dexec.mainClass="client.Main"
```

## Usage

1. **Start the server**: Ensure the server is running.
2. **Launch the client**.
3. **Register/Login**: Create a new account or sign in with existing credentials.
4. **Play**: Choose offline or online mode, build your deck, and challenge opponents.
5. **Chat & React**: Use the in-game chat to communicate and react with emojis.
6. **Save/Load**: Persist your decks and match history between sessions.

## Contributing

Contributions, issues, and feature requests are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m 'Add YourFeature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a Pull Request.

Please adhere to the existing code style and write meaningful commit messages.

## License

This project is released under the **MIT License**. See [LICENSE](LICENSE) for details.

## Acknowledgments

- Inspired by the Gwent card game from **CD Projekt Red**.
- Developed as part of the Advanced Programming course at Sharif University.

