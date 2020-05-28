package ru.kpfu.itis.easybot;

import net.dv8tion.jda.api.JDA;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.commands.Command;
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.User;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile({"dis", "web"})
public class MessageResolver {

    private final ApplicationContext context;
    private final Map<String, Command> commandMap = new HashMap<>();
    private final JDA jda;

    public MessageResolver(ApplicationContext applicationContext, JDA jda) {
        this.context = applicationContext;
        this.jda = jda;
        initializeCommands();
    }

    private void initializeCommands() {
        Collection<Command> commands = context.getBeansOfType(Command.class).values();
        for (Command comma : commands
        ) {
            addCommand(comma);
        }

    }

    private void addCommand(Command command) {
        commandMap.put(command.header().name(), command);
    }


    public void executeCommand(Message message, User author) throws IOException {
        String commandText = message.getText().split(" ")[0];
        Command command = commandMap.get(commandText);

        //if (message.getText().contains("709057881919455263") || message.getText().contains("@EasyBot")) {
            if (command == null) {
                throw new IllegalArgumentException("Unknown header " + commandText);
            }
            System.out.println("executing " + command);
            command.execute(message, author);
        //}
    }
}