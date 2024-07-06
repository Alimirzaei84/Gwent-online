package server.game;


import controller.CardController;
import model.role.Card;
import server.Account.User;
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

    public synchronized String handleCommand(String command) throws IOException {
//        sendBoardObjectToEachPlayer();
        if (!isGameListening()) {
            return "[ERR] server isn't listening right now.";
        }

        User user = User.getUserByUsername(command.split(" ")[0]);
        assert user != null;
        System.out.println("The user with username " + user.getUsername() + " send " + "\"" + command + "\" to the server in round \"" + game.getNumTurn() + "\" and now is the turn of user \"" + game.getCurrentPlayer().getUser().getUsername() + "\"" );
        if (!user.equals(game.getCurrentPlayer().getUser())) {
            // The game is listening to the other player in round 1
            game.getCurrentPlayer().makeHandReady();
            game.getOtherPlayer().makeHandReady();
            sendBoardObjectToEachPlayer();
            return null;
        }


        // The Game is listening to the current player...
        if (GameRegexes.ECHO.matches(command)) {
            String message = GameRegexes.ECHO.getGroup(command, 1);
            game.broadcast(message);
        } else if (GameRegexes.PASS_ROUND.matches(command)) {
            passRound();
            return "[INFO] passed!";
        } else if (GameRegexes.PUT_CARD.matches(command)) {
            putCard(command);
        } else if (GameRegexes.PLAY_LEADER.matches(command)) {
            actionLeader();
        } else if (GameRegexes.VETO_A_CARD.matches(command)) {
            veto(command);
        }

        sendBoardObjectToEachPlayer();
        setPlayerListening();
        return null;
    }

    private synchronized void sendBoardObjectToEachPlayer() throws IOException {
        game.getCurrentPlayer().getUser().sendMessage(game.getCurrentPlayerBoard());
        game.getOtherPlayer().getUser().sendMessage(game.getOtherPlayerBoard());
    }

    private synchronized void veto(String command) throws IOException {
        Card card = game.getCurrentPlayer().getInHand().get(Integer.parseInt(GameRegexes.VETO_A_CARD.getGroup(command, "cardIndex")));
        try {
            game.getCurrentPlayer().veto(card);
            game.getCurrentPlayer().getUser().sendMessage("veto done");
            System.out.println("veto done");
        } catch (Exception e) {
            game.getCurrentPlayer().getUser().sendMessage("[ERR]" + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private synchronized void actionLeader() throws IOException {
        if (game.getCurrentPlayer().isActionLeaderDone()) {
            game.getCurrentPlayer().getUser().sendMessage("[ERR]: You have already used your leader's ability!");
        } else {
            game.getCurrentPlayer().playLeader();
            game.getCurrentPlayer().getUser().sendMessage("[INFO]: You can do your action leader");
        }
    }

    private synchronized void passRound() throws IOException {
        game.getCurrentPlayer().passRound();
    }

    private synchronized void putCard(String command) {
        game.getCurrentPlayer().putCard(CardController.createCardWithName(GameRegexes.PUT_CARD.getGroup(command, "cardName")));
    }

    private boolean isGameListening() {
        return game.isGameListening();
    }

    private void setPlayerListening() {
        game.setPlayerListening();
    }
}
