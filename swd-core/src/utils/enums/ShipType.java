package utils.enums;

/**
 * Class that represents the ShipTypes
 */
public enum ShipType {
    A("Aircraft Carrier", 5, 1),
    B("Battleship", 4, 2),
    S("Submarine", 3, 2),
    D("Destroyer", 3, 2),
    P("Patrol Boat", 2, 2);

    public String fullName;
    public int size;
    public int numShips;

    ShipType(String fullName, int size, int numShips) {
        this.fullName = fullName;
        this.size = size;
        this.numShips = numShips;
    }
}