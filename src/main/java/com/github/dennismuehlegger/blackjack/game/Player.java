package com.github.dennismuehlegger.blackjack.game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private boolean isOut;
    private boolean standing;

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

    public String getFullHand() {
        StringBuilder handStr = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            handStr.append(card.getRank()).append(card.getSuit());
            if (i < hand.size() - 1) {
                handStr.append(", ");
            }
        }
        return handStr.toString();
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

    public boolean isBusted() {
        return getHandValue() > 21;
    }

    public boolean isStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public boolean hasHighScore() {
        return getHandValue() == 21;
    }

    @Override
    public String toString() {
        return name;
    }
}
