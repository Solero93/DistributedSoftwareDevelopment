package model;

import utils.enums.Command;
import utils.enums.Orientation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that represents the Grid Object
 */
public class Grid {
    private HashMap<String, Cell> cells;
    private ArrayList<String> cellsFired;
    private int numShipsLeft;

    public Grid() {
        this.cells = new HashMap<>();
        this.numShipsLeft = 0;
    }

    /**
     * Draws the grid in a String
     * @return
     */
    public String toString() {
        String mensaje;
        mensaje = " |0123456789|\n";
        for (char i = 'A'; i <= 'J'; i = (char) (i + 1)) {
            mensaje = mensaje + i + "|";
            for (char j = '0'; j <= '9'; j = (char) (j + 1)) {
                if (cells.containsKey(i + "" + j)) {
                    mensaje = mensaje + "X";
                } else {
                    mensaje = mensaje + " ";
                }
            }
            mensaje = mensaje + "|\n";
        }
        return mensaje;
    }

    /**
     * Says to the other user if he hited your ships or not
     * @param position
     * @return MISS ERROR HIT SUNK YOU_WIN
     */
    public Command hitCell(String position) {
        Command tmp;
        char character, number;
        if (position.length() != 2) {
            return Command.ERROR;
        }
        position = position.toUpperCase();
        character = position.charAt(0);
        number = position.charAt(1);
        //If is a valide cells or was fired after
        if ((character < 'A') || (character > 'J') || (number < '0') || ((number) > '9') || (cellsFired.contains(position))) {
            return Command.ERROR;
        }
        cellsFired.add(position);//Fired cells

        //
        if (cells.containsKey(position)) {
            tmp = cells.get(position).hitCell();
            if (tmp == Command.SUNK) {
                return (--this.numShipsLeft == 0) ? Command.YOU_WIN : tmp;
            }
            return tmp;
        }
        return Command.MISS;
    }

    /**
     * Put a ship in the grid
     * @param shipSize
     * @param position
     * @param orientation
     * @return True if you can put it, False if you can't
     */
    public boolean putShip(int shipSize, String position, Orientation orientation) {
        char character, number;
        Ship ship = new Ship(shipSize);
        Cell cell;
        String newPosition;

        if (position.length() != 2) {
            return false;
        }
        position = position.toUpperCase();
        character = position.charAt(0);
        number = position.charAt(1);
        // HORIZONTAL
        if (orientation == Orientation.H) {
            // IF the ship is out the grid returns false
            if ((character < 'A') || (character > 'J') || (number < '0') || ((number + shipSize - 1) > '9')) {
                return false;
            }
            // If the ship is on a invalide cell
            for (int i = number; i <= (number + shipSize - 1); i++) {
                if (invalidCell(character, (char) i)) return false;
            }
            //Adding the ship
            for (int i = number; i <= (number + shipSize - 1); i++) {
                newPosition = (character + "" + ((char) (i)));
                cell = new Cell(newPosition, ship);
                cells.put(newPosition, cell);
            }
        // Vertical
        } else {
            // IF the ship is out the grid returns false
            if ((character < 'A') || ((character + shipSize - 1) > 'J') || (number < '0') || (number > '9')) {
                return false;
            }
            // If the ship is on a invalide cell
            for (int i = character; i <= (character + shipSize - 1); i++) {
                if (invalidCell((char) i, number)) return false;
            }
            //Adding the ship
            for (int i = character; i <= (character + shipSize - 1); i++) {
                newPosition = (((char) (i)) + "" + number);
                cell = new Cell(newPosition, ship);
                cells.put(newPosition, cell);
            }
        }
        this.numShipsLeft++;
        return true;
    }

    /**
     * Removes the ship at the position, orientation  and size put
     * @param shipSize
     * @param position
     * @param orientation
     */
    public void removeShip(int shipSize, String position, Orientation orientation) {
        char character, number;
        String newPosition;
        position = position.toUpperCase();
        character = position.charAt(0);
        number = position.charAt(1);
        //Horizontal
        if (orientation == Orientation.H) {
            for (int i = number; i <= (number + shipSize - 1); i++) {
                newPosition = (character + "" + ((char) (i)));
                cells.remove(newPosition);
            }
        //Vertical
        } else {
            for (int i = character; i <= (character + shipSize - 1); i++) {
                newPosition = (((char) (i)) + "" + number);
                cells.remove(newPosition);
            }
        }
        numShipsLeft--;
    }

    /**
     * Return the next free position of the gird
     * @param position
     * @return
     */
    public String nextPosition(String position) {
        char character, number;
        if (position.length() != 2) {
            return null;
        }
        position = position.toUpperCase();
        character = position.charAt(0);
        number = position.charAt(1);
        if (number == '9') {
            character = ((char) (character + 1));
            number = '0';

        } else {
            number = ((char) (number + 1));
        }
        while (character <= 'J') {
            if (!invalidCell(character, number)) return (character + "" + number);
            if (number == '9') {
                character = ((char) (character + 1));
                number = '0';

            } else {
                number = ((char) (number + 1));
            }
        }
        return null;
    }

    /**
     * Validate if is free that cell and the cells next it
     * @param character
     * @param number
     * @return true if is invalid, false if it is valid
     */
    private boolean invalidCell(char character, char number) {
        return (cells.containsKey(character + "" + number)
                || cells.containsKey(((char) (character - 1)) + "" + number)
                || cells.containsKey(((char) (character + 1)) + "" + number)
                || cells.containsKey(character + "" + ((char) (number - 1)))
                || cells.containsKey(character + "" + ((char) (number + 1))));
    }
}