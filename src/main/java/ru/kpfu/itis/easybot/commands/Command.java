package ru.kpfu.itis.easybot.commands;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.kpfu.itis.easybot.model.Channel;
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.User;
import ru.kpfu.itis.easybot.repository.ChannelRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Profile({"dis", "web"})
public abstract class Command {
    @Autowired
    private JDA jda;
    @Autowired
    ChannelRepository channelRepository;

    private static final Map<String, WebSocketSession> sessions = new HashMap<>();
    @Getter
    @Setter
    private ValidationUtils validationUtils;

    public abstract void execute(Message message, User author) throws IOException;

    public abstract Headers header();

    public enum Headers {
        help,
        addPerson,
        addAnswer,
        start,
        questions,
        guess,
        ask
    }


    public void sendMessages(String message) throws IOException {
        List<Channel> channels = channelRepository.findAll();
        for (WebSocketSession currentSession : sessions.values()) {
            currentSession.sendMessage(new TextMessage(message));
        }
        for (Channel channel : channels) {
            Objects.requireNonNull(jda.getTextChannelById(channel.getId())).sendMessage(message).queue();
        }
    }

    public void sendtoDiscord(String message) throws IOException {
        List<Channel> channels = channelRepository.findAll();
        for (Channel channel : channels) {
            (jda.getTextChannelById(channel.getId())).sendMessage(message).queue();
        }
    }

    public void sendtoSockets(String message) throws IOException {
        for (WebSocketSession currentSession : sessions.values()) {
            currentSession.sendMessage(new TextMessage(message));
        }
    }

    public abstract String description();
}