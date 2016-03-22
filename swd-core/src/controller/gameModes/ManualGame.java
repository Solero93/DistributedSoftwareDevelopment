package controller.gameModes;

import java.util.Scanner;

/**
 * Class that represents the -- Object
 */
public class ManualGame extends GameMode {
    public String generateHitPosition() {
        Scanner sc = new Scanner(System.in);
        String position;
        while (true) {
            System.out.println("Enter the field to fire on: ");
            position = sc.nextLine();
            if (!this.cellsFired.contains(position)) {
                this.waitMove = position;
                return position;
            } else {
                System.out.println("You have already fired on this cell, try again.\n");
            }
        }
    }
}
