package ru.kpfu.itis.easybot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import ru.kpfu.itis.easybot.handlers.AuthHandshakeHandler;
import ru.kpfu.itis.easybot.handlers.WebSocketMessagesHandler;
import ru.kpfu.itis.easybot.websocket.WebSocketHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private WebSocketMessagesHandler handler;

    @Autowired
    private AuthHandshakeHandler handshakeHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(handler, "/chat").setHandshakeHandler(handshakeHandler);
    }
}
