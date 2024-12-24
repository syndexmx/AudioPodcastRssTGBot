package com.github.syndexmx.AudioPodcastRssTGBot.services.rssutils;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Podcast;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RssParser {

    String getChannelTitle(String rss);

    List<Podcast> parseRss(String string, Channel owner);
}
