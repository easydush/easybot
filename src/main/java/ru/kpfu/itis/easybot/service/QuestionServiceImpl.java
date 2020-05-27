package ru.kpfu.itis.easybot.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.easybot.dto.QuestionDto;
import ru.kpfu.itis.easybot.model.Question;
import ru.kpfu.itis.easybot.repository.QuestionRepository;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository repository;

    @Override
    public Question addQuestion(QuestionDto qDto) {
        Question q = Question
                .builder()
                .id(qDto.getId())
                .title(qDto.getTitle())
                .build();
        repository.save(q);
        return q;
    }
}
