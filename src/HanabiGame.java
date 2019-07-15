import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class HanabiGame {
    int playercount;
    Deck deck;
    Board board;
    Player[] players;

    HanabiGame(int playercount,Deck deck, Board board) {
        this.playercount = playercount;
        this.deck = deck;
        this.board = board;
    }

    HanabiGame(int playercount) {
        this.playercount = playercount;
        this.deck = deckBuild();
        this.deck.shuffle();
        this.board = boardBuild();
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
                    players[i].views.add(new View(players[j].name,players[j].hand));
            }
        }
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
        HanabiGame game = new HanabiGame(4);
        game.deal(new String[]{"Justin","Alice","Bob","Charlie"});
        game.setView();
        Scanner in = new Scanner(System.in);
        System.out.println("Text based UI is now active");
        boolean start = true;
        Player user = game.players[0];
        game.board.show();
        user.showView();
        String input = in.nextLine();
        int index;
        while (true) {
            if (!start) input = in.nextLine();
            if (input.contains("play")) {
                String playargs[] = input.split(" ");
                if (playargs.length == 1) {
                    System.out.println("Enter the index of the card you would like to play:");
                    index = in.nextInt();
                    user.play(user.hand.get(index),game.board);
                    user.draw(game.deck);
                    game.board.show();
                    user.showView();
                } else if (playargs.length == 2) {
                    if (playargs[1].matches("-?\\d+")) {
                        index = Integer.parseInt(playargs[1]);
                        if (playargs[0].equals("play") && index < user.hand.size() && index >= 0) {
                            user.play(user.hand.get(index),game.board);
                            user.draw(game.deck);
                            game.board.show();
                            user.showView();
                        }
                        else System.out.println("Invalid use of play");
                    }
                    else System.out.println("Invalid use of play");
                }
                else System.out.println("Invalid use of play");
            }
            else if (input.equals("hint")) {
                
            }
            else if (input.contains("discard")) {
                String discardargs[] = input.split("");
                if (discardargs.length == 1) {
                    System.out.println("Enter the index of the card you would like to discard:");
                    index = in.nextInt();
                    user.discard(user.hand.get(index),game.board);
                    user.draw(game.deck);
                    game.board.show();
                    user.showView();
                } else if (discardargs.length == 2) {
                    if (discardargs[1].matches("-?\\d+")) {
                        index = Integer.parseInt(discardargs[1]);
                        if (discardargs[0].equals("discard") && index < user.hand.size() && index >= 0) {
                            user.discard(user.hand.get(index),game.board);
                            user.draw(game.deck);
                            game.board.show();
                            user.showView();
                        }
                        else System.out.println("Invalid use of discard");
                    }
                    else System.out.println("Invalid use of discard");
                }
                else System.out.println("Invalid use of discard");
            }
            else if (input.equals("new")) {
                game = new HanabiGame(4);
                game.deal(new String[]{"Justin","Alice","Bob","Charlie"});
                game.setView();
                start = true;
                user = game.players[0];
                game.board.show();
                user.showView();
            }
            else if (input.equals("help")) {
                System.out.println("play hint discard new help cheat show");
            }
            else if (input.equals("cheat")) {
                System.out.println(user.hand);
            }
            else if (input.equals("show")) {
                game.board.show();
                user.showView();
            }
            else if (input.equals("knowledge")) {
                user.showKnowledge();
            }
            else if (input.equals("exit")) break;
            else {
                System.out.println("\n\"" + input + "\"" + " was not recognised as a valid command\nPlease try again or type \"help\" for a list of commands ");
            }
            start = false;
        }
    }
        // game.players[0].learn(new HashSet<>(Arrays.asList(0,1)),Colour.RED);

    //     for (Player p : game.players) {
    //         System.out.println(p.name);
    //         System.out.println(p.hand);
    //         for (Card[] cards : p.possibleHints()) {
    //             String rtn = "[";
    //             boolean b = false;
    //             for (Card c : cards) {
    //                 if (!rtn.equals("[") || b) rtn += ",";
    //                 b = true;
    //                 if (c!=null) {
    //                     if (c.colour==null) rtn+= c.number;
    //                     else if (c.number==0) rtn+= c.colour;
    //                     else rtn += c.colour + " " + c.number;
    //                 }
    //                 else rtn+=0;
    //             }
    //             rtn += "]";
    //             System.out.println(rtn);
    //         }
    //         System.out.println();
    //     }
    // }
}
