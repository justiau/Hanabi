public class Card {
    int number;
    Colour colour;

    Card(int number, Colour colour) {
        this.number = number;
        this.colour = colour;
    }

    public Card prevCard() {
        return new Card(number-1,colour);
    }

    @Override
    public String toString() {
        return colour + " " + number;
    }
}
