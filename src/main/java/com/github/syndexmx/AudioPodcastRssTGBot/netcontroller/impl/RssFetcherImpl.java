package com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.impl;

import com.github.syndexmx.AudioPodcastRssTGBot.netcontroller.RssFetcher;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class RssFetcherImpl implements RssFetcher {

    public String getPage(String url) {

        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
