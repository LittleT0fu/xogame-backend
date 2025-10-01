package com.kraiwit.xogame.model;

import com.kraiwit.xogame.model.enums.GameStatus;
import lombok.Getter;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.IntStream;

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

        // TODO create a new logic to check win row, col, diaonal, and anti-diagonal
        // row & col check
        for (int i = 0; i < boardSize; i++) {
            // lambda expression can only access final or effectively final variable
            final int row = i;
            // check row
            if (Arrays.stream(board[i]).allMatch(cell -> cell.equals(player))) {
                return true;
            }
            // check column
            if (Arrays.stream(board).allMatch(col -> col[row].equals(player))) {
                return true;
            }
        }
        // Check main diagonal
        if (IntStream.range(0, boardSize).allMatch(i -> board[i][i].equals(player))) {
            return true;
        }

        // Check anti-diagonal
        if (IntStream.range(0, boardSize).allMatch(i -> board[i][boardSize - 1 - i].equals(player))) {
            return true;
        }

        return false;
    }

    private boolean isDraw() {
        // check board is full?
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c].equals(""))
                    return false;
            }
        }
        return true;
    }
}
