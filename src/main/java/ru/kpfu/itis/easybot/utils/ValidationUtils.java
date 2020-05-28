package ru.kpfu.itis.easybot.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dis","web"})
public class ValidationUtils {

    public void validate(String content, int length) throws IllegalArgumentException {
        if (content.split(" ").length != length) {
            throw new IllegalArgumentException("Many or less arguments found for command ");
        }
    }
}