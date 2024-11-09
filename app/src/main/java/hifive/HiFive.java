package hifive;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class HiFive extends CardGame {

    final String trumpImage[] = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};

    static public final int seed = 30008;
    static final Random random = new Random(seed);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    private List<List<String>> playerAutoMovements = new ArrayList<>();

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId();
    }

    private final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 2;
    public final int nbFaceUpCards = 2;
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private static final int FIVE_GOAL = 5;
    private static final int FIVE_POINTS = 100;
    private static final int SUM_FIVE_POINTS = 60;
    private static final int DIFFERENCE_FIVE_POINTS = 20;
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(575, 575)
    };
    private Actor[] scoreActors = {null, null, null, null};
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    private int thinkingTime = 2000;
    private int delayTime = 600;
    private Hand[] hands;

    public void setStatus(String string) {
        setStatusText(string);
    }

    private int[] scores = new int[nbPlayers];

    private int[] autoIndexHands = new int[nbPlayers];
    private boolean isAuto = false;
    private Hand playingArea;
    private Hand pack;

    Font bigFont = new Font("Arial", Font.BOLD, 36);
    Color bgColor = Color.BLACK; // Assuming a background color

    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
            String text = "[" + scores[i] + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private int findIndexWithHighestScore(int[] scoreArray) {
        int maxScore = -1;
        int maxScoreIndex = 0;
        for (int i = 0; i < scoreArray.length; i++) {
            if (maxScore < scoreArray[i]) {
                maxScoreIndex = i;
                maxScore = scoreArray[i];
            }
        }
        return maxScoreIndex;
    }

    void calculateScoreEndOfRound() {
        for (int i = 0; i < hands.length; i++) {
            scores[i] = scoreForHiFive(i);
        }
    }

    void updateScore(int player) {
        removeActor(scoreActors[player]);
        int displayScore = Math.max(scores[player], 0);
        String text = "P" + player + "[" + displayScore + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private void initScores() {
        Arrays.fill(scores, 0);
    }

    private Card selected;

    public Card getSelectedCard() {
        return selected;
    }

    private void initGame() {
        hands = new Hand[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
        }
        playingArea = new Hand(deck);
        dealingOut(hands, nbPlayers, nbStartCards, nbFaceUpCards);
        playingArea.setView(this, new RowLayout(trickLocation, (playingArea.getNumberOfCards() + 2) * trickWidth));
        playingArea.draw();

        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, false);
        }
        CardListener cardListener = new CardAdapter() {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hands[0].setTouchEnabled(false);
            }
        };
        hands[0].addCardListener(cardListener);
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(this, layouts[i]);
            hands[i].setTargetArea(new TargetArea(trickLocation));
            hands[i].draw();
        }
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    public Card getRandomCard(Hand hand) {
        dealACardToHand(hand);
        delay(thinkingTime);
        int x = random.nextInt(hand.getCardList().size());
        return hand.getCardList().get(x);
    }

    private Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue;
        try {
            rankValue = Integer.parseInt(rankString);
        } catch (NumberFormatException e) {
            switch (rankString.toUpperCase()) {
                case "A":
                    return Rank.ACE;
                case "J":
                    return Rank.JACK;
                case "Q":
                    return Rank.QUEEN;
                case "K":
                    return Rank.KING;
                default:
                    return Rank.ACE;
            }
        }
        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }
        return Rank.ACE;
    }

    private Suit getSuitFromString(String cardName) {
        String suitString = cardName.substring(cardName.length() - 1);
        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equalsIgnoreCase(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }

    private Card getCardFromList(List<Card> cards, String cardName) {
        Rank cardRank = getRankFromString(cardName);
        Suit cardSuit = getSuitFromString(cardName);
        for (Card card : cards) {
            if (card.getSuit() == cardSuit && card.getRank() == cardRank) {
                return card;
            }
        }
        return null;
    }

    Card applyAutoMovement(Hand hand, String nextMovement) {
        if (pack.isEmpty()) return null;
        String[] cardStrings = nextMovement.split("-");
        String cardDealtString = cardStrings[0];
        Card dealt = getCardFromList(pack.getCardList(), cardDealtString);
        if (dealt != null) {
            dealt.removeFromHand(false);
            hand.insert(dealt, true);
        } else {
            System.out.println("cannot draw card: " + cardDealtString + " - hand: " + hand);
        }

        if (cardStrings.length > 1) {
            String cardDiscardString = cardStrings[1];
            return getCardFromList(hand.getCardList(), cardDiscardString);
        } else {
            return null;
        }
    }

    List<Integer> generatePossibleValues(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getPossibleValues();
    }

    int scoreForHiFive(int playerIndex) {
        List<Card> privateCards = hands[playerIndex].getCardList();
        List<Integer> values1 = generatePossibleValues(privateCards.get(0));
        List<Integer> values2 = generatePossibleValues(privateCards.get(1));
        int maxScore = scoreForNoFive(privateCards);
        for (int val1 : values1) {
            for (int val2 : values2) {
                if (val1 == FIVE_GOAL || val2 == FIVE_GOAL) {
                    int score = FIVE_POINTS;
                    if (val1 == FIVE_GOAL) {
                        score += ((Suit) privateCards.get(0).getSuit()).getBonusFactor();
                    }
                    if (val2 == FIVE_GOAL) {
                        score += ((Suit) privateCards.get(1).getSuit()).getBonusFactor();
                    }
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
                if (val1 + val2 == FIVE_GOAL) {
                    int score = SUM_FIVE_POINTS +
                            ((Suit) privateCards.get(0).getSuit()).getBonusFactor() +
                            ((Suit) privateCards.get(1).getSuit()).getBonusFactor();
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
                if (Math.abs(val1 - val2) == FIVE_GOAL) {
                    int score = DIFFERENCE_FIVE_POINTS +
                            ((Suit) privateCards.get(0).getSuit()).getBonusFactor() +
                            ((Suit) privateCards.get(1).getSuit()).getBonusFactor();
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
            }
        }
        return maxScore;
    }

    int scoreForNoFive(List<Card> privateCards) {
        int sum = 0;
        for (Card card : privateCards) {
            Rank rank = (Rank) card.getRank();
            sum += rank.getRankCardValue();
        }
        return sum;
    }

    void dealingOut(Hand[] hands, int nbPlayers, int nbCardsPerPlayer, int nbSharedCards) {
        pack = deck.toHand(false);
        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = properties.getProperty(initialCardsKey);
            if (initialCardsValue == null) {
                continue;
            }
            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(false);
                    hands[i].insert(card, false);
                }
            }
        }
        for (int i = 0; i < nbPlayers; i++) {
            int cardsToDeal = nbCardsPerPlayer - hands[i].getNumberOfCards();
            for (int j = 0; j < cardsToDeal; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(new ArrayList<>(pack.getCardList()));
                dealt.removeFromHand(false);
                hands[i].insert(dealt, false);
            }
        }
    }

    void dealACardToHand(Hand hand) {
        if (pack.isEmpty()) return;
        Card dealt = randomCard(new ArrayList<>(pack.getCardList()));
        dealt.removeFromHand(false);
        hand.insert(dealt, true);
    }

    private void addCardPlayedToLog(int player, List<Card> cards) {
        if (cards.size() < 2) {
            return;
        }
        logResult.append("P").append(player).append("-");
        for (int i = 0; i < cards.size(); i++) {
            Rank cardRank = (Rank) cards.get(i).getRank();
            Suit cardSuit = (Suit) cards.get(i).getSuit();
            logResult.append(cardRank.getRankCardLog()).append(cardSuit.getSuitShortHand());
            if (i < cards.size() - 1) {
                logResult.append("-");
            }
        }
        logResult.append(",");
    }

    private void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round").append(roundNumber).append(":");
    }

    private void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i]).append(",");
        }
        logResult.append("\n");
    }

    private void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i]).append(",");
        }
        logResult.append("\n");
        logResult.append("Winners:").append(String.join(", ", winners.stream()
                .map(String::valueOf)
                .collect(Collectors.toList())));
    }

    private void playGame() {
        int roundNumber = 1;
        for (int i = 0; i < nbPlayers; i++) updateScore(i);
        addRoundInfoToLog(roundNumber);
        int nextPlayer = 0;
        while (roundNumber <= 4) {
            selected = null;
            boolean finishedAuto = false;
            Player currentPlayer = players[nextPlayer];
            if (isAuto) {
                int nextPlayerAutoIndex = autoIndexHands[nextPlayer];
                List<String> nextPlayerMovement = playerAutoMovements.get(nextPlayer);
                String nextMovement = "";
                if (nextPlayerMovement.size() > nextPlayerAutoIndex) {
                    nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex);
                    nextPlayerAutoIndex++;
                    autoIndexHands[nextPlayer] = nextPlayerAutoIndex;
                    Hand nextHand = hands[nextPlayer];
                    selected = applyAutoMovement(nextHand, nextMovement);
                    delay(delayTime);
                    if (selected != null) {
                        selected.removeFromHand(true);
                    } else {
                        selected = currentPlayer.selectCardToDiscard(hands[nextPlayer]);
                        if (selected != null) {
                            selected.removeFromHand(true);
                        }
                    }
                } else {
                    finishedAuto = true;
                }
            }
            if (!isAuto || finishedAuto) {
                if (nextPlayer == 0 && currentPlayer instanceof HumanPlayer) {
                    hands[0].setTouchEnabled(true);
                    setStatus("Player 0 is playing. Please double click on a card to discard");
                    selected = null;
                    dealACardToHand(hands[0]);
                    while (selected == null) delay(delayTime);
                    currentPlayer.playCard(selected, hands[nextPlayer]);
                } else {
                    setStatusText("Player " + nextPlayer + " thinking...");
                    dealACardToHand(hands[nextPlayer]);
                    selected = currentPlayer.selectCardToDiscard(hands[nextPlayer]);
                    if (selected != null) {
                        selected.removeFromHand(true);
                    }
                }
            }
            addCardPlayedToLog(nextPlayer, hands[nextPlayer].getCardList());
            if (selected != null) {
                selected.setVerso(false);
                delay(delayTime);
            }
            scores[nextPlayer] = scoreForHiFive(nextPlayer);
            updateScore(nextPlayer);
            nextPlayer = (nextPlayer + 1) % nbPlayers;
            if (nextPlayer == 0) {
                roundNumber++;
                addEndOfRoundToLog();
                if (roundNumber <= 4) {
                    addRoundInfoToLog(roundNumber);
                }
            }
            if (roundNumber > 4) {
                calculateScoreEndOfRound();
            }
            delay(delayTime);
        }
        determineWinners();
    }

    private void determineWinners() {
        for (int i = 0; i < nbPlayers; i++) updateScore(i);
        int maxScore = Arrays.stream(scores).max().orElse(0);
        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < nbPlayers; i++) {
            if (scores[i] == maxScore) {
                winners.add(i);
            }
        }
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " + winners.get(0);
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText(winText);
        refresh();
        addEndOfGameToLog(winners);
    }

    private void setupPlayerAutoMovements() {
        String player0AutoMovement = properties.getProperty("players.0.cardsPlayed");
        String player1AutoMovement = properties.getProperty("players.1.cardsPlayed");
        String player2AutoMovement = properties.getProperty("players.2.cardsPlayed");
        String player3AutoMovement = properties.getProperty("players.3.cardsPlayed");
        String[] playerMovements = new String[]{"", "", "", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }
        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }
        if (player2AutoMovement != null) {
            playerMovements[2] = player2AutoMovement;
        }
        if (player3AutoMovement != null) {
            playerMovements[3] = player3AutoMovement;
        }
        for (int i = 0; i < playerMovements.length; i++) {
            String movementString = playerMovements[i];
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
    }

    private Player[] players = new Player[nbPlayers];

    private void initializePlayers() {
        for (int i = 0; i < nbPlayers; i++) {
            String playerType = properties.getProperty("players." + i, "random").toLowerCase();
            switch (playerType) {
                case "human":
                    players[i] = new HumanPlayer(this);
                    break;
                case "random":
                    players[i] = new RandomPlayer();
                    break;
                case "basic":
                    players[i] = new BasicPlayer();
                    break;
                case "clever":
                    players[i] = new CleverPlayer(this, i);
                    break;
                default:
                    players[i] = new RandomPlayer();
            }
        }
    }

    public String runApp() {
        setTitle("HiFive (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initScores();
        initScore();
        setupPlayerAutoMovements();
        initializePlayers();
        initGame();
        playGame();
        return logResult.toString();
    }

    public HiFive(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto", "false"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "2000"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "600"));
    }

    int calculatePotentialScoreBasedOnCards(List<Card> cards) {
        if (cards.size() != 2) return 0;
        List<Integer> values1 = generatePossibleValues(cards.get(0));
        List<Integer> values2 = generatePossibleValues(cards.get(1));
        int maxScore = scoreForNoFive(cards);
        for (int val1 : values1) {
            for (int val2 : values2) {
                if (val1 == FIVE_GOAL || val2 == FIVE_GOAL) {
                    int score = FIVE_POINTS;
                    if (val1 == FIVE_GOAL) {
                        score += ((Suit) cards.get(0).getSuit()).getBonusFactor();
                    }
                    if (val2 == FIVE_GOAL) {
                        score += ((Suit) cards.get(1).getSuit()).getBonusFactor();
                    }
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
                if (val1 + val2 == FIVE_GOAL) {
                    int score = SUM_FIVE_POINTS +
                            ((Suit) cards.get(0).getSuit()).getBonusFactor() +
                            ((Suit) cards.get(1).getSuit()).getBonusFactor();
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
                if (Math.abs(val1 - val2) == FIVE_GOAL) {
                    int score = DIFFERENCE_FIVE_POINTS +
                            ((Suit) cards.get(0).getSuit()).getBonusFactor() +
                            ((Suit) cards.get(1).getSuit()).getBonusFactor();
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
            }
        }
        return maxScore;
    }
}

