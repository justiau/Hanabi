import java.util.ArrayList;
import java.util.List;

public class Deck {
    List<Card> cards;

    Deck(List<Card> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        List<Card> newCards = new ArrayList<>();
        while(!cards.isEmpty()) {
            int random = (int) (Math.random()*cards.size());
            newCards.add(cards.get(random));
            cards.remove(random);
        }
        this.cards = newCards;
    }

    public Card get(int n) {return cards.get(n);}

    public void remove(int n) {cards.remove(n);}

    public int size() {return cards.size();}

    @Override
    public String toString() {return cards.toString();}
}
