package hifive;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class RandomPlayer implements Player {

    @Override
    public Card selectCardToDiscard(Hand hand) {
        if (hand.isEmpty()) return null;
        return HiFive.randomCard(new ArrayList<>(hand.getCardList()));
    }

    @Override
    public void playCard(Card card, Hand hand) {
    // No need to implement already handled
    }
}
