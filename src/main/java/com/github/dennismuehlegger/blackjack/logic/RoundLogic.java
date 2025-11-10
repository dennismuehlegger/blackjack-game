package com.github.dennismuehlegger.blackjack.logic;

import com.github.dennismuehlegger.blackjack.game.BlackjackGame;
import com.github.dennismuehlegger.blackjack.game.Card;
import com.github.dennismuehlegger.blackjack.game.Player;
import com.github.dennismuehlegger.blackjack.game.Winner;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RoundLogic {
    private static final int SEPARATOR_LENGTH = 40;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 7;
    private static final int HIGHEST_SCORE = 21;
    private static final int MAX_ROUNDS = 5;

    private final CardLogic cardLogic;
    private final BlackjackGame blackjackGame;
    private final List<Player> players;
    private final Scanner scanner;

    public RoundLogic(CardLogic cardLogic, BlackjackGame game, List<Player> players) {
        this(cardLogic, game, players, new Scanner(System.in));
    }

    public RoundLogic(CardLogic cardLogic, BlackjackGame game, List<Player> players, Scanner scanner) {
        this.cardLogic = cardLogic;
        this.blackjackGame = game;
        this.players = players;
        this.scanner = scanner;
    }

    public void createPlayers() {
        int numPlayers = promptForNumberOfPlayers();
        setPlayerNames(numPlayers);
    }

    private int promptForNumberOfPlayers() {
        int numPlayers;
        do {
            try {
                System.out.print("How many players want to play?: ");
                numPlayers = scanner.nextInt();
                scanner.nextLine();

                if (isInvalidPlayerCount(numPlayers)) {
                    System.out.println("At least " + MIN_PLAYERS + " or maximum " + MAX_PLAYERS + " of players need to participate!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
                scanner.next();
                numPlayers = 0;
            }
        } while (isInvalidPlayerCount(numPlayers));
        return numPlayers;
    }

    private boolean isInvalidPlayerCount(int count) {
        return count < MIN_PLAYERS || count > MAX_PLAYERS;
    }

    private void setPlayerNames(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Please enter name of player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }
        System.out.println();
    }

    public void playRounds() {
        if (handleInitialHighScore()) {
            return;
        }

        for (int round = 1; round <= MAX_ROUNDS; round++) {
            printRoundHeader(round);
            cardLogic.showAllHands();

            if (processPlayerTurns() || checkWinCondition()) {
                return;
            }
        }

        determineWinner();
    }

    private boolean handleInitialHighScore() {
        List<Player> highScorePlayers = getPlayersWithHighScore();

        if (highScorePlayers.isEmpty()) {
            return false;
        }

        announceWinner(highScorePlayers, HIGHEST_SCORE, true);
        return true;
    }

    private void printRoundHeader(int round) {
        System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
        System.out.println("*** ROUND " + round + " ***");
        System.out.println("=".repeat(SEPARATOR_LENGTH));
    }

    private boolean processPlayerTurns() {
        for (Player player : players) {
            if (shouldSkipPlayer(player)) {
                continue;
            }

            if (playTurn(player)) {
                determineWinner();
                return true;
            }

            if (checkWinCondition()) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldSkipPlayer(Player player) {
        return player.isBusted() || player.isStanding();
    }

    public boolean playTurn(Player player) {
        printTurnHeader(player);

        if (player.isStanding()) {
            return false;
        }

        String answer = promptHitOrStand(player);

        if (answer.equals("yes")) {
            return handleHit(player);
        } else {
            handleStand(player);
            return false;
        }
    }

    private void printTurnHeader(Player player) {
        System.out.println("\n--- " + player + " turn ---");
        System.out.println("Current hand: " + player.getHandValue());
    }

    private String promptHitOrStand(Player player) {
        String answer;
        do {
            System.out.print(player + ", do you want to hit? (yes/no): ");
            answer = scanner.nextLine().toLowerCase().trim();
        } while (!answer.equals("yes") && !answer.equals("no"));
        return answer;
    }

    private boolean handleHit(Player player) {
        Card card = cardLogic.drawCard();
        player.addCard(card);

        System.out.println("\n" + player + " draws: " + card);
        System.out.println("New hand: " + player.getHandValue());

        if (player.isBusted()) {
            handleBust(player);
            return false;
        } else if (player.hasHighScore()) {
            System.out.println(player + " has exactly " + HIGHEST_SCORE + "!");
            return true;
        }
        return false;
    }

    private void handleBust(Player player) {
        System.out.println(player + " is over " + HIGHEST_SCORE + " and busted!");
        player.setOut(true);
    }

    private void handleStand(Player player) {
        System.out.println(player + " stands (hand stays " + player.getHandValue() + ")");
        player.setStanding(true);
    }

    private boolean checkWinCondition() {
        List<Player> activePlayers = getActivePlayers();

        if (activePlayers.size() == 1) {
            announceSingleWinner(activePlayers.get(0));
            return true;
        }

        if (activePlayers.isEmpty()) {
            announceAllBusted();
            return true;
        }

        if (allPlayersStanding(activePlayers)) {
            determineWinner();
            return true;
        }

        return false;
    }

    private List<Player> getActivePlayers() {
        List<Player> activePlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isOut()) {
                activePlayers.add(player);
            }
        }
        return activePlayers;
    }

    private boolean allPlayersStanding(List<Player> activePlayers) {
        return activePlayers.stream().allMatch(Player::isStanding);
    }

    private void announceSingleWinner(Player winner) {
        showFinalHands();
        printSeparator();
        System.out.println("*** " + winner + " wins! ***");
        System.out.println("Hand: " + winner.getHandValue());
        printSeparator();
    }

    private void announceAllBusted() {
        showFinalHands();
        printSeparator();
        System.out.println("All players busted. Nobody wins!");
        printSeparator();
    }

    private void determineWinner() {
        showFinalHands();

        Winner result = findWinnersWithHighestScore();

        printSeparator();
        announceResult(result);
        printSeparator();
    }

    private Winner findWinnersWithHighestScore() {
        List<Player> winners = new ArrayList<>();
        int highestValue = 0;

        for (Player player : players) {
            if (player.isOut()) {
                continue;
            }

            if (player.getHandValue() > highestValue) {
                highestValue = player.getHandValue();
                winners.clear();
                winners.add(player);
            } else if (player.getHandValue() == highestValue) {
                winners.add(player);
            }
        }

        return new Winner(winners, highestValue);
    }

    private void announceResult(Winner result) {
        if (result.winners.isEmpty()) {
            System.out.println("No winners - all players busted!");
        } else if (result.winners.size() == 1) {
            System.out.println("*** " + result.winners.get(0) + " wins with " + result.highestScore + " points! ***");
        } else {
            printTieMessage(result.winners, result.highestScore);
        }
    }

    private void announceWinner(List<Player> winners, int score, boolean isHighScore) {
        showFinalHands();
        printSeparator();

        if (winners.size() == 1) {
            if (isHighScore) {
                System.out.println("*** " + winners.get(0) + " has 21 and wins! ***");
            } else {
                System.out.println("*** " + winners.get(0) + " wins with " + score + " points! ***");
            }
        } else {
            printTieMessage(winners, score);
        }

        printSeparator();
    }

    private List<Player> getPlayersWithHighScore() {
        List<Player> highScorePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getHandValue() == HIGHEST_SCORE) {
                highScorePlayers.add(player);
            }
        }
        return highScorePlayers;
    }

    private void printTieMessage(List<Player> players, int score) {
        System.out.println("*** TIE! ***");
        System.out.print("Players with " + score + ": ");

        for (int i = 0; i < players.size(); i++) {
            System.out.print(players.get(i));
            if (i < players.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    private void showFinalHands() {
        printSeparator();
        System.out.println("*** FINAL HANDS ***");
        printSeparator();

        for (Player player : players) {
            System.out.printf("%s: %s = %d%s%n",
                    player.getName(),
                    player.getFullHand(),
                    player.getHandValue(),
                    getPlayerStatus(player)
            );
        }

        printSeparator();
    }

    private String getPlayerStatus(Player player) {
        if (player.isOut()) {
            return " (Busted)";
        } else if (player.isStanding()) {
            return " (Standing)";
        }
        return "";
    }

    private void printSeparator() {
        System.out.println("=".repeat(SEPARATOR_LENGTH));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players.clear();
        this.players.addAll(players);
    }

}
