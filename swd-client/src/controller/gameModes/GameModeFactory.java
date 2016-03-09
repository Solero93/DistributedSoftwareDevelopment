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
                return new RandomAI();
            case 2:
                return new BetterAI();
            default:
                return null;
        }
    }
}
