import java.util.List;

public class Discards {
    List<Card> cards;

    Discards(List<Card> cards) {
        this.cards = cards;
    }

    public void add(Card card) {
        cards.add(card);
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
