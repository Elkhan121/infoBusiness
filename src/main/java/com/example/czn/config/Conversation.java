package com.example.czn.config;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.MessageRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import com.example.czn.exception.CommandNotFoundException;
import com.example.czn.service.CommandService;
import com.example.czn.service.LanguageService;
import com.example.czn.util.Const;
import com.example.czn.util.DateUtil;
import com.example.czn.util.SetDeleteMessages;
import com.example.czn.util.UpdateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

import static org.apache.log4j.NDC.clear;

@Component
public class Conversation {

    @Autowired
    private CommandService commandService = new CommandService();
    private Optional<Command> command;
    private Long chatId;
    private static long currentChatId;
    private static final Logger logger = LoggerFactory.getLogger(Conversation.class);
    private MessageRepo messageRepo = TelegramBorRepositoryProvider.getMessageRepo();
    public static long getCurrentChatId() {
        return currentChatId;
    }

    public void handleUpdate(Update update, DefaultAbsSender bot) throws SQLException, TelegramApiException {
        printUpdate(update);
        chatId = UpdateUtil.getChatId(update);
        currentChatId = chatId;
        checkLang(chatId);
        try {
            command = commandService.getCommand(update);
            if (command != null) {
                SetDeleteMessages.deleteKeyboard(chatId, bot);
                SetDeleteMessages.deleteMessage(chatId, bot);
            }
        } catch (CommandNotFoundException e) {
            if (chatId < 0) {
                return;
            }
            if (command == null) {
                SetDeleteMessages.deleteKeyboard(chatId, bot);
                SetDeleteMessages.deleteMessage(chatId, bot);
                Message message = messageRepo.findByIdAndLangId(Const.COMMAND_NOT_FOUND, LanguageService.getLanguage(chatId).getId());
                SendMessage sendMessage = new SendMessage(chatId, message.getName());
                bot.execute(sendMessage);
            }
        }
        if (command != null) {
            if (command.get().isInitNotNormal(update, bot)) {
                clear();
                return;
            }
            boolean commandFinished = command.get().execute();
            if (commandFinished) {
                clear();
            }
        }
    }

    private void checkLang(long chatId) {
        if (LanguageService.getLanguage(chatId) == null) {
            LanguageService.setLanguage(chatId, Language.ru);
        }
    }

    private void printUpdate(Update update) {
        String dateMessage = "";
        if (update.hasMessage()) {
            dateMessage = DateUtil.getDbMmYyyyHhMmSs(new Date((long) update.getMessage().getDate() * 1000));
        }
        logger.debug("New update get {} -> send response {}", dateMessage, DateUtil.getDbMmYyyyHhMmSs(new Date()));
        logger.debug(UpdateUtil.toString(update));
    }

//    public static DefaultAbsSender getBot()
//    {
//        return Main.getBot();
//    }

//    void clear() {
//        command.get().;
//        command = null;
//    }
}
