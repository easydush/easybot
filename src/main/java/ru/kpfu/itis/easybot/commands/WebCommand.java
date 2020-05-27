package ru.kpfu.itis.easybot.commands;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.ValidationUtils;
import ru.kpfu.itis.easybot.dto.MessageDto;

@Component
@Profile("web")
public abstract class WebCommand {

    @Getter
    @Setter
    private ValidationUtils validationUtils;

    public abstract String execute(MessageDto message);

    public abstract Headers header();

    public enum Headers{
        stats,
        friend,
        help,
        approve
    }

    public abstract String description();

}
