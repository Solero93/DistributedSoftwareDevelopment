package controller;

import communication.Communication;
import controller.gameModes.GameMode;
import controller.gameModes.GameModeFactory;
import exceptions.ReadGridException;
import model.Grid;
import utils.Message;
import utils.Orientation;
import utils.ShipType;

import java.io.*;
import java.util.HashMap;

/**
 * Class that represents the Controller Object
 */
public class Controller {
    private Communication server;
    private Grid myGrid;
    private GameMode gm;

    public Controller() {
        this.server = new Communication();
        this.myGrid = new Grid();
    }

    public void generateGridAutomatic() throws IOException, ReadGridException {
        this.generateGridFromFile("../res/layout1.txt"); // TODO might do some algorithmic put
    }

    public void generateGridFromFile(String filename) throws IOException, ReadGridException, IllegalArgumentException {
        String currentLine;
        // Create hashmap to count number of created ships
        HashMap<ShipType, Integer> numShips = new HashMap<>();
        for (ShipType ship : ShipType.values()) {
            numShips.put(ship, ship.numShips);
        }

        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] shipToPut = currentLine.split(",");
            String shipType = shipToPut[0].toUpperCase(), position = shipToPut[1], orientation = shipToPut[2].toUpperCase();
            int shipLeftOfType = numShips.get(ShipType.valueOf(shipType));
            // If you try to put too many ships of a type or
            // There has been an error while putting, throw error
            if (shipLeftOfType == 0 ||
                    !this.myGrid.putShip(ShipType.valueOf(shipType).size, position, Orientation.valueOf(orientation))) {
                bufferedReader.close();
                throw new ReadGridException();
            }
            numShips.put(ShipType.valueOf(shipType), shipLeftOfType - 1);
        }
        bufferedReader.close();
        // If there are ships that weren't put, throw error
        for (ShipType ship : numShips.keySet()) {
            if (numShips.get(ship) != 0) {
                throw new ReadGridException();
            }
        }
    }

    // We force user to put all ships in order
    public void generateGridByUser(String shipType, String position, String orientation) throws ReadGridException, IllegalArgumentException {
        if (!this.myGrid.putShip(ShipType.valueOf(shipType).size, position, Orientation.valueOf(orientation))) {
            throw new ReadGridException();
        }
    }

    public Message hitMyCell(String position) {
        return this.myGrid.hitCell(position);
    }

    // TODO think about treating communication errors
    public Message hitEnemyCell(String position) {
        return this.hitMyCell(position); // TODO quitar esto e ir al servidor, solo está para pruebas
        //return this.server.hitCell(position);
    }

    public void createGameMode(int mode) {
        this.gm = new GameModeFactory().createGameMode(mode);
    }

    public Message play() {
        String position = this.gm.play();
        Message m = this.hitEnemyCell(position);
        if (m == Message.ERROR) {
            this.gm.undoMove();
        } else {
            this.gm.commitMove();
        }
        return m;
    }
}