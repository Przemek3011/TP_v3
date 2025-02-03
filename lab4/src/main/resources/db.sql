CREATE DATABASE IF NOT EXISTS checkers_db;
USE checkers_db;

CREATE TABLE game (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number_of_players INT NOT NULL
);

CREATE TABLE moves (
    game_id BIGINT NOT NULL,
    move_number INT NOT NULL,
    start_x INT NOT NULL,
    start_y INT NOT NULL,
    end_x INT NOT NULL,
    end_y INT NOT NULL,
    PRIMARY KEY (game_id, move_number),
    FOREIGN KEY (game_id) REFERENCES game(id) ON DELETE CASCADE
);