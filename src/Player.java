import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Player {
    String name;
    List<Card> hand;
    List<View> view = new ArrayList<>();
    Card[] knowledge;
    int handLimit = 5;

    Player(String name, List<Card> hand, List<View> view, int playercount, Card[] knowledge) {
        this.name = name;
        this.hand = hand;
        this.view = view;
        this.knowledge = knowledge;
        if (playercount >= 4) handLimit=4;
    }

    Player(String name, Deck deck, int playercount) {
        this.name = name;
        if (playercount >= 4) handLimit=4;
        knowledge = new Card[handLimit];
        Arrays.fill(knowledge,0, handLimit-1,new Card(0,null));
        List<Card> hand = new ArrayList<>();
        while(hand.size()<handLimit && deck.size()>0) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
        this.hand = hand;
    }

    public void draw(Deck deck, int players) {
        if (hand.size() < handLimit && deck.size() > 0) {
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

    public void learn(Set<Integer> cardNumbers, Colour colour) {
        for(Integer i: cardNumbers)
            knowledge[i].colour = colour;
    }

    public void learn(Set<Integer> cardNumbers, int number) {
        for(Integer i: cardNumbers)
            knowledge[i].number = number;
    }

    public List<Card[]> possibleHints() {
        List<Card[]> hints = new ArrayList<>();
        boolean b;
        Card[] hint;
        for (int i = 1; i <= 5; i++) {
            hint = new Card[hand.size()];
            b = false;
            for (int j = 0; j < hand.size(); j++) {
                if (hand.get(j).number == i) {
                    hint[j] = new Card(i, null);
                    b = true;
                }
            }
            if (b == true) hints.add(hint);
        }
        for (Colour c : Colour.values()) {
            hint = new Card[hand.size()];
            b = false;
            for (int j = 0; j < hand.size(); j++) {
                if (hand.get(j).colour == c) {
                    hint[j] = new Card(0, c);
                    b = true;
                }
            }
            if (b == true) hints.add(hint);
        }
        return hints;
    }

    public MoveType generateMove() {
        return MoveType.values()[(int)(Math.random()*3)];
    }

    @Override
    public String toString() {
        String rtn1 = name + " " + hand;
        String rtn2 = "";
        for (int i=0; i<hand.size();i++) {
            if (knowledge[i] != null) {
                rtn2 += "\n";
                if (knowledge[i].colour==null)
                    rtn2 += "Card " + i + " is a " + knowledge[i].number;
                else if (knowledge[i].number==0)
                    rtn2 += "Card " + i + " is " + knowledge[i].colour;
                else {
                    rtn2 += "Card " + i + " is a " + knowledge[i].number + " " + knowledge[i].colour;
                }
            }
        }
        return rtn1 + rtn2;
    }
}
