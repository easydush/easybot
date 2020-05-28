package ru.kpfu.itis.easybot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.dto.PersonDto;
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.Person;
import ru.kpfu.itis.easybot.model.User;
import ru.kpfu.itis.easybot.service.PersonServiceImpl;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.io.IOException;

@Component
@Profile({"dis","web"})
public class PersonAddCommand extends Command {
    private final ValidationUtils validationUtils;
    @Autowired
    PersonServiceImpl personService;

    public PersonAddCommand(ValidationUtils validationUtils) {
        this.validationUtils = validationUtils;
    }

    @Override
    public void execute(Message message, User author) throws IOException {
        String command = message.getText();
        try {
            validationUtils.validate(command, 4);
        } catch (IllegalArgumentException e) {
            super.sendMessages("Can't find command " + this.header().name() + "with allowed arguments");
        }

        new Thread(() ->
        {
            String name = command.split(" ")[2];
            String image = command.split(" ")[3];
            personService.addPerson(new PersonDto(0, name, image, null));
            try {
                super.sendMessages("Person " + name + " was successfully added.");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
