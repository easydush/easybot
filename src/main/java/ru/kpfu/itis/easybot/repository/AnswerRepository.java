package ru.kpfu.itis.easybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.easybot.model.Answer;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.Question;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(long id);

    List<Answer> findAllByPerson(Person person);

    Optional<Answer> findByPersonAndQuestion(Person person, Question question);
}
