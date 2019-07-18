public class Play extends Action{
    Integer index;

    public Play(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Play card " + index;
    }
}