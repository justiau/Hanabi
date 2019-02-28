import java.util.ArrayList;
import java.util.List;

public class HanabiGame {
    int playercount;
    int hints=8;
    int bombs=0;
    Deck deck;
    Board board;
    Discards discards;
    Player[] players = new Player[playercount];

    HanabiGame(int playercount,Deck deck, Board board, Discards discards) {
        this.playercount = playercount;
        this.deck = deck;
        this.board = board;
        this.discards = discards;
    }

    HanabiGame(int playercount) {
        this.playercount = playercount;
        this.deck = deckBuild();
        this.deck.shuffle();
        this.board = boardBuild();
        this.discards = discardsBuild();
    }

    public void deal(String[] names) {
        if (names.length!=playercount) {
            System.out.println("There are more names than players!");
            return;
        }
        players = new Player[playercount];
        for (int i=0; i<playercount; i++) {
            players[i] = new Player(names[i], deck, playercount);
        }
    }

    public void setView() {
        for (int i=0; i<playercount; i++) {
            for (int j=0; j<playercount; j++) {
                if (i!=j)
                    players[i].view.add(new View(players[j].name,players[j].hand));
            }
        }
    }

    public static Discards discardsBuild() {
        return new Discards(new ArrayList<>());
    }

    public static Board boardBuild() {
        Card[] cards = new Card[5];
        for (int i=0; i<5; i++) {
            cards[i] = new Card(0,Colour.values()[i]);
        }
        return new Board(cards);
    }

    public static Deck deckBuild() {
        List<Card> cards = new ArrayList<>();
        for (int n = 1; n <= 5; n++) {
            for (Colour c : Colour.values()) {
                if (n==1) {
                    for (int i = 0; i < 3; i++) {
                        cards.add(new Card (n,c));
                    }
                }
                if (n>=2 && n<= 4) {
                    for (int i = 0; i < 2; i++) {
                        cards.add(new Card (n,c));
                    }
                }
                if (n==5) {
                    cards.add(new Card (n,c));
                }
            }
        }
        Deck deck = new Deck(cards);
        return deck;
    }

    public static void main(String[] args) {
        HanabiGame game = new HanabiGame(3);
        game.deal(new String[]{"Steve","John","Tim"});
        game.setView();
        for (Player p : game.players) {
            System.out.println(p.name);
            System.out.println(p.hand);
            for (Knowledge k : p.possibleHints())
                System.out.println(k);
            System.out.println();
        }
    }
}
