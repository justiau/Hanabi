import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class HanabiGame {
    int playercount;
    int playerindex;
    Deck deck;
    Board board;
    Player[] players;
    int remainingTurns = -1;

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
            for (int j = i+1; j < playercount; j++) players[i].views.add(new View(players[j].name,players[j].hand));
            for (int j = 0; j < i; j++) players[i].views.add(new View(players[j].name,players[j].hand));
        }
    }

        // for (int i = 0; i < playercount; i++) {
        //     for (int j = 0; j < playercount; j++) {
        //         if (i != j)
        //             players[i].views.add(new View(players[j].name, players[j].hand));
        //     }
        // }
    // }

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

    public static Command parseCommand(String input) {
        String[] args = input.split(" ");
        String[] commandTypes = CommandType.stringValues();
        if (Arrays.asList(commandTypes).contains(args[0].toUpperCase())) {
            CommandType commandType = CommandType.valueOf(args[0].toUpperCase());
            if (args.length == 1) {
                return new Command(commandType);
            } else if (args.length == 2) {
                if (commandType == CommandType.DISCARD || commandType == CommandType.PLAY) {
                    return new Command(commandType,Integer.parseInt(args[1]));
                } else if (commandType == CommandType.HINT) {
                    return new Command(commandType,args[1]);
                }
            }
        }
        return null;
    }

    public boolean validateCommand(Command command) {
        if (command == null) return false;
        if (command.commandType == CommandType.PLAY || command.commandType == CommandType.DISCARD) {
            if (command.index >= 0 && command.index < players[playerindex].hand.size()) return true;
            return false;
        } else if (command.commandType == CommandType.HINT) {
            if (getPlayer(command.playerName) != null) return true;
            return false;
        }
        return true;
    }

    public Status handleCommand(Command command) {
        Player user = players[playerindex];
        Status drawStatus;
        switch (command.commandType) {
            case PLAY:
                Status playStatus = user.play(command.index,board);
                if (playStatus!=Status.OK) return playStatus;
                drawStatus = user.draw(deck);
                next();
                if (drawStatus==Status.LASTCARD) remainingTurns = playercount;
                else if (drawStatus==Status.LASTROUND) return lowerLastRound();
                else return Status.OK;
            case DISCARD:
                board.addHint();
                user.discard(command.index,board);
                drawStatus = user.draw(deck);
                next();
                return drawStatus;
            case HINT:
                Player p = getPlayer(command.playerName);
                List<Hint> hints = p.getHints();
                p.showHints();
                Scanner in = new Scanner(System.in);
                Integer hintnum = Integer.parseInt(in.nextLine());
                Hint hint = hints.get(hintnum - 1);
                if (board.useHint()) {
                    p.learn(hint);
                    System.out.println("Telling " + p.name + " that " + hint.toString().toLowerCase());
                    System.out.println(p.name + " now knows " + p.knowledge);
                    next();
                    if (remainingTurns > -1) return lowerLastRound();
                    return Status.OK;
                } else {
                    System.out.println("There are no hints left to use");
                    return Status.REPEAT;
                }
            case HELP:
                System.out.println(Arrays.asList(CommandType.values()));
                return Status.REPEAT;
            case NEW:
                return Status.NEWGAME;
            case CHEAT:
                user.showHand();
                return Status.REPEAT;
            case KNOWLEDGE:
                user.showKnowledge();
                return Status.REPEAT;
            case SHOW:
                System.out.println(board);
            case EXIT:
                return Status.EXIT;
            default:
                return Status.REPEAT;
        }
    }

    public Status lowerLastRound() {
        if (remainingTurns-- == 0) return Status.DEFEAT;
        return Status.LASTROUND;
    }

    public void showCurrentPlayer() {
        System.out.println("You are currently: " + players[playerindex].name + "\n");
    }

    public static HanabiGame newGame() {
        HanabiGame game = new HanabiGame(4);
        game.deal(new String[] { "Justin", "Alice", "Bob", "Charlie" });
        game.setView();
        return game;
    }

    public Status run() {
        Scanner in = new Scanner(System.in);
        Command command;
        Status status;
        List<Status> endStatuses = Arrays.asList(Status.VICTORY,Status.DEFEAT,Status.EXIT,Status.NEWGAME);
        do {
            do {
                board.show();
                showCurrentPlayer();
                players[playerindex].showView();
                command = parseCommand(in.nextLine());
                if (!validateCommand(command)) System.out.println("\nInvalid command\n");
            } while (!validateCommand(command));
            status = handleCommand(command);
        } while(!endStatuses.contains(status));
        return status;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Status status;
        String input;
        HanabiGame game;
        main : do{
            game = newGame();
            status = game.run();
            System.out.println("Your final score was: " + game.board.getScore());
            switch (status) {
                case VICTORY:
                    System.out.println("Congratulations you won!");
                    break;
                case DEFEAT:
                    System.out.println("Game over. Better luck next time...");
                    break;
                case NEWGAME:
                    System.out.println("Starting new game.");
                    continue main;
                case EXIT:
                    return;
                default:
            }
            do {
                input = in.nextLine();
            } while (!input.equals("exit")&&!input.equals("new"));
            if (input.equals("exit")) status = Status.EXIT;
        } while (status != Status.EXIT);
        in.close();
        // while (true) {
        //     user = game.players[game.playerindex];
        //     game.board.show();
        //     user.showView();
        //     input = in.nextLine();
        //     if (input.contains("play")) {
        //         String playargs[] = input.split(" ");
        //         if (playargs.length == 1) {
        //             System.out.println("Enter the index of the card you would like to play:");
        //             index = Integer.parseInt(in.nextLine());
        //             if (!user.play(index, game.board)) {
        //                 if (game.board.isLastBomb()) {
        //                     System.out.println("Game over. All bombs have been activated");
        //                     game = newGame();
        //                 }
        //             } else {
        //                 user.play(index, game.board);
        //                 user.draw(game.deck);
        //                 game.next();
        //             }
        //         } else if (playargs.length == 2) {
        //             if (playargs[1].matches("\\d+")) {
        //                 index = Integer.parseInt(playargs[1]);
        //                 if (playargs[0].equals("play") && index < user.hand.size() && index >= 0) {
        //                     if (!user.play(index, game.board)) {
        //                         if (game.board.isLastBomb()) {
        //                             System.out.println("Game over. All bombs have been activated");
        //                             game = newGame();
        //                         }
        //                     } else {
        //                         user.play(index, game.board);
        //                         user.draw(game.deck);
        //                         game.next();
        //                     }
        //                 } else System.out.println("Invalid use of play");
        //             } else System.out.println("Invalid use of play");
        //         } else System.out.println("Invalid use of play");
        //         game.next();
        //     } else if (input.contains("hint")) {
        //         if (!game.board.useHint()) continue;
        //         String hintargs[] = input.split(" ");
        //         if (hintargs.length == 1) {
        //             System.out.println(game.getOtherNames());
        //             System.out.println("Enter the name of the player you would like to hint:");
        //             String playername = in.nextLine();
        //             Player p = game.getPlayer(playername);
        //             if (p != null) {
        //                 List<Hint> hints = p.getHints();
        //                 p.showHints();
        //                 Integer hintnum = Integer.parseInt(in.nextLine());
        //                 Hint hint = hints.get(hintnum - 1);
        //                 if (hintnum <= hints.size() && hintnum >= 1) {
        //                     p.learn(hint);
        //                     System.out.println("Telling " + p.name + " that " + hint.toString().toLowerCase());
        //                     System.out.println(p.name + " now knows " + p.knowledge);
        //                     game.next();
        //                 } else {
        //                     System.out.println("Hint number was not recognised");
        //                 }
        //             } else System.out.println("Player name not recognised");
        //         }
        //         else if (hintargs.length == 2) {
        //             Player p = game.getPlayer(hintargs[1]);
        //             if (p!=null) {
        //                 List<Hint> hints = p.getHints();
        //                 p.showHints();
        //                 Integer hintnum = Integer.parseInt(in.nextLine());
        //                 Hint hint = hints.get(hintnum - 1);
        //                 if (hintnum <= hints.size() && hintnum >= 1) {
        //                     p.learn(hint);
        //                     System.out.println("Telling " + p.name + " that " + hint.toString().toLowerCase());
        //                     System.out.println(p.knowledge);
        //                     game.next();
        //                 } else {
        //                     System.out.println("Hint number was not recognised");
        //                 }
        //             } else {
        //                 System.out.println("Player name not recognised");
        //             }
        //         }
        //         else {
        //             System.out.println("Invalid use of hint");
        //         }
        //     } else if (input.equals("board")) {
        //     } else if (input.equals("hints")) {
        //         for (Player p : game.players) {
        //             if (!user.equals(p))
        //                 p.showHints();
        //         }
        //     } else if (input.contains("discard")) {
        //         String discardargs[] = input.split(" ");
        //         if (discardargs.length == 1) {
        //             System.out.println("Enter the index of the card you would like to discard:");
        //             index = Integer.parseInt(in.nextLine());
        //             game.board.addHint();
        //             user.discard(index, game.board);
        //             user.draw(game.deck);
        //             game.next();
        //         } else if (discardargs.length == 2) {
        //             if (discardargs[1].matches("-?\\d+")) {
        //                 index = Integer.parseInt(discardargs[1]);
        //                 if (discardargs[0].equals("discard") && index < user.hand.size() && index >= 0) {
        //                     game.board.addHint();
        //                     user.discard(index, game.board);
        //                     user.draw(game.deck);
        //                     game.next();
        //                 } else {
        //                     System.out.println("Invalid use of discard");
        //                 }
        //             } else {System.out.println("Invalid use of discard");
        //             }
        //         } else {
        //             System.out.println("Invalid use of discard");
        //         }
        //         game.next();
        //     } else if (input.equals("new")) {
        //         game = newGame();
        //     } else if (input.equals("ai")) {
        //         AI ai = new AI(game);
        //         Action action = ai.generateAction();
        //         switch (action.getClass().getName()) {
        //             case "Play":
        //                 Play play = (Play) action;
        //                 if (!user.play(play.index, game.board)) {
        //                     if(game.board.isLastBomb()) {
        //                         System.out.println("Game over. All bombs have been activated");;
        //                         game = newGame();
        //                     }
        //                 } else {
        //                     user.draw(game.deck);
        //                     game.next();
        //                 }
        //                 break;
        //             case "Discard":
        //                 Discard discard = (Discard) action;
        //                 user.discard(discard.index, game.board);
        //                 game.board.addHint();
        //                 user.draw(game.deck);
        //                 game.next();
        //                 break;
        //             case "Hint":
        //                 if (game.board.useHint()) {
        //                     Hint hint = (Hint) action;
        //                     Player p = game.players[hint.playerIndex];
        //                     p.learn(hint);
        //                     System.out.println("Telling " + p.name + " that " + hint.toString().toLowerCase());
        //                     System.out.println(p.knowledge);
        //                     game.next();
        //                 }
        //                 break;
        //         }
        //     } else if (input.equals("help")) {
        //         System.out.println("play hint discard new help cheat show");
        //     } else if (input.equals("cheat")) {
        //         System.out.println(user.hand);
        //     } else if (input.equals("knowledge")) {
        //         user.showKnowledge();
        //     } else if (input.equals("exit"))
        //         break;
        //     else {
        //         System.out.println("\n\"" + input + "\" was not recognised as a valid command\nPlease try again or type \"help\" for a list of commands ");
        //     }
        // }
        // in.close();
    }
}
