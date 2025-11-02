
package com.github.dennismuehlegger.blackjack;

import com.github.dennismuehlegger.blackjack.game.BlackjackGame;
import com.github.dennismuehlegger.blackjack.game.Card;
import com.github.dennismuehlegger.blackjack.game.Player;
import com.github.dennismuehlegger.blackjack.logic.CardLogic;
import com.github.dennismuehlegger.blackjack.logic.RoundLogic;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.Assert.*;

public class BlackjackTests {

    @Test
    public void testPlayerLimit() {
        List<Card> deck = new ArrayList<>();
        List<Player> players = new ArrayList<>();

        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        String input = "8\n2\ndennis\nlejla\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.createPlayers();

        assertEquals("2 players should exist", 2, players.size());
        assertEquals("player dennis should exist", "dennis", players.get(0).getName());
        assertEquals("player lejla should exist", "lejla", players.get(1).getName());
    }

    @Test
    public void testPlayerInputMismatch() {
        List<Card> deck = new ArrayList<>();
        List<Player> players = new ArrayList<>();

        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        String input = "f\n2\ndennis\nlejla\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.createPlayers();

        assertEquals("2 players should exist", 2, players.size());
        assertEquals("player dennis should exist", "dennis", players.get(0).getName());
        assertEquals("player lejla should exist", "lejla", players.get(1).getName());
    }

    @Test
    public void testDeckLimit() {
        List<Card> deck = new ArrayList<>();
        List<Player> players = new ArrayList<>();

        String input = "9\n2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        CardLogic cardLogic = new CardLogic(deck, players, scanner);
        cardLogic.setRandom(createDeterministicRandom());

        cardLogic.createDeck();

        assertNotEquals("deck size should not be 468", 468, deck.size());
        assertEquals("deck size should be 104", 104, deck.size());
    }

    @Test
    public void testDeckInputMismatch() {
        List<Card> deck = new ArrayList<>();
        List<Player> players = new ArrayList<>();

        String input = "d\n2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        CardLogic cardLogic = new CardLogic(deck, players, scanner);
        cardLogic.setRandom(createDeterministicRandom());

        cardLogic.createDeck();
        assertEquals("deck size should be 104", 104, deck.size());
    }

    @Test
    public void testDeckCreation() {
        List<Player> players = new ArrayList<>();

        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        CardLogic cardLogic = new CardLogic(new ArrayList<>(), players, scanner);

        cardLogic.fillDeck(2);
        assertEquals("2 decks should have 104 cards", 104, cardLogic.getDeck().size());
    }

    @Test
    public void testAceCalculation() {
        Player dennis = new Player("dennis");
        dennis.addCard(new Card(11, "♠", "A"));
        dennis.addCard(new Card(5, "♥", "5"));

        assertEquals("Ace should be 11 when safe", 16, dennis.getHandValue());
        assertFalse(dennis.isBusted());
    }

    @Test
    public void testAceCalculationPotentialBust() {
        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(10, "♣", "K"));
        dennis.addCard(new Card(11, "♠", "A"));

