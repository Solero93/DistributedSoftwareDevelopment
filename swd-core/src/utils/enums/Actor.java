package utils.enums;


/**
 * Class that represents the Actors of Application
 */
public enum Actor {
    SERVER("S: ", 3),
    CLIENT("C: ", 3);

    public String logTxt;
    public int timeOut;

    /**
     * Constructs an Actor
     *
     * @param logTxt  : String that represents it in the logs
     * @param timeOut : TimeOut in sockets
     */
    Actor(String logTxt, int timeOut) {
        this.logTxt = logTxt;
        this.timeOut = timeOut;
    }
}
