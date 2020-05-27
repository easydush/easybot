package ru.kpfu.itis.easybot.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.easybot.dto.QuestionDto;
import ru.kpfu.itis.easybot.model.Game;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.Question;
import ru.kpfu.itis.easybot.repository.GameRepository;
import ru.kpfu.itis.easybot.repository.PersonRepository;
import ru.kpfu.itis.easybot.repository.QuestionRepository;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {
    @Autowired
    GameRepository repository;
    @Autowired
    PersonRepository personRepository;
    @Override
    public Game addGame() {
        Random random = new Random();
        List<Person> persons = personRepository.findAll();
        int max_value = persons.size();
        Person person = persons.get(random.nextInt(max_value));
        Game game = Game
                .builder()
                .id(null)
                .person(person)
                .isFinished(false)
                .build();
        repository.save(game);
        return game;
    }
}
