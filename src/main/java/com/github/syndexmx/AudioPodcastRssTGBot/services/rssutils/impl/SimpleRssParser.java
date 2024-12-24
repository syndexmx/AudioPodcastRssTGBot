package com.github.syndexmx.AudioPodcastRssTGBot.services.rssutils.impl;

import com.github.syndexmx.AudioPodcastRssTGBot.domain.Channel;
import com.github.syndexmx.AudioPodcastRssTGBot.domain.Podcast;
import com.github.syndexmx.AudioPodcastRssTGBot.services.rssutils.RssParser;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SimpleRssParser implements RssParser {

    Integer DESCRIPTION_LENGTH_LIMIT = 224;

    @Override
    public String getChannelTitle(String rss) {
        String title = rss.substring(rss.indexOf("<title>")+7);
        title = title.substring(0, title.indexOf("</title>"));
        return title;
    }

    @Override
    public List<Podcast> getPodcastListFromRss(String rssText, Channel owner) {
        List<Podcast> outList = new ArrayList<>();
        String text = rssText;
        StringBuilder parsedRss = new StringBuilder();
        while (text.contains("<item>")){
            text = text.substring(text.indexOf("<item>"));
            String aBlock = text.substring(0, text.indexOf("</item>")+1);

            if (aBlock.contains(".mp3")){

                String title = getTitleFromBlock(aBlock);
                String link = getUrlFromBlock(aBlock);
                String date = getDateFromBlock(aBlock);
                String description = getDescriptionFromBlock(aBlock);

                outList.add(Podcast.builder()
                                .podcastUrl(link)
                                .date(date)
                                .description(description)
                                .title(title)
                                .ownerChannel(owner)
                        .build());
            }
            text = text.substring(text.indexOf("</item>")+7);
        }
        return outList;
    }

    private String getTitleFromBlock(String aBlock){
        String title = aBlock.substring(aBlock.indexOf("<title>")+7);
        title = title.substring(0, title.indexOf("</title>"));
        title = title.replace('<','_')
                .replace('>','_')
                .replace('"','*');
        if (title.contains("[") && title.contains("]")){
            title = title.substring(title.lastIndexOf("[")+1);
            title = title.substring(0,title.indexOf("]"));
        }
        return title;
    }

    private String getDescriptionFromBlock(String aBlock) {
        if (!aBlock.contains("<description>") | !aBlock.contains("<description>"))
            return "";
        String description = aBlock.substring(aBlock.indexOf("<description>") + 12);
        description = description.substring(0, description.indexOf("</description>"));
        description = description.replace('"','*');
        description = description.replace("<p>","\n");
        description = description.replace("</p>","\n");
        if (description.contains("[") && description.contains("]")){
            description = description.substring(description.lastIndexOf("[") + 1);
            description = description.substring(0,description.indexOf("]"));
        }
        if (description.length() > DESCRIPTION_LENGTH_LIMIT){
            description = description.substring(0,DESCRIPTION_LENGTH_LIMIT) + " ... ...";
        }
        return description;
    }

    private String getUrlFromBlock(String aBlock) {
        String link = aBlock.substring(aBlock.indexOf("url=\"") + 5);
        link = link.substring(0, link.indexOf("\""));
        return link;
    }

    private String getDateFromBlock(String aBlock) {
        String date = "";
        if (aBlock.contains("<pubDate>")) {
            date = aBlock.substring(aBlock.indexOf("<pubDate>") + 9);
            date = date.substring(0, date.indexOf("</pubDate>"));
        }
        return date;
    }
}