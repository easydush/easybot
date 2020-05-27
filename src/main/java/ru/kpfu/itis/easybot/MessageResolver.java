package ru.kpfu.itis.easybot;

import lombok.var;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.commands.Command;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("dis")
public class MessageResolver {

    private final ApplicationContext context;

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    private final Map<String, Command> commandMap = new HashMap<>();
    private final JDA jda;

    public MessageResolver(ApplicationContext applicationContext, JDA jda) {
        this.context = applicationContext;
        this.jda = jda;
        initializeCommands();
    }

    private void initializeCommands() {
        var commands = context.getBeansOfType(Command.class).values();
        for (Command comma : commands
        ) {
            addCommand(comma);
            System.out.println(comma
            );
        }
    }

    private void addCommand(Command command) {
        commandMap.put(command.header().name(), command);
        System.out.println("put to the map"+command.header().name() + ' ' + command);
    }

    public void executeCommand(MessageReceivedEvent event) {

        Message message = event.getMessage();

        String commandText = message.getContentRaw();
        System.out.println("typed:"+commandText);
        Command command = commandMap.get(commandText.split(" ")[0]);


        if (message.getMentionedMembers().contains(event.getGuild().getSelfMember()) ||
                message.getContentRaw().contains("709057881919455263")) {
            if (command == null) {
                throw new IllegalArgumentException("Unknown header " + commandText);
            }
            System.out.println("executing " + command);
            command.execute(event);
        }
    }
}