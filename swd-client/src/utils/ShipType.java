package utils;

/**
 * Class that represents the -- Object
 */
public enum ShipType {
    A, B, S, D, P;
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