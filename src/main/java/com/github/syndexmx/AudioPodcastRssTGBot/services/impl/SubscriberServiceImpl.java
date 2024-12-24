package com.github.syndexmx.AudioPodcastRssTGBot.services.impl;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Subscriber;
import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.RssFetcher;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.ChannelRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.SubscriberRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.services.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;

@Service
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {

    private RssFetcher rssFetcher;
    private SubscriberRepository subscriberRepository;
    private ChannelRepository channelRepository;

    @Autowired
    public SubscriberServiceImpl(RssFetcher rssFetcher,
                                 SubscriberRepository subscriberRepository,
                                 ChannelRepository channelRepository) {
        this.rssFetcher = rssFetcher;
        this.subscriberRepository = subscriberRepository;
        this.channelRepository = channelRepository;
    }


    @Override
    public String addChannel(Long subscriberId, String url) {
        System.out.println(subscriberId.toString() + " : add : " + url);
        Subscriber subscriber = subscriberRepository.getReferenceById(subscriberId.toString());
        if (subscriber == null) {
            subscriber = Subscriber.builder()
                    .id(subscriberId)
                    .channels(new ArrayList<Channel>())
                    .build();
        }
        Channel channel = channelRepository.getReferenceById(url);
        if (channel == null) {
            channel = Channel.builder()
                    .channelName(url)
                    .url(url)
                    .compressed(url)
                    .build();
        }
        subscriber.getChannels().add(channel);
        return channel.getChannelName();
    }
}
