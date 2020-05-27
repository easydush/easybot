package ru.kpfu.itis.easybot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

@Component
@Profile("dis")
public class PersonAddCommand extends Command {
    private final ValidationUtils validationUtils;
    @Autowired
    PersonServiceImpl personService;

    public PersonAddCommand(ValidationUtils validationUtils) {
        this.validationUtils = validationUtils;
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
            String name = message.split(" ")[2];
            String image = message.split(" ")[3];
            personService.addPerson(new PersonDto(0, name, image, null));
            channel.sendMessage("Person " + name + " was successfully added.").queue();
        }).start();
    }

    @Override
    public Headers header() {
        return Headers.addPerson;
    }

    @Override
    public String description() {
        return "Adding person. \n Use: \n  addPerson @EasyBot <name> <image url>";
    }

}
