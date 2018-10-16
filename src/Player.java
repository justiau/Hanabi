import java.util.ArrayList;
import java.util.List;

public class Player {
    String name;
    List<Card> hand;

    Player(String name, List<Card> hand) {
        this.name = name;
        this.hand = hand;
    }

    Player(String name, Deck deck) {
        this.name = name;
        List<Card> hand = new ArrayList<>();
        while(hand.size()<4 && deck.size()>0) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
        this.hand = hand;
    }

    public void draw(Deck deck) {
        if (hand.size() < 4 && deck.size() > 0) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
    }

    public void discard(Card card, Discards discards) {
        hand.remove(card);
        discards.add(card);
    }

    public void play(Card card, Board board, Discards discards) {
        if (board.contains(card.prevCard())) {
            board.update(card);
            hand.remove(card);
        }
        else this.discard(card,discards);
    }

    @Override
    public String toString() {
        return name + " " + hand;
    }
}
