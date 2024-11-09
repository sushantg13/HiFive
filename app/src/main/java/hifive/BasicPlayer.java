package hifive;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BasicPlayer implements Player {

    @Override
    public Card selectCardToDiscard(Hand hand) {
        List<Card> valueCards = hand.getCardList().stream()
                .filter(card -> {
                    Rank rank = (Rank) card.getRank();
                    return rank.getRankCardValue() >= 2 && rank.getRankCardValue() <= 9;
                })

                .sorted(Comparator.comparingInt((Card card) -> {
                    Suit suit = (Suit) card.getSuit();
                    return suit.getBonusFactor();
                }).thenComparingInt(card -> {
                    Rank rank = (Rank) card.getRank();
                    return rank.getRankCardValue();
                }))
                .collect(Collectors.toList());
        if (!valueCards.isEmpty()) {
            return valueCards.get(0);
        }
        List<Card> pictureCards = hand.getCardList().stream()
                .filter(card -> {
                    Rank rank = (Rank) card.getRank();
                    return rank.getRankCardValue() >= 10 && rank.getRankCardValue() <= 13;
                })
                .sorted(Comparator.comparingInt((Card card) -> {
                    Rank rank = (Rank) card.getRank();
                    return -rank.getRankCardValue();
                }).thenComparingInt(card -> {
                    Suit suit = (Suit) card.getSuit();
                    return -suit.getBonusFactor();
                }))
                .collect(Collectors.toList());
        if (!pictureCards.isEmpty()) {
            return pictureCards.get(0);
        }
        return HiFive.randomCard(new ArrayList<>(hand.getCardList()));
    }

    @Override
    public void playCard(Card card, Hand hand) {
    // No need to implement already handled
    }
}
