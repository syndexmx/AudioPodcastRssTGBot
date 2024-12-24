package com.github.syndexmx.AudioPodcastRssTGBot.services;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import org.springframework.stereotype.Service;

@Service
public interface SubscriberService {

    String addChannel(Long subscriberId, String url);
}
