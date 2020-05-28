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
        JDABuilder builder = JDABuilder.createDefault("NzA5MDU3ODgxOTE5NDU1MjYz.Xs6GDg.YgCgFy27HmooNpvha0AdOG8M0Ug");
          builder.setActivity(Activity.playing("Танюшины нервы"));
        builder.addEventListeners(context.getBean(MessageReceived.class));
        log.info("Bot has been started");
        return builder.build();
    }

}