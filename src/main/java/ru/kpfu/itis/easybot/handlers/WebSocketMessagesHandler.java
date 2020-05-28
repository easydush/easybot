package ru.kpfu.itis.easybot.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.WebSocket;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import org.glassfish.grizzly.http.server.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.kpfu.itis.easybot.MessageResolver;
import ru.kpfu.itis.easybot.model.Message;
import ru.kpfu.itis.easybot.model.User;
import ru.kpfu.itis.easybot.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableWebSocket
@AllArgsConstructor
@Profile("web")
public class WebSocketMessagesHandler extends TextWebSocketHandler {
    private final JDA jda;
    private static final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private final MessageResolver resolver;
    @Autowired
    UserRepository repository;


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String messageText = (String) message.getPayload();
        User user = repository.getUserByName(session.getPrincipal().getName()).orElse(repository.save(new User((long) 0, session.getPrincipal().getName())));
        if (!repository.getUserByName(session.getId()).isPresent()) {
            repository.save(new User((long) 0, session.getId()));
        }
        Message messageFromJson = objectMapper.readValue(messageText, Message.class);

        if (!sessions.containsKey(messageFromJson.getFrom())) {
            sessions.put(messageFromJson.getFrom().getName(), session);
        }

        for (WebSocketSession currentSession : sessions.values()) {
            currentSession.sendMessage(message);
        }
        resolver.executeCommand(new Message(messageText, user), user);


    }
}

