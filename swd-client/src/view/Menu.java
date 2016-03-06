package view;

import controller.Controller;
import exceptions.ReadGridException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class that represents the Menu of the Game
 */
public class Menu {
    Controller ctrl;
    String server, layout;
    int port, mode;

    public Menu(String server, int port, String layout, int mode){
        this.ctrl = new Controller();
        this.server = server;
        this.port = port;
        this.layout = layout;
        this.mode = mode;
    }

    public void showMenu() {
        if (layout == null){
            while(!this.getLayoutFromKeyboard());
        } else {
            try {
                ctrl.generateGridFromFile(layout);
                System.out.println("Your ships are in place. Yay!");
            } catch (IOException e) {
                System.out.println("The specified layout file could not be read");
                return;
            } catch (ReadGridException e) {
                System.out.println("The specified layout file has errors in it");
                return;
            }
        }
        this.playGame();
    }

    private boolean getLayoutFromKeyboard(){
        System.out.println("Since you haven't specified any layout, you'll have to enter ships one by one:");
        Scanner sc = new Scanner(System.in);
        String position, orientation;
        String[] ships = {"Aircraft Carrier", "Battleship", "Battleship", "Submarine", "Submarine",
                "Destroyer", "Destroyer", "Patrol Boat", "Patrol Boat"};
        for (int i=0; i<ships.length;i++){
            System.out.println("Entering ship number " + i + " :");
            System.out.println("Specify the " + ships[i] + "'s position: (e.g. A2):");
            position = sc.nextLine();
            System.out.println("Specify the " + ships[i] + "'s orientation: (H or V):");
            orientation = sc.nextLine();
            try {
                this.ctrl.generateGridByUser(ships[i].substring(0,1), position, orientation);
            } catch (ReadGridException e) {
                System.out.println("The ship could not be put in its place, you will be asked to enter the ships again.\n");
                return false;
            }
        }
        System.out.println("Your ships are in place. Yay!");
        return true;
    }

    private void playGame(){
        this.ctrl.createGameMode(mode);
        boolean keepPlaying = true;
        while (keepPlaying){
            switch(this.ctrl.play()){
                case HIT:
                    System.out.println("You've hit a ship!");
                    break;
                case MISS:
                    System.out.println("You've missed :-(");
                    break;
                case SUNK:
                    System.out.println("You've sunk a ship!");
                    break;
                case WON:
                    System.out.println("You won the game!");
                    keepPlaying = false;
                    break;
                case ERROR:
                    System.out.println("There has been error while this move.");
                    break;
                // TODO Falta poner el caso que el jugador pierde
            }
        }
    }
}