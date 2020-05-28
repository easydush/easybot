package ru.kpfu.itis.easybot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.persistence.ManyToOne;

@Data
@Profile("web")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebMessage {
    private String text;
    private String from;
}
