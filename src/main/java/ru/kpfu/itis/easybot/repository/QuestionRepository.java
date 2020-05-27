package ru.kpfu.itis.easybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.easybot.model.Question;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Override
    Optional<Question> findById(Long aLong);

    Optional<Question> findByTitle(String title);
}
