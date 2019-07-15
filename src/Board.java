import java.util.List;
import java.util.ArrayList;

public class Board {
    Card[] board;
    List<Card> discards = new ArrayList<>();
    int hints=8;
    int bombs=0;

    Board(Card[] board) {
        this.board = board;
    }

    Board(Card[] board, List<Card> discards) {
        this.board = board;
        this.discards = discards;
    }
    public int getBoardIndexForColour(Card card) {
        for (int n = 0; n < board.length; n++) {
            if (board[n].colour.equals(card.colour)) return n;
        }
        return 0;
    }

    public void update(Card card) {
        board[getBoardIndexForColour(card)] = card;
    }

    public boolean contains(Card card) {
        for (Card c : board) {
            if (c.equals(card)) return true;
        }
        return false;
    }

    public void show() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        System.out.println("Hints: " + hints);
        System.out.println("Bombs: " + bombs);
        System.out.println("Discards: " + discards);
        String s = "";
        for (int n = 0; n < 5; n++) {
            if (n==4) {
                s = s + board[n];
            }
            else {
                s = s + board[n] + ", ";
            }
        }
        return s;
    }
}
