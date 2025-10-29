package main.java.Logic;

import main.java.Game.BlackjackGame;
import main.java.Game.Card;
import main.java.Game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoundLogic {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 7;
    private static final int BLACKJACK = 21;
    private static final int MAX_ROUNDS = 5;

    private CardLogic cardLogic;
    private BlackjackGame blackjackGame;
    private List<Player> players;
    private Scanner scanner;

    public RoundLogic(CardLogic cardLogic, BlackjackGame blackjackGame, List<Player> players) {
        this.cardLogic = cardLogic;
        this.blackjackGame = blackjackGame;
        this.players = players;
        this.scanner = new Scanner(System.in);
    }

    public void setupPlayers() {
        int numPlayers;
        do {
            System.out.print("How many players want to play?: ");
            numPlayers = scanner.nextInt();
            scanner.nextLine();

            if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
                System.out.println("At least " + MIN_PLAYERS + " or maximum "
                        + MAX_PLAYERS + " of players need to participate!");
            }
        } while (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS);

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Please enter name of player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }
        System.out.println();
    }

    public void playRounds() {
        List<Player> blackjackPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getHandValue() == BLACKJACK) {
                blackjackPlayers.add(player);
            }
        }

        if (!blackjackPlayers.isEmpty()) {
            System.out.println("\n" + "=".repeat(40));
            if (blackjackPlayers.size() == 1) {
                System.out.println("*** " + blackjackPlayers.get(0) + " has BLACKJACK and wins! ***");
            } else {
                System.out.println("*** TIE! ***");
                System.out.print("Players with BLACKJACK: ");
                for (int i = 0; i < blackjackPlayers.size(); i++) {
                    System.out.print(blackjackPlayers.get(i));
                    if (i < blackjackPlayers.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
            System.out.println("=".repeat(BlackjackGame.SEPARATOR_LENGTH));
            return;
        }

        for (int round = 1; round <= MAX_ROUNDS; round++) {
            System.out.println("\n" + "=".repeat(BlackjackGame.SEPARATOR_LENGTH));
            System.out.println("*** ROUND " + round + " ***");
            System.out.println("=".repeat(BlackjackGame.SEPARATOR_LENGTH));

            cardLogic.showAllHands();

            if (blackjackGame.checkWinCondition()) {
                return;
            }

            for (Player player : players) {
                if (player.isOut()) {
                    continue;
                }

                if (player.hasBlackjack()) {
                    System.out.println("\n*** " + player + " has BLACKJACK and wins! ***");
                    return;
                }

                playTurn(player);

                if (blackjackGame.checkWinCondition()) {
                    return;
                }
            }
        }
        blackjackGame.determineWinner();
    }

    private void playTurn(Player player) {
        System.out.println("\n--- " + player + " turn ---");
        System.out.println("Current hand: " + player.getHandValue());

        String answer;
        do {
            System.out.print(player + ", do you want a card? (yes/no): ");
            answer = scanner.nextLine().toLowerCase().trim();
        } while (!answer.equals("yes") && !answer.equals("no"));

        if (answer.equals("yes")) {
            Card card = cardLogic.drawCard();
            player.addCard(card);
            System.out.println("\n" + player + " draws: " + card);
            System.out.println("New hand: " + player.getHandValue());

            if (player.isBust()) {
                System.out.println(player + " is over " + BLACKJACK + " and busted!");
                player.setOut(true);
            } else if (player.hasBlackjack()) {
                System.out.println(player + " has exactly " + BLACKJACK + "!");
            }
        } else {
            System.out.println(player + " doesnt draw (hand stays " + player.getHandValue() + ")");
        }
    }
}
