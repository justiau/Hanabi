import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class HanabiGame {
    int playercount;
    int playerindex;
    Deck deck;
    Board board;
    Player[] players;

    HanabiGame(int playercount, Deck deck, Board board) {
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
        if (names.length != playercount) {
            System.out.println("There are more names than players!");
            return;
        }
        players = new Player[playercount];
        for (int i = 0; i < playercount; i++) {
            players[i] = new Player(names[i], deck, playercount);
        }
    }

    public void setView() {
        for (int i = 0; i < playercount; i++) {
            for (int j = 0; j < playercount; j++) {
                if (i != j)
                    players[i].views.add(new View(players[j].name, players[j].hand));
            }
        }
    }

    public static Board boardBuild() {
        Card[] cards = new Card[5];
        for (int i = 0; i < 5; i++) {
            cards[i] = new Card(0, Colour.values()[i]);
        }
        return new Board(cards);
    }

    public static Deck deckBuild() {
        List<Card> cards = new ArrayList<>();
        for (int n = 1; n <= 5; n++) {
            for (Colour c : Colour.values()) {
                if (n == 1) {
                    for (int i = 0; i < 3; i++) {
                        cards.add(new Card(n, c));
                    }
                }
                if (n >= 2 && n <= 4) {
                    for (int i = 0; i < 2; i++) {
                        cards.add(new Card(n, c));
                    }
                }
                if (n == 5) {
                    cards.add(new Card(n, c));
                }
            }
        }
        Deck deck = new Deck(cards);
        return deck;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            names.add(p.name);
        }
        return names;
    }

    public List<String> getOtherNames() {
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            if (!p.name.equals(players[playerindex].name)) names.add(p.name);
        }
        return names;
    }

    public Player getPlayer(String s) {
        for (Player p : players) {
            if (p.name.toLowerCase().equals(s.toLowerCase()))
                return p;
        }
        return null;
    }

    public void next() {
        playerindex++;
        if (playerindex >= players.length) {
            playerindex = 0;
        }
    }

    public static void main(String[] args) {
        HanabiGame game = new HanabiGame(4);
        game.deal(new String[] { "Justin", "Alice", "Bob", "Charlie" });
        game.setView();
        Scanner in = new Scanner(System.in);
        System.out.println("Text based UI is now active");
        
        boolean start = true;
        Player user = game.players[game.playerindex];
        game.board.show();
        user.showView();
        String input = in.nextLine();
        int index;
        while (true) {
            if (!start) {
                user = game.players[game.playerindex];
                game.board.show();
                user.showView();
                input = in.nextLine();
            }
            start = false;
            if (input.contains("play")) {
                String playargs[] = input.split(" ");
                if (playargs.length == 1) {
                    System.out.println("Enter the index of the card you would like to play:");
                    index = Integer.parseInt(in.nextLine());
                    user.play(index, game.board);
                    user.draw(game.deck);
                } else if (playargs.length == 2) {
                    if (playargs[1].matches("-?\\d+")) {
                        index = Integer.parseInt(playargs[1]);
                        if (playargs[0].equals("play") && index < user.hand.size() && index >= 0) {
                            user.play(index, game.board);
                            user.draw(game.deck);
                        } else System.out.println("Invalid use of play");
                    } else System.out.println("Invalid use of play");
                } else System.out.println("Invalid use of play");
                game.next();
                continue;
            } else if (input.contains("hint")) {
                String hintargs[] = input.split(" ");
                if (hintargs.length == 1) {
                    System.out.println(game.getOtherNames());
                    System.out.println("Enter the name of the player you would like to hint:");
                    String playername = in.nextLine();
                    Player p = game.getPlayer(playername);
                    if (p != null) {
                        List<Hint> hints = p.getHints();
                        p.showHints();
                        Integer hintnum = Integer.parseInt(in.nextLine());
                        Hint hint = hints.get(hintnum - 1);
                        if (hintnum <= hints.size() && hintnum >= 1) {
                            p.learn(hint);
                            System.out.println("Telling " + p.name + " that " + hint.toString().toLowerCase());
                            System.out.println(p.name + " now knows " + p.knowledge);
                        } else {
                            System.out.println("Hint number was not recognised");
                        }
                    } else System.out.println("Player name not recognised");
                }
                else if (hintargs.length == 2) {
                    Player p = game.getPlayer(hintargs[1]);
                    if (p!=null) {
                        List<Hint> hints = p.getHints();
                        p.showHints();
                        Integer hintnum = Integer.parseInt(in.nextLine());
                        Hint hint = hints.get(hintnum - 1);
                        if (hintnum <= hints.size() && hintnum >= 1) {
                            p.learn(hint);
                            System.out.println("Telling " + p.name + " that " + hint.toString().toLowerCase());
                            System.out.println(p.knowledge);
                        } else {
                            System.out.println("Hint number was not recognised");
                        }
                    } else {
                        System.out.println("Player name not recognised");
                    }
                }
                else {
                    System.out.println("Invalid use of hint");
                }
                game.next();
                continue;
            } else if (input.equals("board")) {
                game.board.show();
            } else if (input.equals("hints")) {
                for (Player p : game.players) {
                    if (!user.equals(p))
                        p.showHints();
                }
            } else if (input.contains("discard")) {
                String discardargs[] = input.split("");
                if (discardargs.length == 1) {
                    System.out.println("Enter the index of the card you would like to discard:");
                    index = Integer.parseInt(in.nextLine());
                    user.discard(index, game.board);
                    user.draw(game.deck);
                } else if (discardargs.length == 2) {
                    if (discardargs[1].matches("-?\\d+")) {
                        index = Integer.parseInt(discardargs[1]);
                        if (discardargs[0].equals("discard") && index < user.hand.size() && index >= 0) {
                            user.discard(index, game.board);
                            user.draw(game.deck);
                        } else {
                            System.out.println("Invalid use of discard");
                        }
                    } else {System.out.println("Invalid use of discard");
                    }
                } else {
                    System.out.println("Invalid use of discard");
                }
                game.next();
                continue;
            } else if (input.equals("new")) {
                game = new HanabiGame(4);
                game.deal(new String[] { "Justin", "Alice", "Bob", "Charlie" });
                game.setView();
                start = true;
                user = game.players[0];
                game.board.show();
                user.showView();
            } else if (input.equals("help")) {
                System.out.println("play hint discard new help cheat show");
            } else if (input.equals("cheat")) {
                System.out.println(user.hand);
            } else if (input.equals("show")) {
                game.board.show();
                user.showView();
            } else if (input.equals("knowledge")) {
                user.showKnowledge();
            } else if (input.equals("next")) {
                game.next();
            } else if (input.equals("exit"))
                break;
            else {
                System.out.println("\n\"" + input + "\""
                        + " was not recognised as a valid command\nPlease try again or type \"help\" for a list of commands ");
            }
        }
        in.close();
    }
}
