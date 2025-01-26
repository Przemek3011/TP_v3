package checkers.Game;

import java.util.List;
/**
 * Default Movement class 
 */
public class Movement implements MovementFactory {
    int[][] board;

    public Movement(int [][]Board) {
        this.board = Board;
    }
    
    public void refreshBoard(int[][] Board){
        this.board=Board;
    }
    /**
     * Check if the move isvalid
     */
    @Override
    public boolean isValidMove(int x1, int y1, List<int[]> moves,int [][] board) {
        if (moves == null || moves.isEmpty()) {
            return false;
        }
        if (moves.size() == 1 && isValidAdjacentMove(x1, y1, moves.get(0))) {
            return true;
        }
        return isValidMultiJump(x1, y1, moves);
    }
  /**
     * Checks if we can jump exactly 2 squares diagonally or in some pattern,
     * with a piece to jump over in the middle.
     */
    public boolean isValidStandardJump(int x1, int y1, int x2, int y2) {
        if (board[x2] [y2] != 1) { 
            return false;
        }
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        return ((dx == 2 && dy == 2 && board[midX] [midY] > 1) || (dx==0 && dy==4 && board[midX] [midY] > 1));
    }
   /**
     * Checks if x2,y2 is an empty adjacent cell (one step away).
     */
    public boolean isValidAdjacentMove(int x1, int y1, int[] end) {
        int x2 = end[0];
        int y2 = end[1];

        if (board[x2][y2] != 1) {
            return false;
        }

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return (dx == 1 && dy == 1) || (dx==0 && dy==2);
    }
    /**   
     * If we have multiple steps (like x2,y2, x3,y3, x4,y4...), 
     * check each step in sequence from the previous step.
     */
    public boolean isValidMultiJump(int x1, int y1, List<int[]> jumps) {
        int currentX = x1;
        int currentY = y1;

        for (int[] next : jumps) {
            int nextX = next[0];
            int nextY = next[1];

            if (!isValidStandardJump(currentX, currentY, nextX, nextY)) {
                return false;
            }

            currentX = nextX;
            currentY = nextY;
        }
        return true;
    }
}
