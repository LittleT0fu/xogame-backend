package com.kraiwit.xogame.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

import com.kraiwit.xogame.dto.GameRequest;
import com.kraiwit.xogame.dto.GameResponse;
import com.kraiwit.xogame.dto.MoveRequest;
import com.kraiwit.xogame.model.Game;
import com.kraiwit.xogame.model.enums.GameStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final AIService aiService = new AIService();

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
            // TODO save game history
            return new GameResponse(game);
        }

        if (game.isVsAI()) {
            int[] nextMove = aiService.getNextMove(game);
            game.makeMove(nextMove[0], nextMove[1], game.getCurrentPlayer());
        }

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            // TODO save game history
            return new GameResponse(game);
        }

        return new GameResponse(game);
    }

}
