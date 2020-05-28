package ru.kpfu.itis.easybot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.dto.AnswerDto;
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.User;
import ru.kpfu.itis.easybot.repository.PersonRepository;
import ru.kpfu.itis.easybot.service.AnswerServiceImpl;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.io.IOException;

@Component
@Profile({"dis", "web"})
public class AnswerAddCommand extends Command {
    private final ValidationUtils validationUtils;
    @Autowired
    AnswerServiceImpl answerService;
    @Autowired
    PersonRepository personRepository;

    public AnswerAddCommand(ValidationUtils validationUtils) {
        this.validationUtils = validationUtils;
    }

    @Override
    public void execute(Message message, User author) throws IOException {
        String command = message.getText();
        try {
            validationUtils.validate(command, 5);
        } catch (IllegalArgumentException e) {
            super.sendMessages("Can't find command " + this.header().name() + "with allowed arguments");
        }

        new Thread(() ->
        {
            String name = command.split(" ")[2];
            String question = command.split(" ")[3];
            String answer = command.split(" ")[4];
            if (personRepository.getPersonByName(name).isPresent()) {
                answerService.addAnswer(new AnswerDto((long) 0, name, question, answer.toLowerCase().equals("true")));
                try {
                    super.sendMessages("Answer " + answer + " was successfully added to " + name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    super.sendMessages("Person " + name + " hasn't found.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public Headers header() {
        return Headers.addAnswer;
    }

    @Override
    public String description() {
        return "Adding answer to person. \n Use: \n addAnswer @EasyBot <name_of_person> <question title> <true|false>";
    }

}
