package utils;

/**
 * Class that represents the -- Object
 */
public class Const {
    public enum ShipType {
        A, B, S, D, P;
        public int size;

        static {
            A.size = 5;
            B.size = 4;
            S.size = 3;
            D.size = 3;
            P.size = 2;
        }

        public int numShips;

        static {
            A.numShips = 1;
            B.numShips = 2;
            S.numShips = 2;
            D.numShips = 2;
            P.numShips = 2;
        }
    }

    public enum Orientation {
        H, V
    }

    public enum Message {
        ALREADY_TRIED, HIT, MISS, SUNK, WON;
        public String message;

        static {
            ALREADY_TRIED.message = "0";
            HIT.message = "A";
            MISS.message = "B";
            SUNK.message = "C";
            WON.message = "D";
        }
    }
}
