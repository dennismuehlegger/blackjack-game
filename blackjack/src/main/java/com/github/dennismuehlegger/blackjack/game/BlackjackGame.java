package com.github.dennismuehlegger.blackjack.game;

import com.github.dennismuehlegger.blackjack.logic.CardLogic;
import com.github.dennismuehlegger.blackjack.logic.RoundLogic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BlackjackGame {
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
