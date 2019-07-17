import java.util.Set;

public class Hint {
    Colour colour;
    Integer number;
    Set<Integer> cardIndices;

    Hint(Set<Integer> cardIndices, Colour colour) {
        this.cardIndices = cardIndices;
        this.colour = colour;
    }

    Hint (Set<Integer> cardIndices, Integer number) {
        this.cardIndices = cardIndices;
        this.number = number;
    }

    @Override
    public String toString() {
        String rtn = "";
        for (Integer i : cardIndices) {
            if (rtn != "") rtn += ", ";
            rtn += i+1;
        }
        String rtn1 = "";
        if (cardIndices.size() > 1) {
            rtn1 = "Cards " + rtn + " are ";
        } else {
            rtn1 = "Card " + rtn + " is ";
        }
        String rtn2 = colour + "";
        if (colour == null) {
            rtn2 = number + "";
        }
        return rtn1 + rtn2 ;
    }

}