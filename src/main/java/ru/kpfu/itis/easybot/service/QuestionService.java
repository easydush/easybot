package ru.kpfu.itis.easybot.service;

import ru.kpfu.itis.easybot.dto.QuestionDto;
import ru.kpfu.itis.easybot.model.Question;

public interface QuestionService {
    Question addQuestion(QuestionDto qDto);
}
