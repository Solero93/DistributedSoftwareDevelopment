package controller.gameModes;

/**
 * Class that represents the RandomAI
 */
public class RandomAI extends GameMode {
    /**
     * Picks randomly a hit position that's not been hit yet.
     *
     * @return Position to Hit
     */
    @Override
    public String generateHitPosition() {
        String letters = "ABCDEFGHIJ", position;
        while (true) {
            position = letters.charAt(this.rand.nextInt(10)) + Integer.toString(this.rand.nextInt(10));
            if (!this.cellsFired.contains(position)) {
                return position;
            }
        }
    }
}