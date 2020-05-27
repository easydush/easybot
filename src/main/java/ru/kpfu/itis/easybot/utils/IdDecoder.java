package ru.kpfu.itis.easybot.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile({"dis", "web"})
public class IdDecoder {
    public String encode(String content) {
        StringBuilder builder = new StringBuilder(content);
        Random random = new Random();
        String salt = Integer.valueOf(random.nextInt(9)).toString() + Integer.valueOf(random.nextInt(9)).toString() + Integer.valueOf(random.nextInt(9)).toString();
        System.out.println("builded: " + salt);
        return builder.append(salt).toString();
    }

    public String decode(String content) {
        return content.substring(0, content.length() - 3);
    }
}
