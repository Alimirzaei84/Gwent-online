package server.game;


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

    public synchronized String handleCommand(String command) throws IOException, InterruptedException {

        game.getCurrentPlayer().makeHandReady();
        game.getOtherPlayer().makeHandReady();

        User user = User.getUserByUsername(command.split(" ")[0]);
        assert user != null;
        System.out.println("The user with username " + user.getUsername() + " send " + "\"" + command + "\" to the server in round \"" + game.getNumTurn() + "\" and now is the turn of user \"" + game.getCurrentPlayer().getUser().getUsername() + "\"");
        if (!user.equals(game.getCurrentPlayer().getUser())) {
            // The game is listening to the other player in round 1
            game.getCurrentPlayer().makeHandReady();
            game.getOtherPlayer().makeHandReady();
            sendBoardObjectToEachPlayer();
            return null;
        }

        game.getCurrentPlayer().makeHandReady();
        game.getOtherPlayer().makeHandReady();


        // The Game is listening to the current player...
        if (GameRegexes.ECHO.matches(command)) {
            String message = GameRegexes.ECHO.getGroup(command, 1);
            game.broadcast(message);
        } else if (GameRegexes.PASS_ROUND.matches(command)) {
            passRound();
            return "[INFO] passed!";
        } else if (GameRegexes.PUT_CARD.matches(command)) {
            System.out.println("put card regex matched");
            putCard(command);
            System.out.println(game.getCurrentPlayer().getInHand().size());
            System.out.println(game.getCurrentPlayer().getInHand().size());
        } else if (GameRegexes.PLAY_LEADER.matches(command)) {
            actionLeader();
        } else if (GameRegexes.VETO_A_CARD.matches(command)) {
            veto(command);
        }


        game.getCurrentPlayer().makeHandReady();
        game.getOtherPlayer().makeHandReady();

        sendBoardObjectToEachPlayer();
        return null;
    }

    private synchronized void sendBoardObjectToEachPlayer() throws IOException {

        // for me
        Board currBoard = game.getCurrentPlayerBoard();
        game.getCurrentPlayer().getUser().sendMessage(currBoard);
        Board otherBoard = game.getOtherPlayerBoard();
        game.getOtherPlayer().getUser().sendMessage(otherBoard);

        // for other
        Board currBoard1 = game.getCurrentPlayerBoard();
        game.getCurrentPlayer().getUser().sendMessage(currBoard1);
        Board otherBoard1 = game.getOtherPlayerBoard();
        game.getOtherPlayer().getUser().sendMessage(otherBoard1);


        // for viewers
        for (User attendee : game.getChatroom().getAttendees())
            attendee.sendMessage(game.getBoardForClosestFriendOfUser(attendee));


    }

    private synchronized void veto(String command) throws IOException {
        Card card = game.getCurrentPlayer().getInHand().get(Integer.parseInt(GameRegexes.VETO_A_CARD.getGroup(command, "cardIndex")));
        try {
            game.getCurrentPlayer().veto(card);
            game.getCurrentPlayer().getUser().sendMessage("veto done");
            System.out.println("veto done");
            game.setPlayerListening();
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
            game.setPlayerListening();
        }
    }

    private synchronized void passRound() throws IOException {
        game.getCurrentPlayer().passRound();
        game.setPlayerListening();
    }


    private synchronized void putCard(String command) {
        game.getCurrentPlayer().putCard(game.getCurrentPlayer().getCardFromHandByName(GameRegexes.PUT_CARD.getGroup(command, "cardName")));
        game.setPlayerListening();
    }


    private boolean isGameListening() {
        return game.isGameListening();
    }


    private void setPlayerListening() {
        game.setPlayerListening();
    }
}
