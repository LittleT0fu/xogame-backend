package com.kraiwit.xogame.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import com.kraiwit.xogame.model.enums.Player;

@Data
public class MoveRequest {
    @NotNull(message = "Game ID is required")
    private String gameID;
    @NotNull(message = "Row is required")
    @Positive(message = "Row must be positive")
    private int row;
    @NotNull(message = "Column is required")
    @Positive(message = "Column must be positive")
    private int col;
    @NotNull(message = "Player is required")
    private Player player;
}
