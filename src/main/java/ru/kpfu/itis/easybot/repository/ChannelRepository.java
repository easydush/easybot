package ru.kpfu.itis.easybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.easybot.model.Channel;
import ru.kpfu.itis.easybot.model.Game;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
