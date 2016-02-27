package controller;

import communication.Communication;
import model.Grid;

import java.util.ArrayList;

/**
 * Class that represents the -- Object
 */
public class Client {
    private ArrayList<String> cellsHit;
    private Grid myGrid;
    private Communication server;
    private int myId; // Temporal attribute for trying

    public Client(int myId){
        this.cellsHit = new ArrayList<>();
        this.myGrid = new Grid();
        this.server = new Communication();
        this.myId = myId;
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
        return this.server.hitCell(position,this.myId);
    }
}