package ru.kpfu.itis.easybot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.*;
import ru.kpfu.itis.easybot.repository.AnswerRepository;
import ru.kpfu.itis.easybot.service.GameService;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.IdDecoder;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Component
@Profile({"dis", "web"})
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
    public void execute(Message message, User author) throws IOException {
        String command = message.getText();
        try {
            validationUtils.validate(command, 2);
        } catch (IllegalArgumentException e) {
            sendMessages("Can't find command " + this.header().name() + "with allowed arguments");
        }

        new Thread(() ->
        {
            Game game = service.addGame();
            Person person = game.getPerson();
            String gameId = decoder.encode(game.getId().toString());
            //List<Answer> answers = answerRepository.findAllByPerson(person);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Questions for " + gameId);
            builder.setColor(new Color(0xFF69B4));
            builder.setDescription("Game is started! Id: " + gameId + "\n Ask questions using: ask @EasyBot <game-id> <question>\n" +
                    "|Get the list of quetsions using: \n" +
                    " questions @EasyBot <game-id>");
//            int i = 0;
//            for (Answer answer : answers
//            ) {
//                builder.addField(String.valueOf(i), answer.getQuestion().getTitle(), false);
//                i++;
//            }
            try {
                StringBuilder stringBuilder = new StringBuilder();
                for (MessageEmbed.Field field : builder.getFields()) {
                    stringBuilder.append("|" + field.getName() + ' ' + field.getValue() + '\n');
                }
                String encoded = stringBuilder.toString();
                sendMessages(encoded);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

