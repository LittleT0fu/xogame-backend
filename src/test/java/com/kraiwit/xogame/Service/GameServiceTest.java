package com.kraiwit.xogame.Service;

import com.kraiwit.xogame.dto.GameRequest;
import com.kraiwit.xogame.dto.GameResponse;
import com.kraiwit.xogame.dto.MoveRequest;
import com.kraiwit.xogame.model.enums.Player;
import com.kraiwit.xogame.service.GameService;
import com.kraiwit.xogame.service.HistoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Game Service Tests")
class GameServiceTest {

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should start a new game successfully")
    void shouldStartNewGame() {
        GameRequest request = new GameRequest();
        request.setBoardSize(3);
        request.setVsAI(false);
        request.setFirstPlayer("X");

        GameResponse response = gameService.startGame(request);

        assertNotNull(response);
        assertNotNull(response.getGameID());
        assertEquals(3, response.getBoardSize());
        assertEquals("X", response.getCurrentPlayer());
        assertEquals("IN_PROGRESS", response.getStatus());
    }

    @Test
    @DisplayName("Should start AI game successfully")
    void shouldStartAIGame() {
        GameRequest request = new GameRequest();
        request.setBoardSize(3);
        request.setVsAI(true);
        request.setFirstPlayer("X");

        GameResponse response = gameService.startGame(request);

        assertNotNull(response);
        assertEquals(3, response.getBoardSize());
        assertEquals("IN_PROGRESS", response.getStatus());
    }

    @Test
    @DisplayName("Should make move successfully in PvP game")
    void shouldMakeMoveInPvPGame() {
        // Start game
        GameRequest gameRequest = new GameRequest();
        gameRequest.setBoardSize(3);
        gameRequest.setVsAI(false);
        gameRequest.setFirstPlayer("X");
        GameResponse gameResponse = gameService.startGame(gameRequest);

        // Make move
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setGameID(gameResponse.getGameID());
        moveRequest.setRow(0);
        moveRequest.setCol(0);
        moveRequest.setPlayer(Player.X);

        GameResponse response = gameService.makeMove(moveRequest);

        MoveRequest moveRequest2 = new MoveRequest();
        moveRequest2.setGameID(gameResponse.getGameID());
        moveRequest2.setRow(0);
        moveRequest2.setCol(1);
        moveRequest2.setPlayer(Player.O);

        GameResponse response2 = gameService.makeMove(moveRequest2);

        assertNotNull(response);
        assertEquals("X", response.getBoard()[0][0]);
        assertEquals("O", response.getCurrentPlayer());

        assertNotNull(response2);
        assertEquals("O", response2.getBoard()[0][1]);
        assertEquals("X", response2.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should throw exception when game not found")
    void shouldThrowExceptionWhenGameNotFound() {
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setGameID("non-existent-game");
        moveRequest.setRow(0);
        moveRequest.setCol(0);
        moveRequest.setPlayer(Player.X);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameService.makeMove(moveRequest);
        });

        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Should save history when game finishes with winner")
    void shouldSaveHistoryWhenGameFinishes() {
        // Start game
        GameRequest gameRequest = new GameRequest();
        gameRequest.setBoardSize(3);
        gameRequest.setVsAI(false);
        gameRequest.setFirstPlayer("X");
        GameResponse gameResponse = gameService.startGame(gameRequest);

        // Play to win
        String gameId = gameResponse.getGameID();
        makeMoveHelper(gameId, 0, 0, Player.X); // X
        makeMoveHelper(gameId, 1, 0, Player.O); // O
        makeMoveHelper(gameId, 0, 1, Player.X); // X
        makeMoveHelper(gameId, 1, 1, Player.O); // O
        makeMoveHelper(gameId, 0, 2, Player.X); // X wins

        verify(historyService, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw exception when making move on finished game")
    void shouldThrowExceptionWhenGameFinished() {
        // Start game
        GameRequest gameRequest = new GameRequest();
        gameRequest.setBoardSize(3);
        gameRequest.setVsAI(false);
        gameRequest.setFirstPlayer("X");
        GameResponse gameResponse = gameService.startGame(gameRequest);

        String gameId = gameResponse.getGameID();

        // Play to win
        makeMoveHelper(gameId, 0, 0, Player.X); // X
        makeMoveHelper(gameId, 1, 0, Player.O); // O
        makeMoveHelper(gameId, 0, 1, Player.X); // X
        makeMoveHelper(gameId, 1, 1, Player.O); // O
        makeMoveHelper(gameId, 0, 2, Player.X); // X wins

        // Try to make another move
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setGameID(gameId);
        moveRequest.setRow(2);
        moveRequest.setCol(2);
        moveRequest.setPlayer(Player.O);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameService.makeMove(moveRequest);
        });

        assertEquals("Game not found.", exception.getMessage()); // Game removed after finish
    }

    @Test
    @DisplayName("Should handle AI move after player move")
    void shouldHandleAIMoveAfterPlayerMove() {
        // Start AI game
        GameRequest gameRequest = new GameRequest();
        gameRequest.setBoardSize(3);
        gameRequest.setVsAI(true);
        gameRequest.setFirstPlayer("X");
        GameResponse gameResponse = gameService.startGame(gameRequest);

        // Player makes move
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setGameID(gameResponse.getGameID());
        moveRequest.setRow(0);
        moveRequest.setCol(0);
        moveRequest.setPlayer(Player.X);

        GameResponse response = gameService.makeMove(moveRequest);

        // Check player move was made
        assertEquals("X", response.getBoard()[0][0]);

        // Check AI made a move (at least one more cell should be filled)
        int filledCells = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!response.getBoard()[i][j].equals("")) {
                    filledCells++;
                }
            }
        }
        assertEquals(2, filledCells); // Player + AI moves
    }

    private void makeMoveHelper(String gameId, int row, int col, Player player) {
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setGameID(gameId);
        moveRequest.setRow(row);
        moveRequest.setCol(col);
        moveRequest.setPlayer(player);
        gameService.makeMove(moveRequest);
    }
}