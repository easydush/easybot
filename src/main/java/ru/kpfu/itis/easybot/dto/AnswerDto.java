package ru.kpfu.itis.easybot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.easybot.model.Answer;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.Question;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerDto {
    private Long id;
    private String person;
    private String question;
    private Boolean isTrue;


    public static AnswerDto from(Answer look) {
        return AnswerDto.builder()
                .id(look.getId())
                .person(PersonDto.from(look.getPerson()).getName())
                .question(QuestionDto.from(look.getQuestion()).getTitle())
                .isTrue(look.getIsTrue())
                .build();
    }


    public static List<AnswerDto> from(List<Answer> looks) {
        return looks.stream()
                .map(AnswerDto::from)
                .collect(Collectors.toList());
    }
}
