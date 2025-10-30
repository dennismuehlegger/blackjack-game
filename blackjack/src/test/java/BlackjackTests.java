
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
    public void testPlayerBust() {
        Player player = new Player("dennis");
        player.addCard(new Card(10, "♠", "K"));
        player.addCard(new Card(10, "♥", "Q"));
        player.addCard(new Card(5, "♦", "5"));

        assertTrue(player.isBusted());
        assertEquals(25, player.getHandValue());
    }

    @Test
    public void testPlayerNoBust() {
        Player player = new Player("dennis");
        player.addCard(new Card(10, "♠", "K"));
        player.addCard(new Card(10, "♥", "Q"));
        // ace calculation works as well
        player.addCard(new Card(1, "♦", "A"));

        assertFalse(player.isBusted());
        assertEquals(21, player.getHandValue());
    }

    @Test
    public void testPlayerStand() {
        Player player = new Player("dennis");
        player.addCard(new Card(10, "♠", "K"));
        player.setStanding(true);

        assertTrue(player.isStanding());
    }

    @Test
    public void testRoundLogicPlayerBusts() {
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

        assertEquals(22, dennis.getHandValue());
        assertTrue(dennis.isBusted());
        assertEquals(20, lejla.getHandValue());
        assertFalse(lejla.isBusted());

        System.setIn(System.in);
    }

    @Test
    public void testRoundLogicPlayerStands() {
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

        assertEquals(15, dennis.getHandValue());
        assertTrue(dennis.isStanding());
        assertEquals(16, lejla.getHandValue());
        assertFalse(lejla.isStanding());

        System.setIn(System.in);
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
