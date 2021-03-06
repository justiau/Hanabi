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

    public boolean useHint() {
        if (hints < 1) return false;
        hints--;
        return true;
    }

    public boolean addHint() {
        if (hints > 7) return false;
        hints++;
        return true;
    }

    public boolean isLastBomb() {
        if (bombs++ == 3) return true;
        return false;
    }

    public boolean isVictory() {
        boolean vic = true;
        for (Card c : board) {
            if (c.number != 5) vic = false;
        }
        return vic;
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

    public int getScore() {
        int sum = 0;
        for (Card c : board) {
            sum+=c.number;
        }
        return sum;
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
