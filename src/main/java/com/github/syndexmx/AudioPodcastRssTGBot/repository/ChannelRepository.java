package com.github.syndexmx.AudioPodcastRssTGBot.repository;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {
}
