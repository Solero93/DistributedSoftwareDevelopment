package model;

import utils.enums.Command;

/**
 * Class that represents the Cell Object
 */
public class Cell {
    private String position;
    private Ship shipOnCell;

    /**
     * Constructior without ships
     *
     * @param position
     */
    public Cell(String position) {
        this.position = position;
        this.shipOnCell = null;
    }

    /**
     * Constructor whit the ship
     *
     * @param position
     * @param ship
     */
    public Cell(String position, Ship ship) {
        this.position = position;
        this.shipOnCell = ship;
    }

    /**
     * Looks if is the last hit on the ship to sunk
     *
     * @return
     */
    public Command hitCell() {
        return (shipOnCell.loseLife()) ? Command.SUNK : Command.HIT;
    }

}