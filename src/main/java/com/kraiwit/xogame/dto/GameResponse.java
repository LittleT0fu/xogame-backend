package com.kraiwit.xogame.dto;

import com.kraiwit.xogame.model.Game;
import com.kraiwit.xogame.model.enums.GameStatus;

import lombok.Data;

@Data
public class GameResponse {
    private String gameID;
    private int boardSize;
    private String[][] board;
    private String currentPlayer;
    private String status;
    private String winner;

    public GameResponse(Game game) {
        this.gameID = game.getGameId();
        this.boardSize = game.getBoardSize();
        this.board = game.getBoard();
        this.currentPlayer = game.getCurrentPlayer();
        this.status = game.getStatus().toString();
        this.winner = (game.getStatus() == GameStatus.X_WIN) ? "X"
                : (game.getStatus() == GameStatus.O_WIN) ? "O" : null;
    }
}
