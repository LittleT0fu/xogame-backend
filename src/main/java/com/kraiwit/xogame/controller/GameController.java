package com.kraiwit.xogame.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kraiwit.xogame.dto.GameRequest;
import com.kraiwit.xogame.dto.GameResponse;
import com.kraiwit.xogame.dto.MoveRequest;
import com.kraiwit.xogame.service.GameService;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService = new GameService();

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

}
