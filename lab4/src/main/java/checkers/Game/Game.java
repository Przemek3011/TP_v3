package checkers.Game;

import java.util.List;
/**
 * Class that operates our game 
 * Observator pattern? (not really)
 */
public class Game {
    private FactoryBoard board;
    private MovementFactory movement;
    private int numberOfPlayers;
    private int numberOnBoard;
    private int ID;
    int [][] Board;
    public Game(int numberOfPlayers,int ID, String variant) {
        this.numberOfPlayers = numberOfPlayers;
        this.ID=ID;
        switch(variant){
            case "s": board = new Board();
            board.createBoard(numberOfPlayers);
            movement = new FastMovement(board.getBoard());
            break;
            case "r": 
            board = new RandomBoard();
            board.createBoard(numberOfPlayers);
            movement = new Movement(board.getBoard());
            break;
            case "d":
            board = new Board();
            board.createBoard(numberOfPlayers);
            movement = new Movement(board.getBoard());
            break;
        }
        Board=board.getBoard();
    }
    /**
     * based on condition give a piece value to the Client 
     * @param numberOfPlayers
     * @param ID
     * @return
     */
    public int getNumberOnBoard( int numberOfPlayers,int ID){

        switch(numberOfPlayers){
            case 2:
            switch(ID){
                case 1: numberOnBoard=2; break;
                case 2: numberOnBoard=5; break;
                }break;
            case 3:
            switch(ID){
                case 1:numberOnBoard=2; break;
                case 2:numberOnBoard=4; break;
                case 3:numberOnBoard=6; break;
            }break;
            case 4:
            switch(ID){
                case 1:numberOnBoard=2;break;
                case 2: numberOnBoard=3;break;
                case 3: numberOnBoard=5;break;
                case 4:numberOnBoard=6;break;
                }break;
                case 6: numberOnBoard=ID+1; break;
        }
        return numberOnBoard;
    }
    /**
     * check if move is valid 
     */
    public boolean isValidMove(int x1,int y1, List<int[]> moves){
        movement.refreshBoard(getBoard());
        return movement.isValidMove(x1, y1, moves,getBoard());
    }

    /**
     * Get the current state of the board.
     *
     * @return A 2D array representing the board.
     */
    public int[][] getBoard() {
        return board.getBoard();
    }
    /**
     * check if the player won 
     * @param ID - piece ID
     * @return - if player with piece ID won
     */
    public boolean hasWon(int ID){
        switch(ID){
            case 2:
            if(Board[16][12]==2 && Board[15][11]==2 && Board[15][13]==2 &&Board[14][10]==2 && Board[14][12]==2 && Board[14][14]==2 && Board[13][9]==2 && Board[13][11]==2 && Board[13][13]==2 && Board[13][15]==2 )
            return true;
            break;
            case 3: 
            if(Board[12][0]==6 && Board[12][2]==6 && Board[12][4]==6 && Board[12][6]==6 && Board[11][1]==6 && Board[11][3]==6 && Board[11][5]==6 && Board[10][2]==6 && Board[10][4]==6 && Board[9][5]==6 )
            return true;
            break;
            case 4: 
            if(Board[4][0]==4 && Board[4][2]==4 && Board[4][4]==4 && Board[4][6]==4 && Board[5][1]==4 && Board[5][3]==4 && Board[5][5]==4 && Board[6][2]==4 && Board[6][4]==4 && Board[7][3]==4)
            return true;
            break;
            case 5:
            if(Board[0][12]==5 && Board[1][11]==5 && Board[1][10]==5 &&Board[2][9]==5 && Board[2][11]==5 && Board[2][13]==5 && Board[3][9]==5 && Board[3][10]==5 && Board[3][12]==5 && Board[3][14]==5 )
            return true;
            case 6:
            if(Board[4][24]==6 && Board[4][22]==6 && Board[4][20]==6 && Board[4][18]==6 && Board[5][23]==6 && Board[5][21]==6 && Board[5][19]==6 && Board[6][22]==6 && Board[6][20]==6 && Board[7][21]==6)
            return true;
            break;
            case 7:
            if(Board[12][24]==7 && Board[12][22]==7 && Board[12][20]==7 && Board[12][18]==7 && Board[11][23]==7 && Board[11][21]==7 && Board[11][19]==7 && Board[10][22]==7 && Board[10][20]==7 && Board[9][21]==7)
            return true;
            break;
            default: 
            return false;
        }
        return false;
    }
    /**
     * return string that shows the color of class 
     * @param ID
     * @return color of piece 
     */
    public String color(int ID){
        switch(ID){
            case 2: return "RED";
            case 3: return "BLUE";
            case 4: return "GREEN"; 
            case 5: return "YELLOW"; 
            case 6: return "ORANGE"; 
            case 7: return "PINK"; 
        }
        return "";
    }
    /**
     * change board in game
     * @param x y 
     * @param y x 
     * @param Piece piece value 
     */
    public void setGamePiece(int x, int y, int Piece){
        Board[x][y]=Piece;
    }

}
