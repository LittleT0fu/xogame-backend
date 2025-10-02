package com.kraiwit.xogame.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraiwit.xogame.dto.GameRequest;
import com.kraiwit.xogame.dto.GameResponse;
import com.kraiwit.xogame.dto.MoveRequest;
import com.kraiwit.xogame.model.Game;
import com.kraiwit.xogame.model.History;
import com.kraiwit.xogame.model.enums.GameStatus;
import com.kraiwit.xogame.repository.HistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final AIService aiService = new AIService();
    private final HistoryRepository historyRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public GameResponse startGame(GameRequest gameRequest) {
        Game game = new Game(gameRequest.getBoardSize(), gameRequest.isAI(), gameRequest.getFirstPlayer());
        games.put(game.getGameId(), game);
        return new GameResponse(game);
    }

    public GameResponse makeMove(MoveRequest moveRequest) {
        Game game = games.get(moveRequest.getGameID());

        if (game == null) {
            throw new RuntimeException("Game not found.");
        }
        if (!game.getStatus().equals(GameStatus.IN_PROGRESS)) {
            throw new RuntimeException("Game already finished.");
        }

        // player make move
        game.makeMove(moveRequest.getRow(), moveRequest.getCol(), moveRequest.getPlayer().toString());

        // If player won or draw → save history and return
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            saveGameHistory(game);
            return new GameResponse(game);
        }

        if (game.isVsAI()) {
            int[] nextMove = aiService.getNextMove(game);
            game.makeMove(nextMove[0], nextMove[1], game.getCurrentPlayer());
        }

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            saveGameHistory(game);
            return new GameResponse(game);
        }

        return new GameResponse(game);
    }

    private void saveGameHistory(Game game) {
        try {
            History history = new History();
            history.setGameId(game.getGameId());
            history.setBoard(objectMapper.writeValueAsString(game.getBoard()));
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

            historyRepository.save(history);

            games.remove(game.getGameId());
        } catch (Exception e) {
            System.err.println("Failed to save game history: " + e.getMessage());
        }
    }

}
