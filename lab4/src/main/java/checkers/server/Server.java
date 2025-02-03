package checkers.server;

import checkers.Game.Game;
import checkers.Game.Bot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Example Server class handling both human players (via sockets) and bot players locally.
 */
public class Server {

    private final ServerSocket serverSocket;
    private final int numOfPlayers;       
    private final String variant;         
    private final int numberOfBots;       

    private final List<ClientHandler> clientHandlers;

    private final List<Integer> allIDs = new ArrayList<>();

    private final Map<Integer, Boolean> PlayerBots = new HashMap<>();

    private final Map<Integer, Integer> clientIdToPieceValue = new HashMap<>();

    private final Map<Integer, Bot> bots = new HashMap<>();

    private static int clientCounter = 0;
    private int currentTurnIndex = 0;

    private Game game;
    private int[][] board; 

    /**
     * Server constructor.
     *
     * @param serverSocket the ServerSocket
     * @param numOfPlayers total participants (including bots)
     * @param variant variant for example 'd'
     * @param numberOfBots how many bots
     */
    public Server(ServerSocket serverSocket, int numOfPlayers, String variant, int numberOfBots) {
        this.serverSocket = serverSocket;
        this.numOfPlayers = numOfPlayers;
        this.variant = variant;
        this.numberOfBots = numberOfBots;

        this.clientHandlers = new ArrayList<>();
    }

    /**
     * Start the server: accept humans, create bots, create the game, etc.
     */
    public void startServer() {
        try {
            // 1) Accept (numOfPlayers - numberOfBots) human players
            while(!serverSocket.isClosed() 
               && clientHandlers.size() < (numOfPlayers - numberOfBots)) {
                
                Socket socket = serverSocket.accept();
                System.out.println("A player connected.");
               
                clientCounter++;
                ClientHandler ch = new ClientHandler(socket, this, clientCounter);
                clientHandlers.add(ch);

                PlayerBots.put(clientCounter, false);

                allIDs.add(clientCounter);

                new Thread(ch).start();
            }
            this.game = new Game(numOfPlayers, 0, variant);
            this.board = game.getBoard();

            // 2) Create the bot participants
            for(int i=0; i < numberOfBots; i++) {
                clientCounter++;
                System.out.println("Adding BOT with clientID=" + clientCounter);

                PlayerBots.put(clientCounter, true);
                // Create the actual Bot object
                Bot bot = new Bot(numOfPlayers, clientCounter,game.getBoard());
                bots.put(clientCounter, bot);

                // Add to allIDs
                allIDs.add(clientCounter);
            }

            System.out.println("All players (including bots) are set.");

         
            for(int i=0; i<clientHandlers.size(); i++){
                int cID = clientHandlers.get(i).getClientID();
                Game tmp = new Game(numOfPlayers, cID, variant);
                int pieceVal = tmp.getNumberOnBoard(numOfPlayers, i+1);
                clientIdToPieceValue.put(cID, pieceVal);

                System.out.println("Assigning Player #" + cID 
                    + " the piece value of " + pieceVal);
            }

            // 5) Assign piece values to bots

            int humanCount = clientHandlers.size();
            for(int i=0; i<numberOfBots; i++){
                int botID = allIDs.get(humanCount ) + i  ; 
                // or (humanCount + 1 + i) if that's how you enumerated
                Game tmp = new Game(numOfPlayers, botID, variant);
                int pieceVal = tmp.getNumberOnBoard(numOfPlayers, botID);
                clientIdToPieceValue.put(botID, pieceVal);

                System.out.println("Assigning BOT #" + botID
                    + " the piece value " + pieceVal);
            }

            // 6) broadcast initial board, notify first turn
            broadcastUpdatedBoard();
            notifyCurrentPlayer();

        } catch(IOException e) {
            System.err.println("Error in startServer(): " + e.getMessage());
        }
    }

    /**
     * handleMove from a human player's command (like 'MOVE y1 x1 y2 x2' or 'SKIP').
     */
    public synchronized void handleMove(ClientHandler clientHandler, String command) {
        int clientID = clientHandler.getClientID();
        
        // If it's a bot => ignore
        if(Boolean.TRUE.equals(PlayerBots.get(clientID))) {
            System.out.println("Ignoring command from BOT (id=" + clientID + ")");
            return;
        }

        // get pieceValue
        int pieceValue = clientIdToPieceValue.get(clientID);
        if(game.hasWon(pieceValue)){
            switchTurn();
            return;
        }

        // Check if it's the correct turn
        // We see who is the current ID in allIDs
        int currentID = allIDs.get(currentTurnIndex);
        if(clientID != currentID){
            clientHandler.sendMessage("SERVER: It's not your turn. Please wait.");
            return;
        }

        String trimmed = command.trim().toUpperCase();
        if(trimmed.equals("SKIP")){
            broadcastMessage("SERVER: Player #" + clientID + " skipped their turn.");
            switchTurn();
            return;
        }
        else if(trimmed.startsWith("MOVE")){
            String[] parts = trimmed.split("\\s+");
            if(parts.length < 5){
                clientHandler.sendMessage("SERVER: Invalid MOVE format. Use: MOVE y1 x1 y2 x2");
                return;
            }
            if((parts.length - 1) % 2 != 0){
                clientHandler.sendMessage("SERVER: Unbalanced coords. Provide pairs of y,x.");
                return;
            }
            try {
                int y1 = Integer.parseInt(parts[1]);
                int x1 = Integer.parseInt(parts[2]);
                if(!isInBounds(y1, x1)){
                    clientHandler.sendMessage("SERVER: Out-of-bounds start coords.");
                    return;
                }

                List<int[]> moves = new ArrayList<>();
                for(int i=3; i<parts.length; i+=2){
                    int ny = Integer.parseInt(parts[i]);
                    int nx = Integer.parseInt(parts[i+1]);
                    if(!isInBounds(ny, nx)){
                        clientHandler.sendMessage("SERVER: Out-of-bounds coords in the move list.");
                        return;
                    }
                    moves.add(new int[]{ny, nx});
                }

                // check piece ownership
                int[][] currentBoard = game.getBoard();
                if(currentBoard[y1][x1] != pieceValue){
                    clientHandler.sendMessage("SERVER: That piece isn't yours!");
                    return;
                }
                // check validity
                if(!game.isValidMove(y1, x1, moves)){
                    clientHandler.sendMessage("SERVER: Invalid move according to game rules.");
                    return;
                }

                // do the move
                int[] last = moves.get(moves.size()-1);
                int y2 = last[0];
                int x2 = last[1];

                game.setGamePiece(y2, x2, pieceValue);
                game.setGamePiece(y1, x1, 1);

                broadcastMessage("SERVER: Player #" + clientID
                    + " moved from (" + y1 + "," + x1 
                    + ") to (" + y2 + "," + x2 + ").");
                broadcastUpdatedBoard();

                if(game.hasWon(pieceValue)){
                    broadcastMessage("SERVER: Player #" + clientID + " has WON!!");
                }
                switchTurn();

            } catch(NumberFormatException e){
                clientHandler.sendMessage("SERVER: Could not parse coords. Use: MOVE y1 x1 y2 x2");
            }
        }
        else {
            clientHandler.sendMessage("SERVER: Unrecognized command. Use SKIP or MOVE y1 x1 y2 x2");
        }
    }

