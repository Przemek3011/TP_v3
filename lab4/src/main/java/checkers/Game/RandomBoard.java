package checkers.Game;
/**
 * class that creates random Board
 */
public class RandomBoard implements FactoryBoard {
    private int board[][];
    private int CellToFill[][]=new int[61][2];
    int k=0;
    /**
     * constructor of class
     * shuffles the int 
     */
    public RandomBoard(){
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
        for(int i=0;i<17;i++){
            for(int j=0;j<25;j++){
                if(board[i][j]==1) {
                    CellToFill[k][0]=i;
                    CellToFill[k][1]=j;
                    k++;
            }  
        }}
        Clean(board);
        shuffle(CellToFill);
    }
    /**
     * take all pieces and set them as free cell
     * @param Board
     * @return
     */
    public int[][] Clean(int [][]Board){
        for(int i=0;i<17;i++){
            for(int j=0;j<25;j++){
                if(Board[i][j]!=0 && Board[i][j]!=1) {
                    Board[i][j]=1;
            }  
        }}
        return Board;
    }
    /**
     * shuffle the cells that are inside hexagon
     * @param CellToFill
     * @return shuffled array
     */
    public int[][] shuffle(int[][] CellToFill){
        for(int i=0;i<61;i++){
            int temp1,temp2;
            int random=(int)(Math.random()*61);
            temp1=CellToFill[i][0];
            temp2=CellToFill[i][1];
            CellToFill[i][0]=CellToFill[random][0];
            CellToFill[i][1]=CellToFill[random][1];
            CellToFill[random][0]=temp1;
            CellToFill[random][1]=temp2;
        }

        return CellToFill;
    }
    
    /**
     * create random Board
     */
    @Override
    public int[][] createBoard(int numberOfPlayers) {
        for(int i=0;i<numberOfPlayers*10;i++){
            board[CellToFill[i][0]][CellToFill[i][1]]=i/10 + 2;
        }
        return board;
    }
    /**
     * get Board
     */
    @Override
    public int[][] getBoard() {
        return board;
    }
    
}
