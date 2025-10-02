package com.kraiwit.xogame.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Data
public class GameRequest {
    @NotNull(message = "Board size is required")
    @Positive(message = "Board size must be positive")
    @Min(value = 3, message = "Board size must be at least 3")
    private int boardSize;
    @NotNull(message = "Is AI is required")
    private boolean isAI;
    @NotNull(message = "First player is required")
    @Pattern(regexp = "^[XO]$", message = "First player must be X or O")
    private String firstPlayer;
}
