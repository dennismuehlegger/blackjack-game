package main.java.Logic;

import main.java.Game.Card;
import main.java.Game.Player;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CardLogic {

    private List<Card> deck;
    private List<Player> players;
    private Scanner scanner;
    private Random random;

    public CardLogic(List<Card> deck, List<Player> players) {
        this.deck = deck;
        this.players = players;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void fillDeck() {
        System.out.print("How many decks should be used this round?: ");
        int numDecks = scanner.nextInt();
        String[] suits = {"♠", "♥", "♦", "♣"};

        for (int i = 0; i < numDecks; i++) {
            for (String suit : suits) {
                for (int value = 2; value <= 9; value++) {
                    deck.add(new Card(value, suit, String.valueOf(value)));
                }
                deck.add(new Card(10, suit, "10"));
                deck.add(new Card(10, suit, "J"));
                deck.add(new Card(10, suit, "Q"));
                deck.add(new Card(10, suit, "K"));
                deck.add(new Card(11, suit, "A"));
            }
        }
    }

    public Card drawCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("Deck is empty!");
        }
        int index = random.nextInt(deck.size());
        return deck.remove(index);
    }

    public void dealInitialCards() throws InterruptedException {
        System.out.println("--- First deal ---");

        for (Player player : players) {
            Card card = drawCard();
            player.addCard(card);
            System.out.println(player + " gets: " + card);
            Thread.sleep(1000);
        }

        System.out.println("\n--- Second deal ---");

        for (Player player : players) {
            Card card = drawCard();
            player.addCard(card);
            System.out.println(player + " gets: " + card);
            Thread.sleep(1000);
        }

        System.out.println("\n" + "=".repeat(30));
        showAllHands();
    }

    public void showAllHands() {
        System.out.println("\n=== Current hand ===");
        for (Player player : players) {
            if (player.isOut()) {
                System.out.printf("%s: %d (Busted)%n", player.getName(), player.getHandValue());
            }
            else if (player.isStanding()){
                System.out.printf("%s: %d (Standing)%n", player.getName(), player.getHandValue());
            }
            else {
                System.out.printf("%s: %d%n", player.getName(), player.getHandValue());
            }
        }
        System.out.println();
    }
}
