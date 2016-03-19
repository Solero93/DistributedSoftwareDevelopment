package controller;

import controller.gameModes.GameMode;
import controller.gameModes.GameModeFactory;
import exceptions.ReadGridException;
import model.Grid;
import utils.enums.Message;
import utils.enums.Orientation;
import utils.enums.ShipType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class that represents the Controller Object
 */
public class Controller {
    private Grid myGrid;
    private GameMode gm;

    public Controller() {
        this.myGrid = new Grid();
    }

    public void generateGridAutomatic() throws ReadGridException {
        List<ShipType> ships = Arrays.asList(ShipType.A, ShipType.B, ShipType.B, ShipType.S, ShipType.S,
                ShipType.D, ShipType.D, ShipType.P, ShipType.P);
        Collections.shuffle(ships); // Randomizing ships
        ArrayList<Orientation> orient = new ArrayList<>();
        Random orientRandom = new Random();
        for (int i = 0; i < ships.size(); i++) { // Randomizing orientation of each ship
            switch (orientRandom.nextInt(2)) {
                case 0:
                    orient.add(Orientation.H);
                    break;
                case 1:
                    orient.add(Orientation.V);
                    break;
            }
        }
        ArrayList<String> shipPositions = new ArrayList<>();
        int i = 0;
        for (String currPosition = "A0"; shipPositions.size() < ships.size();
             currPosition = myGrid.nextPosition(currPosition)) {
            if (currPosition == null) {
                // If there are no more positions to put a ship
                // We do backtracking
                currPosition = shipPositions.remove(i - 1);
                this.myGrid.removeShip(ships.get(i - 1).size, currPosition, orient.get(i - 1));
                i--;
            } else if (this.myGrid.putShip(ships.get(i).size, currPosition, orient.get(i))) {
                // If you could put the ship in place
                // Try to put the next ship
                shipPositions.add(currPosition);
                i++;
            }
        }
    }

    public void generateGridFromFile(String filename) throws IOException, ReadGridException, IllegalArgumentException {
        // Create hashmap to count number of created ships
        HashMap<ShipType, Integer> numShips = new HashMap<>();
        for (ShipType ship : ShipType.values()) {
            numShips.put(ship, ship.numShips);
        }
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] shipToPut = currentLine.split(",");
            String shipType = shipToPut[0].toUpperCase(), position = shipToPut[1], orientation = shipToPut[2].toUpperCase();
            int shipLeftOfType = numShips.get(ShipType.valueOf(shipType));
            // If you try to put too many ships of a type or
            // there has been an error while putting, throw error
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

    public String play() {
        return this.gm.play();
    }

    public void commitMove(Message m) {
        this.gm.commitMove(m);
    }

    public String getCurrentGrid() {
        return this.myGrid.toString();
    }
}