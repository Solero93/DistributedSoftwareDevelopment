package controller;

import utils.LogCreator;
import utils.enums.Actor;
import utils.enums.Command;

import java.io.IOException;

/**
 * Class that represents the -- Object
 */
public class SelectorCtrl extends Controller {
    private LogCreator logWriter;

    public void createLog(String filename) throws IOException {
        this.logWriter = new LogCreator(filename);
    }

    public void writeToLog(Actor a, Command c, String params) throws IOException{
        this.logWriter.writeToLog(a,c,params);
    }

    public void close(){
        try {
            this.logWriter.close();
        } catch (IOException e) {
            //TODO do something
        }
    }
}