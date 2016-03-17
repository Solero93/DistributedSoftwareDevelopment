package utils;

/**
 * Class that represents the Server messages
 */
public enum Message {
    START, THROW, FIRE, HIT, MISS, SUNK, YOU_WIN,
    ERROR, GRID_RDY, HUMAN_FIRST, DRAW, UNKNOW;
    public String message;

    static {
        // Messages are made up currently
        START.message = "STRT";
        THROW.message = "THRW";
        FIRE.message = "FIRE";
        HIT.message = "HIT_";
        MISS.message = "MISS";
        SUNK.message = "SUNK";
        YOU_WIN.message = "WIN_";
        ERROR.message = "ERRO";
        GRID_RDY.message = "GRID";
        HUMAN_FIRST.message = "FRST";
        DRAW.message = "DRAW";

    }

    public String toString(){
        return this.message;
    }
}
