package checkers.Game;
/**
 * interface of Boards (factory method)
 */
public interface FactoryBoard {
    int[][] createBoard(int numberOfPlayers); 
    int[][] getBoard();  
} 
