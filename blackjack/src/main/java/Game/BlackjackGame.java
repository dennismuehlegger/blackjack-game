package main.java.Game;

import main.java.Logic.CardLogic;
import main.java.Logic.RoundLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BlackjackGame {
    public static final int SEPARATOR_LENGTH = 40;
    private List<Card> deck;
    public List<Player> players;
    private Scanner scanner;
    private Random random;

    public BlackjackGame() {
        this.deck = new ArrayList<>();
        this.players = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public BlackjackGame(List<Card> deck, List<Player> players) {
        this.deck = deck;
        this.players = players;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public boolean checkWinCondition() {
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

    public void showFinalHands() {
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


    public void determineWinner() {
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

    public void setup() throws InterruptedException {
        CardLogic cardLogic = new CardLogic(this.deck, this.players);
        RoundLogic roundLogic = new RoundLogic(cardLogic, this, this.players);
        cardLogic.createDeck();
        roundLogic.createPlayers();
        cardLogic.dealInitialCards();
        roundLogic.playRounds();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("====================================");
        System.out.println("        BLACKJACK GAME             ");
        System.out.println("====================================\n");

        Scanner scanner = new Scanner(System.in);
        String resumePlaying;
        do {
            BlackjackGame game = new BlackjackGame();
            game.setup();
            System.out.print("\nDo you want to play another round? (yes/no): ");
            resumePlaying = scanner.nextLine().toLowerCase().trim();
        } while (resumePlaying.equals("yes"));

        System.out.println("\nThanks for playing!");
    }

}
