package ru.kpfu.itis.easybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.easybot.model.Game;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findById(long id);
}
