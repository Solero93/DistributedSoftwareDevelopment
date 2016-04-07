package controller;

import utils.LogCreator;
import utils.enums.Actor;
import utils.enums.Command;

import java.io.IOException;

/**
 * Class that represents the Controller of Selector
 */
public class SelectorCtrl extends Controller {
    private LogCreator logWriter;

    /**
     * @see LogCreator#LogCreator(String)
     */
    public void createLog(String filename) throws IOException {
        this.logWriter = new LogCreator(filename);
    }

    /**
     * @see LogCreator#writeToLog(Actor, Command, String)
     */
    public void writeToLog(Actor a, Command c, String params) throws IOException {
        this.logWriter.writeToLog(a, c, params);
    }

    /**
     * @see LogCreator#close()
     */
    public void close() {
        try {
            this.logWriter.close();
        } catch (IOException e) {
            //Shouldn't do anything
        }
    }
}