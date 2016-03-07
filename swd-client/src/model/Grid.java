package model;

import utils.Message;
import utils.Orientation;

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

    public Message hitCell(String position) {
        Message tmp;
        char character, number;
        if (position.length() != 2) {
            return Message.ERROR;
        }
        position = position.toUpperCase();
        character = position.charAt(0);
        number = position.charAt(1);
        if ((character < 'A') || (character > 'J') || (number < '0') || ((number) > '9')) {
            return Message.ERROR;
        }

        if (cells.containsKey(position)) {
            tmp = cells.get(position).hitCell();
            if (tmp == Message.SUNK) {
                return (--this.numShipsLeft == 0) ? Message.YOU_WIN : tmp;
            }
            return tmp;
        }
        return Message.MISS;
    }

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
        if (orientation == Orientation.H) {
            if ((character < 'A') || (character > 'J') || (number < '0') || ((number + shipSize - 1) > '9')) {
                return false;
            }
            for (int i = number; i <= (number + shipSize - 1); i++) {
                if (invalidCell(character, (char) i)) return false;
            }
            for (int i = number; i <= (number + shipSize - 1); i++) {
                newPosition = (character + "" + ((char) (i)));
                cell = new Cell(newPosition, ship);
                cells.put(newPosition, cell);


            }

        } else {
            if ((character < 'A') || ((character + shipSize - 1) > 'J') || (number < '0') || (number > '9')) {
                return false;
            }
            for (int i = character; i <= (character + shipSize - 1); i++) {
                if (invalidCell((char) i, number)) return false;
            }
            for (int i = character; i <= (character + shipSize - 1); i++) {
                newPosition = (((char) (i)) + "" + number);
                cell = new Cell(newPosition, ship);
                cells.put(newPosition, cell);


            }

        }
        this.numShipsLeft++;
        return true;
    }


    public void removeShip(int shipSize, String position, Orientation orientation) {
        char character, number;
        String newPosition;
        position = position.toUpperCase();
        character = position.charAt(0);
        number = position.charAt(1);
        if (orientation == Orientation.H) {
            for (int i = number; i <= (number + shipSize - 1); i++) {
                newPosition = (character + "" + ((char) (i)));
                cells.remove(newPosition);
            }
        } else {
            for (int i = character; i <= (character + shipSize - 1); i++) {
                newPosition = (((char) (i)) + "" + number);
                cells.remove(newPosition);
            }
        }
    }

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

    // TODO "Generate grid" method missing
}
