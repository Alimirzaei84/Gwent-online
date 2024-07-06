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

    private synchronized void sendBoardObjectToEachPlayer() throws IOException, InterruptedException {
        System.out.println("70 " + game.getOtherPlayer().getInHand().size());
        System.out.println("71 " + game.getCurrentPlayer().getInHand().size());


        Board currBoard = game.getCurrentPlayerBoard();
        System.out.println(currBoard);
        System.out.println("the board hand size is " + currBoard.getMyHand().size() + " " + currBoard.getOppHand().size());
        game.getCurrentPlayer().getUser().sendMessage(currBoard);
        Board otherBoard = game.getOtherPlayerBoard();
        System.out.println(otherBoard);
        System.out.println(otherBoard.getMyHand().size() + " " + otherBoard.getOppHand().size());
        game.getOtherPlayer().getUser().sendMessage(otherBoard);

        System.out.println("73 " + game.getOtherPlayer().getInHand().size());
        System.out.println("74 " + game.getCurrentPlayer().getInHand().size());


        for (int i = 0; i < 10; i++) {

            Thread.sleep(4000);
            Board currBoard1 = game.getCurrentPlayerBoard();
            System.out.println(currBoard1);
            System.out.println("the board hand size is " + currBoard1.getMyHand().size() + " " + currBoard1.getOppHand().size());
            game.getCurrentPlayer().getUser().sendMessage(currBoard1);
            Board otherBoard1 = game.getOtherPlayerBoard();
            System.out.println(otherBoard1);
            System.out.println(otherBoard1.getMyHand().size() + " " + otherBoard1.getOppHand().size());
            game.getOtherPlayer().getUser().sendMessage(otherBoard1);

        }
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
