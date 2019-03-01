import java.util.List;

public class View {
    String name;
    List<Card> cards;

    View(String name, List<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    @Override
    public String toString() {
        return name + " has: " + cards;
    }
}
