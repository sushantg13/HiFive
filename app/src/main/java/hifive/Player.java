package hifive;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface Player {
    Card selectCardToDiscard(Hand hand);
    void playCard(Card card, Hand hand);
}
