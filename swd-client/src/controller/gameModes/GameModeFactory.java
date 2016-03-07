package controller.gameModes;

/**
 * Class that represents the -- Object
 */
public class GameModeFactory {
    public GameMode createGameMode(int mode) {
        switch (mode) {
            case 0:
                return new ManualGame();
            case 1:
                return new BetterAI();
            case 2:
                return new RandomAI();
            default:
                return null;
        }
    }
}
