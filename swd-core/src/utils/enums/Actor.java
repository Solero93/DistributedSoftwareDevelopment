package utils.enums;


/**
 * Class that represents the -- Object
 */
public enum Actor {
    SERVER("S: "),
    CLIENT("C: ");

    public String logTxt;
    Actor(String logTxt){
        this.logTxt = logTxt;
    }
}
