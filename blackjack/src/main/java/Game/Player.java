package main.java.Game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private boolean isOut;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isOut = false;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public int getHandValue() {
        int value = 0;
        int aces = 0;

        for (Card card : hand) {
            value += card.getValue();
            if (card.getValue() == 11) {
                aces++;
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public boolean isBust() {
        return getHandValue() > 21;
    }

    public boolean hasBlackjack() {
        return getHandValue() == 21;
    }

    @Override
    public String toString() {
        return name;
    }
}
