package checkers;

import checkers.Game.*;


import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MovementTest {

    @Test
    public void testNormalDiagonalMove() {
        Game game = new Game(6,0,"d");
        game.getBoard();
        int x1 = 3;
        int y1 = 9;
        List<int[]> moveTo= new ArrayList<int[]>();
        moveTo.add(new int[]{4,8});

        boolean isValid = game.isValidMove(x1, y1, moveTo);
        assertTrue( "Adjacent move should be valid.",isValid);
    }

@Test
    public void testValidHorizontalMove() {
      
        Game game = new Game(6, 0, "d");
       
        game.setGamePiece(4, 8, 2); 
     
        game.setGamePiece(4, 10, 1);

        int x1 = 4;
        int y1 = 8;
        List<int[]> moveTo = new ArrayList<>();
        moveTo.add(new int[]{4,10}); 

        // Act
        boolean isValid = game.isValidMove(x1, y1, moveTo);

        // Assert
        assertTrue( "Adjacent move to an free cell.",isValid);
    }
@Test
public void testValidMultijump() {
      
    Game game = new Game(6, 0, "d");
    game.getBoard();
   
    game.setGamePiece(4, 14, 2);
    game.setGamePiece(3, 13, 1);

    int x1 = 3;
    int y1 = 9;
    List<int[]> moveTo = new ArrayList<>();
    moveTo.add(new int[]{3,13});
    moveTo.add(new int[]{5,15}); 
   
    // Act
    boolean isValid = game.isValidMove(x1, y1, moveTo);

    // Assert
    assertTrue( "MultiJump move to  free cell. "+ game.getBoard()[3][9]+" "+ game.getBoard()[3][13] +" "+game.getBoard()[4][14]+" "+ game.getBoard()[5][15],isValid);
}


}
