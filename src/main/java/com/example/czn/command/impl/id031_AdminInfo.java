package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.util.DateKeyboard;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.sql.SQLException;

public class id031_AdminInfo extends Command {
    DateKeyboard dateKeyboard = new DateKeyboard();

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                sendMessageWithKeyboard(getText(1323) ,201);
                waitingType = WaitingType.SET_DATE;
                return COMEBACK;
            case SET_DATE:
                if (isButton(205)) {
                    sendMessageWithKeyboard(getText(1300), dateKeyboard.getCalendarKeyboard());
                    waitingType = WaitingType.CAREER_OBJECTIVE;
                    return COMEBACK;
                } else if (isButton(206)){
                    sendMessageWithKeyboard(getText(1300), dateKeyboard.getCalendarKeyboard());
                    waitingType = WaitingType.CHOOSE_USER;
                    return COMEBACK;
                }
            case CAREER_OBJECTIVE:
                if (hasCallbackQuery()) {
                    System.out.println(dateKeyboard.getDateDate(update.getCallbackQuery().getData()));
                    sendMessageWithKeyboard(getText(1301), dateKeyboard.getCalendarKeyboard());
                    System.out.println(dateKeyboard.getDateDate(updateMessageText));
                    return false;
                }
                waitingType = WaitingType.CHOOSE_USER;
                return COMEBACK;
            case CHOOSE_USER:
                if(hasCallbackQuery()){
                    sendMessageWithKeyboard(getText(1300), dateKeyboard.getCalendarKeyboard());
                    waitingType = WaitingType.DEVICE_DATE;
                    System.out.println(dateKeyboard.getDateDate(update.getCallbackQuery().getData()));
                    sendMessageWithKeyboard(getText(1301), dateKeyboard.getCalendarKeyboard());
                    return COMEBACK;
                }
                return COMEBACK;
        }
        return true;
    }
}
