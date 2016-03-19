package utils.enums;

/**
 * Class that represents the Server messages
 */
public enum Message {
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
    UNKNOWN;

    public String messageCode;

    // Constructor of Unknown
    Message() {
    }

    Message(String messageCode) {
        this.messageCode = messageCode;
    }

    public static Message getMessageFromCode(String messageCode) {
        for (Message m : Message.values()) {
            if (m.messageCode.equals(messageCode)) {
                return m;
            }
        }
        return UNKNOWN;
    }
}