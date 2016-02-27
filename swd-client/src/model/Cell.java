package model;

/**
 * Class that represents the -- Object
 */
public class Cell {
    private String position;
    private boolean visited;
    private Ship shipOnCell;

    public Cell(String position){
        this.position = position;
        this.visited = false;
        this.shipOnCell = null;
    }

    public int hitCell(){
        return -1; // TODO make switch of cases to return
    }

    // No need to return boolean since it's checked in Grid class
    public void putShip(Ship ship){
        this.shipOnCell = ship;
    }
}