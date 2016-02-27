package model;

/**
 * Class that represents the -- Object
 */
public class Ship {
    //String position --> no hace falta, porque ya lo vamos colocando, la celda ya tiene esa información
    //boolean isHorizontal; --> lo mismo , aunque no recuerdo si esto lo dijimos para put o para jugar
    private int size;
    private int life; // TODO Shouldn't really need, since size is never consulted ever after

    public Ship(int size){
        this.size = size;
        this.life = this.size;
    }

    public boolean loseLife(){
        return (--this.life == 0);
    }
}