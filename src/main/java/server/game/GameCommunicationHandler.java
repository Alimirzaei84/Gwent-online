package server.game;


import server.Enum.GameRegexes;

import java.io.IOException;

public class GameCommunicationHandler implements Runnable {

    private final Game game;

    protected GameCommunicationHandler(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
    }

    public String handleCommand(String command) throws IOException {
        if (!isGameListening()) {
            return "[ERROR] server isn't listening right now.";
        }

        // game is listening
        if (GameRegexes.ECHO.matches(command)) {
            String message = GameRegexes.ECHO.getGroup(command, 1);
            game.broadcast(message);
        }

        else if (GameRegexes.PASS_ROUND.matches(command)) {
            setPlayerListening();
            return "[INFO] passed!";
        }

        return null;
    }

    private boolean isGameListening() {
        return game.isGameListening();
    }

    private void setPlayerListening() {
        game.setPlayerListening();
    }
}
