package ru.kpfu.itis.easybot.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.easybot.dto.AnswerDto;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.dto.QuestionDto;
import ru.kpfu.itis.easybot.model.Answer;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.Question;
import ru.kpfu.itis.easybot.repository.AnswerRepository;
import ru.kpfu.itis.easybot.repository.PersonRepository;
import ru.kpfu.itis.easybot.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    AnswerRepository repository;
    @Autowired
    QuestionRepository qrepository;
    @Autowired
    PersonRepository prepository;
    @Autowired
    QuestionServiceImpl qservice;

    @Override
    public Answer addAnswer(AnswerDto aDto) {
        Question question;
        System.out.println("qoption: "+qrepository.findByTitle(aDto.getQuestion()));
        if (qrepository.findByTitle(aDto.getQuestion()).isPresent()) {
            question = qrepository.findByTitle(aDto.getQuestion()).get();
            System.out.println("found "+question);
        } else {
            question = qservice.addQuestion(new QuestionDto(0, aDto.getQuestion()));
            System.out.println("created q: "+question);
        }
        Answer a = Answer
                .builder()
                .id(aDto.getId())
                .question(question)
                .person(prepository.getPersonByName(aDto.getPerson()).get())
                .isTrue(aDto.getIsTrue())
                .build();
        repository.save(a);
        return a;
    }
}
