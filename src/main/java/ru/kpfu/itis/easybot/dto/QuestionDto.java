package ru.kpfu.itis.easybot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.easybot.model.Answer;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionDto {
    private long id;
    private String title;

    public static QuestionDto from(Question person) {
        return QuestionDto.builder()
                .id(person.getId())
                .title(person.getTitle())
                .build();
    }
    public static List<QuestionDto> from(List<Question> looks) {
        return looks.stream()
                .map(QuestionDto::from)
                .collect(Collectors.toList());
    }
}
