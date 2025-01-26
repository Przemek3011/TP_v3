package checkers.client;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * Client class
 */
public class Client {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;
    private final BoardGUI board;
    private final int numberOfPlayers;

    public Client(String serverAddress, int port,int numberOfPlayers) throws IOException {
        this.numberOfPlayers=numberOfPlayers;
        this.socket = new Socket(serverAddress, port);
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.board = new BoardGUI(this,numberOfPlayers);
    }
    /**
     * send message to server
     * @param message message sent to server
     */
    public void sendMessageToServer(String message) {
        out.println(message);
    }
    /**
     * get messate from server 
     * @return get message from server
     */
    public String receiveMessageFromServer() {
        if (in.hasNextLine()) {
            return in.nextLine();
        }
        return null;
    }   

    /**
     * close connection
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        socket.close();
    }
    /**
     * start game and functions of client in game
     */
    public void launch() {
        new Thread(() -> {
            try {
                while (!socket.isClosed() && in.hasNextLine()) {
                    String line = receiveMessageFromServer();
                    if (line == null) break;

                    SwingUtilities.invokeLater(() -> {
                        // If it's a board update
                        if (line.startsWith("update:")) {
                            String updateData = line.substring(7);
                            handleBoardUpdate(updateData);
                        } 
                        else {
                            // For everything else (server announcements, chat, etc.)
                            board.appendMessage(line);
                        }
                    });
                }
                closeConnection();
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> board.appendMessage("Connection lost."));
            }
        }).start();
    }
    /**
     * change string that represent board into board of integers
     * @param updateData string that represents board
     */
    private void handleBoardUpdate(String updateData) {
        String[] cells = updateData.split(",");
        int[][] newBoard = new int[17][25];

        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 25; j++) {
                newBoard[i][j] = Integer.parseInt(cells[i * 25 + j]);
            }
        }
        board.updateBoard(newBoard);
    }
}
