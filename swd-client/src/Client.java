import view.Menu;

/**
 * Main class with the Client
 */
public class Client {
    public static void main(String args[]) {
        // Parsing command line arguments
        String helpMessage = "Us: java Client -s <maquina_servidora> -p <port> [-f <layout>] [-i 0|1|2]";
        for (String s : args) {
            if (s.equals("-h")) {
                System.out.println(helpMessage);
                return;
            }
        }
        String server = null;
        int port = -1;
        String layout = null;
        int mode = 0; // Default value of mode

        for (int i = 0; i < args.length; i += 2) {
            try {
                switch (args[i]) {
                    case "-s":
                        server = args[i + 1];
                        break;
                    case "-p":
                        port = Integer.parseInt(args[i + 1]);
                        break;
                    case "-f":
                        layout = args[i + 1];
                        break;
                    case "-i":
                        mode = Integer.parseInt(args[i + 1]);
                        break;
                    default:
                        System.out.println("Error in  command format\n" + helpMessage);
                        return;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error parsing numerical arguments\n" + helpMessage);
                return;
            }
        }
        if (server == null || port == -1) {
            System.out.println("Missing parametres\n" + helpMessage);
            return;
        }
        if (mode < 0 || mode > 2) {
            System.out.println("Invalid mode\n" + helpMessage);
            return;
        }

        Menu menu = new Menu(server, port, layout, mode);
        menu.prepareGame();
        System.out.println("Thank you for playing Battleships!");
    }
}