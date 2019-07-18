import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Player {
    String name;
    List<Card> hand;
    List<View> views = new ArrayList<>();
    List<Card> knowledge;
    int handLimit = 5;

    Player(String name, List<Card> hand, List<View> views, int playercount, List<Card> knowledge) {
        this.name = name;
        this.hand = hand;
        this.views = views;
        this.knowledge = knowledge;
        if (playercount >= 4) handLimit=4;
    }

    Player(String name, Deck deck, int playercount) {
        this.name = name;
        if (playercount >= 4) handLimit=4;
        knowledge = new ArrayList<Card>();
        for (int i=0; i<handLimit; i++) {
            knowledge.add(new Card(0, null));
        }
        List<Card> hand = new ArrayList<>();
        while(hand.size()<handLimit && deck.size()>0) {
            hand.add(deck.get(0));
            deck.remove(0);
        }
        this.hand = hand;
    }

    public Status draw(Deck deck) {
        if (hand.size() < handLimit && deck.size() > 0) {
            hand.add(deck.get(0));
            deck.remove(0);
            if (deck.size()==0) return Status.LASTCARD;
            return Status.OK;
        }
        return Status.LASTROUND;
    }

    public void discard (int index, Board board) {
        board.discards.add(hand.get(index));
        hand.remove(index);
        knowledge.remove(index);
        knowledge.add(new Card(0, null));
    }

    public Status play(int index, Board board) {
        Card card = hand.get(index);
        if (board.contains(card.prevCard())) {
            board.update(card);
            hand.remove(index);
            knowledge.remove(index);
            knowledge.add(new Card(0, null));
            if (board.isVictory()) return Status.VICTORY;
            return Status.OK;
        }
        discard(index,board);
        if (board.isLastBomb()) return Status.DEFEAT;
        return Status.OK;
    }

    public void learn(Hint hint) {
        if (hint.colour==null) {
            for (Integer i: hint.cardIndices) {
                knowledge.get(i).number = hint.number;
            }
        } else {
            for (Integer i: hint.cardIndices) {
                knowledge.get(i).colour = hint.colour;
            }
        }
    }

    public List<Hint> getHints() {
        List<Hint> hints = new ArrayList<>();
        boolean b;
        Set<Integer> indices;
        for (int i = 1; i <= 5; i++) { //iterate over possible numbers
            indices = new HashSet<>();
            b = false;
            for (int j = 0; j < hand.size(); j++) { //compare current number with numbers in hand
                if (hand.get(j).number == i) {
                    indices.add(j);
                    b = true;
                }
            }
            if (b == true) hints.add(new Hint(indices,i)); //if any number is matched, add array to list
        }
        for (Colour c : Colour.values()) {  //iterate over possible colours
            indices = new HashSet<>();
            b = false;
            for (int j = 0; j < hand.size(); j++) { //compare current colour with colours in hand
                if (hand.get(j).colour == c) {
                    indices.add(j);
                    b = true;
                }
            }
            if (b == true) hints.add(new Hint(indices,c)); //if any colour is matched, add new hint to list
        }
        return hints;
    }

    public void showHints() {
        System.out.println(name);
        System.out.println(hand);
        List<Hint> hints = getHints();
        for (int i=0; i<hints.size(); i++) {
            System.out.println(i+1 + ": " + hints.get(i));
        }
    }

    public void showView() {
        for(View v : views) {
            System.out.println(v);
        }
    }

    public void showKnowledge() {
        for (Card c : knowledge) {
            System.out.println(c);
        }
    }

    public void showHand() {
        System.out.println(hand);
    }

    @Override
    public String toString() {
        String rtn1 = name + " " + hand;
        String rtn2 = "";
        for (int i=0; i<hand.size();i++) {
            if (knowledge.get(i) != null) {
                rtn2 += "\n";
                if (knowledge.get(i).colour==null)
                    rtn2 += "Card " + i + " is a " + knowledge.get(i).number;
                else if (knowledge.get(i).number==0)
                    rtn2 += "Card " + i + " is " + knowledge.get(i).colour;
                else {
                    rtn2 += "Card " + i + " is a " + knowledge.get(i).number + " " + knowledge.get(i).colour;
                }
            }
        }
        return rtn1 + rtn2;
    }
}
