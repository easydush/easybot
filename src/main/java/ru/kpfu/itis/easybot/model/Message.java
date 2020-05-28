package ru.kpfu.itis.easybot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Invite;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;

@Data
@Profile("dis")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String text;
    @ManyToOne
    private User from;
}
