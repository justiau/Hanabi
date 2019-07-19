public class Command {
    public Integer index = null;
    public String playerName = null;
    public CommandType commandType;

    Command(CommandType commandType) {
        this.commandType = commandType;
    }

    Command(CommandType commandType, int index) {
        this.index = index;
        this.commandType = commandType;
    }

    Command(CommandType commandType, String playerName) {
        this.playerName = playerName;
        this.commandType = commandType;
    }

    @Override
    public String toString() {
        return (commandType.name() + (index==null ? "" : " " + index) + (playerName==null ? "" : " " + playerName));
    }

    public static void main(String args[]) {
        String input = "play -2";
        System.out.println(HanabiGame.parseCommand(input));
    }
}