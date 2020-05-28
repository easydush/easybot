package ru.kpfu.itis.easybot.events;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.easybot.MessageResolver;
import ru.kpfu.itis.easybot.dto.MessageDto;
import ru.kpfu.itis.easybot.dto.UserDto;
import ru.kpfu.itis.easybot.model.Channel;
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.User;
import ru.kpfu.itis.easybot.repository.ChannelRepository;
import ru.kpfu.itis.easybot.repository.UserRepository;

import javax.annotation.Nonnull;

@Component
@Slf4j
@Profile("dis")
public class MessageReceived extends ListenerAdapter {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    private final MessageResolver resolver;

    public MessageReceived(@Lazy MessageResolver resolver) {
        this.resolver = resolver;
    }

    @SneakyThrows
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String name = event.getAuthor().getAsMention();
        User author = null;
        if (userRepository.getUserByName(name).isPresent()) {
            author = userRepository.getUserByName(name).get();
        } else {
            author = userRepository.save(new User((long) 0, name));
        }
        if (!channelRepository.findById(event.getChannel().getIdLong()).isPresent()) {
            channelRepository.save(new Channel(event.getChannel().getIdLong()));
        }

        resolver.executeCommand(new Message(event.getMessage().getContentRaw(), author), author);
    }

}