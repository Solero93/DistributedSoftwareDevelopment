package controller;

import communication.Communication;
import model.Grid;

import java.util.ArrayList;

/**
 * Class that represents the Client Object
 */
public class Client {
    private Communication server;
    private ArrayList<String> cellsHit;
    private Grid myGrid;

    public Client(){
        this.server = new Communication();
        this.cellsHit = new ArrayList<>();
        this.myGrid = new Grid();
    }

    public boolean generateGridAutomatic(){
        return false;
    }

    public boolean generateGridFromFile(String filename){
        return false;
    }
    public boolean generateGridByUser(ArrayList<String> entry){
        return false;
    }

    public int hitMyCell(String position){
        return this.myGrid.hitCell(position);
    }

    // TODO think about treating communication errors
        // TODO Also add position to cellsHit
    public int hitEnemyCell(String position){
        return this.server.hitCell(position);
    }
}