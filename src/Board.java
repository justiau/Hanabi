public class Board {
    Card[] board;

    Board(Card[] board) {
        this.board = board;
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
            if (c == card) return true;
        }
        return false;
    }

    @Override
    public String toString() {
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
