package ru.kpfu.itis.easybot.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.easybot.model.Answer;
import ru.kpfu.itis.easybot.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PersonDto {
    private long id;
    private String name;
    private String image;
    private List<Answer> answers;

    public static PersonDto from(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .image(person.getImage())
                .build();
    }
    public static List<PersonDto> from(List<Person> looks) {
        return looks.stream()
                .map(PersonDto::from)
                .collect(Collectors.toList());
    }
}