    /**
     * Moves to the next participant in allIDs and calls notifyCurrentPlayer().
     */
    public synchronized void switchTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % allIDs.size();
        notifyCurrentPlayer();
    }

    /**
     * Called after switchTurn() to see who is the next participant. 
     * If it's a bot => handleBotTurn(...), if it's human => "Your turn..."
     */
    public synchronized void notifyCurrentPlayer() {
        if(allIDs.isEmpty()) return;

        int currentID = allIDs.get(currentTurnIndex);

        // check if BOT
        if(Boolean.TRUE.equals(PlayerBots.get(currentID))) {
            System.out.println("SERVER: It's BOT's turn. (id="+ currentID + ")");
            handleBotTurn(currentID);
        } else {
            // human
            // find the clientHandler
            ClientHandler ch = findClientHandlerById(currentID);
            if(ch != null){
                int pieceVal = clientIdToPieceValue.get(currentID);
                ch.sendMessage("SERVER: It's your turn. (Color: " 
                    + game.color(pieceVal) + ")");
            }
        }
    }

    private ClientHandler findClientHandlerById(int id){
        for(ClientHandler ch : clientHandlers){
            if(ch.getClientID() == id){
                return ch;
            }
        }
        return null;
    }

    /**
     * handleBotTurn: we call bot.BotMove(...), get [y1,x1,y2,x2], do the move and broadcast.
     */
    private synchronized void handleBotTurn(int botID) {
        Bot bot = bots.get(botID);
        if (bot == null) {
            System.out.println("No Bot object for clientID=" + botID);
            switchTurn();
            return;
        }
    
        int pieceValue = clientIdToPieceValue.get(botID);
    
        // 1) Jeśli bot już wygrał, omijamy jego kolej
        if (game.hasWon(pieceValue)) {
            System.out.println("Bot #" + botID + " has already won! Skipping turn...");
            switchTurn();
            return;
        }
    
        // 2) Pobieramy aktualną planszę, wywołujemy BotMove
        int[][] currentBoard = game.getBoard();
        int[] move = bot.BotMove(currentBoard, pieceValue); // [y1,x1,y2,x2]
    
        if (move[0]==0 && move[1]==0 && move[2]==0 && move[3]==0) {
            broadcastMessage("SERVER: Bot #" + botID + " has no moves => skip");
            switchTurn();
            return;
        }
    
        int y1 = move[0];
        int x1 = move[1];
        int y2 = move[2];
        int x2 = move[3];
    
        if (!isInBounds(y1,x1) || !isInBounds(y2,x2)) {
            System.out.println("Bot gave invalid coords => skip " 
                + y1 + " " + x1 + " " + y2 + " " + x2 
                + " piece value: " + bot.showNumberonBoard());
            switchTurn();
            return;
        }
        else{
            game.setGamePiece(y2, x2, pieceValue);
            game.setGamePiece(y1, x1, 1);
        
            broadcastMessage("SERVER: Bot #" + botID
                + " moved from (" + y1 + "," + x1 + ") to (" + y2 + "," + x2 + ").");
            broadcastUpdatedBoard();
        switchTurn();}

    }
    

    private boolean isInBounds(int y, int x) {
        board = game.getBoard();
        return (y >= 0 && y < board.length 
             && x >= 0 && x < board[0].length);
    }

    /**
     * Broadcasts a message to all humans connected via clientHandlers.
     */
    public synchronized void broadcastMessage(String msg){
        for(ClientHandler ch : clientHandlers){
            ch.sendMessage(msg);
        }
    }

    /**
     * Broadcasts the updated board state as a CSV string (e.g. "update:1,1,2,3,...").
     */
    private void broadcastUpdatedBoard(){
        int[][] currentBoard = game.getBoard();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<currentBoard.length; i++){
            for(int j=0; j<currentBoard[i].length; j++){
                sb.append(currentBoard[i][j]).append(",");
            }
        }
        if(sb.length()>0) sb.setLength(sb.length()-1);
        broadcastMessage("update:" + sb.toString());
    }

    /**
     * Closes the server socket if needed.
     */
    public void closeServerSocket(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
