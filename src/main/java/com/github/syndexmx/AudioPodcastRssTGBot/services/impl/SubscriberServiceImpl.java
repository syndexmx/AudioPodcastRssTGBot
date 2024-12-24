package com.github.syndexmx.AudioPodcastRssTGBot.services.impl;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Podcast;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Subscriber;
import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.RssFetcher;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.ChannelRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.PodcastRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.repository.SubscriberRepository;
import com.github.syndexmx.AudioPodcastRssTGBot.services.SubscriberService;
import com.github.syndexmx.AudioPodcastRssTGBot.services.rssutils.RssParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {

    private RssFetcher rssFetcher;
    private SubscriberRepository subscriberRepository;
    private ChannelRepository channelRepository;
    private final RssParser rssParser;
    private final PodcastRepository podcastRepository;

    public SubscriberServiceImpl(@Autowired RssFetcher rssFetcher,
                                 @Autowired SubscriberRepository subscriberRepository,
                                 @Autowired ChannelRepository channelRepository,
                                 @Autowired RssParser rssParser,
                                 @Autowired PodcastRepository podcastRepository) {
        this.rssFetcher = rssFetcher;
        this.subscriberRepository = subscriberRepository;
        this.channelRepository = channelRepository;
        this.rssParser = rssParser;
        this.podcastRepository = podcastRepository;
    }


    @Override
    public String addChannel(Long subscriberId, String url) {
        log.info(subscriberId.toString() + " : add : " + url);
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
                    //.subscribers(List.of(subscriber))
                    .build();
            channel = channelRepository.save(channel);
        } else {
            channel = channelOptional.get();
        }
        subscriber.getChannels().add(channel);
        String rss = rssFetcher.getPage(url);
        List<Podcast> podcastList = rssParser.getPodcastListFromRss(rss, channel);

        // Temporary for debuggung reasons
        podcastList = podcastList.stream().skip(1).toList();

        podcastRepository.saveAll(podcastList);
        log.info(podcastList.size() + " podcasts found");
        return channel.getTitle();
    }
}
