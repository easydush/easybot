package ru.kpfu.itis.easybot.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
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
import ru.kpfu.itis.easybot.repository.GameRepository;
import ru.kpfu.itis.easybot.repository.PersonRepository;
import ru.kpfu.itis.easybot.service.GameService;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.IdDecoder;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.awt.*;
import java.util.List;
import java.util.Random;

@Component
@Profile("dis")
public class GuessCommand extends Command {
    private final ValidationUtils validationUtils;
    private final IdDecoder decoder;
    @Autowired
    GameService service;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    PersonRepository personRepository;


    public GuessCommand(ValidationUtils validationUtils, IdDecoder decoder) {
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
            String name = message.split(" ")[3];
            if (gameRepository.findById(Long.valueOf(gameId)).isPresent() & personRepository.getPersonByName(name).isPresent()) {
                Game game = gameRepository.findById(Long.valueOf(gameId)).get();
                if (!game.getIsFinished()) {
                    String guessed_name = game.getPerson().getName();
                    User guess_user = ((MessageReceivedEvent) event).getAuthor();
                    if (name.equals(guessed_name)) {
                        game.setIsFinished(true);
                        gameRepository.delete(game);
                        messageReceivedEvent.getChannel().sendMessage("You are the winner, " + guess_user.getAsMention()).queue();
                    } else {
                        messageReceivedEvent.getChannel().sendMessage("Your answer is false, " + guess_user.getAsMention()).queue();
                    }
                } else {
                    messageReceivedEvent.getChannel().sendMessage("Game is over.").queue();
                }
            } else {
                messageReceivedEvent.getChannel().sendMessage("Game or user is not found.").queue();
            }
        }).start();
    }

    @Override
    public Headers header() {
        return Headers.guess;
    }

    @Override
    public String description() {
        return "Guess command to try your chance.\n Use: \n guess @EasyBot <game-id> <person-name>";
    }

}