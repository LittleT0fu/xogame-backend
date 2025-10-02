package com.kraiwit.xogame.model;

import com.kraiwit.xogame.config.StringArrayConverter;
import com.kraiwit.xogame.model.enums.GameStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "game_history")
@Getter
@Setter
public class History {
    @Id
    private String gameId;

    @Column(columnDefinition = "JSON")
    @Convert(converter = StringArrayConverter.class)
    private String[][] board;

    private int boardSize;
    private boolean vsAI;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    private String winner;
    private LocalDateTime createdAt;
}
