package checkers.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MoveRepository moveRepository;

    private Game currentGame;

    public void startNewGame(Integer p1, Integer p2, Integer p3, Integer p4, Integer p5, Integer p6) {
        currentGame = new Game(p1, p2, p3, p4, p5, p6);
        gameRepository.save(currentGame);
    }

    public void saveMove(int moveNo, int startX, int startY, int endX, int endY) {
        if (currentGame == null) return;
        Move move = new Move(currentGame, moveNo, startX, startY, endX, endY);
        moveRepository.save(move);
    }
}