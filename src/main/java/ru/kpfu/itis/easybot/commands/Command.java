package ru.kpfu.itis.easybot.commands;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.ValidationUtils;

@Component
@Profile("dis")
public abstract class Command {

    @Getter
    @Setter
    private ValidationUtils validationUtils;

    public abstract void execute(GenericEvent event);

    public abstract Headers header();

    public enum Headers {
        test,
        help,
        addPerson,
        addAnswer,
        start,
        questions,
        guess,
        ask
    }

    public abstract String description();
}