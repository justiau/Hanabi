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
    private static final Scanner in = new Scanner(System.in);

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

    public Command getCommand() {
        Command command;
        Integer index;
        String name;
        do {
            command = parseCommand(in.nextLine());
            if (command.commandType==CommandType.DISCARD || command.commandType==CommandType.PLAY) {
                if (command.index==null) {
                    System.out.println("Enter card index:");
                    index = (Integer) getArg(in.nextLine());
                    if (index==null) System.out.println("Index not recognized");  
                    else command.index=index;
                }
            } else if (command.commandType==CommandType.HINT) {
                if (command.playerName==null) {
                    System.out.println(getOtherNames());
                    System.out.println("Enter player name:");
                    name = (String) getArg(in.nextLine());
                    if (name==null) System.out.println("Name not recognized");
                    command.playerName=name;
                }
                if (command.playerName!=null) {
                    if (command.index==null) {
                        Player p = getPlayer(command.playerName);
                        p.showHints();
                        System.out.println("Enter hint number:");
                        index = (Integer) getArg(in.nextLine());
                        if (index==null) System.out.println("Hint number not recognized");
                        else command.index=index;
                    }
                }
            }
        } while (!validateCommand(command));
        
        return command;
    }

    private Object getArg(String input) {
        if (input.matches("\\d")) return Integer.parseInt(input);
        if (getPlayer(input) != null) return input;
        return null;
    }

    public boolean validateCommand(Command command) {
        if (command == null) return false;
        if (command.commandType == CommandType.PLAY || command.commandType == CommandType.DISCARD) {
            if (command.index == null) {
                System.out.println("Null index error");
                return false;
            }
            if (command.index >= 0 && command.index < players[playerindex].hand.size()) return true;
            System.out.println("Invalid index error");
            return false;
        } else if (command.commandType == CommandType.HINT) {
            if (command.playerName!=null&&command.index!=null) {
                if (getPlayer(command.playerName)!=null) return true;
                System.out.println("Name is not recognized");
            }
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
                Hint hint = hints.get(command.index - 1);
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
            case AI:
                AI ai = new AI(this);
                Command aiCommand = ai.generateCommand();
                return handleCommand(aiCommand);
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
        Status status;
        List<Status> endStatuses = Arrays.asList(Status.VICTORY,Status.DEFEAT,Status.EXIT,Status.NEWGAME);
        do {
            board.show();
            showCurrentPlayer();
            players[playerindex].showView();
            status = handleCommand(getCommand());
        } while (!endStatuses.contains(status));
        return status;
    }

    public static void main(String[] args) {
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
    }
}
