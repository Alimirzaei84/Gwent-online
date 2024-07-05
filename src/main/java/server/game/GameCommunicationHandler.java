package server.game;


import controller.CardController;
import model.role.Card;
import server.Account.User;
import server.Enum.GameRegexes;

import java.io.IOException;
import java.util.Objects;

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

        User user = User.getUserByUsername(command.split(" ")[0]);
        assert user != null;
        if (!user.equals(game.getCurrentPlayer().getUser())) {
            game.getOtherPlayer().getUser().sendMessage("[ERR]: Now is your opponent turn");
            return "[ERR]: Now is your opponent turn";
        }


        // game is listening
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

    private void sendBoardObjectToEachPlayer() throws IOException {
        game.getCurrentPlayer().getUser().sendMessage(game.getCurrentPlayerBoard());
        game.getOtherPlayer().getUser().sendMessage(game.getOtherPlayerBoard());
    }

    private void veto(String command) throws IOException {
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

    private void actionLeader() throws IOException {
        if (game.getCurrentPlayer().isActionLeaderDone()) {
            game.getCurrentPlayer().getUser().sendMessage("[ERR]: You have already used your leader's ability!");
        } else {
            game.getCurrentPlayer().playLeader();
            game.getCurrentPlayer().getUser().sendMessage("[INFO]: You can do your action leader");
        }
    }

    private void passRound() {
        game.getCurrentPlayer().passRound();
    }

    private void putCard(String command) {
        game.getCurrentPlayer().putCard(CardController.createCardWithName(GameRegexes.PUT_CARD.getGroup(command, "cardName")));
    }

    private boolean isGameListening() {
        return game.isGameListening();
    }

    private void setPlayerListening() {
        game.setPlayerListening();
    }
}
