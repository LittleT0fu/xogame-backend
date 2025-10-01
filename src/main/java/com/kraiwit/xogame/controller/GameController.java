package com.kraiwit.xogame.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/game")
public class GameController {
    @GetMapping("/start")
    public ResponseEntity<String> startGame() {
        return ResponseEntity.ok(new String("Game started"));
    }

    @GetMapping("/makemove")
    public ResponseEntity<String> makeMove(@RequestParam String param) {
        return ResponseEntity.ok(new String("make a move"));
    }

}
