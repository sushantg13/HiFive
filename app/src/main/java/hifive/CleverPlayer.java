package hifive;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;

public class CleverPlayer implements Player {
    private HiFive game;
    private int playerIndex;

    public CleverPlayer(HiFive game, int playerIndex) {
        this.game = game;
        this.playerIndex = playerIndex;
    }

    @Override
    public Card selectCardToDiscard(Hand hand) {
        List<Card> possibleCards = new ArrayList<>(hand.getCardList());
        Card bestCard = null;
        int maxScore = -1;
        for (Card card : possibleCards) {
            List<Card> remainingCards = new ArrayList<>(possibleCards);
            remainingCards.remove(card);
            int potentialScore = game.calculatePotentialScoreBasedOnCards(remainingCards);
            if (potentialScore > maxScore) {
                maxScore = potentialScore;
                bestCard = card;
            }
        }
        return bestCard != null ? bestCard : HiFive.randomCard(new ArrayList<>(possibleCards));
    }

    @Override
    public void playCard(Card card, Hand hand) {
    // No need to implement already handled
    }
}
