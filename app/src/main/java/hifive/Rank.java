package hifive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Rank {
    ACE(1, 1),
    KING(13, 13),
    QUEEN(12, 12),
    JACK(11, 11),
    TEN(10, 10),
    NINE(9, 9),
    EIGHT(8, 8),
    SEVEN(7, 7),
    SIX(6, 6),
    FIVE(5, 5),
    FOUR(4, 4),
    THREE(3, 3),
    TWO(2, 2);

    private int rankCardValue;
    private int scoreValue;

    Rank(int rankCardValue, int scoreValue) {
        this.rankCardValue = rankCardValue;
        this.scoreValue = scoreValue;
    }

    public int getRankCardValue() {
        return rankCardValue;
    }

    public int getScoreCardValue() {
        return scoreValue;
    }

    public String getRankCardLog() {
        return String.format("%d", rankCardValue);
    }

    public boolean isWildCard() {
        return this == ACE || this == JACK || this == QUEEN || this == KING;
    }

    public List<Integer> getPossibleValues() {
        switch (this) {
            case ACE:
                return Arrays.asList(1, 11, 12, 13);
            case JACK:
                return Arrays.asList(1, 2, 3, 4, 11);
            case QUEEN:
                return Arrays.asList(6, 7, 8, 9, 12);
            case KING:
                return Arrays.asList(1, 3, 7, 9, 11, 13);
            default:
                return Collections.singletonList(this.rankCardValue);
        }
    }
}
