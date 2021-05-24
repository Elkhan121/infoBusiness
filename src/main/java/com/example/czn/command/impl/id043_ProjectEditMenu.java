package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id043_ProjectEditMenu extends Command {
    public static String buttonName = "";


    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                sendListCategory();
                waitingType = WaitingType.ONE;
                return COMEBACK;
            case ONE:
                buttonName = updateMessageText;
                return COMEBACK;
        }
        return EXIT;
    }
    private void sendListCategory() throws TelegramApiException {
        sendMessageWithKeyboard(getText(1458), 214);
    }
}
