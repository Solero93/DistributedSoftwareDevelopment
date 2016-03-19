package utils;

import exceptions.PackageBuildException;
import utils.enums.Message;

/**
 * Class that represents the PackageBuilder Object
 */
public class PackageBuilder {
    private Message message;
    private String position;
    private String errorCode;
    private String errorText;


    public PackageBuilder() {
        this.message = null;
        this.position = null;
        this.errorCode = null;
        this.errorText = null;
    }

    public PackageBuilder setMessage(Message m) {
        this.message = m;
        return this;
    }

    public PackageBuilder setPosition(String position) {
        this.position = position;
        return this;
    }

    public PackageBuilder setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public PackageBuilder setErrorText(String errorText) {
        this.errorText = errorText;
        return this;
    }

    public String buildPackage() throws PackageBuildException {
        if (this.message == null) throw new PackageBuildException();
        switch (this.message) {
            case FIRE:
                if (this.position == null) throw new PackageBuildException();
                return (message.messageCode + ' ' + this.position);
            case ERROR:
                if (this.errorCode == null || this.errorText == null) throw new PackageBuildException();
                return (message.messageCode + ' ' + this.errorCode + this.errorText);
            case UNKNOWN:
                throw new PackageBuildException();
            default:
                return message.messageCode;
        }
    }
}