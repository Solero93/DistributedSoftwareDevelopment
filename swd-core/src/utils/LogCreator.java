package utils;

import utils.enums.Actor;
import utils.enums.Command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that represents the LogCreator Object
 */
public class LogCreator {
    BufferedWriter logWriter;
    boolean isFirstLine;

    /**
     * Constructor of class.
     *
     * @param filename : Name of file to create the log into
     * @throws IOException
     */
    public LogCreator(String filename) throws IOException {
        this.logWriter = new BufferedWriter(
                new FileWriter(filename));
        this.isFirstLine = true;
    }

    /**
     * Write a certain event into logfile
     *
     * @param a      : Who is sending the command
     * @param c      : What he's doing
     * @param params : What parameters does the command have
     * @throws IOException
     */
    public void writeToLog(Actor a, Command c, String params) throws IOException {
        if (!this.isFirstLine) {
            this.logWriter.newLine();
        } else {
            this.isFirstLine = false;
        }
        if (params == null) {
            this.logWriter.write(a.logTxt + c.commandCode);
        } else {
            this.logWriter.write(a.logTxt + c.commandCode + " " + params);
        }
    }

    /**
     * Close logs.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        this.logWriter.flush(); // Must write last contents into file
        this.logWriter.close();
    }
}