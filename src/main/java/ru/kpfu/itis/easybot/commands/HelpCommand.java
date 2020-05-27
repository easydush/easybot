package ru.kpfu.itis.easybot.commands;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import ru.kpfu.itis.easybot.MessageResolver;
import ru.kpfu.itis.easybot.utils.ValidationUtils;

import java.awt.*;

@Component
@Profile("dis")
public class HelpCommand extends Command {

    private final ApplicationContext context;
    private final ValidationUtils validationUtils;
    //    @Autowired
//    MessageResolver messageResolver;
    @Autowired
    HelpCommand help;
    @Autowired
    PersonAddCommand addPerson;
    @Autowired
    AnswerAddCommand addAnswer;
    @Autowired
    StartGameCommand start;
    @Autowired
    GuessCommand guess;
    @Autowired
    AskCommand ask;

    @Autowired
    AllQuestionsCommand questions;

    public HelpCommand(ApplicationContext context, ValidationUtils validationUtils) {
        this.context = context;
        this.validationUtils = validationUtils;
    }

    @Override
    public void execute(GenericEvent event) {
        MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;

        try {
            validationUtils.validate(messageReceivedEvent.getMessage().getContentRaw(), 2);
        } catch (IllegalArgumentException e) {
            messageReceivedEvent.getChannel().sendMessage("Can't find command " + this.header().name() + " with allowed arguments").queue();
        }

        var headers = Headers.values();

        EmbedBuilder builder = new EmbedBuilder();
        //window opening
        builder.setTitle("Help commands for @EasyBot");
        builder.setColor(new Color(0x008B8B));
        builder.setImage("https://cultofthepartyparrot.com/parrots/hd/phparrot.gif");
        builder.setDescription("Commands: \n");
        builder.setAuthor("easydush");

        // add others
        builder.addField(help.header().name(), help.description(), false);
        builder.addField(addPerson.header().name(), addPerson.description(), false);
        builder.addField(addAnswer.header().name(), addAnswer.description(), false);
        builder.addField(start.header().name(), start.description(), true);
        builder.addField(questions.header().name(), questions.description(), true);
        builder.addField(ask.header().name(), ask.description(), true);
        builder.addField(guess.header().name(), guess.description(), true);

        messageReceivedEvent.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public Headers header() {
        return Headers.help;
    }

    @Override
    public String description() {
        return "Show all commands";
    }
}