package com.github.dennismuehlegger.blackjack.logic;

import com.github.dennismuehlegger.blackjack.game.BlackjackGame;
import com.github.dennismuehlegger.blackjack.game.Card;
import com.github.dennismuehlegger.blackjack.game.Player;

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

    private CardLogic cardLogic;
    private BlackjackGame blackjackGame;
    private List<Player> players;
    private Scanner scanner;

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
        int numPlayers;
        do {
            try {
                System.out.print("How many players want to play?: ");
                numPlayers = scanner.nextInt();
                scanner.nextLine();

                if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
                    System.out.println("At least " + MIN_PLAYERS + " or maximum " + MAX_PLAYERS + " of players need to participate!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
                scanner.next();
                numPlayers = 0;
            }
        } while (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS);
        setPlayerNames(numPlayers);
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

            if (processPlayerTurns()) {
                return;
            }

            if (checkWinCondition()) {
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

        showFinalHands();
        System.out.println("\n" + "=".repeat(40));

        if (highScorePlayers.size() == 1) {
            System.out.println("*** " + highScorePlayers.get(0) + " has 21 and wins! ***");
        } else {
            printTieMessage(highScorePlayers);
        }

        System.out.println("=".repeat(SEPARATOR_LENGTH));
        return true;
    }

    private void printRoundHeader(int round) {
        System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
        System.out.println("*** ROUND " + round + " ***");
        System.out.println("=".repeat(SEPARATOR_LENGTH));
    }

    private boolean processPlayerTurns() {
        for (Player player : players) {
            if (player.isBusted() || player.isStanding()) {
                continue;
            }

            if (player.hasHighScore()) {
                showFinalHands();
                System.out.println("\n*** " + player + " has 21 and wins! ***");
                return true;
            }

            playTurn(player);

            if (checkWinCondition()) {
                return true;
            }
        }
        return false;
    }


    public void playTurn(Player player) {
        System.out.println("\n--- " + player + " turn ---");
        System.out.println("Current hand: " + player.getHandValue());

        String answer;
        if (!player.isStanding()) {
            do {
                System.out.print(player + ", do you want to hit? (yes/no): ");
                answer = scanner.nextLine().toLowerCase().trim();
            } while (!answer.equals("yes") && !answer.equals("no"));

            if (answer.equals("yes")) {
                Card card = cardLogic.drawCard();
                player.addCard(card);
                System.out.println("\n" + player + " draws: " + card);
                System.out.println("New hand: " + player.getHandValue());

                if (player.isBusted()) {
                    System.out.println(player + " is over " + HIGHEST_SCORE + " and busted!");
                    player.setOut(true);
                } else if (player.hasHighScore()) {
                    System.out.println(player + " has exactly " + HIGHEST_SCORE + "!");
                }
            } else {
                System.out.println(player + " stands (hand stays " + player.getHandValue() + ")");
                player.setStanding(true);
            }
        }
    }

    private boolean checkWinCondition() {
        List<Player> activePlayers = new ArrayList<>();

        for (Player player : players) {
            if (!player.isOut()) {
                activePlayers.add(player);
            }
        }

        if (activePlayers.size() == 1) {
            showFinalHands();
            System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
            System.out.println("*** " + activePlayers.get(0) + " wins! ***");
            System.out.println("Hand: " + activePlayers.get(0).getHandValue());
            System.out.println("=".repeat(SEPARATOR_LENGTH));
            return true;
        }

        if (activePlayers.isEmpty()) {
            showFinalHands();
            System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
            System.out.println("All players busted. Nobody wins!");
            System.out.println("=".repeat(SEPARATOR_LENGTH));
            return true;
        }

        boolean allStanding = activePlayers.stream().allMatch(Player::isStanding);
        if (allStanding) {
            determineWinner();
            return true;
        }

        return false;
    }


    private void determineWinner() {
        showFinalHands();
        Player winner = null;
        int highestValue = 0;

        for (Player player : players) {
            if (!player.isOut() && player.getHandValue() > highestValue) {
                highestValue = player.getHandValue();
                winner = player;
            }
        }

        if (winner != null) {
            System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
            System.out.println("*** " + winner + " wins with " + highestValue + " points! ***");
            System.out.println("=".repeat(SEPARATOR_LENGTH));
        } else {
            System.out.println("\nNo winners - all players busted!");
        }
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

    private void printTieMessage(List<Player> highScorePlayers) {
        System.out.println("*** TIE! ***");
        System.out.print("Players with 21: ");
        for (int i = 0; i < highScorePlayers.size(); i++) {
            System.out.print(highScorePlayers.get(i));
            if (i < highScorePlayers.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    private void showFinalHands() {
        System.out.println("=".repeat(SEPARATOR_LENGTH));
        System.out.println("*** FINAL HANDS ***");
        System.out.println("=".repeat(SEPARATOR_LENGTH));

        for (Player player : players) {
            String status = "";
            if (player.isOut()) {
                status = " (Busted)";
            } else if (player.isStanding()) {
                status = " (Standing)";
            }

            System.out.printf("%s: %s = %d%s%n",
                    player.getName(),
                    player.getFullHand(),
                    player.getHandValue(),
                    status
            );
        }

        System.out.println("=".repeat(SEPARATOR_LENGTH));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}