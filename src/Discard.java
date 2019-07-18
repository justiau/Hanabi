public class Discard extends Action{
    Integer index;

    public Discard(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Discard card " + index;
    }
}