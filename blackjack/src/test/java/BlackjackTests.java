
package test.java;

import main.java.Game.BlackjackGame;
import main.java.Game.Card;
import main.java.Game.Player;
import main.java.Logic.CardLogic;
import main.java.Logic.RoundLogic;
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.createPlayers();

        assertEquals("2 players should exist",2, players.size());
        assertEquals("player dennis should exist","dennis", players.get(0).getName());
        assertEquals("player lejla should exist","lejla", players.get(1).getName());

        System.setIn(System.in);
    }

    @Test
    public void testDeckCreation() {
        List<Player> players = new ArrayList<>();
        CardLogic cardLogic = new CardLogic(new ArrayList<>(), players);

        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        cardLogic.fillDeck(2);
        assertEquals("2 decks should have 104 cards", 104, cardLogic.getDeck().size());

        System.setIn(System.in);
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
    public void testInputRoundLogicPlayerBusts() {
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playTurn(dennis);
        roundLogic.playTurn(lejla);

        assertEquals("player dennis should have 22 points",22, dennis.getHandValue());
        assertTrue("player dennis should bust", dennis.isBusted());
        assertEquals("player lejla should have 20 points",20, lejla.getHandValue());
        assertFalse("player lejla should not bust", lejla.isBusted());

        System.setIn(System.in);
    }

    @Test
    public void testInputRoundLogicPlayerStands() {
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
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playTurn(dennis);
        roundLogic.playTurn(lejla);

        assertEquals("player dennis should have 15 points", 15, dennis.getHandValue());
        assertTrue("player dennis should stand", dennis.isStanding());
        assertEquals("player lejla should have 16 points", 16, lejla.getHandValue());
        assertFalse("player lejla should not stand", lejla.isStanding());

        System.setIn(System.in);
    }

    @Test
    public void testInputPlayTurnPlayerReaches21MidRound() {
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

        String input = "yes\nyes\nyes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playTurn(dennis);
        roundLogic.playTurn(lejla);

        assertEquals("player dennis should win with 21 points", 21, dennis.getHandValue());
        assertEquals("player lejla should not win with 20 points", 20, lejla.getHandValue());

        System.setIn(System.in);
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

        System.setIn(System.in);
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
    public void testPlayRoundsHighScoreTie() {
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

        assertEquals("player dennis should have 21 points",21, dennis.getHandValue());
        assertEquals("player lejla should have 21 points",21, lejla.getHandValue());
    }

    //todo these need to be fixed
    /*@Test
    public void testPlayRoundsAllPlayersStand() {
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
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playRounds();

        assertTrue(dennis.isStanding());
        assertTrue(lejla.isStanding());
        assertEquals(19, dennis.getHandValue());
        assertEquals(18, lejla.getHandValue());

        System.setIn(System.in);
    }

    @Test
    public void testPlayRoundsSkipsBustedAndStandingPlayers() {
        List<Card> deck = new ArrayList<>();
        deck.add(new Card(10, "♥", "K"));

        Player dennis = new Player("dennis");
        dennis.addCard(new Card(10, "♦", "10"));
        dennis.addCard(new Card(8, "♣", "8"));

        Player lejla = new Player("lejla");
        lejla.addCard(new Card(10, "♥", "10"));
        lejla.addCard(new Card(9, "♠", "9"));

        List<Player> players = Arrays.asList(dennis, lejla);
        CardLogic cardLogic = new CardLogic(deck, players);
        cardLogic.setRandom(createDeterministicRandom());

        String input = "yes\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BlackjackGame blackjackGame = new BlackjackGame();
        RoundLogic roundLogic = new RoundLogic(cardLogic, blackjackGame, players);

        roundLogic.playRounds();

        assertTrue(dennis.isBusted());
        assertTrue(lejla.isStanding());

        System.setIn(System.in);
    }*/


    private Random createDeterministicRandom() {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };
    }

}
