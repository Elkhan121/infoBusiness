package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Language;
import com.example.czn.service.LanguageService;
import com.example.czn.util.Const;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
//@NoArgsConstructor
public class id002_SelectionLanguage extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        deleteMessage(updateMessageId);
        chosenLanguage();
//        sendMessageWithAddition();
        sendMessageWithKeyboard(getText(106), 1);
        return EXIT;
    }

    private void chosenLanguage() {
        if (isButton(Const.RU_LANGUAGE)) {
            LanguageService.setLanguage(chatId, Language.ru);
        }
        if (isButton(Const.KZ_LANGUAGE)) {
            LanguageService.setLanguage(chatId, Language.kz);
        }
        if (isButton(Const.EN_LANGUAGE)) {
            LanguageService.setLanguage(chatId, Language.en);
        }
    }
}
