package checkers.Game;


import java.util.List;
/**
 * interface for movement class (Factory Pattern)
 */
public interface MovementFactory {
    boolean isValidMove(int x1,int y1,List<int[]> moves, int[][] Board);
    void refreshBoard(int [][]Board);
}