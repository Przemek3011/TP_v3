package checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerApplication {

    /**
     * Initializes the game by getting the number of players and bots.
     *
     * @return an array where [0] = number of players, [1] = number of bots
     */
    private static int[] initGame() {
        System.out.println("Hello! Welcome to Trylma!");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Enter the number of players:");
                int players = Integer.parseInt(scanner.nextLine().trim());

                System.out.println("Enter the number of bots:");
                int bots = Integer.parseInt(scanner.nextLine().trim());

                int total = players + bots;

                if (total == 2 || total == 3 || total == 4 || total == 6) {
                    return new int[]{players, bots}; // Return an array of players and bots
                } else {
                    System.out.println("The total number of players and bots must be 2, 3, 4, or 6. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input is not a valid number. Please try again.");
            }
        }
    }

    /**
     * Initializes the game variant by getting user input.
     *
     * @return the chosen variant (d, s, or r)
     */
    private static String initVariant() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose a variant: d - Default, s - Super, r - Random");
            String variant = scanner.nextLine().trim();

            if (variant.equals("d") || variant.equals("s") || variant.equals("r")) {
                return variant;
            } else {
                System.out.println("Invalid variant. Please choose again.");
            }
        }
    }

    /**
     * Starts the server application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        // Initialize the game settings
        int[] playersAndBots = initGame(); // Get number of players and bots
        int totalPlayers = playersAndBots[0] + playersAndBots[1];
        String variant = initVariant(); // Get the game variant

        // Start the server
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            Server server = new Server(serverSocket, totalPlayers, variant,playersAndBots[1]);
            server.startServer();
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }
}
