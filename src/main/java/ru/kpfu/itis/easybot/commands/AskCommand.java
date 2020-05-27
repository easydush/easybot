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

import java.util.List;
import java.util.Random;

@Component
@Profile("dis")
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
    public void execute(GenericEvent event) {
        MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
        try {
            validationUtils.validate(messageReceivedEvent.getMessage().getContentRaw(), 4);
        } catch (IllegalArgumentException e) {
            messageReceivedEvent.getChannel().sendMessage("Can't find command " + this.header().name() + "with allowed arguments").queue();
        }

        new Thread(() ->
        {

            MessageChannel channel = messageReceivedEvent.getTextChannel();
            String message = ((MessageReceivedEvent) event).getMessage().getContentRaw();
            String gameId = decoder.decode(message.split(" ")[2]);
            String question = message.split(" ")[3];
            User guess_user = ((MessageReceivedEvent) event).getAuthor();
            if (gameRepository.findById(Long.valueOf(gameId)).isPresent()) {
                Game game = gameRepository.findById(Long.valueOf(gameId)).get();
                Person person = game.getPerson();
                if (questionRepository.findByTitle(question).isPresent()) {
                    Question asked = questionRepository.findByTitle(question).get();
                    if (answerRepository.findByPersonAndQuestion(person, asked).isPresent()) {
                        System.out.println(answerRepository.findByPersonAndQuestion(person, asked).get());
                        messageReceivedEvent.getChannel().sendMessage(answerRepository.findByPersonAndQuestion(person, asked).get().getIsTrue() + ", " + guess_user.getAsMention()).queue();
                    } else {
                        messageReceivedEvent.getChannel().sendMessage("I don't know. Try another question, " + guess_user.getAsMention()).queue();

                    }
                } else {
                    messageReceivedEvent.getChannel().sendMessage("Question is not found, " + guess_user.getAsMention()).queue();
                }
            } else {
                messageReceivedEvent.getChannel().sendMessage("Game is not found, " + guess_user.getAsMention()).queue();
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

