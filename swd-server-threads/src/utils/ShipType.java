package utils;

/**
 * Class that represents the -- Object
 */
public enum ShipType {
    A, B, S, D, P;

    public String fullName;

    static {
        A.fullName = "Aircraft Carrier";
        B.fullName = "Battleship";
        S.fullName = "Submarine";
        D.fullName = "Destroyer";
        P.fullName = "Patrol Boat";
    }

    public int size;

    static {
        A.size = 5;
        B.size = 4;
        S.size = 3;
        D.size = 3;
        P.size = 2;
    }

    public int numShips;

    static {
        A.numShips = 1;
        B.numShips = 2;
        S.numShips = 2;
        D.numShips = 2;
        P.numShips = 2;
    }
}