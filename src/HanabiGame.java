import java.util.ArrayList;
import java.util.List;

public class HanabiGame {
    public static Deck deckBuild() {
        List<Card> cards = new ArrayList<>();
        for (int n = 1; n <= 5; n++) {
            for (Colour c : Colour.values()) {
                if (n==1) {
                    for (int i = 0; i < 3; i++) {
                        cards.add(new Card (n,c));
                    }
                }
                if (n>=2 && n<= 4) {
                    for (int i = 0; i < 2; i++) {
                        cards.add(new Card (n,c));
                    }
                }
                if (n==5) {
                    cards.add(new Card (n,c));
                }
            }
        }
        Deck deck = new Deck(cards);
        return deck;
    }
    public static void main(String[] args) {
        Deck deck = deckBuild();
        deck.shuffle();
        System.out.println(deck);
        System.out.println(deck.size());
    }
}
