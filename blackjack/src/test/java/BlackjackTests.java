
package test.java;

import main.java.Game.BlackjackGame;
import main.java.Game.Card;
import main.java.Game.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BlackjackTests {

    /*@Before
    public void init() {

    }

    @After
    public void cleanup() {

    }*/

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

}
