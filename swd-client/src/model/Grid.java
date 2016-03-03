package model;

import java.util.HashMap;

/**
 * Class that represents the Grid Object
 */
public class Grid {
    private HashMap<String, Cell> cells;
    private int numShipsLeft;

    public Grid(){
        this.cells = new HashMap<>();
        this.numShipsLeft = 0;
    }

    public int hitCell(String position){
        return cells.get(position).hitCell(); // TODO switch of cases to return
    }

    public boolean putShip(String position, Ship ship, boolean isHorizontal){
        char character, number;
        int characterAscii, numberAscii, shipSize;
        position=position.toLowerCase();
        if (position.length()!=2){
            return false;
        }
        character= position.charAt(0);
        number= position.charAt(1);
        characterAscii= (int) character;
        numberAscii= (int) number;
        shipSize=ship.getSize();
        if( isHorizontal){
            if((characterAscii< 97 ) || (characterAscii > 106) || (numberAscii< 48) || ((numberAscii + shipSize )> 57 )){
                return false;
            }


        }else{
            if((characterAscii< 97 ) || ((characterAscii + shipSize ) > 106) || (numberAscii< 48) || (numberAscii> 57 )){
                return false;
            }

        }
        return true; // TODO check if ship can be put or not
    }

    public 

    // TODO "Generate grid" method missing
}
