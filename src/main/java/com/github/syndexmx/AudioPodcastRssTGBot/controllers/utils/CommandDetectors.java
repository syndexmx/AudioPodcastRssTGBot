package com.github.syndexmx.AudioPodcastRssTGBot.controllers.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CommandDetectors {

    private String ADD_COMMAND = "add ";

    public boolean isAddCommand(String message) {
        if (message.indexOf(ADD_COMMAND) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String extractUrlAddCommandUrl(String message) {
        return message.substring(ADD_COMMAND.length());
    }

}
