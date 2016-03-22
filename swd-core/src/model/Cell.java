package model;

import utils.enums.Command;

/**
 * Class that represents the Cell Object
 */
public class Cell {
    private String position; // TODO quitarlo porque el hashmap ya lo guarda
    private boolean visited;
    private Ship shipOnCell;

    public Cell(String position) {
        this.position = position;
        this.visited = false;
        this.shipOnCell = null;
    }

    public Cell(String position, Ship ship) {
        this.position = position;
        this.visited = false;
        this.shipOnCell = ship;
    }

    public Command hitCell() {
        return (shipOnCell.loseLife()) ? Command.SUNK : Command.HIT;
    }

    // No need to return boolean since it's checked in Grid class
    public void putShip(Ship ship) {
        this.shipOnCell = ship;
    }

}