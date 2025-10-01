package com.kraiwit.xogame.model;

import com.kraiwit.xogame.model.enums.GameStatus;
import lombok.Getter;

import java.util.Arrays;
import java.util.UUID;

@Getter
public class Game {
    private String gameId = UUID.randomUUID().toString();
    private String[][] board;
    private int boardSize;
    private boolean vsAI;
    private String currentPlayer;
    private GameStatus status = GameStatus.IN_PROGRESS;

    public Game(int boardSize, boolean vsAI, String firstPlayer) {
        this.boardSize = boardSize;
        this.vsAI = vsAI;
        this.currentPlayer = firstPlayer;
        this.board = new String[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            Arrays.fill(board[r], "");
        }
    }

    public void makeMove(int row, int col, String player) {
        if (!board[row][col].equals("")) {
            throw new RuntimeException("Cell already occupied");
        }

        board[row][col] = player;

        if (checkWin(player)) {
            status = player.equals("X") ? GameStatus.X_WIN : GameStatus.O_WIN;
            return;
        }

        if (isDraw()) {
            status = GameStatus.DRAW;
            return;
        }

        currentPlayer = player.equals("X") ? "O" : "X";
    }

    private boolean checkWin(String player) {
        // Row & column checks
        for (int i = 0; i < boardSize; i++) {
            if (Arrays.stream(board[i]).allMatch(cell -> cell.equals(player)))
                return true;
            boolean colWin = true;
            for (int j = 0; j < boardSize; j++) {
                if (!board[j][i].equals(player)) {
                    colWin = false;
                    break;
                }
            }
            if (colWin)
                return true;
        }

        // Diagonal checks
        boolean diag1 = true, diag2 = true;
        for (int i = 0; i < boardSize; i++) {
            if (!board[i][i].equals(player))
                diag1 = false;
            if (!board[i][boardSize - i - 1].equals(player))
                diag2 = false;
        }
        return diag1 || diag2;
    }

    private boolean isDraw() {
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c].equals(""))
                    return false;
            }
        }
        return true;
    }
}
