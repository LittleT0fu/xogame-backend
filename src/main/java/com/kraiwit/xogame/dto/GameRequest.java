package com.kraiwit.xogame.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
public class GameRequest {
    @NotNull(message = "Board size is required")
    @Positive(message = "Board size must be positive")
    @Size(min = 3, message = "Board size must be a least 3")
    private int boardSize;
    @NotNull(message = "Is AI is required")
    private boolean isAI;
    @NotNull(message = "First player is required")
    @Size(min = 1, max = 2)
    private String firstPlayer;
}
