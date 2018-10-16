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
        List<Card> emptyList = new ArrayList<>();
        Discards discards = new Discards(emptyList);
        Deck deck = deckBuild();
        deck.shuffle();
        Player player1 = new Player("P1",deck);
        System.out.println(player1);
        player1.discard(player1.hand.get(0),discards);
        System.out.println(player1);
        player1.draw(deck);
        System.out.println(player1);
    }
}
