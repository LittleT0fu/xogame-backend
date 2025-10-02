package com.kraiwit.xogame.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kraiwit.xogame.dto.GameRequest;
import com.kraiwit.xogame.dto.GameResponse;
import com.kraiwit.xogame.dto.MoveRequest;
import com.kraiwit.xogame.model.History;
import com.kraiwit.xogame.service.GameService;
import com.kraiwit.xogame.service.HistoryService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final HistoryService historyService;

    @GetMapping
    public String helloWorld() {
        return new String("Hello World");
    }

    @PostMapping("/start")
    public ResponseEntity<GameResponse> startGame(@Valid @RequestBody GameRequest gameRequest) {
        GameResponse gameResponse = gameService.startGame(gameRequest);
        return ResponseEntity.ok(gameResponse);
    }

    @PostMapping("/makemove")
    public ResponseEntity<GameResponse> makeMove(@Valid @RequestBody MoveRequest moveRequest) {
        GameResponse gameResponse = gameService.makeMove(moveRequest);
        return ResponseEntity.ok(gameResponse);
    }

    @GetMapping("/history")
    public ResponseEntity<List<History>> getHistory() {
        List<History> history = historyService.getHistory();
        return ResponseEntity.ok(history);
    }

}
