package com.github.dennismuehlegger.blackjack.game;

import java.util.List;

public class Winner {
    public final List<Player> winners;
    public final int highestScore;

    public Winner(List<Player> winners, int highestScore) {
        this.winners = winners;
        this.highestScore = highestScore;
    }
}
