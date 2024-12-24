package com.github.syndexmx.AudioPodcastRssTGBot.netcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
public interface RssFetcher {

    public String getPage(String url);

}
