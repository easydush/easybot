package ru.kpfu.itis.easybot.service;

import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.Person;

public interface PersonService {
    Person addPerson(PersonDto personDto);
}
