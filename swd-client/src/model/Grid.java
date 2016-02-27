package model;

import java.util.HashMap;

/**
 * Class that represents the -- Object
 */
public class Grid {
    private HashMap<String, Cell> cells;
    private int numShipsLeft;

    public Grid(){
        this.cells = new HashMap<>();
        this.numShipsLeft = 9;
    }

    public int hitCell(String position){
        return cells.get(position).hitCell(); // TODO switch of cases to return
    }

    public boolean putShip(String position, Ship ship, boolean isHorizontal){
        return false; // TODO check if ship can be put or not
    }

    // TODO "Generate grid" method missing
}
