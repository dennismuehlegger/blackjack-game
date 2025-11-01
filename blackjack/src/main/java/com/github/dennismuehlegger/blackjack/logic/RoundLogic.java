package com.github.dennismuehlegger.blackjack.logic;

import com.github.dennismuehlegger.blackjack.game.BlackjackGame;
import com.github.dennismuehlegger.blackjack.game.Card;
import com.github.dennismuehlegger.blackjack.game.Player;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RoundLogic {
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

    public void createPlayers(){
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

    public void setPlayerNames(int numPlayers) {
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

            if (blackjackGame.checkWinCondition()) {
                return;
            }

        }

        blackjackGame.determineWinner();
    }

    private boolean handleInitialHighScore() {
        List<Player> highScorePlayers = getPlayersWithHighScore();

        if (highScorePlayers.isEmpty()) {
            return false;
        }

        blackjackGame.showFinalHands();
        System.out.println("\n" + "=".repeat(40));

        if (highScorePlayers.size() == 1) {
            System.out.println("*** " + highScorePlayers.get(0) + " has 21 and wins! ***");
        } else {
            printTieMessage(highScorePlayers);
        }

        System.out.println("=".repeat(BlackjackGame.SEPARATOR_LENGTH));
        return true;
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

    private void printRoundHeader(int round) {
        System.out.println("\n" + "=".repeat(BlackjackGame.SEPARATOR_LENGTH));
        System.out.println("*** ROUND " + round + " ***");
        System.out.println("=".repeat(BlackjackGame.SEPARATOR_LENGTH));
    }

    private boolean processPlayerTurns() {
        for (Player player : players) {
            if (player.isBusted() || player.isStanding()) {
                continue;
            }

            if (player.hasHighScore()) {
                blackjackGame.showFinalHands();
                System.out.println("\n*** " + player + " has 21 and wins! ***");
                return true;
            }

            playTurn(player);

            if (blackjackGame.checkWinCondition()) {
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}