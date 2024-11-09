package hifive;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class HumanPlayer implements Player {
    private HiFive game;

    public HumanPlayer(HiFive game) {
        this.game = game;
    }

    @Override
    public Card selectCardToDiscard(Hand hand) {
        return game.getSelectedCard();
    }

    @Override
    public void playCard(Card card, Hand hand) {
    // No need to implement already handled
    }
}
