public class Card {
    int number;
    Colour colour;

    Card(int number, Colour colour) {
        this.number = number;
        this.colour = colour;
    }

    public boolean equals(Card card) {
        if (card.colour.equals(colour)&&card.number==number) return true;
        return false;
    }

    public Card prevCard() {
        return new Card(number-1,colour);
    }

    @Override
    public String toString() {
        if (colour == null) return number + "";
        else return colour + " " + number;
    }
}
