package checkers.Game;

public class Bot {
    int numberOnBoard;
    int ID, numberOfPlayers;
    int[][] goal;
    int [][]board;
    // Usuwamy board z pola, bo i tak przekazujemy je w metodzie BotMove
    // int [][]board;

    /**
     * Ruchy o 1 pole (po skosie/pion/poziom) – bot będzie wykonywał wyłącznie te.
     */
    int[][] moves1 = {
        {0,2}, {0,-2}, {1,1}, {1,-1}, {-1,1}, {-1,-1}
    };
    int[][] moves2 = { 
        {0,4},{0,-4},{2,2},{2,-2},{-2,2},{-2,-2}
    };

    // Usuwamy tablicę moves2, by bot nie mógł skakać dalej
    // int[][] moves2 = {...};

    MovementFactory movement;

    public Bot(int numberOfPlayers, int ID,int [][]board) {
        this.ID = ID;
        this.numberOfPlayers = numberOfPlayers;
        // Na razie movement nie jest faktycznie używany,
        // bo i tak sprawdzamy logikę w samej metodzie BotMove
        this.movement = new Movement(null);
        numberOnBoard=getNumberOnBoard(numberOfPlayers, ID);
        this.board=board;

    }

    /**
     * Wylicza wartość pionka (2..7) zależnie od liczby graczy i ID.
     */
    public int getNumberOnBoard(int numberOfPlayers, int ID) {
        switch(numberOfPlayers) {
            case 2:
                switch(ID) {
                    case 1: numberOnBoard = 2; break;
                    case 2: numberOnBoard = 5; break;
                }
                break;
            case 3:
                switch(ID) {
                    case 1: numberOnBoard = 2; break;
                    case 2: numberOnBoard = 4; break;
                    case 3: numberOnBoard = 6; break;
                }
                break;
            case 4:
                switch(ID) {
                    case 1: numberOnBoard = 2; break;
                    case 2: numberOnBoard = 3; break;
                    case 3: numberOnBoard = 5; break;
                    case 4: numberOnBoard = 6; break;
                }
                break;
            case 6:
                numberOnBoard = ID + 1;
                break;
        }
        return numberOnBoard;
    }

    /**
     * Ustawia pola docelowe (goal) zależnie od numberOnBoard.
     */
    private int[][] setGoal(int numberOnBoard) {
        switch(numberOnBoard) {
            case 2:
                goal = new int[][] {
                    {16,12},{15,11},{15,13},{14,10},{14,12},
                    {14,14},{13,9},{13,11},{13,13},{13,15}
                };
                break;
            case 3:
                goal = new int[][] {
                    {12,0},{12,2},{12,4},{12,6},{11,1},
                    {11,3},{11,5},{10,2},{10,4},{9,5}
                };
                break;
            case 4:
                goal = new int[][] {
                     {4,2},{4,4},{4,6},{5,5},{5,3},{5,1},{6,2},{7,3},{4,0},{6,4}
                };
                break;
            case 5:
                goal = new int[][] {
                    {0,12},{1,11},{1,13},{2,10},{2,12},
                    {2,14},{3,9},{3,11},{3,13},{3,15}
                };
                break;
            case 6:
                goal = new int[][] {
                    {4,24},{4,22},{4,20},{4,18},{5,23},
                    {5,21},{5,19},{6,22},{6,20},{7,21}
                };
                break;
            case 7:
                goal = new int[][] {
                    {12,24},{12,22},{12,20},{12,18},{11,23},
                    {11,21},{11,19},{10,22},{10,20},{9,21}
                };
                break;
        }
        return goal;
    }

    /**
     * Metoda wyszukuje pojedynczy (najlepszy) ruch z moves1,
     * szacując odległość do pierwszego napotkanego pola docelowego 'goal'.
     * Zwraca tablicę [y1, x1, y2, x2].
     */
    public int[] BotMove(int[][] board, int numberOnBoard) {
        int[] maxmove = new int[4];
        double minS = 9999.0;

        // 1) Ustal tablicę goal
        int[][] possibleGoals = setGoal(numberOnBoard);
        int index_goal=0;
        int[] myGoal=null;
        for(int i=0;i<10;i++){
            if(board[possibleGoals[i][0]][possibleGoals[i][1]]!=numberOnBoard){
                myGoal = new int[]{ possibleGoals[i][0], possibleGoals[i][1]};
                index_goal=i;
            }
        }

        for(int i = 0; i < 17; i++) {
            for(int j = 0; j < 25; j++) {
            
                if(board[i][j] == numberOnBoard && isPieceInHouse(i, j, index_goal)) {
                   
                    for(int v = 0; v < moves1.length; v++) {
                        int ny = i + moves1[v][0];  // docelowe y
                        int nx = j + moves1[v][1];  // docelowe x

                        
                        if(ny >= 0 && ny < 17 && nx >= 0 && nx < 25) {
                            
                            if(board[ny][nx] == 1) {
                              
                                int dy = Math.abs(ny - myGoal[0]);
                                int dx = Math.abs(nx - myGoal[1]);
                                double length = Math.sqrt(dy * dy + dx * dx);
                                
                                if(length < minS) {
                                    minS = length;
                                    maxmove[0] = i;   // y1
                                    maxmove[1] = j;   // x1
                                    maxmove[2] = ny;  // y2
                                    maxmove[3] = nx;  // x2
                                }
              
                            }
                        }
                    } 
                    

                }
            }
        }

        return maxmove;
    }
    //sprawdzamy czy jakis pionek juz stoi na poprzednim goalu
    private boolean isPieceInHouse( int x1,int y1,int index_goal){
        int [][]Goal=setGoal(numberOnBoard);
        for(int i=0;i<index_goal;i++){
            if(x1==Goal[i][0] && y1==Goal[i][1])
            return false;
        }
        
        return true;
    }


    public int showNumberonBoard(){
        return numberOnBoard;
    }
}
