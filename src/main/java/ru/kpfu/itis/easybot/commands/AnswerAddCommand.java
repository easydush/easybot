package ru.kpfu.itis.easybot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.dto.AnswerDto;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.repository.PersonRepository;
import ru.kpfu.itis.easybot.service.AnswerServiceImpl;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

@Component
@Profile("dis")
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
    public void execute(GenericEvent event) {

        MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
        try {
            validationUtils.validate(messageReceivedEvent.getMessage().getContentRaw(), 5);
        } catch (IllegalArgumentException e) {
            messageReceivedEvent.getChannel().sendMessage("Can't find command " + this.header().name() + "with allowed arguments").queue();
        }

        new Thread(() ->
        {
            MessageChannel channel = messageReceivedEvent.getTextChannel();
            String message = ((MessageReceivedEvent) event).getMessage().getContentRaw();
            String name = message.split(" ")[2];
            String question = message.split(" ")[3];
            String answer = message.split(" ")[4];
            if (personRepository.getPersonByName(name).isPresent()) {
                answerService.addAnswer(new AnswerDto((long) 0, name, question, answer.toLowerCase().equals("true")));
                channel.sendMessage("Answer " + answer + " was successfully added to " + name).queue();
            } else {
                channel.sendMessage("Person " + name + " hasn't found.").queue();
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
