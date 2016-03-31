package utils.enums;

/**
 * Class that represents the Server messages
 */
public enum Command {
    START("STRT"),
    THROW("THRW"),
    FIRE("FIRE"),
    HIT("HIT_"),
    MISS("MISS"),
    SUNK("SUNK"),
    YOU_WIN("WIN_"),
    ERROR("ERRO"),
    GRID_RDY("GRID"),
    HUMAN_FIRST("FRST"),
    DRAW("DRAW"),
    UNKNOWN(null);

    public String commandCode;

    /**
     * Constructs a Command from its code
     *
     * @param commandCode
     */
    Command(String commandCode) {
        this.commandCode = commandCode;
    }

    /**
     * Returns a command from its commandCode. If it doesn't find it, returns UNKNOWN
     *
     * @param commandCode
     * @return
     */
    public static Command getCommandFromCode(String commandCode) {
        String upperCommandCode = commandCode.toUpperCase();
        for (Command m : Command.values()) {
            if (m.commandCode.equals(upperCommandCode)) {
                return m;
            }
        }
        return UNKNOWN;
    }
}