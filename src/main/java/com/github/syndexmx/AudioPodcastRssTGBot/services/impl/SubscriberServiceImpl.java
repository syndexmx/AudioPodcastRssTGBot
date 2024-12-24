package com.github.syndexmx.AudioPodcastRssTGBot.services.impl;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Subscriber;
import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.RssFetcher;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.ChannelRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.SubscriberRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.services.SubscriberService;
import com.github.syndexmx.AudioPodcastRssTGBot.services.rssutils.RssParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {

    private RssFetcher rssFetcher;
    private SubscriberRepository subscriberRepository;
    private ChannelRepository channelRepository;
    private final RssParser rssParser;

    public SubscriberServiceImpl(@Autowired RssFetcher rssFetcher,
                                 @Autowired SubscriberRepository subscriberRepository,
                                 @Autowired ChannelRepository channelRepository,
                                 @Autowired RssParser rssParser) {
        this.rssFetcher = rssFetcher;
        this.subscriberRepository = subscriberRepository;
        this.channelRepository = channelRepository;
        this.rssParser = rssParser;
    }


    @Override
    public String addChannel(Long subscriberId, String url) {
        System.out.println(subscriberId.toString() + " : add : " + url);
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(subscriberId.toString());
        Subscriber subscriber;
        if (subscriberOptional.isEmpty()) {
            subscriber = Subscriber.builder()
                    .id(subscriberId)
                    .channels(new ArrayList<Channel>())
                    .build();
            subscriber = subscriberRepository.save(subscriber);
        } else {
            subscriber = subscriberOptional.get();
        }
        Optional<Channel> channelOptional = channelRepository.findById(url);
        Channel channel;
        if (channelOptional.isEmpty()) {
            String rss = rssFetcher.getPage(url);
            channel = Channel.builder()
                    .title(rssParser.getChannelTitle(rss))
                    .url(url)
                    .compressed(rssParser.getChannelTitle(rss).replaceAll(" ", ""))
                    .build();
            channel = channelRepository.save(channel);
        } else {
            channel = channelOptional.get();
        }
        subscriber.getChannels().add(channel);
        return channel.getTitle();
    }
}
