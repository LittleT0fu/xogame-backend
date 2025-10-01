package com.kraiwit.xogame.dto;

import lombok.Data;

@Data
public class GameResponse {
    private String gameID;
    private int boardSize;
    private String[][] board;
    private String currentPlayer;
    private String status;
    private String winner;

    public GameResponse(String gameID, int boardSize, String[][] board, String currentPlayer, String status,
            String winner) {
        this.gameID = gameID;
        this.boardSize = boardSize;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.status = status;
        this.winner = winner;
    }
}
