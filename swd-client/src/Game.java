import controller.Controller;
import exceptions.ReadGridException;
import utils.Message;
import utils.enums.Command;
import utils.enums.ShipType;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class that represents the Game of the Game
 */
public class Game {
    Controller ctrl;
    String server, layout;
    int port, mode;

    public Game(String server, int port, String layout, int mode) {
        this.ctrl = new Controller();
        this.server = server;
        this.port = port;
        this.layout = layout;
        this.mode = mode;
    }

    public void prepareGame() {
        if (layout == null) {
            if (mode == 0) {
                System.out.println("Since you haven't specified any layout, you'll have to enter ships one by one: ");
                while (!this.getLayoutFromKeyboard()) ;
                System.out.println("Your ships are in place. Yay!");
            } else {
                try {
                    this.ctrl.generateGridAutomatic();
                    System.out.println(this.ctrl.getCurrentGrid() + "\n");
                    System.out.println("Your ships are in place. Yay!");
                } catch (ReadGridException e) {
                    System.out.println("There has been an error when autogenerating grid");
                    return;
                }
            }
        } else {
            try {
                this.ctrl.generateGridFromFile(layout);
                System.out.println(this.ctrl.getCurrentGrid() + "\n");
                System.out.println("Your ships are in place. Yay!");
            } catch (IOException e) {
                System.out.println("The specified layout file could not be read");
                return;
            } catch (ReadGridException e) {
                System.out.println("The specified layout file has errors in it");
                return;
            }
        }
        try {
            this.ctrl.createGameMode(mode);
            this.ctrl.createCommunication(server, port);
        } catch (IOException e) {
            System.out.println("There has been an error while trying to communicate with server");
            return;
        }
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

    public void playGame() {
        Message firstResponse;
        try {
            this.ctrl.sendMessage(Command.START, null);
                firstResponse = this.ctrl.waitForEnemy();
            if (firstResponse.getCommand() != Command.GRID_RDY) return;
            this.throwDice();
        } catch (IOException e) {
            return; // Shouldn't arrive here
        }

        while (true) {
            Message enemyResponse;
            try {
                this.ctrl.play();
                enemyResponse = this.ctrl.waitForEnemy();
            } catch (IOException e) {
                return; // Shouldn't arrive here
            }
            if (this.myMove(enemyResponse)) break;

            Message myResponse;
            try {
                myResponse = this.ctrl.waitForEnemy();
            } catch (IOException e) {
                return; // Shouldn't arrive here
            }
            if (this.enemyMove(myResponse)) break;
        }
    }

    private void throwDice() throws IOException {
        boolean keepThrowing = true;
        while (keepThrowing) {
            this.ctrl.sendMessage(Command.THROW, null);
            Message throwResponse = this.ctrl.waitForEnemy();
            switch (throwResponse.getCommand()) {
                case HUMAN_FIRST:
                    keepThrowing = false;
                    System.out.println("You start!");
                    break;
                case DRAW:
                    break;
                case FIRE:
                    Message myResponse = this.ctrl.hitMyCell(throwResponse.getParams());
                    this.enemyMove(myResponse);
                    keepThrowing = false;
                    System.out.println("Server starts!");
                    break;
                case ERROR:
                    System.out.println("There has been an error while trying to perform this move: "
                            + throwResponse.getParams());
                default:
                    System.out.println("Illegal command.");
                    return; // Shouldn't arrive here
            }
        }
    }

    private boolean myMove(Message msg) {
        switch (msg.getCommand()) {
            case HIT:
                System.out.println("You hit a ship at position" + msg.getParams());
                break;
            case MISS:
                System.out.println("You missed a ship at position" + msg.getParams());
                break;
            case SUNK:
                System.out.println("You sunk a ship at position" + msg.getParams());
                break;
            case YOU_WIN:
                System.out.println("You won the game! :-D");
                return true;
            case ERROR:
                System.out.println("There has been an error while trying to perform move" + msg.getParams());
                return true;
            default:
                System.out.println("Illegal command.");
        }
        return false;
    }

    private boolean enemyMove(Message msg) {
        switch (msg.getCommand()) {
            case HIT:
                System.out.println("Enemy hit a ship at position" + msg.getParams());
                break;
            case MISS:
                System.out.println("Enemy missed a ship at position" + msg.getParams());
                break;
            case SUNK:
                System.out.println("Enemy sunk a ship at position" + msg.getParams());
                break;
            case YOU_WIN:
                System.out.println("You lost the game :-(");
                return true;
            case ERROR:
                System.out.println("There has been an error while trying to perform move: " + msg.getParams());
                return true;
            default:
                System.out.println("Illegal command.");
        }
        return false;
    }

    public void closeGame(){
        this.ctrl.closeConnections();
    }
}