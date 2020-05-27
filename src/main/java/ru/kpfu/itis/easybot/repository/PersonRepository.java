package ru.kpfu.itis.easybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.User;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> getPersonByName(String name);
}
