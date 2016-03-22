package utils;

import utils.enums.Command;

/**
 * Class that represents the Message Object
 */
public class Message {
    private Command command;
    private String params;

    public Message() {
        this.command = null;
        this.params = null;
    }

    public Message(String pkg) {
        String[] tmp = pkg.split(" ");
        this.command = Command.getCommandFromCode(tmp[0].toUpperCase());
        if (tmp.length >= 1){
            this.params = tmp[1].toUpperCase();
        } else {
            this.params = null;
        }
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
        this.params = params;
        return this;
    }

    public String buildPackage() {
        switch (this.command) {
            case FIRE:
                return (command.commandCode + ' ' + this.params);
            case ERROR:
                return (command.commandCode + ' ' + this.params);
            default:
                return command.commandCode;
        }
    }
}