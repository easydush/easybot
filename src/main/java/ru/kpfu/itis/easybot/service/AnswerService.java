package ru.kpfu.itis.easybot.service;

import ru.kpfu.itis.easybot.dto.AnswerDto;
import ru.kpfu.itis.easybot.model.Answer;

public interface AnswerService {
    Answer addAnswer(AnswerDto aDto);
}
