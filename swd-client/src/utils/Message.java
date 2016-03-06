package utils;

/**
 * Class that represents the -- Object
 */
public enum Message {
    HIT, MISS, SUNK, WON, ERROR;
    public String message;

    static {
        // Messages are made up currently
        HIT.message = "A";
        MISS.message = "B";
        SUNK.message = "C";
        WON.message = "D";
        ERROR.message = "E";
    }
}
