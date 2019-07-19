public enum CommandType {
    PLAY, DISCARD, HINT, HELP, NEW, CHEAT, KNOWLEDGE, AI, SHOW, EXIT;

    public static String[] stringValues() {
        int size = CommandType.values().length;
        String[] rtn = new String[size];
        for (int i=0; i < size; i++) {
            rtn[i] = CommandType.values()[i].toString();
        }
        return rtn;
    }
}