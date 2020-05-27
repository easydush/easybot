package ru.kpfu.itis.easybot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.Answer;
import ru.kpfu.itis.easybot.model.Game;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.repository.AnswerRepository;
import ru.kpfu.itis.easybot.service.GameService;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.IdDecoder;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.awt.*;
import java.util.List;
import java.util.Random;

@Component
@Profile("dis")
public class StartGameCommand extends Command {
    private final ValidationUtils validationUtils;
    private final IdDecoder decoder;
    @Autowired
    GameService service;
    @Autowired
    AnswerRepository answerRepository;


    public StartGameCommand(ValidationUtils validationUtils, IdDecoder decoder) {
        this.validationUtils = validationUtils;
        this.decoder = decoder;
    }

    @Override
    public void execute(GenericEvent event) {
        MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
        try {
            validationUtils.validate(messageReceivedEvent.getMessage().getContentRaw(), 2);
        } catch (IllegalArgumentException e) {
            messageReceivedEvent.getChannel().sendMessage("Can't find command " + this.header().name() + "with allowed arguments").queue();
        }

        new Thread(() ->
        {
            MessageChannel channel = messageReceivedEvent.getTextChannel();
            Game game = service.addGame();
            Person person = game.getPerson();
            String gameId = decoder.encode(game.getId().toString());
            List<Answer> answers = answerRepository.findAllByPerson(person);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Questions for " + gameId);
            builder.setColor(new Color(0xFF69B4));
            builder.setDescription("Game is started! Id: " + gameId + "\n Ask questions using: ask @EasyBot <question> <game-id>\n");
            int i = 0;
            for (Answer answer : answers
            ) {
                builder.addField(String.valueOf(i), answer.getQuestion().getTitle(), false);
                i++;
            }
            messageReceivedEvent.getChannel().sendMessage(builder.build()).queue();
        }).start();
    }

    @Override
    public Headers header() {
        return Headers.start;
    }

    @Override
    public String description() {
        return "Starting new game.\n Use: \n start @EasyBot";
    }

}

