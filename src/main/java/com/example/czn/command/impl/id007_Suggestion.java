package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.SuggestionRepo;
import com.example.czn.entity.custom.Suggestion;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Date;

public class id007_Suggestion extends Command {
    @Autowired
    Suggestion suggestion;
    SuggestionRepo suggestionRepo;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                getFullName();
                waitingType = WaitingType.SET_FULL_NAME;
                return COMEBACK;
            case SET_FULL_NAME:
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    suggestion = new Suggestion();
                    suggestion.setFullName(update.getMessage().getText());
                    suggestion.setPostDate(new Date());
                    getPhone();
                    waitingType = WaitingType.SET_PHONE_NUMBER;
                    return COMEBACK;
                } else {
                    wrongData();
                    getFullName();
                    return COMEBACK;
                }
            case SET_PHONE_NUMBER:
                if (botUtils.hasContactOwner(update)) {
                    suggestion.setPhoneNumber(update.getMessage().getContact().getPhoneNumber());
                    getLocation();
                    waitingType = WaitingType.SET_LOCATION;
                    return COMEBACK;
                } else {
                    wrongData();
                    getPhone();
                    return COMEBACK;
                }
            case SET_LOCATION:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    suggestion.setLocation(update.getMessage().getText());
                    getSuggestion();
                    waitingType = WaitingType.SUGGESTION;
                    return COMEBACK;
                } else {
                    wrongData();
                    getLocation();
                    return COMEBACK;
                }
            case SUGGESTION:
                if(update.hasMessage() & update.getMessage().hasText()) {
                    suggestion.setText(update.getMessage().getText());
                    // suggestionDao.insert(suggestion);
                    suggestionRepo.save(suggestion);
                    sendMessage("Уважаемый(ая) " + suggestion.getFullName() +
                            ", ваша жалоба №" + suggestionRepo.findAll().size()+1 + "отправлена!" );
                    return EXIT;
                } else {
                    wrongData();
                    getSuggestion();
                    return COMEBACK;
                }
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
        return botUtils.sendMessage(Const.SET_MOBILE_PHONE_NUMBER, chatId);
    }

    private int getLocation() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_LOCATION, chatId);
    }

    private int getSuggestion() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_SUGGESTION, chatId);
    }
}
