package checkers.Game;
/**
 * Class that creates and has methods of default Board
 */
public class Board implements FactoryBoard {
    private final int[][] board;
    public Board(){
        // ATTENTION: here the X axis is vertical, and Y is horizontal.
        board=new int[][]{
            {0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,2,0,2,0,2,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,2,0,2,0,2,0,2,0,0,0,0,0,0,0,0,0},
            {7,0,7,0,7,0,7,0,1,0,1,0,1,0,1,0,1,0,3,0,3,0,3,0,3},
            {0,7,0,7,0,7,0,1,0,1,0,1,0,1,0,1,0,1,0,3,0,3,0,3,0},
            {0,0,7,0,7,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,3,0,3,0,0},
            {0,0,0,7,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,3,0,0,0},
            {0,0,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,0,0},
            {0,0,0,6,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,4,0,0,0},
            {0,0,6,0,6,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,4,0,4,0,0},
            {0,6,0,6,0,6,0,1,0,1,0,1,0,1,0,1,0,1,0,4,0,4,0,4,0},
            {6,0,6,0,6,0,6,0,1,0,1,0,1,0,1,0,1,0,4,0,4,0,4,0,4},
            {0,0,0,0,0,0,0,0,0,5,0,5,0,5,0,5,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,5,0,5,0,5,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,5,0,5,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0}
        };
    }
    @Override
    public int[][] getBoard(){
        return board;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    /**
     * create Board based on number of players
     */
    @Override
    public int[][] createBoard(int numberOfPlayers) {
        switch (numberOfPlayers){
            case 2:
        for (int i=0;i<17;i++) {
            for (int j=0;j<25;j++) {
                if(board[i][j]==3 || board[i][j]==4 || board[i][j]==6 || board[i][j]==7 ){
                    board[i][j]=1;
                }
    }}  break;
    
        case 3:
    for (int i=0;i<17;i++) {
        for (int j=0;j<25;j++) {
            if(board[i][j]==3 || board[i][j]==5 || board[i][j]==7  ){
                board[i][j]=1;
            }   
    }} break;
    case 4:
    for (int i=0;i<17;i++) {
        for (int j=0;j<25;j++) {
            if(board[i][j]==4 || board[i][j]==7  ){
                board[i][j]=1;
            }
    }} break;
    }
    return board;
    }

}