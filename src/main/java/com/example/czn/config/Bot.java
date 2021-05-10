package com.example.czn.config;

import com.example.czn.util.UpdateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
@Getter
@Setter
public class Bot extends TelegramLongPollingBot {

    private Map<Long, Conversation> conversations = new HashMap<>();

    @PostConstruct
    public  void            initIt() throws TelegramApiRequestException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(this);
        log.info("Bot was registered : " + getBotUsername());
    }
    //    private ButtonRepository buttonRepository  = TelegramBotRepositoryProvider.getButtonRepository();
    @Override
    public void onUpdateReceived(Update update) {

        Conversation conversation = getConversation(update);
        try {

            try {
                conversation.handleUpdate(update, this);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (TelegramApiException e) {
            log.error("Error in conversation handleUpdate" + e);
        }
    }
    private Conversation    getConversation(Update update) {
        Long chatId                 = UpdateUtil.getChatId(update);
        Conversation conversation   = conversations.get(chatId);
        if (conversation == null) {
            log.info("InitNormal new conversation for '{}'", chatId);
            conversation            = new Conversation();
            conversations.put(chatId, conversation);
        }
        return conversation;
    }

    @Override
    public String getBotUsername() {
        return "MiniSocialNetwork";
    }

    @Override
    public String getBotToken() {
//        return "840528216:AAG0nr1Yi22M8A0u_QR3s6hV7uVvu0_1GkA";  // мой
//        return "1018614657:AAFSXmF1acRFpyo6TkOpnsPdwLkwNc71838";
//        return "1772101252:AAH5UDGTwyNst5wHWbqk2PWYn_dNsUyI97U"; //@testBot
        return "1648884653:AAEFWRv5mocX8ZiXz7t-HAya-WU7KJCBCGE"; //MiniSocialNetwork
//        return "1636752449:AAF82CRAEPisqFe3FT6MVDSX6e41Cw_ddRA";   //@centrnit_bot
    }
}



//package com.example.czn.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
//
//import javax.annotation.PostConstruct;
//import java.io.Serializable;
//
//@Slf4j
//@Component
//public class Bot extends TelegramLongPollingBot {
//
//    private static final Logger logger = LoggerFactory.getLogger(Bot.class);
//
//    @Autowired
//    private UpdateHandler updateHandler = new UpdateHandler();
//
//
//    @PostConstruct
//    public  void            initIt() throws TelegramApiRequestException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        telegramBotsApi.registerBot(this);
//        log.info("Bot was registered : " + getBotUsername());
//    }
//    @Override
//    public void onUpdateReceived(Update update) {
//        //System.out.println(update.getMessage().getChatId());
//
//
//        logger.debug("------ get UPDATE: " + getBotUsername());
//        updateHandler.handle(update, this);
//        logger.debug("------ UPDATE processed success");
//    }
//
//    @Override
//    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
//        if (method instanceof SendMessage) {
//            ((SendMessage) method).disableWebPagePreview();
//        }
//        return super.execute(method);
//    }
//
//    @Override
//    public String getBotUsername() {
//        return "just_test_example_bot";
//    }
//
//    @Override
//    public String getBotToken() {
////        return "840528216:AAG0nr1Yi22M8A0u_QR3s6hV7uVvu0_1GkA"; // мой
////        return "1018614657:AAFSXmF1acRFpyo6TkOpnsPdwLkwNc71838";
//        return "1772101252:AAH5UDGTwyNst5wHWbqk2PWYn_dNsUyI97U";
//    }
//}
