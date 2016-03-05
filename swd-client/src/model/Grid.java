package model;

import utils.Const;

import java.util.HashMap;

/**
 * Class that represents the Grid Object
 */
public class Grid {
    private HashMap<String, Cell> cells;
    private int numShipsLeft;

    public Grid() {
        this.cells = new HashMap<>();
        this.numShipsLeft = 0;
    }

    public Const.Message hitCell(String position) {
        return cells.get(position).hitCell(); // TODO switch of cases to return
    }

    public boolean putShip(String position, int shipSize, Const.Orientation orientation) {
        char character, number;
        Ship ship = new Ship(shipSize);
        Cell cell;
        String newPosition;
        position = position.toUpperCase();
        if (position.length() != 2) {
            return false;
        }
        character = position.charAt(0);
        number = position.charAt(1);
        if (orientation == Const.Orientation.H) {
            if ((character < 'A') || (character > 'J') || (number < '0') || ((number + shipSize - 1) > '9')) {
                return false;
            }
            for (int i = number; i <= (number + shipSize - 1); i++) {
                if (invalidCell(character, (char) i)) return false;
            }
            for (int i = number; i <= (number + shipSize - 1); i++) {
                cell = new Cell(character + "" + ((char) (i)), ship);//TODO Acabar


            }

        } else {
            if ((character < 'A') || ((character + shipSize - 1) > 'J') || (number < '0') || (number > '9')) {
                return false;
            }
            for (int i = character; i <= (character + shipSize - 1); i++) {
                if (invalidCell((char) i, number)) return false;//TODO Acabar
            }

        }
        return true; // TODO check if ship can be put or not
    }

    public boolean invalidCell(char character, char number) {

        return (cells.containsKey(character + number)
                || cells.containsKey(((char) (character - 1)) + number)
                || cells.containsKey(((char) (character + 1)) + number)
                || cells.containsKey(character + ((char) (number - 1)))
                || cells.containsKey(character - 1 + ((char) (number + 1))));

    }

    // TODO "Generate grid" method missing
}
