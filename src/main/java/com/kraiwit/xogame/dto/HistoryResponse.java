package com.kraiwit.xogame.dto;

import com.kraiwit.xogame.model.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private String gameId;
    private String board; // JSON string representation
    private int boardSize;
    private boolean vsAI;
    private GameStatus status;
    private String winner;
    private LocalDateTime createdAt;
}
