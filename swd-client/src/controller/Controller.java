package controller;

import communication.Communication;
import utils.Const;
import exceptions.ReadGridException;
import model.Grid;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that represents the Controller Object
 */
public class Controller {
    private Communication server;
    private ArrayList<String> cellsHit;
    private Grid myGrid;

    public Controller() {
        this.server = new Communication();
        this.cellsHit = new ArrayList<>();
        this.myGrid = new Grid();
    }

    public void generateGridAutomatic() throws IOException, ReadGridException {
        this.generateGridFromFile("../res/layout1.txt"); // TODO might do some algorithmic put
    }

    public void generateGridFromFile(String filename) throws IOException, ReadGridException, IllegalArgumentException {
        String currentLine;
        HashMap<String, Integer> numShips = new HashMap<>(5);
        numShips.put("A", Const.ShipType.A.numShips);
        numShips.put("B", Const.ShipType.B.numShips);
        numShips.put("S", Const.ShipType.S.numShips);
        numShips.put("D", Const.ShipType.D.numShips);
        numShips.put("P", Const.ShipType.P.numShips);

        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] shipToPut = currentLine.split(",");
            String position = shipToPut[0], shipType = shipToPut[1].toUpperCase(), orientation = shipToPut[2].toUpperCase();
            int shipLeftOfType = numShips.get(shipType);
            if (shipLeftOfType == 0 ||
                    !this.myGrid.putShip(position, Const.ShipType.valueOf(shipType).size, Const.Orientation.valueOf(orientation))) {
                bufferedReader.close();
                throw new ReadGridException();
            }
            numShips.put(shipType, shipLeftOfType - 1);
        }
    }

    // We force user to put all ships in order
    public void generateGridByUser(ArrayList<String[]> shipsToPut) throws ReadGridException, IllegalArgumentException {
        for (String[] shipToPut : shipsToPut) {
            String position = shipToPut[0], shipType = shipToPut[1].toUpperCase(), orientation = shipToPut[2].toUpperCase();
            if (!this.myGrid.putShip(position, Const.ShipType.valueOf(shipType).size, Const.Orientation.valueOf(orientation))) {
                throw new ReadGridException();
            }
        }
    }

    public Const.Message hitMyCell(String position) {
        return this.myGrid.hitCell(position);
    }

    // TODO think about treating communication errors
    // TODO Also add position to cellsHit
    public Const.Message hitEnemyCell(String position) {
        if (cellsHit.contains(position)) {
            return Const.Message.ALREADY_TRIED;
        } else {
            return this.server.hitCell(position);
        }
    }
}