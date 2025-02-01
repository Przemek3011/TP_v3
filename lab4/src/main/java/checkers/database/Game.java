package checkers.database;

import jakarta.persistence.*;
import org.hibernate.mapping.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @Column(name = "player1")
    private Integer player1;

    @Column(name = "player2")
    private Integer player2;

    @Column(name = "player3")
    private Integer player3;

    @Column(name = "player4")
    private Integer player4;

    @Column(name = "player5")
    private Integer player5;

    @Column(name = "player6")
    private Integer player6;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List moves;

    public Game() {}

    public Game(Integer p1, Integer p2, Integer p3, Integer p4, Integer p5, Integer p6) {
        this.player1 = p1;
        this.player2 = p2;
        this.player3 = p3;
        this.player4 = p4;
        this.player5 = p5;
        this.player6 = p6;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlayer1() {
        return player1;
    }

    public void setPlayer1(Integer player1) {
        this.player1 = player1;
    }

    public Integer getPlayer2() {
        return player2;
    }

    public void setPlayer2(Integer player2) {
        this.player2 = player2;
    }

    public Integer getPlayer3() {
        return player3;
    }

    public void setPlayer3(Integer player3) {
        this.player3 = player3;
    }

    public Integer getPlayer4() {
        return player4;
    }

    public void setPlayer4(Integer player4) {
        this.player4 = player4;
    }

    public Integer getPlayer5() {
        return player5;
    }

    public void setPlayer5(Integer player5) {
        this.player5 = player5;
    }

    public Integer getPlayer6() {
        return player6;
    }

    public void setPlayer6(Integer player6) {
        this.player6 = player6;
    }

    public List getMoves() {
        return moves;
    }

    public void setMoves(List moves) {
        this.moves = moves;
    }
}