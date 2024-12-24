package com.github.syndexmx.AudioPodcastRssTGBot.services.impl;

import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.RssFetcher;
import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.impl.RssFetcherImpl;
import com.github.syndexmx.AudioPodcastRssTGBot.services.WebMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class WebMonitorImpl implements WebMonitor {

    private Integer updateInterval;
    private volatile boolean initiated = false;
    private RssFetcher rssFetcher;

    public WebMonitorImpl(@Value("${web-monitor.update-interval.ms}") Integer updateInterval,
                          @Autowired RssFetcher rssFetcher) {
        this.updateInterval = updateInterval;
        this.rssFetcher = rssFetcher;
    }


    @Override
    public void startMonitor() {
        if (initiated) return;
        initiated = true;
        //log.info("Monitor service started");
        while (true) {
            try {
                // scanWebSources();
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            goToSleep();
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
