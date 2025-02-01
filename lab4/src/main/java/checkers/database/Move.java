package checkers.database;

import jakarta.persistence.*;

@Entity
@Table(name = "moves")
public class Move {

    @EmbeddedId
    private MoveId id;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "start_x", nullable = false)
    private int startX;

    @Column(name = "start_y", nullable = false)
    private int startY;

    @Column(name = "end_x", nullable = false)
    private int endX;

    @Column(name = "end_y", nullable = false)
    private int endY;

    public Move() {}

    public Move(Game game, int moveNo, int startX, int startY, int endX, int endY) {
        this.id = new MoveId(game.getId(), moveNo);
        this.game = game;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public MoveId getId() {
        return id;
    }

    public void setId(MoveId id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }
}