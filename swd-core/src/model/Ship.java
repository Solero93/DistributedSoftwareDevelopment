package model;

/**
 * Class that represents the Ship Object
 */
public class Ship {

    private int life;

    /**
     * Constructor
     *
     * @param size
     */
    public Ship(int size) {
        this.life = size;
    }


    /**
     * Takes a life from the ship.
     * When life is equals to 0 the ship is sunk
     *
     * @return
     */
    public boolean loseLife() {
        return (--this.life == 0);
    }
}