public class AI {
    HanabiGame game;

    AI(HanabiGame game) {
        this.game = game;
    }

    public Action generateAction() {
        Player player = game.players[game.playerindex];
        Integer index = (int) (Math.random() * player.hand.size());
        return new Play(index);
    }

    public static void main (String args[]) {
        HanabiGame game = new HanabiGame(4);
        game.deal(new String[] { "Justin", "Alice", "Bob", "Charlie" });
        game.setView();
        AI ai = new AI(game);
        System.out.println(ai.generateAction());
    }
}