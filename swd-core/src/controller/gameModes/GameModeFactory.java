package controller.gameModes;

/**
 * Factory object that creates a Game Mode
 */
public class GameModeFactory {
    /**
     * Constructs a GameMode depending on mode parameter
     *
     * @param mode : Specifies which mode to pick
     * @return GameMode object
     */
    public GameMode createGameMode(int mode) {
        switch (mode) {
            case 0:
                return new ManualGame();
            case 1:
                return new RandomAI();
            case 2:
                return new BetterAI();
            default:
                return null;
        }
    }
}
