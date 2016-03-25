package utils;

import utils.enums.Actor;
import utils.enums.Command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that represents the -- Object
 */
public class LogCreator {
    BufferedWriter logWriter;

    public LogCreator(String filename) throws IOException{
        this.logWriter = new BufferedWriter(
                new FileWriter(filename));
    }

    public void writeToLog(Actor a, Command c, String params) throws IOException {
        if(params == null){
            this.logWriter.write(a.logTxt + c.commandCode);
        } else {
            this.logWriter.write(a.logTxt + c.commandCode + " " + params);
        }
    }

    public void close() throws IOException{
        this.logWriter.flush(); // Must write last contents into file
        this.logWriter.close();
    }
}