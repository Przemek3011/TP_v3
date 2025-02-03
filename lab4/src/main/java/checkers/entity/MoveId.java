package checkers.entity;

import java.io.Serializable;
import java.util.Objects;

public class MoveId implements Serializable {

    private Long game;
    private int moveNumber;

    public MoveId() {}

    public MoveId(Long game, int moveNumber) {
        this.game = game;
        this.moveNumber = moveNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveId moveId = (MoveId) o;
        return moveNumber == moveId.moveNumber && Objects.equals(game, moveId.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, moveNumber);
    }
}