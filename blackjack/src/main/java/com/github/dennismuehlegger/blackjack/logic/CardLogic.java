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
    private static final int DEAL_DELAY_MS = 1000;
    private static final int HAND_SEPARATOR_LENGTH = 30;
    private static final String[] SUITS = {"♠", "♥", "♦", "♣"};

    private static final int MIN_NUMBER_VALUE = 2;
    private static final int MAX_NUMBER_VALUE = 9;
    private static final int FACE_CARD_VALUE = 10;
    private static final int ACE_VALUE = 11;

    private final List<Card> deck;
    private final List<Player> players;
    private final Scanner scanner;
    private final Random random;

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
        int numDecks = promptForNumberOfDecks();
        fillDeck(numDecks);
    }

    private int promptForNumberOfDecks() {
        int numDecks;
        do {
            System.out.print("How many decks should be used this round?: ");
            try {
                numDecks = scanner.nextInt();

                if (isInvalidDeckCount(numDecks)) {
                    printDeckCountError();
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
                scanner.next();
                numDecks = 0;
            }
        } while (isInvalidDeckCount(numDecks));

        return numDecks;
    }

    private boolean isInvalidDeckCount(int count) {
        return count < MIN_DECK || count > MAX_DECK;
    }

    private void printDeckCountError() {
        System.out.println("At least " + MIN_DECK + " or maximum " + MAX_DECK + " of decks can be used!");
    }

    public void fillDeck(int numDecks) {
        for (int i = 0; i < numDecks; i++) {
            addSingleDeckToDeck();
        }
    }

    private void addSingleDeckToDeck() {
        for (String suit : SUITS) {
            addNumberCards(suit);
            addFaceCards(suit);
        }
    }

    private void addNumberCards(String suit) {
        for (int value = MIN_NUMBER_VALUE; value <= MAX_NUMBER_VALUE; value++) {
            deck.add(new Card(value, suit, String.valueOf(value)));
        }
    }

    private void addFaceCards(String suit) {
        deck.add(new Card(FACE_CARD_VALUE, suit, "10"));
        deck.add(new Card(FACE_CARD_VALUE, suit, "J"));
        deck.add(new Card(FACE_CARD_VALUE, suit, "Q"));
        deck.add(new Card(FACE_CARD_VALUE, suit, "K"));
        deck.add(new Card(ACE_VALUE, suit, "A"));
    }

    public Card drawCard() {
        int index = random.nextInt(deck.size());
        return deck.remove(index);
    }

    public void dealInitialCards() throws InterruptedException {
        dealRound("First deal");
        dealRound("Second deal");

        printHandSeparator();
        showAllHands();
    }

    private void dealRound(String roundName) throws InterruptedException {
        System.out.println("--- " + roundName + " ---");

        for (Player player : players) {
            dealCardToPlayer(player);
        }

        System.out.println();
    }

    private void dealCardToPlayer(Player player) throws InterruptedException {
        Card card = drawCard();
        player.addCard(card);
        System.out.println(player + " gets: " + card);
        Thread.sleep(DEAL_DELAY_MS);
    }

    private void printHandSeparator() {
        System.out.println("=".repeat(HAND_SEPARATOR_LENGTH));
    }

    public void showAllHands() {
        System.out.println("\n=== Current hand ===");

        for (Player player : players) {
            printPlayerHand(player);
        }

        System.out.println();
    }

    private void printPlayerHand(Player player) {
        String status = getPlayerStatus(player);
        System.out.printf("%s: %d%s%n", player.getName(), player.getHandValue(), status);
    }

    private String getPlayerStatus(Player player) {
        if (player.isOut()) {
            return " (Busted)";
        } else if (player.isStanding()) {
            return " (Standing)";
        }
        return "";
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck.clear();
        this.deck.addAll(deck);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players.clear();
        this.players.addAll(players);
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
    }
}
