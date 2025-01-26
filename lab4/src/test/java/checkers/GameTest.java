package checkers;
import checkers.Game.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GameTest {

    @Test
    public void hasWon(){
        Game game=new Game(6,0,"d");
        game.setGamePiece(16, 12, 2);
        game.setGamePiece(15, 11, 2);
        game.setGamePiece(15, 13, 2);
        game.setGamePiece(14, 10, 2);
        game.setGamePiece(14, 12, 2);
        game.setGamePiece(14, 14, 2);
        game.setGamePiece(13, 15, 2);
        game.setGamePiece(13, 13, 2);
        game.setGamePiece(13, 11, 2);
        assertFalse( "Game not won yet.",game.hasWon(2));
       
        game.setGamePiece(13, 9, 2);
    
        
        assertTrue( "Game won.",game.hasWon(2));
    }   
}
