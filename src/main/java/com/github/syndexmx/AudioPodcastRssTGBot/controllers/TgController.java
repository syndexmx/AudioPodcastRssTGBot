package com.github.syndexmx.AudioPodcastRssTGBot.controllers;

import com.github.syndexmx.AudioPodcastRssTGBot.controllers.utils.CommandDetectors;
import com.github.syndexmx.AudioPodcastRssTGBot.services.SubscriberService;
import com.github.syndexmx.AudioPodcastRssTGBot.services.WebMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Controller
@Slf4j
public class TgController extends TelegramLongPollingBot {

    final String BOT_NAME;
    final String BOT_TOKEN;

    final private WebMonitor webMonitor;
    final private CommandDetectors commandDetectors;
    final private SubscriberService subscriberService;

    public TgController(@Value("${tg-bot.name}") String botName,
                        @Value("${tg-bot.token}") String botToken,
                        @Autowired WebMonitor webMonitor,
                        @Autowired CommandDetectors commandDetectors,
                        @Autowired SubscriberService subscriberService) throws TelegramApiException {
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
        this.webMonitor = webMonitor;
        this.commandDetectors = commandDetectors;
        this.subscriberService = subscriberService;
        Thread backgroundThread = new Thread() {
            public void run() {
                webMonitor.startMonitor();
            }
        };
        backgroundThread.start();
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (commandDetectors.isAddCommand(userMessage)) {
                String channelName = subscriberService.addChannel(chatId,
                        commandDetectors.extractUrlAddCommandUrl(userMessage));
                sendMessage(channelName, chatId);
            }
        }
    }

    public void sendMessage (String m, long chatId) {
        // How to from  https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(chatId);
        message.setText(m);
        // message.setReplyMarkup(BotMenu.prepareKeyboard(new ArrayList<>()));
        message.setParseMode("markdown");

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
