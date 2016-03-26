package utils.enums;


/**
 * Class that represents the -- Object
 */
public enum Actor {
    SERVER("S: ",3),
    CLIENT("C: ",3);

    public String logTxt;
    public int timeOut;
    Actor(String logTxt, int timeOut){
        this.logTxt = logTxt;
        this.timeOut = timeOut;
    }
}
