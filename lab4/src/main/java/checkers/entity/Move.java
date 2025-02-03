package checkers.entity;

import javax.persistence.*;

@Entity
@Table(name = "moves")
@IdClass(MoveId.class)
public class Move {

    @Id
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Id
    @Column(name = "move_number", nullable = false)
    private int moveNumber;

    @Column(name = "start_x", nullable = false)
    private int startX;

    @Column(name = "start_y", nullable = false)
    private int startY;

    @Column(name = "end_x", nullable = false)
    private int endX;

    @Column(name = "end_y", nullable = false)
    private int endY;

    public Move() {}

    public Move(Game game, int moveNumber, int startX, int startY, int endX, int endY) {
        this.game = game;
        this.moveNumber = moveNumber;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public Game getGame() {
        return game;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}