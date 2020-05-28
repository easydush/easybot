package ru.kpfu.itis.easybot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import ru.kpfu.itis.easybot.model.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Profile({"dis","web"})
public class MessageDto {

    private String text;
    private UserDto from;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .text(message.getText())
                .from(UserDto.from(message.getFrom()))
                .build();
    }
}