        assertEquals("Ace should be 1 to avoid bust", 21, dennis.getHandValue());
        assertFalse(dennis.isBusted());
    }

    @Test
    public void testInputPlayerBusts() {
        List<Card> deck = new ArrayList<>();
        deck.add(new Card(6, "♥", "6"));
        deck.add(new Card(10, "♠", "K"));

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(6, "♣", "6"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(7, "♦", "7"));
        lejla.addCard(new Card(3, "♥", "3"));

        List<Player> players = Arrays.asList(dennis, lejla);

        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        String input = "yes\nyes\nyes\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.playTurn(dennis);
        roundLogic.playTurn(lejla);

        assertEquals("player dennis should have 22 points", 22, dennis.getHandValue());
        assertTrue("player dennis should bust", dennis.isBusted());
        assertEquals("player lejla should have 20 points", 20, lejla.getHandValue());
        assertFalse("player lejla should not bust", lejla.isBusted());
    }

    @Test
    public void testInputPlayerStands() {
        List<Card> deck = new ArrayList<>();
        deck.add(new Card(6, "♥", "6"));

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(5, "♣", "5"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(7, "♦", "7"));
        lejla.addCard(new Card(3, "♥", "3"));

        List<Player> players = Arrays.asList(dennis, lejla);

        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        String input = "no\nyes\nyes\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.playTurn(dennis);
        roundLogic.playTurn(lejla);

        assertEquals("player dennis should have 15 points", 15, dennis.getHandValue());
        assertTrue("player dennis should stand", dennis.isStanding());
        assertEquals("player lejla should have 16 points", 16, lejla.getHandValue());
        assertFalse("player lejla should not stand", lejla.isStanding());
    }

    @Test
    public void testPlayRoundsAllPlayersBust() {
        List<Card> deck = new ArrayList<>();

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(6, "♣", "6"));
        dennis.addCard(new Card(6, "♣", "6"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(10, "♥", "10"));
        lejla.addCard(new Card(8, "♠", "8"));
        lejla.addCard(new Card(6, "♣", "6"));

        List<Player> players = Arrays.asList(dennis, lejla);
        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playRounds();

        assertTrue("player dennis should bust", dennis.isBusted());
        assertTrue("player lejla should bust", lejla.isBusted());
    }

    @Test
    public void testPlayRoundsImmediateHighScoreSingleWinner() {
        List<Card> deck = new ArrayList<>();

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(11, "♠", "A"));
        dennis.addCard(new Card(10, "♥", "K"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(10, "♦", "10"));
        lejla.addCard(new Card(9, "♣", "9"));

        List<Player> players = Arrays.asList(dennis, lejla);
        CardLogic cardLogic = new CardLogic(deck, players);

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playRounds();

        assertEquals("player dennis should have 21 points", 21, dennis.getHandValue());
        assertEquals("player lejla should have 19 points", 19, lejla.getHandValue());
    }

    @Test
    public void testPlayRoundsImmediateHighScoreTie() {
        List<Card> deck = new ArrayList<>();

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(11, "♠", "A"));
        dennis.addCard(new Card(10, "♥", "K"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(11, "♦", "A"));
        lejla.addCard(new Card(10, "♣", "Q"));

        List<Player> players = Arrays.asList(dennis, lejla);
        CardLogic cardLogic = new CardLogic(deck, players);

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playRounds();

        assertEquals("player dennis should have 21 points", 21, dennis.getHandValue());
        assertEquals("player lejla should have 21 points", 21, lejla.getHandValue());
    }

    @Test
    public void testInputPlayRoundsAllPlayersStand() {
        List<Card> deck = new ArrayList<>();

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(9, "♣", "9"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(10, "♥", "10"));
        lejla.addCard(new Card(8, "♠", "8"));

        List<Player> players = Arrays.asList(dennis, lejla);
        CardLogic cardLogic = new CardLogic(deck, players);

        String input = "no\nno\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame(deck, players);
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.playRounds();

        assertTrue(dennis.isStanding());
        assertTrue(lejla.isStanding());
        assertEquals(19, dennis.getHandValue());
        assertEquals(18, lejla.getHandValue());
    }

    @Test
    public void testInputPlayRoundsWinMidRound() {
        List<Card> deck = new ArrayList<>();
        deck.add(new Card(6, "♥", "6"));
        deck.add(new Card(10, "♥", "10"));

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(5, "♣", "5"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(7, "♦", "7"));
        lejla.addCard(new Card(3, "♥", "3"));

        List<Player> players = Arrays.asList(dennis, lejla);
        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        String input = "yes\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame(deck, players);
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.playRounds();

        assertEquals("player dennis should have 21 points", 21, dennis.getHandValue());
        assertEquals("player lejla should have 10 points",10, lejla.getHandValue());
    }

    @Test
    public void testInputPlayRoundsStandingPlayersTie() {
        List<Card> deck = new ArrayList<>();
        deck.add(new Card(3, "♥", "6"));
        deck.add(new Card(8, "♥", "10"));

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(5, "♣", "5"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(7, "♦", "7"));
        lejla.addCard(new Card(3, "♥", "3"));

        List<Player> players = Arrays.asList(dennis, lejla);
        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        String input = "yes\nyes\nno\nno\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame(deck, players);
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.playRounds();

        assertEquals("player dennis should have 18 points", 18, dennis.getHandValue());
        assertEquals("player lejla should have 18 points",18, lejla.getHandValue());
    }

    @Test
    public void testInputPlayRoundsSkipsBustedAndStandingPlayers() {
        List<Card> deck = new ArrayList<>();
        deck.add(new Card(10, "♥", "K"));
        deck.add(new Card(2, "♥", "2"));
        deck.add(new Card(3, "♥", "3"));
        deck.add(new Card(4, "♥", "4"));

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(8, "♣", "8"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(10, "♥", "10"));
        lejla.addCard(new Card(9, "♠", "9"));

        Player ethan = new Player("ethan");
        ethan.addCard(new Card(6, "♥", "6"));
        ethan.addCard(new Card(9, "♠", "9"));

        Player ellis = new Player("ellis");
        ellis.addCard(new Card(2, "♥", "2"));
        ellis.addCard(new Card(7, "♠", "7"));

        List<Player> players = Arrays.asList(dennis, lejla, ethan, ellis);
        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        // Input sequence: dennis draws a card and busts, lejla stands, both of them get skipped for the remainder of the game
        // ethan draws 2 and gets to 17 points, ellis draws 3 and gets to 12 points,
        // ethan draws 4 and gets to 21 points, ellis doesn't get asked again since ethan reached 21 before his turn
        String input = "yes\nno\nyes\nyes\nyes\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame(deck, players);
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players, scanner);

        roundLogic.playRounds();

        assertTrue("player dennis should be busted", dennis.isBusted());
        assertTrue("player lejla should be standing", lejla.isStanding());
        assertEquals("player dennis should have 28 points", 28, dennis.getHandValue());
        assertEquals("player lejla should have 19 points",19, lejla.getHandValue());
        assertEquals("player ethan should have 21 points",21, ethan.getHandValue());
        assertEquals("player ellis should have 12 points",12, ellis.getHandValue());
    }


    private Random createDeterministicRandom() {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };
    }

}
