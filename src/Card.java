public class Card {
    int number;
    Colour colour;

    Card(int number, Colour colour) {
        this.number = number;
        this.colour = colour;
    }

    @Override
    public String toString() {
        return colour + " " + number;
    }
}
