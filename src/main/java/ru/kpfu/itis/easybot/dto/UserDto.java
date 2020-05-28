package ru.kpfu.itis.easybot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import ru.kpfu.itis.easybot.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Profile("web")
public class UserDto {

    private String name;

    public static UserDto from(User user) {
        return UserDto.builder()
                .name(user.getName())
                .build();
    }
}