package com.kraiwit.xogame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kraiwit.xogame.model.History;
import com.kraiwit.xogame.model.enums.GameStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, String> {

    // Find all games sorted by creation date (most recent first)
    List<History> findAllByOrderByCreatedAtDesc();

    // Find games by status
    List<History> findByStatus(GameStatus status);

    // Find games by winner
    List<History> findByWinner(String winner);

    // Find AI games only
    List<History> findByVsAIOrderByCreatedAtDesc(boolean vsAI);

    // Find games by date range
    List<History> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    // Count total games
    long count();

    // Count wins by player
    long countByWinner(String winner);

    // Get latest N games
    @Query("SELECT h FROM History h ORDER BY h.createdAt DESC LIMIT :limit")
    List<History> findLatestGames(int limit);
}
