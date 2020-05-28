package ru.kpfu.itis.easybot.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.model.*;
import ru.kpfu.itis.easybot.repository.AnswerRepository;
import ru.kpfu.itis.easybot.repository.GameRepository;
import ru.kpfu.itis.easybot.service.GameService;
import ru.kpfu.itis.easybot.utils.IdDecoder;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Component
@Profile({"dis", "web"})
public class AllQuestionsCommand extends Command {
    private final ValidationUtils validationUtils;
    private final IdDecoder decoder;
    @Autowired
    GameService service;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    GameRepository gameRepository;


    public AllQuestionsCommand(ValidationUtils validationUtils, IdDecoder decoder) {
        this.validationUtils = validationUtils;
        this.decoder = decoder;
    }

    @Override
    public void execute(Message message, User author) throws IOException {
        try {
            validationUtils.validate(message.getText(), 3);
        } catch (IllegalArgumentException e) {
            super.sendMessages("Can't find command " + this.header().name() + "with allowed arguments");
        }

        new Thread(() ->
        {

            String gameId = decoder.decode(message.getText().split(" ")[2]);
            System.out.println(gameId);
            if (gameRepository.findById(Long.valueOf(gameId)).isPresent()) {
                Game game = gameRepository.findById(Long.valueOf(gameId)).get();
                Person person = game.getPerson();
                List<Answer> answers = answerRepository.findAllByPerson(person);
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Questions for " + gameId);
                builder.setColor(new Color(0xFF69B4));
                int i = 0;
                for (Answer answer : answers
                ) {
                    builder.addField(String.valueOf(i), answer.getQuestion().getTitle(), false);
                    i++;
                }
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (MessageEmbed.Field field : builder.getFields()) {
                        stringBuilder.append("|"+field.getName()+' '+field.getValue()+'\n');
                    }
                    String encoded = stringBuilder.toString();
                    super.sendMessages(encoded);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    super.sendMessages("Game is not found.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public Headers header() {
        return Headers.questions;
    }

    @Override
    public String description() {
        return "All questions for game.\n Use: \n questions @EasyBot <game-id>";
    }

}
