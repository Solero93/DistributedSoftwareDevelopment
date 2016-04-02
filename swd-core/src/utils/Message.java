package utils;

import utils.enums.Command;

/**
 * Class that represents the Messages
 */
public class Message {
    private Command command;
    private String params;

    /**
     * Constructs an empty message object.
     */
    public Message() {
        this.command = null;
        this.params = null;
    }

    public Command getCommand() {
        return this.command;
    }

    public Message setCommand(Command c) {
        this.command = c;
        return this;
    }

    public String getParams() {
        return this.params;
    }

    public Message setParams(String params) {
        if (params != null) {
            this.params = params.toUpperCase();
        }
        return this;
    }

    /**
     * Builds the string to send over the network.
     *
     * @return String to send
     */
    public String buildPackage() {
        switch (this.command) {
            case FIRE:
                return (command.commandCode + ' ' + this.params);
            case ERROR:
                // Let's suppose no one writes a message longer than 99 characters
                String length = String.format("%02d", this.params.length());
                return (command.commandCode + ' ' + length + this.params);
            default:
                return command.commandCode;
        }
    }
}