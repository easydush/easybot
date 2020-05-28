package ru.kpfu.itis.easybot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.Game;
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.Question;
import ru.kpfu.itis.easybot.repository.AnswerRepository;
import ru.kpfu.itis.easybot.repository.GameRepository;
import ru.kpfu.itis.easybot.repository.PersonRepository;
import ru.kpfu.itis.easybot.repository.QuestionRepository;
import ru.kpfu.itis.easybot.service.GameService;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.IdDecoder;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Component
@Profile({"dis", "web"})
public class AskCommand extends Command {
    private final ValidationUtils validationUtils;
    private final IdDecoder decoder;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;


    public AskCommand(ValidationUtils validationUtils, IdDecoder decoder) {
        this.validationUtils = validationUtils;
        this.decoder = decoder;
    }

    @Override
    public void execute(Message message, ru.kpfu.itis.easybot.model.User author) throws IOException {
        String command = message.getText();
        try {
            validationUtils.validate(command, 4);
        } catch (IllegalArgumentException e) {
            super.sendMessages("Can't find command " + this.header().name() + "with allowed arguments");
        }

        new Thread(() ->
        {

            String gameId = decoder.decode(command.split(" ")[2]);
            String question = command.split(" ")[3];
            if (gameRepository.findById(Long.valueOf(gameId)).isPresent()) {
                Game game = gameRepository.findById(Long.valueOf(gameId)).get();
                Person person = game.getPerson();
                if (questionRepository.findByTitle(question).isPresent()) {
                    Question asked = questionRepository.findByTitle(question).get();
                    if (answerRepository.findByPersonAndQuestion(person, asked).isPresent()) {
                        System.out.println(answerRepository.findByPersonAndQuestion(person, asked).get());
                        try {
                            super.sendMessages(answerRepository.findByPersonAndQuestion(person, asked).get().getIsTrue() + ", " + message.getFrom().getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            super.sendMessages("I don't know. Try another question, " + message.getFrom().getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    try {
                        super.sendMessages("Question is not found, " + message.getFrom().getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    super.sendMessages("Game is not found, " + message.getFrom().getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public Headers header() {
        return Headers.ask;
    }

    @Override
    public String description() {
        return "Asking a question.\n Use: \n ask @EasyBot <game-id> <question>";
    }

}

