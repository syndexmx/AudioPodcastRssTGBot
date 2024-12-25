package com.github.syndexmx.AudioPodcastRssTGBot.services.impl;

import com.github.syndexmx.AudioPodcastRssTGBot.controllers.TgController;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Podcast;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Subscriber;
import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.RssFetcher;
import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.impl.RssFetcherImpl;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.ChannelRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.PodcastRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.SubscriberRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.services.WebMonitor;
import com.github.syndexmx.AudioPodcastRssTGBot.services.rssutils.RssParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@Slf4j
public class WebMonitorImpl implements WebMonitor {

    private final Integer updateInterval;
    private volatile boolean initiated = false;
    private final RssFetcher rssFetcher;
    private final SubscriberRepository subscriberRepository;
    private final ChannelRepository channelRepository;
    private final RssParser rssParser;
    private final PodcastRepository podcastRepository;

    @Autowired
    @Lazy
    private TgController tgController;

    public WebMonitorImpl(@Value("${web-monitor.update-interval.ms}") Integer updateInterval,
                          @Autowired RssFetcher rssFetcher,
                          @Autowired SubscriberRepository subscriberRepository,
                          @Autowired ChannelRepository channelRepository,
                          @Autowired RssParser rssParser,
                          @Autowired PodcastRepository podcastRepository) {
        this.updateInterval = updateInterval;
        this.rssFetcher = rssFetcher;
        this.subscriberRepository = subscriberRepository;
        this.channelRepository = channelRepository;
        this.rssParser = rssParser;
        this.podcastRepository = podcastRepository;
    }


    @Override
    public void startMonitor() {
        if (initiated) return;
        initiated = true;
        //log.info("Monitor service started");
        while (true) {
            try {
                monitor();
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            goToSleep();
        }
    }

    private void monitor() {
        List<Channel> channelList = channelRepository.findAll();
        for (Channel channel : channelList) {
            String rss = rssFetcher.getPage(channel.getUrl());
            List<Podcast> podcastsListInRss = rssParser.getPodcastListFromRss(rss, channel);
            List<Podcast> podcastsListInStore = podcastRepository.findAll();
            List<Podcast> newPodcastsList = podcastsListInRss.stream()
                    .filter(podcast -> !podcastsListInStore.contains(podcast)).toList();
            podcastRepository.saveAll(newPodcastsList);
            for (Podcast podcast : newPodcastsList) {
                log.info("New podcast found : " + podcast.getTitle());
                    for (Subscriber subscriber : channel.getSubscribers()) {
                        // TODO : Bring the podcast file to the subscriber
                        tgController.sendMessage(podcast.getTitle(), subscriber.getId());
                    }
            }
        }
    }

    private void goToSleep() {
        try {
            Thread.sleep(updateInterval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
