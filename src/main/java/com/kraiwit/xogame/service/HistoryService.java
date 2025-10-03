package com.kraiwit.xogame.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kraiwit.xogame.model.Game;
import com.kraiwit.xogame.model.History;
import com.kraiwit.xogame.repository.HistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public void save(Game game) {
        try {
            History history = new History();
            history.setGameId(game.getGameId());
            history.setBoard(game.getBoard());
            history.setBoardSize(game.getBoardSize());
            history.setVsAI(game.isVsAI());
            history.setStatus(game.getStatus());

            // Set winner based on game status
            String winner = switch (game.getStatus()) {
                case X_WIN -> "X";
                case O_WIN -> "O";
                case DRAW -> "DRAW";
                default -> null;
            };
            history.setWinner(winner);
            history.setCreatedAt(LocalDateTime.now());
            List<int[]> winningLine = game.getWinningLine();
            if (!winningLine.isEmpty()) {
                history.setWinningLine(winningLine);
            }
            history.setFirstPlayer(game.getFirstPlayer());
            historyRepository.save(history);

        } catch (Exception e) {
            System.err.println("Failed to save game history: " + e.getMessage());
        }
    }

    public List<History> getHistory() {
        return historyRepository.findAllByOrderByCreatedAtDesc();
    }

}
