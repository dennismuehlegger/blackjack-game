package com.github.dennismuehlegger.blackjack.logic;

import com.github.dennismuehlegger.blackjack.game.Card;
import com.github.dennismuehlegger.blackjack.game.Player;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CardLogic {
    private static final int MIN_DECK = 1;
    private static final int MAX_DECK = 8;
    private List<Card> deck;
    private List<Player> players;
    private Scanner scanner;
    private Random random;

    public CardLogic(List<Card> deck, List<Player> players) {
        this(deck, players, new Scanner(System.in));
    }

    public CardLogic(List<Card> deck, List<Player> players, Scanner scanner) {
        this.deck = deck;
        this.players = players;
        this.scanner = scanner;
        this.random = new Random();
    }

    public void createDeck() {
        int numDecks;
        do {
            System.out.print("How many decks should be used this round?: ");
            try {
                numDecks = scanner.nextInt();

                if (numDecks < MIN_DECK || numDecks > MAX_DECK) {
                    System.out.println("At least " + MIN_DECK + " or maximum " + MAX_DECK + " of decks can be used!");

                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
                scanner.next();
                numDecks = 0;
            }
        } while (numDecks < MIN_DECK || numDecks > MAX_DECK);

        fillDeck(numDecks);
    }

    public void fillDeck(int numDecks) {
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
            } else if (player.isStanding()) {
                System.out.printf("%s: %d (Standing)%n", player.getName(), player.getHandValue());
            } else {
                System.out.printf("%s: %d%n", player.getName(), player.getHandValue());
            }
        }
        System.out.println();
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
