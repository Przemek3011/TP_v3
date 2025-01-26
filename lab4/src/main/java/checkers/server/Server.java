package checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import checkers.Game.Game;
/**
 * class that represents server and handles messages from clients
 */
public class Server {

    private final ServerSocket serverSocket;
    private final int numOfPlayers;
    private final ArrayList<ClientHandler> clientHandlers;
    private final String variant;
    private Map<Integer, Integer> clientIdToPieceValue = new HashMap<>();
    int MyPieceNumber;
    int currentX,currentY;

    private static int clientCounter = 0;
    private int currentTurnIndex = 0;

    private Game game;

    private int[][] board;

/**
 * constructor of server class 
 * @param serverSocket socket
 * @param numOfPlayers number of players from server application
 * @param variant variant of game
 */
public Server(ServerSocket serverSocket, int numOfPlayers,String variant) {
    this.variant=variant;
    this.serverSocket = serverSocket;
    this.numOfPlayers = numOfPlayers;
    this.clientHandlers = new ArrayList<>();
}
    /** start server
     * assign pieces to client 
     * create game
     */
public void startServer(){
    try {
        
        while(!serverSocket.isClosed() && clientHandlers.size() < numOfPlayers) {
            Socket socket = serverSocket.accept();
            System.out.println("A player connected.");

            ClientHandler clientHandler = new ClientHandler(socket, this, ++clientCounter);
            clientHandlers.add(clientHandler);

            Thread thread = new Thread(clientHandler);
            thread.start();
        }
        
        System.out.println("All players connected.");

        this.game = new Game(numOfPlayers, 0, variant);
        game.getBoard();

        //Assign player piece number
        for (int i = 0; i < clientHandlers.size(); i++) {
           
            int clientID = clientHandlers.get(i).getClientID();
           
            Game tempGame = new Game(numOfPlayers, clientID, variant);
            int pieceValue = tempGame.getNumberOnBoard(numOfPlayers, i+1);

            clientIdToPieceValue.put(clientID, pieceValue);

            System.out.println("Assigning Player #" + clientID 
                               + " the piece value of " + pieceValue);
        }
        
        broadcastUpdatedBoard();
        notifyCurrentPlayer();

    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
}

    /**
     * Called by each ClientHandler when it reads a command from its client.
     * handles moves like skip or move y1 x1 y2 x2 [y3 x3]
     */
    public synchronized void handleMove(ClientHandler clientHandler, String command) {
        int clientID = clientHandler.getClientID();
        int pieceValue = clientIdToPieceValue.get(clientID);
        if(game.hasWon(clientIdToPieceValue.get(clientID))){switchTurn();}
        else{
        int playerIndex = clientHandlers.indexOf(clientHandler);
        if (playerIndex != currentTurnIndex) {
            clientHandler.sendMessage("SERVER: It's not your turn. Please wait.");
            return;
        }
    
        String trimmed = command.trim().toUpperCase();
    
        if (trimmed.equals("SKIP")) {
            broadcastMessage("SERVER: Player #" + clientHandler.getClientID() + " skipped their turn.");
            switchTurn();
            return;
        }
        else if (trimmed.startsWith("MOVE")) {
            String[] parts = trimmed.split("\\s+");
            if (parts.length < 5) {
                clientHandler.sendMessage("SERVER: Invalid MOVE format. Use: MOVE y1 x1 y2 x2 [y3 x3] ...");
                return;
            }
            // Also, (parts.length - 1) should be even (because each cell = 2 ints).
            // e.g. "MOVE" is 1 token, so the rest must be an even number:
            if ((parts.length - 1) % 2 != 0) {
                clientHandler.sendMessage("SERVER: Unbalanced coordinates. Provide pairs of x,y.");
                return;
            }
            
            try {
                int x1 = Integer.parseInt(parts[1]);
                int y1 = Integer.parseInt(parts[2]);
                if(!isInBounds(x1, y1)){clientHandler.sendMessage("SERVER: Too big x or y.");
                return;}
                
                List<int[]> moves = new ArrayList<>();
                for (int i = 3; i < parts.length; i += 2) {
                    int nx = Integer.parseInt(parts[i]);
                    int ny = Integer.parseInt(parts[i+1]);
                    currentX=nx;
                    currentY=ny;
                    if(!isInBounds(currentX,currentY))
                    {clientHandler.sendMessage("SERVER: Too big x or y.");
                return;}
                    moves.add(new int[]{nx, ny});
                }
    
                int[][] board = game.getBoard();
                if(board[x1][y1] == 0 || board[x1][y1]==1){
                    clientHandler.sendMessage("SERVER: You picked a cell without any pieces. Choose again.");
                    return;
                }
                if(board[currentX][currentY]!=1){
                    clientHandler.sendMessage("SERVER:There is piece on end move. Choose again.");
                    return;

                }
                if (board[x1][y1] != pieceValue) {
                    clientHandler.sendMessage("SERVER: That piece isn't yours. You can only move your own pieces.");
                    return;
                }
                if(!game.isValidMove(x1, y1, moves)){
                    clientHandler.sendMessage("SERVER: Wrong Move.");
                    return;
                }
    
                game.setGamePiece(currentX, currentY, pieceValue);
                game.setGamePiece(x1, y1, 1);
    
                broadcastMessage("SERVER: Player #" + clientID
                                 + " moved from (" + x1 + "," + y1
                                 + ") to (" + currentX + "," + currentY + ").");
    
                broadcastUpdatedBoard();
                if(game.hasWon(pieceValue)){
                    broadcastMessage("SERVER: Player #" + clientID+ "has won!!");

                }
                switchTurn();
    
            } catch (NumberFormatException e) {
                clientHandler.sendMessage("SERVER: Could not parse coordinates. Use: MOVE y1 x1 y2 x2 [y3 x3]");
            }
        }
        else {
            clientHandler.sendMessage("SERVER: Unrecognized command. Type 'SKIP' or 'MOVE y1 x1 y2 x2 [y3 x3]'.");
        }
    }}

    /**
     * Moves to the next player's turn, and notifies them.
     */
    public synchronized void switchTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % clientHandlers.size();
        notifyCurrentPlayer();
    }

    /**
     * Tells the current player it's their turn.
     */
    public synchronized void notifyCurrentPlayer() {
        ClientHandler currentPlayer = clientHandlers.get(currentTurnIndex);
        
        
        int clientID = currentPlayer.getClientID();
        
        
        Integer pieceValueObj = clientIdToPieceValue.get(clientID);
        
        if (pieceValueObj == null) {
            currentPlayer.sendMessage("SERVER: Your piece value is not assigned. Contact the server administrator.");
            return;
        }
        currentPlayer.sendMessage("SERVER: It's your turn. Make your move! (Your color is: " + game.color(pieceValueObj) + ")");
    }
    

    /**
     * Broadcasts a message to every connected client.
     */
    public synchronized void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    /**
     * Broadcasts the current board as a string prefixed by "update:".
     * Each client can parse and repaint their board accordingly.
     */
    private void broadcastUpdatedBoard() {
        int[][] currentBoard = game.getBoard();  // from the Game
    
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                sb.append(currentBoard[i][j]);
                sb.append(",");
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); // remove trailing comma
        }
    
        broadcastMessage("update:" + sb.toString());
    }

    /**
     * Simple helper to check board boundaries.
     */
    private boolean isInBounds(int x, int y) {
        board=game.getBoard();
        return (x >= 0 && x < board.length 
             && y >= 0 && y < board[0].length);
    }

    /**
     * Close the server socket if needed.
     */
    public void closeServerSocket(){
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
