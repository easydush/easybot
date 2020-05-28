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
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.repository.AnswerRepository;
import ru.kpfu.itis.easybot.repository.GameRepository;
import ru.kpfu.itis.easybot.repository.PersonRepository;
import ru.kpfu.itis.easybot.service.GameService;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.IdDecoder;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Component
@Profile({"dis","web"})
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
            String name = command.split(" ")[3];
            if (gameRepository.findById(Long.valueOf(gameId)).isPresent() & personRepository.getPersonByName(name).isPresent()) {
                Game game = gameRepository.findById(Long.valueOf(gameId)).get();
                if (!game.getIsFinished()) {
                    String guessed_name = game.getPerson().getName();
                    if (name.equals(guessed_name)) {
                        game.setIsFinished(true);
                        gameRepository.delete(game);
                        try {
                            super.sendMessages("You are the winner, " + message.getFrom().getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            super.sendMessages("Your answer is false, " + message.getFrom().getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        super.sendMessages("Game is over.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    super.sendMessages("Game or user is not found.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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