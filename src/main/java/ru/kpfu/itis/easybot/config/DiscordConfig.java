package ru.kpfu.itis.easybot.config;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.kpfu.itis.easybot.events.MessageReceived;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan(basePackages = "ru.kpfu.itis.easybot")
@Slf4j
@Profile("dis")
public class DiscordConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public JDA jda() throws LoginException {
//
//        Collection<GatewayIntent> intents = new ArrayList<>();
//        intents.add(GatewayIntent.GUILD_MEMBERS);
//        intents.add(GatewayIntent.GUILD_EMOJIS);
//        intents.add(GatewayIntent.GUILD_BANS);
//        intents.add(GatewayIntent.GUILD_INVITES);
//        intents.add(GatewayIntent.GUILD_VOICE_STATES);
//        intents.add(GatewayIntent.GUILD_PRESENCES);
//        intents.add(GatewayIntent.GUILD_MESSAGES);
//        intents.add(GatewayIntent.GUILD_MESSAGE_REACTIONS);
//        intents.add(GatewayIntent.GUILD_MESSAGE_TYPING);
//        intents.add(GatewayIntent.DIRECT_MESSAGES);
//        intents.add(GatewayIntent.DIRECT_MESSAGE_REACTIONS);
//        intents.add(GatewayIntent.DIRECT_MESSAGE_TYPING);

        JDABuilder builder = JDABuilder.createDefault("NzA5MDU3ODgxOTE5NDU1MjYz.XrkCpg.gUNhg8634h1CpQZj4eb4fFjE5OA");
          builder.setActivity(Activity.playing("Танюшины нервы"));
        builder.addEventListeners(context.getBean(MessageReceived.class));
        log.info("Bot has been started");
        return builder.build();
    }

}