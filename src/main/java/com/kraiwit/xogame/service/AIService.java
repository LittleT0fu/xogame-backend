package com.kraiwit.xogame.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.kraiwit.xogame.model.Game;

@Service
public class AIService {

    public int[] getNextMove(Game game) {
        String[][] board = game.getBoard();

        return getRandomMove(game);
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

        return new int[] { 0, 0 };
    }

    private int[] getMinimaxMove(Game game) {
        String[][] board = game.getBoard();
        int boardSize = game.getBoardSize();
        return new int[] { 0, 0 };
    }

}
