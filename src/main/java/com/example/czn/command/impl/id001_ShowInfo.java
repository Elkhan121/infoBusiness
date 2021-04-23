package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.UserRepo;
import com.example.czn.entity.standart.User;
import com.example.czn.service.KeyboardService;
import com.example.czn.service.LanguageService;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class id001_ShowInfo extends Command {
    private User user;

    private KeyboardService keyboardService = new KeyboardService();

    @Override
    public boolean execute() throws TelegramApiException {

        if (userRepo.countByChatId(chatId) == 0) {
            int del = 0;
            switch (waitingType) {
                case START:
                    getFullName();
                    user = new User();
                    user.setChatId(chatId);
                    user.setUserName(update.getMessage().getFrom().getUserName());
                    waitingType = WaitingType.SET_FULL_NAME;
                    return COMEBACK;
                case SET_FULL_NAME:
                    if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                        user.setFullName(update.getMessage().getText());
                        del = getPhone();
                        waitingType = WaitingType.SET_PHONE_NUMBER;
                        return COMEBACK;
                    } else {
                        wrongData();
                        getFullName();
                        return COMEBACK;
                    }
                case SET_PHONE_NUMBER:
                    if (botUtils.hasContactOwner(update)) {
                        user.setPhone(update.getMessage().getContact().getPhoneNumber());
                        userRepo.save(user);
                        deleteMessage(del);
                        sendMessage("Регистрация успешно пройдена!", chatId);
                        sendMessageWithKeyboard("Главное меню: ", 1);
                        sendMessageWithAddition();
                        return EXIT;
                    } else {
                        wrongData();
                        getPhone();
                        return COMEBACK;
                    }
            }
        }

        if (isButton(9)) {
            sendMessageWithKeyboard(getText(4), 2);

        } else if (isButton(10)) {
            sendMessageWithKeyboard(getText(106), 1);
            // sendMessageWithAddition();
            return EXIT;
        }
        return EXIT;

    }

    private int wrongData() throws TelegramApiException {
        return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId);
    }

    private int getFullName() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_FULL_NAME, chatId);
    }

    private int getPhone() throws TelegramApiException {
        return sendMessageWithKeyboard(getText(Const.SET_MOBILE_PHONE_NUMBER),
                keyboardService.getKeyboard(keyboardMarkUpRepo.findById(12), LanguageService.getLanguage(chatId)));
    }   
}
