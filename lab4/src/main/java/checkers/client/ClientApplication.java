package checkers.client;

import java.io.IOException;
import java.util.Scanner;
/**
 * main method to turn on clients
 */
public class ClientApplication {
    /**
     * innit function to get number of clients (it could be done 1 client for each run of program but it would be tedious)
     * @return number of clients
     */
    private static int getNumberOfClients() {
        System.out.println("Enter number of executed client applications.");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                System.out.println("Enter number 2, 3, 4 or 6.");
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
     * main method that creates clients
     * @param args
     */
    public static void main(String[] args) {
        int clientApps = getNumberOfClients();
        for (int i = 0; i < clientApps; i++) {
            try {
                Client client = new Client("localhost", 8000,clientApps);
                client.launch();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}