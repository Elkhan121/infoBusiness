package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import com.example.czn.service.LanguageService;
import com.example.czn.util.Const;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class id003_ShowAdminInfo extends Command {

    Language currentLanguage;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        currentLanguage = LanguageService.getLanguage(chatId);
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        deleteMessage(updateMessageId);

        Message message1 = messageRepo.findByIdAndLangId(messageId, currentLanguage.getId());
        sendMessage(messageId, chatId, null, message1.getPhoto());
        if (message1.getFile() != null) {
            switch (message1.getTypeFile()) {
                case audio:
                    bot.execute(new SendAudio().setAudio(message1.getFile()).setChatId(chatId));
                case video:
                    bot.execute(new SendVideo().setVideo(message1.getFile()).setChatId(chatId));
                case document:
                    bot.execute(new SendDocument().setChatId(chatId).setDocument(message1.getFile()));
            }
        }
        return EXIT;
    }
}
