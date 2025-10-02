package com.kraiwit.xogame.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.kraiwit.xogame.model.Game;

@Service
public class AIService {

    public int[] getNextMove(Game game) {
        Random random = new Random();
        double probability = random.nextDouble();

        // 30% chance for random move, 70% for best move
        if (probability < 0.3) {
            return getRandomMove(game);
        }
        return getBestMove(game);

    }

    private int[] getRandomMove(Game game) {
        String[][] board = game.getBoard();
        int boardSize = game.getBoardSize();
        // ค้นหาช่องว่างที่ว่าง
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].equals("")) {
                    emptyCells.add(new int[] { i, j });
                }
            }
        }
        // ไม่มีช่องว่าง
        if (emptyCells.isEmpty())
            return null;

        // สุ่มเลือกหนึ่งจาก empty cells
        int[] randomEmptyCell = emptyCells.get(new Random().nextInt(emptyCells.size()));
        return randomEmptyCell;
    }

    private int[] getBestMove(Game game) {
        String[][] board = game.getBoard();
        int boardSize = game.getBoardSize();
        String currentPlayer = game.getCurrentPlayer();

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        // Try all empty cells
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].equals("")) {
                    // Make the move
                    board[i][j] = currentPlayer; // AI

                    // Calculate score using minimax
                    int score = minimax(game, 0, false);

                    // Undo the move
                    board[i][j] = "";

                    // Update best move
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[] { i, j };
                    }
                }
            }
        }

        // If no best move found, return random move as fallback
        return bestMove != null ? bestMove : getRandomMove(game);
    }

    private int minimax(Game game, int depth, boolean isMaximizing) {
        String[][] board = game.getBoard();
        int boardSize = game.getBoardSize();
        String AIPlayer = game.getCurrentPlayer();
        String humanPlayer = AIPlayer.equals("X") ? "O" : "X";
        // Check terminal states
        String winner = checkWinner(board, boardSize);
        if (winner != null) {
            if (winner.equals(AIPlayer))
                return 10 - depth; // AI wins (prefer faster wins)
            if (winner.equals(humanPlayer))
                return depth - 10; // Human wins (prefer slower losses)
            if (winner.equals("DRAW"))
                return 0; // Draw
        }

        if (isMaximizing) {
            // AI's turn (maximizing player)
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (board[i][j].equals("")) {
                        board[i][j] = AIPlayer;
                        int score = minimax(game, depth + 1, false);
                        board[i][j] = "";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            // Human's turn (minimizing player)
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (board[i][j].equals("")) {
                        board[i][j] = humanPlayer;
                        int score = minimax(game, depth + 1, true);
                        board[i][j] = "";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private String checkWinner(String[][] board, int boardSize) {
        // Check rows and columns
        for (int i = 0; i < boardSize; i++) {
            // Check row
            if (!board[i][0].equals("") &&
                    allSame(board[i][0], board[i])) {
                return board[i][0];
            }
            // Check column
            boolean colWin = true;
            String firstCell = board[0][i];
            if (!firstCell.equals("")) {
                for (int j = 1; j < boardSize; j++) {
                    if (!board[j][i].equals(firstCell)) {
                        colWin = false;
                        break;
                    }
                }
                if (colWin)
                    return firstCell;
            }
        }

        // Check main diagonal
        if (!board[0][0].equals("")) {
            boolean diagWin = true;
            String firstCell = board[0][0];
            for (int i = 1; i < boardSize; i++) {
                if (!board[i][i].equals(firstCell)) {
                    diagWin = false;
                    break;
                }
            }
            if (diagWin)
                return firstCell;
        }

        // Check anti-diagonal
        if (!board[0][boardSize - 1].equals("")) {
            boolean antiDiagWin = true;
            String firstCell = board[0][boardSize - 1];
            for (int i = 1; i < boardSize; i++) {
                if (!board[i][boardSize - 1 - i].equals(firstCell)) {
                    antiDiagWin = false;
                    break;
                }
            }
            if (antiDiagWin)
                return firstCell;
        }

        // Check for draw
        boolean isFull = true;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].equals("")) {
                    isFull = false;
                    break;
                }
            }
            if (!isFull)
                break;
        }

        return isFull ? "DRAW" : null;
    }

    /**
     * Helper method to check if all cells in a row are the same
     */
    private boolean allSame(String value, String[] row) {
        for (String cell : row) {
            if (!cell.equals(value)) {
                return false;
            }
        }
        return true;
    }

}
