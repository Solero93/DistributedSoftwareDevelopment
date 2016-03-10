package utils;

/**
 * Class that represents the Server messages
 */
public enum Message {
    START, THROW, FIRE, HIT, MISS, SUNK, YOU_WIN, ERROR;
    public String message;

    static {
        // Messages are made up currently
        START.message = "-1";
        THROW.message = "-2";
        FIRE.message = "-3";
        HIT.message = "A";
        MISS.message = "B";
        SUNK.message = "C";
        YOU_WIN.message = "D";
        ERROR.message = "E";
    }
}
