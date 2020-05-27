package ru.kpfu.itis.easybot.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.Answer;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.repository.PersonRepository;

import java.lang.reflect.AnnotatedWildcardType;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonRepository repository;

    @Override
    public Person addPerson(PersonDto personDto) {
        Person look = Person
                .builder()
                .id(personDto.getId())
                .name(personDto.getName())
                .image(personDto.getImage())
                .build();
        repository.save(look);
        return look;
    }
}
