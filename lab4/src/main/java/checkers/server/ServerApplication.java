package checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerApplication {
        /**
         * init function that gives number of players
         * @return number of players
         */
    private static int initGame() {
        System.out.println("Hello! Welcome to Trylma!");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                System.out.println("Enter number of players: 2, 3, 4 or 6.");
                int number = Integer.parseInt(scanner.nextLine());
                if (number != 2 && number != 3 && number != 4 && number != 6) {
                    System.out.println("This number is wrong.");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input is not a number.");
            }
        }
    }
    /**
     * init function that passes variant of the game
     * @return variant of the game
     */
    private static String initVariant() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
                System.out.println("Choose variant: d - Default, s - Super, r - Random");
                String variant = scanner.nextLine();
                if (!variant.equals("s") && !variant.equals("r")  && !variant.equals("d")) {
                    System.out.println("This variant is wrong.");
                } else {
                    return variant;
                }
        }
    }

    /**
     * function that turns on Server
     * @param args
     */
    public static void main(String[] args) {
        int numberOfPlayers = initGame();
        String variant = initVariant();
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            Server server = new Server(serverSocket,numberOfPlayers,variant);
            server.startServer();
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }
}
