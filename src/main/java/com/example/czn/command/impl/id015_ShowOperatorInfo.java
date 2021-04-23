package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.MessageRepo;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import com.example.czn.service.LanguageService;
import com.example.czn.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id015_ShowOperatorInfo extends Command {
    Language currentLanguage = LanguageService.getLanguage(chatId);
    @Autowired
    MessageRepo messageRepo;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isOperator()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        deleteMessage(updateMessageId);
//        Message message = messageDao.getMessage(messageId);
        Message message = messageRepo.findByIdAndLangId(messageId, currentLanguage.getId());
        sendMessage(messageId, chatId, null, message.getPhoto());
        if (message.getFile() != null) {
            switch (message.getTypeFile()) {
                case audio:
                    bot.execute(new SendAudio().setAudio(message.getFile()).setChatId(chatId));
                case video:
                    bot.execute(new SendVideo().setVideo(message.getFile()).setChatId(chatId));
                case document:
                    bot.execute(new SendDocument().setChatId(chatId).setDocument(message.getFile()));
            }
        }
        return EXIT;
    }
}
