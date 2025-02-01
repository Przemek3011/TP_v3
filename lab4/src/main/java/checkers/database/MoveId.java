package checkers.database;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MoveId implements Serializable {

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "move_no")
    private int moveNo;

    public MoveId() {}

    public MoveId(Long gameId, int moveNo) {
        this.gameId = gameId;
        this.moveNo = moveNo;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public int getMoveNo() {
        return moveNo;
    }

    public void setMoveNo(int moveNo) {
        this.moveNo = moveNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveId moveId = (MoveId) o;
        return moveNo == moveId.moveNo && Objects.equals(gameId, moveId.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, moveNo);
    }
}