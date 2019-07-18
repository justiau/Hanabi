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

    public boolean mistake() {
        if (bombs++ == 3) return true;
        return false;
    }

    public boolean useHint() {
        if (hints < 1) return false;
        hints--;
        return true;
    }

    public boolean getHint() {
        if (hints > 7) return false;
        hints++;
        return true;
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
        String rtn = "";
        for (Card c : board) {
            if (rtn != "") rtn += "\n";
            rtn += c.colour + ": " + c.number;
        }
        return "\n" + rtn + "\n";
    }
}
