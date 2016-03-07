package view;

import controller.Controller;
import exceptions.ReadGridException;
import utils.ShipType;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class that represents the Menu of the Game
 */
public class Menu {
    Controller ctrl;
    String server, layout;
    int port, mode;

    public Menu(String server, int port, String layout, int mode) {
        this.ctrl = new Controller();
        this.server = server;
        this.port = port;
        this.layout = layout;
        this.mode = mode;
    }

    public void showMenu() {
        if (layout == null) {
            System.out.println("Since you haven't specified any layout, you'll have to enter ships one by one: ");
            while (!this.getLayoutFromKeyboard()) ;
            System.out.println("Your ships are in place. Yay!");
        } else {
            try {
                ctrl.generateGridAutomatic();
                //ctrl.generateGridFromFile(layout);
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

    private boolean getLayoutFromKeyboard() {
        Scanner sc = new Scanner(System.in);
        String position, orientation;
        ShipType[] ships = {ShipType.A, ShipType.B, ShipType.B, ShipType.S, ShipType.S,
                ShipType.D, ShipType.D, ShipType.P, ShipType.P};
        for (int i = 0; i < ships.length; i++) {
            System.out.println("Entering ship number " + i + " :");
            System.out.println("Specify the " + ships[i].fullName + "'s position: (e.g. A2):");
            position = sc.nextLine();
            System.out.println("Specify the " + ships[i].fullName + "'s orientation: (H or V):");
            orientation = sc.nextLine();
            try {
                this.ctrl.generateGridByUser(ships[i].name(), position, orientation);
            } catch (ReadGridException e) {
                System.out.println("The ship could not be put in its place, you will be asked to enter the ships again.\n");
                return false;
            }
        }
        return true;
    }

    private void playGame() {
        this.ctrl.createGameMode(mode);
        boolean keepPlaying = true;
        while (keepPlaying) {
            switch (this.ctrl.play()) {
                case HIT:
                    System.out.println("You've hit a ship!");
                    break;
                case MISS:
                    System.out.println("You've missed :-(");
                    break;
                case SUNK:
                    System.out.println("You've sunk a ship!");
                    break;
                case YOU_WIN:
                    System.out.println("You won the game!");
                    keepPlaying = false;
                    break;
                case ERROR:
                    System.out.println("There has been error while trying to perform this move.");
                    break;
                // TODO Need to put case player loses
            }
            System.out.println(this.ctrl.getCurrentGrid() + "\n");
        }
    }
}